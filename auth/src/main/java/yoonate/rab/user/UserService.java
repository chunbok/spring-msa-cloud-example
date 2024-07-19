package yoonate.rab.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import yoonate.rab.jpa.entity.AssignRole;
import yoonate.rab.jpa.entity.Role;
import yoonate.rab.jpa.enums.AUTH_ROLE;
import yoonate.rab.jpa.entity.User;
import yoonate.rab.jpa.plugs.RoleId;
import yoonate.rab.jpa.repository.RoleAssignRepository;
import yoonate.rab.jpa.repository.RoleRepository;
import yoonate.rab.jpa.repository.UserRepository;
import yoonate.rab.jwt.JwtService;
import yoonate.rab.redis.entity.Session;
import yoonate.rab.redis.repository.SessionRepository;
import yoonate.rab.user.dto.PairRoleInfo;
import yoonate.rab.user.param.AssignInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleAssignRepository roleAssignRepository;
    private final SessionRepository sessionRepository;
    private final JwtService jwtService;

    public User register(String id, String password) {

        Optional<User> userInfo = userRepository.findById(id);

        if(userInfo.isEmpty()) {
            return userRepository.save(User.builder(). id(id).password(password).activated(true).build());
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "already exist userId");
        }
    };

    public boolean userServiceOut(String id) {
        Optional<User> userInfo = userRepository.findById(id);

        userInfo.ifPresent(userRepository::delete);

        return true;
    }

    public Session login(String id) {
        Optional<User> userInfo = userRepository.findById(id);

        // 다시 로그인 하면 논리 세션을 모두 지우고 다시 생성
        sessionRepository.findByUserId(id).forEach(session -> sessionRepository.deleteById(session.getId()));;

        return sessionRepository.save(
                userInfo.map(user -> {
                    String sessionId = jwtService.generateToken(user);

                    return Session.builder()
                            .id(sessionId)
                            .no(user.getNo())
                            .userId(user.getId())
                            .createdAt(LocalDateTime.now())
                            .build();
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No User Found"))
        );
    }

    public Role registerRole(AUTH_ROLE.SERVICE serviceRole, AUTH_ROLE.CONTACT contactRole, String description) {
            return this.roleRepository.save(Role.save(
                                            serviceRole
                                            , contactRole
                                            , description));
    }

    public String assignRole(int userNo, List<PairRoleInfo> roles) {
        //TODO active 상태 확인해야 됨

        List<AssignRole> resultList = this.roleAssignRepository.saveAll(
            roles.stream().map(role -> AssignRole.save(userNo, role)).toList()
        );
        // TODO response 타입이 달라야 하기때문에 throw로 던져야 한다.
        return resultList.size() == roles.size() ? "성공" : "실패";
    }

}
