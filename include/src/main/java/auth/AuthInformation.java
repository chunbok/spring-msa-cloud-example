package auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
