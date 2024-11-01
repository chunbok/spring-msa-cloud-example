package jpa.generator;

import jpa.entity.AssignRole;
import jpa.plugs.AssignRoleId;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class AssignRoleIdGenerator implements IdentifierGenerator {

    @Override
    public AssignRoleId generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        AssignRole role = (AssignRole) o;

        return AssignRoleId.builder()
                .userNo(role.getUser().getNo())
                .roleId(RoleIdGenerator.GenerateIdByInfo(role.getRole().getRoleInfo()))
                .build();
    }
}
