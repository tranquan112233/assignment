package poly.edu.ass_sof3022.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.ass_sof3022.model.OrderDetail;

public interface OrderDetailDAO extends JpaRepository<OrderDetail, Integer> {
}
