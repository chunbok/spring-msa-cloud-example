package jpa.dto;

import jakarta.validation.constraints.NotNull;
import jpa.enums.AUTH_ROLE;
import jpa.plugs.RoleId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PairRoleInfo {
    @NotNull
    AUTH_ROLE.SERVICE service;
    @NotNull
    AUTH_ROLE.CONTACT contact;

    public static RoleId GenerateIdByInfo(PairRoleInfo info) {
        return Optional.of(info).map(pairRoleInfo -> RoleId.builder()
                .serviceRole(pairRoleInfo.getService().name())
                .contactRole(pairRoleInfo.getContact().name())
                .build()).orElseThrow(() -> new RuntimeException("temp"));
    }
}
