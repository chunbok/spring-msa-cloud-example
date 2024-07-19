package yoonate.rab.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import yoonate.rab.jpa.entity.Role;
import yoonate.rab.jpa.entity.User;
import yoonate.rab.redis.entity.Session;
import yoonate.rab.user.param.AssignInfo;
import yoonate.rab.user.param.RoleInfo;
import yoonate.rab.user.param.UserInfo;

import java.util.Collections;
import java.util.Objects;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(value = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> doRegisterMember(@RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(userService.register(userInfo.userId(), userInfo.password()));
    }

    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Session> doLogin(@RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(userService.login(userInfo.userId()));
    }

    @DeleteMapping(value="user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> doUserOut(@RequestBody UserInfo userInfo) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.userServiceOut(userInfo.userId()));
    }

    @PutMapping(value="role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Role> registerRole(@RequestBody RoleInfo role) {
        return ResponseEntity.ok(userService.registerRole(role.service(), role.contact(), role.description()));
    }

    @PostMapping(value="assign_role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> assignRole(@RequestBody AssignInfo assignInfo) {
        // TODO 나중에 validation으로 처리해야 한다.
        // TODO response 타입이 달라야 하기때문에 throw로 던져야 한다.
        if(Objects.isNull(assignInfo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파라미터가 없습니다.");
        }
        if(!(assignInfo.userNo() != 0 || Objects.nonNull(assignInfo.userId()))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Identify가 없습니다.");
        }
        if(CollectionUtils.isEmpty(assignInfo.roles())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User role 정보가 없습니다.");
        }
        // TODO id가 있으면 no으로 획득한다.
        return ResponseEntity.ok(
            userService.assignRole(assignInfo.userNo(), assignInfo.roles())
        );
    }
}
