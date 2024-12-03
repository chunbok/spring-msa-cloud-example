package user.redis.repository;

import org.springframework.data.repository.CrudRepository;
import user.redis.entity.Session;

import java.util.List;

public interface SessionRepository extends CrudRepository<Session, String> {
        List<Session> findByUserId(String userId);
}
