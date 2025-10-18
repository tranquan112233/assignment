package poly.edu.ass_sof3022.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.ass_sof3022.model.Category;
import poly.edu.ass_sof3022.model.Supplier;

public interface SupplierDAO extends JpaRepository<Supplier,Integer> {
    // Phân trang + search tất cả dự liệu
    Page<Supplier> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
