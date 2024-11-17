package beans.result;

import jpa.entity.User;
import lombok.Builder;

import java.util.List;

@Builder
public record UserResult(
    Integer no
    , String id
    , String password
    , boolean activated
    , List<RoleResult> roles
) {
    public static UserResult from(User user) {
        return UserResult.builder()
                .id(user.getId())
                .password(user.getPassword())
                .activated(user.isActivated())
                .roles(user.getAssignRoles().stream().map(RoleResult::from).toList())
                .build();
    }
}
