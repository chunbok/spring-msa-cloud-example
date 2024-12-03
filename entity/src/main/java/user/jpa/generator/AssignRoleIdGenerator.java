package user.jpa.generator;

import user.jpa.entity.AssignRole;
import user.jpa.plugs.AssignRoleId;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class AssignRoleIdGenerator implements IdentifierGenerator {

    @Override
    public AssignRoleId generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        AssignRole role = (AssignRole) o;

        return AssignRoleId.builder()
                .userNo(role.getUser().getNo())
                .roleId(RoleIdGenerator.generateIdByInfo(role.getRole().getRoleInfo()))
                .build();
    }
}
