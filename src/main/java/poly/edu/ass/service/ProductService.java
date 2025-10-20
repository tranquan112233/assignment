package poly.edu.ass.service;

import poly.edu.ass.Entity.DanhMuc;
import poly.edu.ass.Entity.SanPham;

import java.util.List;

public interface ProductService {
    List<SanPham> getAllProducts();
    List<SanPham> getByDanhMuc(DanhMuc danhMuc);
}
