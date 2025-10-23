package poly.edu.ass_sof3022.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.edu.ass_sof3022.model.Category;
import poly.edu.ass_sof3022.model.Supplier;

public interface CategoryDAO extends JpaRepository<Category,Integer> {
    // Phân trang + search tất cả dự liệu
    @Query("SELECT c FROM Category c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "CAST(c.id AS string) LIKE CONCAT('%', :keyword, '%')")
    Page<Category> searchAll(@Param("keyword") String keyword, Pageable pageable);
}
