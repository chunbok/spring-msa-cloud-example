package yoonate.rab.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonate.rab.jpa.entity.AssignRole;
import yoonate.rab.jpa.plugs.AssignRoleColumns;


public interface RoleAssignRepository extends JpaRepository<AssignRole, AssignRoleColumns> {
}
