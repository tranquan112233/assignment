package poly.edu.ass_sof3022.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
            categoryPage = dao.searchAll(keyword, pageable);
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

    // 💾 LƯU (THÊM HOẶC CẬP NHẬT) với thông báo
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category,
                               RedirectAttributes redirectAttributes) {
        try {
            dao.save(category);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu danh mục thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu danh mục!");
        }
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

    // ❌ XÓA với thông báo
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id,
                                 RedirectAttributes redirectAttributes) {
        Optional<Category> categoryOpt = dao.findById(id);
        if (categoryOpt.isPresent()) {
            dao.delete(categoryOpt.get());
            redirectAttributes.addFlashAttribute("successMessage", "Xóa danh mục thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Danh mục không tồn tại!");
        }
        return "redirect:/admin/categories";
    }
}
