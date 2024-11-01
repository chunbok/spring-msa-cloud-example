package yoontae.rab.jwt;

import auth.AuthInformation;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jpa.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application-info.jwt.secret}")
    private String secret;
    @Value("${application-info.jwt.default-access-expire-days}")
    private long expireDayForAccess;
    @Value("${application-info.jwt.default-refresh-expire-days}")
    private long expireDayForRefresh;
    @Value("${application-info.jwt.default-direct-expire-days}")
    private long expireDayForDirect;

    public String generateToken(User payLoad, AuthInformation.TokenType type) {
        LocalDateTime now = LocalDateTime.now();

//        Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.RS256.getValue());
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        LocalDateTime exp = switch (type) {
            case ACCESS -> LocalDateTime.now().plusDays(expireDayForAccess);
            case REFRESH -> LocalDateTime.now().plusDays(expireDayForRefresh);
            case DIRECT -> LocalDateTime.now().plusDays(expireDayForDirect);
        };

        return Jwts.builder()
                .subject(AuthInformation.TokenSubject.MSA.getSubject())
                .issuedAt(new Date())
                .expiration(Date.from(exp.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key)
                .claims(
                    Map.of(
                        AuthInformation.tokenTypeKey, type.name(),
                        AuthInformation.payloadYUserKey , payLoad
                    )
                )
                .compact();
    }

    public String generateToken(User user) {
        return this.generateToken(user, AuthInformation.TokenType.ACCESS);
    }
}
