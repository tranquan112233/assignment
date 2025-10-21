package poly.edu.ass.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import poly.edu.ass.repository.DanhMucRepository;
import poly.edu.ass.repository.SanPhamRepository;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DanhMucRepository danhMucRepository;
    private final SanPhamRepository sanPhamRepository;

    @GetMapping({"/", "/home"})
    public String home(Model model,
                       @RequestParam(value = "ordered", required = false) Integer ordered) {

        // Header cần danh mục
        model.addAttribute("danhMucs", danhMucRepository.findAll());

        // CHỈ HIỆN SẢN PHẨM "NEW" = tên KHÔNG chứa "đã sử dụng"
        model.addAttribute("products", sanPhamRepository.findAllNew());

        // nếu có toast "đặt hàng thành công" ở trang chủ
        if (ordered != null) {
            model.addAttribute("ordered", ordered);
        }

        return "index";
    }
}
