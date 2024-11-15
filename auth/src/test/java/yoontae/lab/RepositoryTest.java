package yoontae.lab;

import annotation.AutoConfigureUserService;
import jakarta.transaction.Transactional;
import jpa.dto.PairRoleInfo;
import jpa.entity.Role;
import jpa.entity.User;
import jpa.enums.AUTH_ROLE;
import jpa.plugs.RoleId;
import jpa.repository.RoleRepository;
import jpa.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureDataRedis // DataJpaTest에는 Redis이존성이 없으므로 redis 의존성을 추가
@AutoConfigureUserService
public class RepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    // autoconfiguration으로 주입됐음 오류가 안뜨게 하는 방법을 찾아야 함
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private UserRepository userRepository;

    @Autowired
    // autoconfiguration으로 주입됐음 오류가 안뜨게 하는 방법을 찾아야 함
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private RoleRepository roleRepository;

    @Test
    @DisplayName("No를 기반으로 User table을 조회한다.")
    public void UserSelectByNo() {
        Optional<User> user  = userRepository.findByNo(1);
        assertTrue(user.isEmpty());
    }

    @Test
    @DisplayName("Id를 기반으로 User table을 조회한다.")
    public void UserSelectById() {
        Optional<User> user  = userRepository.findById("chunbok");
        assertTrue(user.isEmpty());
    }

    @Test
    @DisplayName("user를 등록해본다.")
    public void registUser() {
        List<User> register = userRepository.saveAll(
            List.of(
                User.builder().id("test_user1").password("test_user1").build(),
                User.builder().id("test_user2").password("test_user2").build()
            )
        );

        testEntityManager.flush();
        testEntityManager.clear();

        Optional<User> user1  = userRepository.findById("test_user1");
        Optional<User> user2  = userRepository.findById("test_user2");

        assertTrue(user1.isPresent());
        assertTrue(user2.isPresent());

        assertEquals(register.get(0).getNo(), user1.get().getNo());
        assertEquals(register.get(1).getNo(), user2.get().getNo());
    }

    @Test
    @DisplayName("Role을 등록해본다.")
    @Transactional
    public void registerRole() {
        AUTH_ROLE.SERVICE serviceRole = AUTH_ROLE.SERVICE.NUTS;
        AUTH_ROLE.CONTACT contactRole = AUTH_ROLE.CONTACT.OPEN;

        this.roleRepository.save(Role.getAddEntity(serviceRole, contactRole, "test description"));

        testEntityManager.flush();
        testEntityManager.clear();

        Optional<Role> find = this.roleRepository.findById(RoleId.builder()
                .serviceRole(AUTH_ROLE.SERVICE.NUTS.name())
                .contactRole(AUTH_ROLE.CONTACT.OPEN.name())
                .build()
        );


        assertTrue(find.isPresent());
    }

    @Test
    @DisplayName("유저에게 role을 assign 한다.")
    @Transactional
    public void assignRoleToUser() {

        testEntityManager.clear();

        User user = userRepository.save(User.builder().id("test_user11").password("test_user11").build());

        user.addAssignRole(PairRoleInfo.builder().service(AUTH_ROLE.SERVICE.BERRIES).contact(AUTH_ROLE.CONTACT.OPEN).build(), "test description");
        user.addAssignRole(PairRoleInfo.builder().service(AUTH_ROLE.SERVICE.BERRIES).contact(AUTH_ROLE.CONTACT.NORMAL_USER).build(), "test description");

        testEntityManager.flush();
        testEntityManager.clear(); // 영속성 초기화 해서 다시 확인해본다.
        User findUser = userRepository.findById(user.getId()).get();

    }
}
