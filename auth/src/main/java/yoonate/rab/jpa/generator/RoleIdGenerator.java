package yoonate.rab.jpa.generator;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.util.IdGenerator;
import yoonate.rab.jpa.entity.Role;
import yoonate.rab.jpa.plugs.RoleId;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class RoleIdGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        Role role = (Role) o;

        return RoleId.builder()
                .serviceRole(role.getRoleInfo().getService().name())
                .contactRole(role.getRoleInfo().getContact().name())
                .build();
    }
}
