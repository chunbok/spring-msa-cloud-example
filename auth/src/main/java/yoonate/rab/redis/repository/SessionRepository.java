package yoonate.rab.redis.repository;

import org.springframework.data.repository.CrudRepository;
import yoonate.rab.redis.entity.Session;

import java.util.List;
import java.util.Set;

public interface SessionRepository extends CrudRepository<Session, String> {
        List<Session> findByUserId(String userId);
}
