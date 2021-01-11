package pl.itacademy.tictac;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.itacademy.tictac.domain.Game;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/games")
public class GameController {

    private final GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<Game> createNewGame(@RequestParam("playerName") String playerName,
                                              @RequestParam("playerPassword") String playerPassword) {
        return ResponseEntity.ok(gameService.createGame(playerName, playerPassword));
    }
}
