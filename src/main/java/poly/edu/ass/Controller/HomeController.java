package poly.edu.ass.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import poly.edu.ass.Entity.DanhMuc;
import poly.edu.ass.Entity.SanPham;
import poly.edu.ass.service.DanhMucService;
import poly.edu.ass.service.ProductService;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private DanhMucService danhMucService;

    // Trang chủ
    @GetMapping("/")
    public String index(Model model) {
        List<SanPham> products = productService.getAllProducts();
        List<DanhMuc> danhMucs = danhMucService.getAll();

        model.addAttribute("products", products);
        model.addAttribute("danhMucs", danhMucs);

        return "index";
    }

    // Trang danh mục: ví dụ /danh-muc/1
    @GetMapping("/danh-muc/{id}")
    public String showByDanhMuc(@PathVariable("id") Integer id, Model model) {
        DanhMuc dm = danhMucService.getById(id);
        if (dm == null) return "redirect:/";

        List<SanPham> products = productService.getByDanhMuc(dm);
        List<DanhMuc> danhMucs = danhMucService.getAll();

        model.addAttribute("products", products);
        model.addAttribute("danhMucs", danhMucs);
        model.addAttribute("currentDanhMuc", dm);

        return "index"; // vẫn dùng index.html để hiển thị
    }
}
