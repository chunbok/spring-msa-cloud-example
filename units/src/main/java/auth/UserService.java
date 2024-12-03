package auth;

import auth.result.RoleResult;
import auth.result.SessionResult;
import auth.result.UserResult;
import user.jpa.entity.AssignRole;
import user.jpa.entity.Role;
import user.jpa.entity.User;
import user.jpa.repository.RoleAssignRepository;
import user.jpa.repository.RoleRepository;
import user.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import user.redis.entity.Session;
import user.redis.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleAssignRepository roleAssignRepository;
    private final SessionRepository sessionRepository;
    private final JwtService jwtService;

    public UserResult register(String id, String password) {

        Optional<User> userInfo = userRepository.findById(id);

        if (userInfo.isEmpty()) {
            return UserResult.from(userRepository.save(User.builder().id(id).password(password).build()));

        } else {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "already exist userId");
            throw new RuntimeException("임시");
        }
    }

    ;

    public UserResult userServiceOut(String id) {
        Optional<User> userInfo = userRepository.findById(id);

        User user = userInfo.orElseThrow(() -> new RuntimeException("임시"));
        userRepository.delete(user);

        return UserResult.from(user);
    }

    public SessionResult login(String id) {
        User userInfo = userRepository.findById(id).orElseThrow(() -> new RuntimeException("임시"));

        // 다시 로그인 하면 논리 세션을 모두 지우고 다시 생성
        sessionRepository.findByUserId(id).forEach(session -> sessionRepository.deleteById(session.getId()));
        String directToken = jwtService.generateToken(userInfo, AUTH_TOKEN.TOKEN_TYPE.DIRECT);


        return SessionResult.from(
                sessionRepository.save(
                        Session.builder()
                                .userId(userInfo.getId())
                                .createdAt(LocalDateTime.now())
                                .id(directToken)
                                .build()
                )
        );
    }

    public RoleResult registerRole(AUTH_ROLE.SERVICE serviceRole, AUTH_ROLE.CONTACT contactRole, String description) {
        return RoleResult.from(this.roleRepository.save(Role.getAddEntity(
                serviceRole
                , contactRole
                , description)));
    }

    public String assignRole(int userNo, List<PAIR_ROLE> roles) {
        //TODO active 상태 확인해야 됨

        List<AssignRole> resultList = this.roleAssignRepository.saveAll(
                roles.stream().map(role -> AssignRole.getSave(userNo, role)).toList()
        );
        // TODO response 타입이 달라야 하기때문에 throw로 던져야 한다.
        return resultList.size() == roles.size() ? "성공" : "실패";


    }
}
