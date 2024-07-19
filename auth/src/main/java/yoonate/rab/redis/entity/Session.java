package yoonate.rab.redis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@RedisHash(value="session", timeToLive = 1000 * 60 * 60 * 24 * 7)
@Builder
public class Session {

    @Id
    private String id;
    @Indexed
    private String userId;
    @JsonIgnore
    private long no;
    private LocalDateTime createdAt;

}
