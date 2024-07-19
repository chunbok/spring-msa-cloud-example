package yoonate.rab.user.param;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import yoonate.rab.jpa.enums.AUTH_ROLE;

@Builder
public record RoleInfo(
    @NotNull
    AUTH_ROLE.SERVICE service,
    @NotNull
    AUTH_ROLE.CONTACT contact,
    String description
) {

}
