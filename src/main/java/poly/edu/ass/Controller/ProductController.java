// ProductController.java
package poly.edu.ass.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.edu.ass.repository.SanPhamRepository;
import poly.edu.ass.Entity.SanPham;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final SanPhamRepository spRepo;

    @GetMapping("/sanpham/{id}")
    public String productDetail(@PathVariable Integer id, Model model) {
        SanPham p = spRepo.findById(id).orElse(null);
        if (p == null) return "redirect:/"; // hoặc trang 404

        // liên quan: cùng danh mục, loại trừ chính nó, giới hạn 8
        List<SanPham> related = spRepo.findAll().stream()
                .filter(x -> x.getMaSP() != null && !x.getMaSP().equals(id))
                .filter(x -> p.getDanhMuc() != null && x.getDanhMuc() != null
                        && x.getDanhMuc().getMaDanhMuc().equals(p.getDanhMuc().getMaDanhMuc()))
                .limit(8).toList();

        model.addAttribute("p", p);
        model.addAttribute("related", related);
        return "product-detail";
    }
}
