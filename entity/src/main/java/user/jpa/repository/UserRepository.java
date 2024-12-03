package user.jpa.repository;


import user.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNo(long no);
    Optional<User> findById(String id);
}
