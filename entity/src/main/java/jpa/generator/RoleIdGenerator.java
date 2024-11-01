package jpa.generator;

import jpa.dto.PairRoleInfo;
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

        return RoleIdGenerator.GenerateIdByInfo(role.getRoleInfo());
    }

    public static RoleId GenerateIdByInfo(PairRoleInfo info) {
        return Optional.of(info).map(pairRoleInfo -> RoleId.builder()
                .serviceRole(pairRoleInfo.getService().name())
                .contactRole(pairRoleInfo.getContact().name())
                .build()).orElseThrow(() -> new RuntimeException("temp"));
    }
}
