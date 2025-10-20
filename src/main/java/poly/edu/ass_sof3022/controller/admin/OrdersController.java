package poly.edu.ass_sof3022.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.ass_sof3022.dao.*;
import poly.edu.ass_sof3022.model.*;

import java.util.*;

@Controller
@RequestMapping("/admin/orders")
public class OrdersController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AddressDAO addressDAO;

    // 📋 DANH SÁCH + TÌM KIẾM + PHÂN TRANG
    @GetMapping
    public String listOrders(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            orderPage = orderDAO.searchAll(keyword, pageable);
        } else {
            orderPage = orderDAO.findAll(pageable);
        }

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        // Form trống (khi thêm mới)
        Order newOrder = new Order();
        newOrder.setUser(new User());
        newOrder.setAddress(new Address());
        model.addAttribute("o", newOrder);

        model.addAttribute("users", userDAO.findAll());
        model.addAttribute("addresses", addressDAO.findAll());

        return "admin/orders/index";
    }

    // 💾 CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG
    @PostMapping("/save")
    public String updateOrderStatus(@ModelAttribute("o") Order order,
                                    RedirectAttributes redirectAttributes) {
        try {
            Optional<Order> opt = orderDAO.findById(order.getId());
            if (opt.isPresent()) {
                Order existing = opt.get();
                existing.setStatus(order.getStatus());
                orderDAO.save(existing);
                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái đơn hàng thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đơn hàng!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật!");
        }
        return "redirect:/admin/orders";
    }

    // ✏️ SỬA - HIỂN THỊ LÊN FORM
    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable("id") Integer id, Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {

        Order order = orderDAO.findById(id).orElseGet(Order::new);

        Page<Order> orderPage = orderDAO.findAll(PageRequest.of(page, size));
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("o", order);

        model.addAttribute("users", userDAO.findAll());
        model.addAttribute("addresses", addressDAO.findAll());

        return "admin/orders/index";
    }

    // ❌ XÓA
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id,
                              RedirectAttributes redirectAttributes) {
        Optional<Order> orderOpt = orderDAO.findById(id);
        if (orderOpt.isPresent()) {
            orderDAO.delete(orderOpt.get());
            redirectAttributes.addFlashAttribute("successMessage", "Xóa đơn hàng thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Đơn hàng không tồn tại!");
        }
        return "redirect:/admin/orders";
    }
}
