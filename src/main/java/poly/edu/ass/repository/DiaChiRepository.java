package poly.edu.ass.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.ass.Entity.DiaChi;

import java.util.Optional;

public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {
    Optional<DiaChi> findFirstByNguoiDung_MaNguoiDungAndMacDinhTrue(Integer userId);
}
