package poly.edu.ass_sof3022.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.edu.ass_sof3022.model.Category;
import poly.edu.ass_sof3022.model.Product;

public interface ProductDAO extends JpaRepository<Product,Integer> {
    // Truy vấn có tìm kiếm theo tên (keyword) + fetch category + supplier
    @Query(value = """
        SELECT p FROM Product p
        JOIN FETCH p.category
        JOIN FETCH p.supplier
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """,
            countQuery = "SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    )
    Page<Product> findAllWithRelations(String keyword, Pageable pageable);

    // Nếu không có keyword (xem tất cả)
    @Query(value = "SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.supplier",
            countQuery = "SELECT COUNT(p) FROM Product p")
    Page<Product> findAllWithRelations(Pageable pageable);
}
