package poly.edu.ass.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.ass.Entity.*;
import poly.edu.ass.dto.CheckoutItem;
import poly.edu.ass.repository.*;
import poly.edu.ass.service.DanhMucService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final DanhMucService danhMucService;
    private final GioHangRepository gioHangRepo;
    private final SanPhamRepository sanPhamRepo;
    private final HoaDonRepository hoaDonRepo;
    private final HoaDonChiTietRepository hdctRepo;
    private final DiaChiRepository diaChiRepo;
    private final NguoiDungRepository nguoiDungRepo;

    @GetMapping("/checkout")
    public String showCheckout(@RequestParam Integer userId,
                               @RequestParam(value = "buyNow", required = false) Integer buyNowProductId,
                               @RequestParam(value = "qty", required = false) Integer qty,
                               @RequestParam(value = "error", required = false) String error,
                               Model model) {

        model.addAttribute("danhMucs", danhMucService.getAll());
        model.addAttribute("userId", userId);
        if (error != null && !error.isBlank()) {
            model.addAttribute("error", error); // nếu cần bạn show bằng JS từ main.js
        }

        DiaChi defaultAddr = diaChiRepo
                .findFirstByNguoiDung_MaNguoiDungAndMacDinhTrue(userId)
                .orElse(null);
        model.addAttribute("defaultAddr", defaultAddr);

        if (buyNowProductId != null) {
            SanPham sp = sanPhamRepo.findById(buyNowProductId).orElseThrow();
            int soLuong = (qty == null || qty < 1) ? 1 : qty;

            CheckoutItem bnItem = new CheckoutItem(sp, soLuong, sp.getGia());
            BigDecimal subtotal = bnItem.getDonGia().multiply(BigDecimal.valueOf(soLuong));

            model.addAttribute("buyNow", true);
            model.addAttribute("bnProductId", sp.getMaSP());
            model.addAttribute("bnQty", soLuong);
            model.addAttribute("bnItem", bnItem);
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("total", subtotal);
            return "checkout";
        }

        List<GioHang> rows = gioHangRepo.findByNguoiDung_MaNguoiDung(userId);
        BigDecimal subtotal = rows.stream()
                .map(i -> i.getDonGia().multiply(BigDecimal.valueOf(i.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("buyNow", false);
        model.addAttribute("items", rows);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", subtotal);
        return "checkout";
    }

    @PostMapping("/checkout")
    @Transactional
    public String doCheckout(@RequestParam Integer userId,
                             @RequestParam String email,
                             @RequestParam String ho,
                             @RequestParam String ten,
                             @RequestParam String diaChi,
                             @RequestParam String dienThoai,
                             @RequestParam(defaultValue = "COD") String payment,
                             @RequestParam(value = "buyNow", required = false) Integer bnProductId,
                             @RequestParam(value = "qty", required = false) Integer bnQty,
                             RedirectAttributes ra) {

        NguoiDung user = nguoiDungRepo.findById(userId).orElseThrow();

        DiaChi dc = new DiaChi();
        dc.setNguoiDung(user);
        dc.setDiaChiChiTiet(diaChi);
        dc.setMacDinh(false);
        dc = diaChiRepo.save(dc);

        HoaDon hd = new HoaDon();
        hd.setNguoiDung(user);
        hd.setDiaChi(dc);
        hd.setNgayLap(LocalDate.now());
        hd.setTrangThai("Chờ xác nhận");

        BigDecimal tong = BigDecimal.ZERO;

        if (bnProductId != null) {
            SanPham sp = sanPhamRepo.findById(bnProductId).orElseThrow();
            int qty = (bnQty == null || bnQty < 1) ? 1 : bnQty;

            if (sp.getSoLuong() < qty) {
                ra.addAttribute("error", "Sản phẩm hết hàng: " + sp.getTenSP());
                ra.addAttribute("userId", userId);
                ra.addAttribute("buyNow", bnProductId);
                ra.addAttribute("qty", qty);
                return "redirect:/checkout";
            }

            sp.setSoLuong(sp.getSoLuong() - qty);
            sanPhamRepo.save(sp);

            tong = sp.getGia().multiply(BigDecimal.valueOf(qty));
            hd.setTongTien(tong);
            hd = hoaDonRepo.save(hd);

            HoaDonChiTiet ct = new HoaDonChiTiet();
            ct.setHoaDon(hd);
            ct.setSanPham(sp);
            ct.setSoLuong(qty);
            ct.setDonGia(sp.getGia());
            hdctRepo.save(ct);

        } else {
            List<GioHang> items = gioHangRepo.findByNguoiDung_MaNguoiDung(userId);
            if (items.isEmpty()) {
                // không có gì để thanh toán
                return "redirect:/";
            }

            for (GioHang gh : items) {
                SanPham sp = gh.getSanPham();
                if (sp.getSoLuong() < gh.getSoLuong()) {
                    ra.addAttribute("error", "Hết hàng: " + sp.getTenSP());
                    ra.addAttribute("userId", userId);
                    return "redirect:/checkout";
                }
            }

            for (GioHang gh : items) {
                SanPham sp = gh.getSanPham();
                sp.setSoLuong(sp.getSoLuong() - gh.getSoLuong());
                sanPhamRepo.save(sp);
                tong = tong.add(gh.getDonGia().multiply(BigDecimal.valueOf(gh.getSoLuong())));
            }

            hd.setTongTien(tong);
            hd = hoaDonRepo.save(hd);

            for (GioHang gh : items) {
                HoaDonChiTiet ct = new HoaDonChiTiet();
                ct.setHoaDon(hd);
                ct.setSanPham(gh.getSanPham());
                ct.setSoLuong(gh.getSoLuong());
                ct.setDonGia(gh.getDonGia());
                hdctRepo.save(ct);
            }

            gioHangRepo.deleteByNguoiDung_MaNguoiDung(userId);
        }

        return "redirect:/order-success?orderId=" + hd.getMaHoaDon();
    }

    @GetMapping("/order-success")
    public String orderSuccess(@RequestParam("orderId") Integer orderId, Model model) {
        model.addAttribute("danhMucs", danhMucService.getAll());
        model.addAttribute("orderId", orderId);
        return "order-success";
    }
}
