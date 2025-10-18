package poly.edu.ass_sof3022.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.ass_sof3022.model.Category;

public interface CategoryDAO extends JpaRepository<Category,Integer> {
    // Phân trang + search tất cả dự liệu
    Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
