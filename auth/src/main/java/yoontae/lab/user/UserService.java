package yoontae.lab.user;

import auth.AuthInformation;
import jpa.dto.PairRoleInfo;
import jpa.entity.AssignRole;
import jpa.entity.Role;
import jpa.entity.User;
import jpa.enums.AUTH_ROLE;
import jpa.repository.RoleAssignRepository;
import jpa.repository.RoleRepository;
import jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import redis.entity.Session;
import redis.repository.SessionRepository;
import yoontae.lab.jwt.JwtService;

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
            return userRepository.save(User.builder(). id(id).password(password).build());
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "already exist userId");
        }
    };

    public User userServiceOut(String id) {
        Optional<User> userInfo = userRepository.findById(id);

        User user = userInfo.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        userRepository.delete(user);

        return user;
    }

    public Session login(String id) {
        User userInfo = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        // 다시 로그인 하면 논리 세션을 모두 지우고 다시 생성
        sessionRepository.findByUserId(id).forEach(session -> sessionRepository.deleteById(session.getId()));
        String directToken  = jwtService.generateToken(userInfo, AuthInformation.TokenType.DIRECT);


        return sessionRepository.save(
                Session.builder()
                        .userId(userInfo.getId())
                        .createdAt(LocalDateTime.now())
                        .id(directToken)
                        .build()
        );
    }

    public Role registerRole(AUTH_ROLE.SERVICE serviceRole, AUTH_ROLE.CONTACT contactRole, String description) {
            return this.roleRepository.save(Role.getAddEntity(
                                            serviceRole
                                            , contactRole
                                            , description));
    }

    public String assignRole(int userNo, List<PairRoleInfo> roles) {
        //TODO active 상태 확인해야 됨

        List<AssignRole> resultList = this.roleAssignRepository.saveAll(
            roles.stream().map(role -> AssignRole.getSave(userNo, role)).toList()
        );
        // TODO response 타입이 달라야 하기때문에 throw로 던져야 한다.
        return resultList.size() == roles.size() ? "성공" : "실패";
    }

}