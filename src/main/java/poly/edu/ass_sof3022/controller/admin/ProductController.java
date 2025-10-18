package poly.edu.ass_sof3022.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import poly.edu.ass_sof3022.dao.CategoryDAO;
import poly.edu.ass_sof3022.dao.ProductDAO;
import poly.edu.ass_sof3022.dao.SupplierDAO;
import poly.edu.ass_sof3022.model.Category;
import poly.edu.ass_sof3022.model.Product;
import poly.edu.ass_sof3022.model.Supplier;

import java.util.*;

@Controller
@RequestMapping("/admin/products")
public class ProductController {
    @Autowired
    ProductDAO dao;

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    SupplierDAO supplierDAO;

    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) String keyword){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            productPage = dao.findAllWithRelations(keyword, pageable);
        } else {
            productPage = dao.findAll(pageable);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        Product product = new Product();
        product.setCategory(new Category());
        product.setSupplier(new Supplier());
        model.addAttribute("product", product);

        // Load danh mục & nhà cung cấp cho select box
        model.addAttribute("categories", categoryDAO.findAll());
        model.addAttribute("suppliers", supplierDAO.findAll());

        return "admin/products/index";
    }

    // 💾 LƯU (THÊM HOẶC CẬP NHẬT)
    @PostMapping("/save")
    public String saveSupplier(@ModelAttribute("product") Product product) {
        dao.save(product);
        return "redirect:/admin/products";
    }

    // ✏️ SỬA - HIỂN THỊ LÊN FORM
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {

        Optional<Product> optional = dao.findById(id);
        Product product = optional.orElseGet(Product::new);

        Page<Product> productPage = dao.findAll(PageRequest.of(page, size));
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("product", product);

        // Load danh mục & nhà cung cấp cho select box
        model.addAttribute("categories", categoryDAO.findAll());
        model.addAttribute("suppliers", supplierDAO.findAll());

        return "admin/products/index";
    }

    // ❌ XÓA
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        dao.deleteById(id);
        return "redirect:/admin/products";
    }
}
