package auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 토큰/로그인 관련 액션을 위한 정보
 */
public class AuthInformation {

    public static final String tokenTypeKey = "type";
    public static final String payloadYUserKey = "user";

    private AuthInformation() {}

    public enum TokenType {
        ACCESS,
        REFRESH,
        DIRECT;
    }

    @RequiredArgsConstructor
    @Getter
    public enum TokenSubject {
        MSA("msa_subject");

        private final String subject;
    }
}
