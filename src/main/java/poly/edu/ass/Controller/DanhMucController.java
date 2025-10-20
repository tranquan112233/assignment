package poly.edu.ass.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import poly.edu.ass.Entity.DanhMuc;
import poly.edu.ass.Entity.SanPham;
import poly.edu.ass.repository.DanhMucRepository;
import poly.edu.ass.repository.SanPhamRepository;
import poly.edu.ass.service.DanhMucService;
import poly.edu.ass.service.ProductService;

import java.util.List;

@Controller
public class DanhMucController {

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @GetMapping("/danhmuc/{id}")
    public String xemSanPhamTheoDanhMuc(@PathVariable("id") Integer id, Model model) {
        DanhMuc danhMuc = danhMucRepository.findById(id).orElse(null);
        List<SanPham> products = sanPhamRepository.findByDanhMuc_MaDanhMuc(id);

        model.addAttribute("danhMuc", danhMuc);
        model.addAttribute("products", products);
        model.addAttribute("danhMucs", danhMucRepository.findAll());


        return "danhmuc"; // tên file HTML mày đặt là danhmuc.html
    }
}

