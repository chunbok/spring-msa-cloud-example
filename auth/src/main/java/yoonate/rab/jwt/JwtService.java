package yoonate.rab.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yoonate.rab.jpa.entity.User;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application-info.jwt.secret}")
    private String secret;
    @Value("${application-info.jwt.default-expire-days}")
    private long expireDay;

    public String generateToken(User payLoad) {
        LocalDateTime now = LocalDateTime.now();

//        Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.RS256.getValue());
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        LocalDateTime exp = now.plusDays(expireDay);

        return Jwts.builder()
                .subject("msa_auth")
                .issuedAt(new Date())
                .expiration(Date.from(exp.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key)
                .compact();
    }

}
