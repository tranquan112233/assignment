package poly.edu.ass_sof3022.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.edu.ass_sof3022.model.GoodsReceipt;

public interface GoodsReceiptDAO extends JpaRepository<GoodsReceipt, Integer> {

    @Query(value = """
        SELECT g FROM GoodsReceipt g
        WHERE LOWER(CAST(g.id AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(CAST(g.date AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(CAST(g.totalAmount AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(g.supplier.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<GoodsReceipt> searchAll(@Param("keyword") String keyword, Pageable pageable);
}
