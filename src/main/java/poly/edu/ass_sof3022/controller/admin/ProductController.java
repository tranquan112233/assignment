package poly.edu.ass_sof3022.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.edu.ass_sof3022.dao.CategoryDAO;
import poly.edu.ass_sof3022.dao.ProductDAO;
import poly.edu.ass_sof3022.dao.SupplierDAO;
import poly.edu.ass_sof3022.model.Category;
import poly.edu.ass_sof3022.model.Product;
import poly.edu.ass_sof3022.model.Supplier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                               @RequestParam(required = false) String keyword) {
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

    // 💾 LƯU (THÊM HOẶC CẬP NHẬT) với thông báo
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam("imageFile") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        try {
            String uploadDir = "src/main/resources/static/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Nếu có file mới được upload
            if (!file.isEmpty()) {
                // Nếu là cập nhật -> xóa ảnh cũ
                if (product.getId() != null) {
                    Optional<Product> oldProductOpt = dao.findById(product.getId());
                    if (oldProductOpt.isPresent()) {
                        Product oldProduct = oldProductOpt.get();
                        if (oldProduct.getImage() != null) {
                            File oldFile = new File(uploadDir + oldProduct.getImage());
                            if (oldFile.exists()) oldFile.delete(); // Xóa ảnh cũ
                        }
                    }
                }

                // Tạo tên file duy nhất để tránh trùng
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, file.getBytes());

                product.setImage(fileName);

            } else {
                // Nếu không chọn ảnh mới khi sửa, giữ lại ảnh cũ
                if (product.getId() != null) {
                    Optional<Product> existing = dao.findById(product.getId());
                    existing.ifPresent(p -> product.setImage(p.getImage()));
                }
            }

            dao.save(product);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu sản phẩm thành công!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu sản phẩm!");
        }

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

        model.addAttribute("categories", categoryDAO.findAll());
        model.addAttribute("suppliers", supplierDAO.findAll());

        return "admin/products/index";
    }

    // ❌ XÓA (kèm xóa ảnh) + thông báo
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id,
                                RedirectAttributes redirectAttributes) {
        String uploadDir = "src/main/resources/static/uploads/";

        Optional<Product> productOpt = dao.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            // Xóa file ảnh nếu tồn tại
            if (product.getImage() != null) {
                File imageFile = new File(uploadDir + product.getImage());
                if (imageFile.exists()) imageFile.delete();
            }

            dao.delete(product);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không tồn tại!");
        }

        return "redirect:/admin/products";
    }
}
