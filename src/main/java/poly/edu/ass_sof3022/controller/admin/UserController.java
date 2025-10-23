package poly.edu.ass_sof3022.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.ass_sof3022.dao.RoleDAO;
import poly.edu.ass_sof3022.dao.UserDAO;
import poly.edu.ass_sof3022.model.Role;
import poly.edu.ass_sof3022.model.User;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    UserDAO userDAO;
    @Autowired
    RoleDAO roleDAO;

    @GetMapping
    public String listUsers(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            userPage = userDAO.findByNameContainingIgnoreCase(keyword, pageable);
        } else {
            userPage = userDAO.findAll(pageable);
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("userPage", userPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("user", new User());
        return "admin/user/index";
    }

    @PostMapping("/save")
    public String saveUserRole(@ModelAttribute("user") User userForm,
                               RedirectAttributes redirectAttributes) {
        try {
            // Lấy user gốc từ database (để tránh ghi đè toàn bộ)
            User user = userDAO.findById(userForm.getId())
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));
            user.setRole(userForm.getRole());
            userDAO.save(user);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật quyền của người dùng thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu quyền của người dùng!");
        }
        return "redirect:/admin/user";
    }


    @GetMapping("/edit/{id}")
    public String editUserRole(@PathVariable("id") Integer id, Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {

        // Lấy user theo id
        Optional<User> optional = userDAO.findById(id);
        User user = optional.orElseGet(User::new);

        // Lấy danh sách user (phân trang)
        Page<User> userPage = userDAO.findAll(PageRequest.of(page, size));

        // Lấy tất cả Role để hiển thị trong combobox
        List<Role> roles = roleDAO.findAll();

        // Gửi dữ liệu sang view
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        return "admin/user/index";
    }

}

