package poly.edu.ass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.ass.Entity.Quyen;

import java.util.Optional;

public interface QuyenRepository extends JpaRepository<Quyen, Integer> {
    Optional<Quyen> findByTenQuyen(String tenQuyen);
}
