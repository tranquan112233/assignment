package poly.edu.ass.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import poly.edu.ass.Entity.SanPham;
import poly.edu.ass.service.ProductService;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(Model model) {
        List<SanPham> products = productService.getAllProducts();
        model.addAttribute("products", products); // Truyền dữ liệu sang view
        return "index"; // Trả về file index.html trong templates
    }
}