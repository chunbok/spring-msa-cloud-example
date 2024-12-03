package user.jpa.repository;


import user.jpa.entity.Role;
import user.jpa.plugs.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {
}
