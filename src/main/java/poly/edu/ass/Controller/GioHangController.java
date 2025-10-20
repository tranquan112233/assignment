package poly.edu.ass.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import poly.edu.ass.Entity.GioHang;
import poly.edu.ass.Entity.HoaDon;
import poly.edu.ass.Entity.HoaDonChiTiet;
import poly.edu.ass.Entity.NguoiDung;
import poly.edu.ass.Entity.SanPham;

import poly.edu.ass.dto.CartItemView;
import poly.edu.ass.repository.DiaChiRepository;
import poly.edu.ass.repository.GioHangRepository;
import poly.edu.ass.repository.HoaDonChiTietRepository;
import poly.edu.ass.repository.HoaDonRepository;
import poly.edu.ass.repository.NguoiDungRepository;
import poly.edu.ass.repository.SanPhamRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class GioHangController {

    private final GioHangRepository ghRepo;
    private final SanPhamRepository spRepo;
    private final NguoiDungRepository ndRepo;
    private final HoaDonRepository hdRepo;
    private final HoaDonChiTietRepository hdctRepo;
    private final DiaChiRepository dcRepo;

    // Convert entity -> DTO cho UI
    private CartItemView toView(GioHang gh) {
        SanPham sp = gh.getSanPham();
        BigDecimal line = gh.getDonGia().multiply(BigDecimal.valueOf(gh.getSoLuong()));
        return new CartItemView(
                gh.getMaGH(),
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getHinhAnh(),
                gh.getDonGia(),
                gh.getSoLuong(),
                line
        );
    }

    // GET /api/cart?userId=3
    @GetMapping
    public Map<String, Object> getCart(@RequestParam Integer userId) {
        var rows = ghRepo.findByNguoiDung_MaNguoiDung(userId);
        var items = rows.stream().map(this::toView).toList();

        var subtotal = items.stream()
                .map(CartItemView::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> res = new HashMap<>();
        res.put("items", items);
        res.put("count", items.stream().mapToInt(CartItemView::soLuong).sum());
        res.put("subtotal", subtotal);
        return res;
    }

    // POST /api/cart/add?userId=3&productId=1&qty=2
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam Integer userId,
                                 @RequestParam Integer productId,
                                 @RequestParam(defaultValue = "1") Integer qty) {
        var sp = spRepo.findById(productId).orElse(null);
        if (sp == null) return ResponseEntity.badRequest().body("Product not found");
        if (qty == null || qty <= 0) qty = 1;

        var opt = ghRepo.findByNguoiDung_MaNguoiDungAndSanPham_MaSP(userId, productId);
        if (opt.isPresent()) {
            var row = opt.get();
            row.setSoLuong(row.getSoLuong() + qty);
            ghRepo.save(row);
        } else {
            var row = new GioHang();
            // lấy reference cho nhanh, không cần query full
            NguoiDung userRef = ndRepo.getReferenceById(userId);
            row.setNguoiDung(userRef);
            row.setSanPham(sp);
            row.setSoLuong(qty);
            row.setDonGia(sp.getGia()); // chốt giá tại thời điểm thêm
            ghRepo.save(row);
        }
        return ResponseEntity.ok(getCart(userId));
    }

    // PATCH /api/cart/{cartId}/qty?qty=3&userId=3
    @PatchMapping("/{cartId}/qty")
    public ResponseEntity<?> updateQty(@PathVariable Integer cartId,
                                       @RequestParam Integer qty,
                                       @RequestParam Integer userId) {
        var row = ghRepo.findById(cartId).orElse(null);
        if (row == null || !row.getNguoiDung().getMaNguoiDung().equals(userId))
            return ResponseEntity.notFound().build();

        row.setSoLuong(Math.max(1, qty));
        ghRepo.save(row);
        return ResponseEntity.ok(getCart(userId));
    }

    // DELETE /api/cart/{cartId}?userId=3
    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> remove(@PathVariable Integer cartId,
                                    @RequestParam Integer userId) {
        var row = ghRepo.findById(cartId).orElse(null);
        if (row != null && row.getNguoiDung().getMaNguoiDung().equals(userId)) {
            ghRepo.delete(row);
        }
        return ResponseEntity.ok(getCart(userId));
    }

    // DELETE /api/cart/clear?userId=3
    // GioHangController.java
    @DeleteMapping("/clear")
    @Transactional
    public ResponseEntity<?> clear(@RequestParam Integer userId) {
        var items = ghRepo.findByNguoiDung_MaNguoiDung(userId);
        if (!items.isEmpty()) {
            ghRepo.deleteAllInBatch(items);   // hoặc deleteAll(items)
        }
        // trả về giỏ rỗng (đỡ phải query lại)
        Map<String, Object> res = new HashMap<>();
        res.put("items", List.of());
        res.put("count", 0);
        res.put("subtotal", java.math.BigDecimal.ZERO);
        return ResponseEntity.ok(res);
    }

    // Fallback nếu DELETE bị chặn CSRF/nginx → gọi bằng POST
    @PostMapping("/clear")
    @Transactional
    public ResponseEntity<?> clearPost(@RequestParam Integer userId) {
        return clear(userId);
    }

    // POST /api/cart/checkout?userId=3&diaChiId=1
    @PostMapping("/checkout")
    @Transactional
    public ResponseEntity<?> checkout(@RequestParam Integer userId,
                                      @RequestParam Integer diaChiId) {
        var items = ghRepo.findByNguoiDung_MaNguoiDung(userId);
        if (items.isEmpty()) return ResponseEntity.badRequest().body("Giỏ trống");
        if (!dcRepo.existsById(diaChiId)) return ResponseEntity.badRequest().body("Địa chỉ không tồn tại");

        BigDecimal tong = BigDecimal.ZERO;

        // Trừ tồn & tính tổng
        for (var gh : items) {
            var sp = spRepo.findById(gh.getSanPham().getMaSP()).orElseThrow();
            if (sp.getSoLuong() < gh.getSoLuong())
                return ResponseEntity.badRequest().body("Hết hàng: " + sp.getTenSP());
            sp.setSoLuong(sp.getSoLuong() - gh.getSoLuong());
            spRepo.save(sp);

            tong = tong.add(gh.getDonGia().multiply(BigDecimal.valueOf(gh.getSoLuong())));
        }

        // Tạo hóa đơn: set đối tượng liên kết, KHÔNG set id
        var hd = new HoaDon();
        hd.setNguoiDung(ndRepo.getReferenceById(userId));   // << thay setMaNguoiDung(...)
        hd.setDiaChi(dcRepo.getReferenceById(diaChiId));    // << thay setMaDiaChi(...)
        hd.setTongTien(tong);
        hd.setTrangThai("Chờ xác nhận");
        hd = hdRepo.save(hd);

        // Chi tiết hóa đơn: set HoaDon & SanPham (đối tượng), KHÔNG set id
        for (var gh : items) {
            var ct = new HoaDonChiTiet();
            ct.setHoaDon(hd);                                // << thay setMaHoaDon(...)
            ct.setSanPham(gh.getSanPham());                  // << thay setMaSP(...)
            ct.setSoLuong(gh.getSoLuong());
            ct.setDonGia(gh.getDonGia());
            hdctRepo.save(ct);
        }

        // Xóa giỏ
        ghRepo.deleteByNguoiDung_MaNguoiDung(userId);

        Map<String, Object> res = new HashMap<>();
        res.put("orderId", hd.getMaHoaDon());
        res.put("total", tong);
        return ResponseEntity.ok(res);
    }

}
