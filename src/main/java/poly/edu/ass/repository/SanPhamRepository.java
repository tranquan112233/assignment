package poly.edu.ass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.ass.Entity.DanhMuc;
import poly.edu.ass.Entity.SanPham;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    List<SanPham> findByDanhMuc(DanhMuc danhMuc);
}
