package poly.edu.ass_sof3022.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.edu.ass_sof3022.dao.CategoryDAO;
import poly.edu.ass_sof3022.model.Category;

import java.util.Optional;

@Controller
@RequestMapping("/admin/categories")
public class CategoriesController {

    @Autowired
    private CategoryDAO dao;

    // 📋 DANH SÁCH + TÌM KIẾM + PHÂN TRANG
    @GetMapping
    public String listCategories(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            categoryPage = dao.findByNameContainingIgnoreCase(keyword, pageable);
        } else {
            categoryPage = dao.findAll(pageable);
        }

        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", new Category());

        return "admin/categories/index";
    }

    // 💾 LƯU (THÊM HOẶC CẬP NHẬT)
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        dao.save(category);
        return "redirect:/admin/categories";
    }

    // ✏️ SỬA - HIỂN THỊ LÊN FORM
    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Integer id, Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {

        Optional<Category> optional = dao.findById(id);
        Category category = optional.orElseGet(Category::new);

        Page<Category> categoryPage = dao.findAll(PageRequest.of(page, size));
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("category", category);

        return "admin/categories/index";
    }

    // ❌ XÓA
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id) {
        dao.deleteById(id);
        return "redirect:/admin/categories";
    }
}
