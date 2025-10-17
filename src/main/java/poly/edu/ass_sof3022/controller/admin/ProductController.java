package poly.edu.ass_sof3022.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import java.util.*;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private List<Map<String, Object>> productList = new ArrayList<>();

    public ProductController() {
        // Tạo dữ liệu giả để test
        for (int i = 1; i <= 35; i++) {
            Map<String, Object> p = new HashMap<>();
            p.put("id", i);
            p.put("name", "Sản phẩm " + i);
            p.put("price", 10000 + i * 1000);
            p.put("quantity", 10 + i);
            p.put("category", "Danh mục " + ((i % 3) + 1));
            p.put("supplier", "Nhà cung cấp " + ((i % 2) + 1));
            productList.add(p);
        }
    }

    // 📄 Hiển thị danh sách sản phẩm có phân trang
    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {

        int start = page * size;
        int end = Math.min((start + size), productList.size());
        List<Map<String, Object>> pageContent = productList.subList(start, end);

        Page<Map<String, Object>> productPage =
                new PageImpl<>(pageContent, PageRequest.of(page, size), productList.size());

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "admin/products/index";
    }
}
