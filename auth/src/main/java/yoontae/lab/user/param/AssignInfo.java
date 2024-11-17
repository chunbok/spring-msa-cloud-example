package yoontae.lab.user.param;

import auth.PairRoleInfo;
import lombok.Builder;

import java.util.List;

@Builder
public record AssignInfo(
        int userNo,
        String userId,
        List<PairRoleInfo> roles
) {

}
