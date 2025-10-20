package poly.edu.ass.service.IMPL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.ass.Entity.DanhMuc;
import poly.edu.ass.Entity.SanPham;
import poly.edu.ass.repository.SanPhamRepository;
import poly.edu.ass.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Override
    public List<SanPham> getAllProducts() {
        return sanPhamRepository.findAll();
    }

    @Override
    public List<SanPham> getByDanhMuc(DanhMuc danhMuc) {
        return sanPhamRepository.findByDanhMuc(danhMuc);
    }
}
