import config.UserUnitAutoConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import user.redis.repository.SessionRepository;

@DataRedisTest
@ImportAutoConfiguration(classes = {
        UserUnitAutoConfig.class
})
public class RedisTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    @DisplayName("세션에 유저가 존재하는지 확인해 본다.")
    public void checkSession () {

    }
}
