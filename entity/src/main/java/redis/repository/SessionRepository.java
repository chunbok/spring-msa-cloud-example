package redis.repository;

import org.springframework.data.repository.CrudRepository;
import redis.entity.Session;

import java.util.List;

public interface SessionRepository extends CrudRepository<Session, String> {
        List<Session> findByUserId(String userId);
}
