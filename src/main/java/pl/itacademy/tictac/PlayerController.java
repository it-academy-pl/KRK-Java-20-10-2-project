package pl.itacademy.tictac;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.itacademy.tictac.domain.Player;

@Controller
@RequestMapping(path = "/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/register")
    public ResponseEntity<Player> registerNewPlayer(@RequestParam("name") String name, @RequestParam("password") String password) {
        return null;
    }
}
