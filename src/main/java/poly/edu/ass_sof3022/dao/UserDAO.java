package poly.edu.ass_sof3022.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.ass_sof3022.model.User;

public interface UserDAO extends JpaRepository<User,Integer> {
    Page<User> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
