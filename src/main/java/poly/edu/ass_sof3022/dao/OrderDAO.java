package poly.edu.ass_sof3022.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.edu.ass_sof3022.model.Order;

public interface OrderDAO extends JpaRepository<Order, Integer> {
    @Query("""
        SELECT o FROM Order o
        WHERE LOWER(o.user.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(o.address.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(o.status) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR CAST(o.id AS string) LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Order> searchAll(@Param("keyword") String keyword, Pageable pageable);
}
