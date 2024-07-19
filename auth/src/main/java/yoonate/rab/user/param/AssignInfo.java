package yoonate.rab.user.param;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import yoonate.rab.jpa.enums.AUTH_ROLE;
import yoonate.rab.jpa.plugs.RoleId;
import yoonate.rab.user.dto.PairRoleInfo;

import java.util.List;

@Builder
public record AssignInfo(
        int userNo,
        String userId,
        List<PairRoleInfo> roles
) {

}
