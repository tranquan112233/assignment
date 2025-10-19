// Repository (DAO mới - src/main/java/poly/edu/ass/repository/SanPhamRepository.java)
package poly.edu.ass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.ass.Entity.SanPham;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
}