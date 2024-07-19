package yoonate.rab.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonate.rab.jpa.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByNo(long no);
    public Optional<User> findById(String id);
}
