package jpa.repository;


import jpa.entity.Role;
import jpa.plugs.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {
}
