package auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface AUTH_ROLE {

    interface Named{
        String getRoleName();
    }

    @AllArgsConstructor
    @Getter
    enum SERVICE implements Named {
        BERRIES("berries service"),
        NUTS("nuts service");
        private final String roleName;
    }


    @AllArgsConstructor
    @Getter
    enum CONTACT implements Named {
        OPEN("open authorized"),
        NORMAL_USER("normal user authorized");
        private final String roleName;
    }
}
