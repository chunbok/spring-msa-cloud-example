import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import yoonate.rab.AuthApplication;
import yoonate.rab.jpa.entity.AssignRole;
import yoonate.rab.jpa.entity.Role;
import yoonate.rab.jpa.entity.User;
import yoonate.rab.jpa.enums.AUTH_ROLE;
import yoonate.rab.jpa.plugs.RoleId;
import yoonate.rab.jpa.repository.RoleAssignRepository;
import yoonate.rab.jpa.repository.RoleRepository;
import yoonate.rab.jpa.repository.UserRepository;
import yoonate.rab.user.dto.PairRoleInfo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes=AuthApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleAssignRepository roleAssignRepository;

    @Test
    @DisplayName("No를 기반으로 User table을 조회한다.")
    public void UserSelectByNo() {
        Optional<User> user  = userRepository.findByNo(1);
        assertTrue(user.isPresent());
        assertEquals(user.get().getId(), "chunbok");
    }

    @Test
    @DisplayName("Id를 기반으로 User table을 조회한다.")
    public void UserSelectById() {
        Optional<User> user  = userRepository.findById("chunbok");
        assertTrue(user.isPresent());
        assertEquals(user.get().getNo(), 1);
    }

    @Test
    @DisplayName("user를 등록해본다.")
    public void registUser() {
        List<User> register = userRepository.saveAll(
            List.of(
                User.builder().id("test_user1").password("test_user1").activated(true).build(),
                User.builder().id("test_user2").password("test_user2").activated(true).build()
            )
        );

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

        Role register = this.roleRepository.save(Role.save(serviceRole, contactRole, "test description"));

        Optional<Role> find = this.roleRepository.findById(RoleId.builder()
                .serviceRole(AUTH_ROLE.SERVICE.NUTS.name())
                .contactRole(AUTH_ROLE.CONTACT.OPEN.name())
                .build()
        );

        testEntityManager.flush();

        assertTrue(find.isPresent());
        assertEquals(register, find.get());
    }

    @Test
    @DisplayName("유저에게 role을 assign 한다.")
    @Transactional
    public void assignRoleToUser() {

        testEntityManager.clear();

        User user = userRepository.save(User.builder().id("test_user11").password("test_user11").activated(true).build());

//        this.roleRepository.save(Role.save(AUTH_ROLE.SERVICE.BERRIES, AUTH_ROLE.CONTACT.OPEN, "test description"));
        this.roleRepository.save(Role.save(AUTH_ROLE.SERVICE.BERRIES, AUTH_ROLE.CONTACT.NORMAL_USER, "test description"));

        testEntityManager.flush();

        List<PairRoleInfo> roles = List.of(
            PairRoleInfo.builder().service(AUTH_ROLE.SERVICE.BERRIES).contact(AUTH_ROLE.CONTACT.OPEN).build(),
            PairRoleInfo.builder().service(AUTH_ROLE.SERVICE.BERRIES).contact(AUTH_ROLE.CONTACT.NORMAL_USER).build()
        );

        List<AssignRole> result = roleAssignRepository.saveAll(
            roles.stream().map(
                role -> AssignRole.save(user.getNo(), role)
            ).toList()
        );

        testEntityManager.flush();

        User findUser = userRepository.findById(user.getId()).get();
        testEntityManager.flush();

    }

    @Test
    @DisplayName("로그인에 사용될 role이 포함된 유저정보를 획득한다.")
    public void getUserInfoWithRole() {

    }
}
