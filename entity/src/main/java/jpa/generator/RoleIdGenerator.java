package jpa.generator;

import auth.PairRoleInfo;
import jpa.entity.Role;
import jpa.plugs.RoleId;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Optional;

@Slf4j
public class RoleIdGenerator implements IdentifierGenerator {

    @Override
    public RoleId generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        Role role = (Role) o;

        return RoleIdGenerator.generateIdByInfo(role.getRoleInfo());
    }

    public static RoleId generateIdByInfo(PairRoleInfo info) {
        return Optional.of(info).map(pairRoleInfo -> RoleId.builder()
                .serviceRole(pairRoleInfo.getService().name())
                .contactRole(pairRoleInfo.getContact().name())
                .build()).orElseThrow(() -> new RuntimeException("temp"));
    }
}
