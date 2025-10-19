package poly.edu.ass_sof3022.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.ass_sof3022.model.Address;

import java.util.List;

public interface AddressDAO extends JpaRepository<Address, Integer> {
    Page<Address> findByUserId(Integer userId, Pageable pageable);
    List<Address> findByUserId(Long userId);
}
