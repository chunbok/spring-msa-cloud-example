package auth.result;

import user.jpa.entity.AssignRole;
import user.jpa.entity.Role;
import lombok.Builder;

@Builder
public record RoleResult(
        String serviceRole
        , String contactRole
        , String serviceRoleName
        , String contactRoleName
        , String description
) {
    public static RoleResult from(Role role) {
        return RoleResult.builder()
                .serviceRole(role.getId().getServiceRole())
                .contactRole(role.getId().getContactRole())
                .serviceRoleName(role.getServiceRoleName())
                .contactRoleName(role.getContactRoleName())
                .description(role.getDescription())
                .build();
    }

    public static RoleResult from(AssignRole assignRole) {
        return from(assignRole.getRole());
    }
}
