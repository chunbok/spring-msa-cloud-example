package beans.result;

import lombok.Builder;
import redis.entity.Session;

import java.time.LocalDateTime;

@Builder
public record SessionResult(
        String id
        , String userId
        , long no
        , LocalDateTime createdAt
) {
    public static SessionResult from(Session session) {
        return SessionResult.builder()
                .id(session.getId())
                .userId(session.getUserId())
                .no(session.getNo())
                .createdAt(session.getCreatedAt())
                .build();
    }
}
