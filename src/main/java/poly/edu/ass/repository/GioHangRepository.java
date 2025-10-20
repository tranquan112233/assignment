package poly.edu.ass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.ass.Entity.GioHang;
import java.util.List;
import java.util.Optional;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    List<GioHang> findByNguoiDung_MaNguoiDung(Integer userId);
    Optional<GioHang> findByNguoiDung_MaNguoiDungAndSanPham_MaSP(Integer userId, Integer productId);
    void deleteByNguoiDung_MaNguoiDung(Integer userId);
}
