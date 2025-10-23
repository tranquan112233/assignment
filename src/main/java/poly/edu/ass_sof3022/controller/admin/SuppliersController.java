package poly.edu.ass_sof3022.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.ass_sof3022.dao.SupplierDAO;
import poly.edu.ass_sof3022.model.Supplier;

import java.util.Optional;

@Controller
@RequestMapping("/admin/suppliers")
public class SuppliersController {

    @Autowired
    SupplierDAO dao;

    // 📋 DANH SÁCH + TÌM KIẾM + PHÂN TRANG
    @GetMapping
    public String listSuppliers(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Supplier> supplierPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            supplierPage = dao.searchAll(keyword, pageable);
        } else {
            supplierPage = dao.findAll(pageable);
        }

        model.addAttribute("suppliers", supplierPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", supplierPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("supplier", new Supplier());

        return "admin/suppliers/index";
    }

    // 💾 LƯU (THÊM HOẶC CẬP NHẬT) với thông báo
    @PostMapping("/save")
    public String saveSupplier(@ModelAttribute("supplier") Supplier supplier,
                               RedirectAttributes redirectAttributes) {
        try {
            dao.save(supplier);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu nhà cung cấp thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu nhà cung cấp!");
        }
        return "redirect:/admin/suppliers";
    }

    // ✏️ SỬA - HIỂN THỊ LÊN FORM
    @GetMapping("/edit/{id}")
    public String editSupplier(@PathVariable("id") Integer id, Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Optional<Supplier> optional = dao.findById(id);
        Supplier supplier = optional.orElseGet(Supplier::new);

        Page<Supplier> supplierPage = dao.findAll(PageRequest.of(page, size));
        model.addAttribute("suppliers", supplierPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", supplierPage.getTotalPages());
        model.addAttribute("supplier", supplier);

        return "admin/suppliers/index";
    }

    // ❌ XÓA với thông báo
    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable("id") Integer id,
                                 RedirectAttributes redirectAttributes) {
        Optional<Supplier> optional = dao.findById(id);
        if (optional.isPresent()) {
            dao.delete(optional.get());
            redirectAttributes.addFlashAttribute("successMessage", "Xóa nhà cung cấp thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Nhà cung cấp không tồn tại!");
        }
        return "redirect:/admin/suppliers";
    }
}
