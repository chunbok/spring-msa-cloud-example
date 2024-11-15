package yoontae.lab.user.param;

import jakarta.validation.constraints.NotNull;
import jpa.enums.AUTH_ROLE;
import lombok.Builder;

@Builder
public record RoleInfo(
    @NotNull
    AUTH_ROLE.SERVICE service,
    @NotNull
    AUTH_ROLE.CONTACT contact,
    String description
) {

}
