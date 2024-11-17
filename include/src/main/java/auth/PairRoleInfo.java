package auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PairRoleInfo {
    @NotNull
    AUTH_ROLE.SERVICE service;
    @NotNull
    AUTH_ROLE.CONTACT contact;

}
