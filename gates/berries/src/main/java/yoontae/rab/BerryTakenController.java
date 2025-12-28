package yoontae.rab;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class BerryTakenController {

    @GetMapping(value = "/taken"
        , consumes = {MediaType.APPLICATION_JSON_VALUE}
        , produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<String> getTakenBerry(@RequestBody String berry) {
        log.info("Berry Taken: {}", berry);
        return ResponseEntity.ok("success");
    }
}
