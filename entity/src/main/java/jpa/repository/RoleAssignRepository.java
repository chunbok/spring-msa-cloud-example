package jpa.repository;


import jpa.entity.AssignRole;
import jpa.plugs.AssignRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleAssignRepository extends JpaRepository<AssignRole, AssignRoleId> {
    Optional<List<AssignRole>> findByInfoUserNo(Integer id);
}
