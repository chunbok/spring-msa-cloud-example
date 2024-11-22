package yoontae.lab.user.param;

import auth.PAIR_ROLE;
import lombok.Builder;

import java.util.List;

@Builder
public record AssignInfo(
        int userNo,
        String userId,
        List<PAIR_ROLE> roles
) {

}
