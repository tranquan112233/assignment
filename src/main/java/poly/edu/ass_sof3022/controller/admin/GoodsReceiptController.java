package poly.edu.ass_sof3022.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.ass_sof3022.dao.GoodsReceiptDAO;
import poly.edu.ass_sof3022.dao.SupplierDAO;
import poly.edu.ass_sof3022.dao.ProductDAO;
import poly.edu.ass_sof3022.model.GoodsReceipt;
import poly.edu.ass_sof3022.model.GoodsReceiptDetail;
import poly.edu.ass_sof3022.model.Supplier;

import java.util.Optional;

@Controller
@RequestMapping("/admin/goodsreceipt")
public class GoodsReceiptController {

    @Autowired
    private GoodsReceiptDAO goodsReceiptDAO;

    @Autowired
    private SupplierDAO supplierDAO;

    @Autowired
    private ProductDAO productDAO;

    // 📋 DANH SÁCH + TÌM KIẾM + PHÂN TRANG
    @GetMapping
    public String listGoodsReceipts(Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size);
        Page<GoodsReceipt> pageData;

        if (keyword != null && !keyword.trim().isEmpty()) {
            pageData = goodsReceiptDAO.searchAll(keyword, pageable);
        } else {
            pageData = goodsReceiptDAO.findAll(pageable);
        }

        model.addAttribute("goodsReceipts", pageData.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageData.getTotalPages());
        model.addAttribute("keyword", keyword == null ? "" : keyword);

        if (!model.containsAttribute("goodsReceipt")) {
            model.addAttribute("goodsReceipt", new GoodsReceipt());
        }

        model.addAttribute("suppliers", supplierDAO.findAll());
        model.addAttribute("products", productDAO.findAll());

        return "admin/goodsreceipt/index";
    }

    // 💾 LƯU / CẬP NHẬT PHIẾU NHẬP
    @PostMapping("/save")
    public String saveGoodsReceipt(@ModelAttribute("goodsReceipt") GoodsReceipt goodsReceipt,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Fetch supplier nếu cần
            if (goodsReceipt.getSupplier() != null && goodsReceipt.getSupplier().getId() != null) {
                Integer supId = goodsReceipt.getSupplier().getId();
                Supplier sup = supplierDAO.findById(supId).orElse(null);
                goodsReceipt.setSupplier(sup);
            }

            // Link chi tiết về parent
            if (goodsReceipt.getGoodsReceiptDetails() != null) {
                for (GoodsReceiptDetail d : goodsReceipt.getGoodsReceiptDetails()) {
                    d.setGoodsReceipt(goodsReceipt);
                }
            }

            // Tính tổng tiền từ chi tiết
            Double total = 0.0;
            if (goodsReceipt.getGoodsReceiptDetails() != null) {
                for (GoodsReceiptDetail d : goodsReceipt.getGoodsReceiptDetails()) {
                    total += d.getQuantity() * d.getUnitPrice();
                }
            }
            goodsReceipt.setTotalAmount(total);

            goodsReceiptDAO.save(goodsReceipt);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu phiếu nhập thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu phiếu nhập!");
        }
        return "redirect:/admin/goodsreceipt";
    }

    // ✏️ SỬA - HIỂN THỊ LÊN FORM
    @GetMapping("/edit/{id}")
    public String editGoodsReceipt(@PathVariable("id") Integer id, Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {

        GoodsReceipt goodsReceipt = goodsReceiptDAO.findById(id)
                .orElse(new GoodsReceipt());
        Page<GoodsReceipt> pageData = goodsReceiptDAO.findAll(PageRequest.of(page, size));

        model.addAttribute("goodsReceipts", pageData.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageData.getTotalPages());
        model.addAttribute("goodsReceipt", goodsReceipt);

        model.addAttribute("suppliers", supplierDAO.findAll());
        model.addAttribute("products", productDAO.findAll());

        return "admin/goodsreceipt/index";
    }

    // ❌ XÓA
    @GetMapping("/delete/{id}")
    public String deleteGoodsReceipt(@PathVariable("id") Integer id,
                                     RedirectAttributes redirectAttributes) {
        Optional<GoodsReceipt> grOpt = goodsReceiptDAO.findById(id);
        if (grOpt.isPresent()) {
            goodsReceiptDAO.delete(grOpt.get());
            redirectAttributes.addFlashAttribute("successMessage", "Xóa phiếu nhập thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Phiếu nhập không tồn tại!");
        }
        return "redirect:/admin/goodsreceipt";
    }

    // 👁️ XEM CHI TIẾT (CHO MODAL)
    @GetMapping("/view/{id}")
    public String viewDetails(@PathVariable("id") Integer id, Model model) {
        GoodsReceipt goodsReceipt = goodsReceiptDAO.findById(id).orElse(null);
        if (goodsReceipt == null) {
            model.addAttribute("errorMessage", "Không tìm thấy phiếu nhập!");
            return "redirect:/admin/goodsreceipt";
        }

        model.addAttribute("goodsReceipt", goodsReceipt);
        model.addAttribute("details", goodsReceipt.getGoodsReceiptDetails());
        return "admin/goodsreceipt/detail"; // modal chi tiết
    }
}
