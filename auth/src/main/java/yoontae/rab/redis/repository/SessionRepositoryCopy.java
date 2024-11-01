package yoontae.rab.redis.repository;

import org.springframework.data.repository.CrudRepository;
import yoontae.rab.redis.entity.SessionCopy;

import java.util.List;

public interface SessionRepositoryCopy extends CrudRepository<SessionCopy, String> {
        List<SessionCopy> findByUserId(String userId);
}
