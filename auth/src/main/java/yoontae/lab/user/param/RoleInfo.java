package yoontae.lab.user.param;

import auth.AUTH_ROLE;
import jakarta.validation.constraints.NotNull;
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
