package pl.itacademy.tictac;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.itacademy.tictac.domain.Game;
import pl.itacademy.tictac.domain.GameStatus;
import pl.itacademy.tictac.domain.Player;
import pl.itacademy.tictac.exception.GameNotAvailableForRegistrationException;
import pl.itacademy.tictac.exception.GameNotFoundException;
import pl.itacademy.tictac.exception.IllegalMoveException;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;

    public Game createGame(String playerName, String playerPassword) {
        Game game = new Game();
        Player player = playerService.getPlayerByNameAndPassword(playerName, playerPassword);
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

    public void makeMove(long gameId, int gridPosition, String playerName, String playerPassword) {
        Game game =
                gameRepository.getById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));

        Player player = playerService.getPlayerByNameAndPassword(playerName, playerPassword);
        if(!game.getPlayerX().equals(player) && !game.getPlayerO().equals(player)) {
            throw new GameNotFoundException("Wrong game.");
        }

        char[] grid = game.getGrid();
        if (grid[gridPosition] != 0) {
            throw new IllegalMoveException(String.format("Cell %d is not empty", gridPosition));
        }

    }
}
