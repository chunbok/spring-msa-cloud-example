package yoontae.rab.user.param;

import jpa.dto.PairRoleInfo;
import lombok.Builder;

import java.util.List;

@Builder
public record AssignInfo(
        int userNo,
        String userId,
        List<PairRoleInfo> roles
) {

}
