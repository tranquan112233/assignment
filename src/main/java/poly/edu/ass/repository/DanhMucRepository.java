package poly.edu.ass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.ass.Entity.DanhMuc;

import java.util.Optional;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    Optional<DanhMuc> findByTenDanhMucIgnoreCase(String tenDanhMuc);
}
