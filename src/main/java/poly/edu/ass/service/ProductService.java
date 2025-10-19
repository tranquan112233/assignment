// Service (Service mới - src/main/java/poly/edu/ass/service/ProductService.java)
package poly.edu.ass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.ass.Entity.SanPham;
import poly.edu.ass.repository.SanPhamRepository;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private SanPhamRepository sanPhamRepository;

    public List<SanPham> getAllProducts() {
        return sanPhamRepository.findAll();
    }
}