package poly.edu.ass_sof3022.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poly.edu.ass_sof3022.dao.OrderDAO;
import poly.edu.ass_sof3022.dao.ProductDAO;
import poly.edu.ass_sof3022.dao.UserDAO;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    ProductDAO daop;

    @Autowired
    UserDAO daou;

    @Autowired
    OrderDAO daoo;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalProducts = daop.count();
        long totalCustomers = daou.count();
        double totalRevenue = daoo.getTotalRevenueThisMonth();

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("totalRevenue", totalRevenue);
        return "admin/dashboard";
    }
}

