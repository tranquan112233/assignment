package poly.edu.ass.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import poly.edu.ass.Entity.DanhMuc;
import poly.edu.ass.Entity.SanPham;
import poly.edu.ass.repository.DanhMucRepository;
import poly.edu.ass.repository.SanPhamRepository;
import poly.edu.ass.service.DanhMucService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ClearanceController {

    private final DanhMucService danhMucService;
    private final DanhMucRepository danhMucRepo;
    private final SanPhamRepository sanPhamRepo;

    @GetMapping("/thanh-ly")
    public String thanhLy(Model model) {
        // dùng cho header
        model.addAttribute("danhMucs", danhMucService.getAll());

        List<SanPham> items = Collections.emptyList();

        // 1) Ưu tiên: nếu có danh mục tên "Hàng thanh lý"
        Optional<DanhMuc> dm = danhMucRepo.findByTenDanhMucIgnoreCase("Hàng thanh lý");
        if (dm.isPresent()) {
            items = sanPhamRepo.findByDanhMuc_MaDanhMuc(dm.get().getMaDanhMuc());
        } else {
            // 2) Fallback: mô tả có chứa "đã qua sử dụng" hoặc "used"
            items = sanPhamRepo.findByMoTaContainingIgnoreCaseOrMoTaContainingIgnoreCase("đã qua sử dụng", "used");
        }

        model.addAttribute("items", items);
        return "clearance";
    }
}
