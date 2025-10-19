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
public class DanhMucController {

    @Autowired
    private DanhMucService danhMucService;

    @Autowired
    private ProductService productService;

    // Lấy danh mục theo slug hoặc id
    @GetMapping("/danhmuc/{maDanhMuc}")
    public String showProductsByDanhMuc(@PathVariable("maDanhMuc") Integer maDanhMuc, Model model) {
        DanhMuc dm = danhMucService.getById(maDanhMuc);
        if(dm == null) {
            return "redirect:/"; // Nếu ko có thì về trang home
        }

        List<SanPham> products = productService.getByDanhMuc(dm);
        model.addAttribute("products", products);
        model.addAttribute("danhMuc", dm);

        return "danhmuc"; // Tạo file danhmuc.html
    }
}
