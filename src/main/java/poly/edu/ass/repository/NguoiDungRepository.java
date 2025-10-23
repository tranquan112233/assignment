package poly.edu.ass.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.ass.Entity.GioHang;
import poly.edu.ass.Entity.NguoiDung;

import java.util.List;
import java.util.Optional;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
//    List<GioHang> findByNguoiDung_MaNguoiDung(Integer userId);
    Optional<NguoiDung> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByTenNguoiDung(String tenNguoiDung);

}
