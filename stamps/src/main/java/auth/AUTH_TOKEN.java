package auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 토큰/로그인 관련 액션을 위한 정보
 */
public class AUTH_TOKEN {

    public static final String tokenTypeKey = "type";
    public static final String payloadYUserKey = "user";

    private AUTH_TOKEN() {}

    public enum TOKEN_TYPE {
        ACCESS,
        REFRESH,
        DIRECT;
    }

    @RequiredArgsConstructor
    @Getter
    public enum TOKEN_SUBJECT {
        MSA("msa_subject");

        private final String subject;
    }
}
