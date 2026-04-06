package j2ee.BuiTanThanh.repositories;

import j2ee.BuiTanThanh.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(Long id);

    @Query("SELECT r FROM Role r WHERE r.name = ?1")
    Role findRoleByName(String name);
}
