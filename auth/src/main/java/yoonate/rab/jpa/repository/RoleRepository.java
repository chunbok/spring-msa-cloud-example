package yoonate.rab.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonate.rab.jpa.entity.Role;
import yoonate.rab.jpa.plugs.RoleId;

public interface RoleRepository extends JpaRepository<Role, RoleId> {
}
