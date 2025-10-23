package poly.edu.ass.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.edu.ass.Entity.NguoiDung;
import poly.edu.ass.service.AuthService;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new NguoiDung());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") NguoiDung user, Model model) {
        String result = authService.registerCustomer(user);
        if (!result.equals("success")) {
            model.addAttribute("error", result);
            return "register";
        }
        model.addAttribute("msg", "Đăng ký thành công. Vui lòng đăng nhập.");
        return "login";
    }


    @GetMapping("/dashboard")
    public String home(Authentication auth, Model model) {
        if (auth == null) return "redirect:/login";
        // Lấy thông tin user
        model.addAttribute("username", auth.getName());
        // Lấy role đầu tiên (hoặc danh sách)
        String role = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("role", role);
        // Hiển thị trang chung
        return "index";
    }


}
