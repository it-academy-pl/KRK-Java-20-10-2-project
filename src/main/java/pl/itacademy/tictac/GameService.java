package pl.itacademy.tictac;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.itacademy.tictac.domain.Game;
import pl.itacademy.tictac.domain.GameStatus;
import pl.itacademy.tictac.domain.Player;
import pl.itacademy.tictac.exception.GameNotAvailableForRegistrationException;
import pl.itacademy.tictac.exception.GameNotFoundException;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;

    public Game createGame(String playerName, String playerPassword) {
        Game game = new Game();
        Player player =
                playerService.getPlayerByNameAndPassword(playerName, playerPassword);
        game.setPlayerX(player);
        return game;
    }

    public void joinGame(long gameId, String playerName, String playerPassword) {
        Game game = gameRepository.getById(gameId).orElseThrow(() -> new GameNotFoundException(String.format("Game %d not found", gameId)));
        if (game.getGameStatus() != GameStatus.NEW_GAME) {
            throw new GameNotAvailableForRegistrationException(String.format("The game %d has already started!", gameId));
        } else {
            Player joinedPlayer = playerService.getPlayerByNameAndPassword(playerName, playerPassword);
            game.setPlayerO(joinedPlayer);
            game.setGameStatus(GameStatus.MOVE_X);
        }
    }

    public void makeMove(long gameId, String playerName, String playerPassword) {
        gameRepository.getById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));
    }
}

