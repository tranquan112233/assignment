package poly.edu.ass_sof3022.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.ass_sof3022.model.Role;

@Repository
public interface RoleDAO extends JpaRepository<Role,Integer> {
}
