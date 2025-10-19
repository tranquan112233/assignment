package poly.edu.ass_sof3022.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.edu.ass_sof3022.dao.UserDAO;
import poly.edu.ass_sof3022.model.User;

import java.util.Optional;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    UserDAO userDAO;

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
}

