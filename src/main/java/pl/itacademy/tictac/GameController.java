package pl.itacademy.tictac;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.itacademy.tictac.domain.Game;
import pl.itacademy.tictac.domain.GameStatus;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/games")
public class GameController {

    private final GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameResponse> createNewGame(@RequestParam("playerName") String playerName,
                                                      @RequestParam("playerPassword") String playerPassword) {
        return ResponseEntity.ok(GameResponse.fromGame(gameService.createGame(playerName, playerPassword)));
    }

    @PutMapping("/join")
    public ResponseEntity<GameResponse> joinGame(@RequestParam("gameId") Long gameId,
                                                 @RequestParam("playerName") String playerName,
                                                 @RequestParam("playerPassword") String playerPassword) {
        return ResponseEntity.ok(GameResponse.fromGame(gameService.joinGame(gameId, playerName, playerPassword)));
    }

    @PostMapping("/makeMove")
    public ResponseEntity<GameResponse> makeMove(@RequestParam("gameId") Long gameId,
                                                 @RequestParam("gridPosition") Integer gridPosition,
                                                 @RequestParam("playerName") String playerName,
                                                 @RequestParam("playerPassword") String playerPassword) {
        return ResponseEntity.ok(GameResponse.fromGame(gameService.makeMove(gameId, gridPosition, playerName, playerPassword)));
    }

    @PostMapping("/playAgain")
    public ResponseEntity<GameResponse> playAgain(@RequestParam("finishedGameId") long finishedGameId) {
        return ResponseEntity.ok(GameResponse.fromGame(gameService.playAgain(finishedGameId)));
    }

    @PostMapping("/statistic")
    public ResponseEntity<GameStatistic> getStatistic(@RequestParam("playerName") String playerName,
                                                      @RequestParam("playerPassword") String playerPassword) {
        GameStatistic statistic = gameService.statisticFor(playerName, playerPassword);
        return ResponseEntity.ok(statistic);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class GameResponse {
        private final long id;
        private final char[] grid;
        private String playerX;
        private String playerO;
        private GameStatus gameStatus;

        public static GameController.GameResponse fromGame(Game game) {
            return new GameController.GameResponse(
                    game.getId(),
                    game.getGrid(),
                    game.getPlayerX() == null ? "" : game.getPlayerX().getName(),
                    game.getPlayerO() == null ? "" : game.getPlayerO().getName(),
                    game.getGameStatus()
            );
        }
    }
}
