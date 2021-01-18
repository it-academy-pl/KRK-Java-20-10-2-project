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

    //TODO: fix a bug with non-saved game
    //FIXME: start from test
    public Game createGame(String playerName, String playerPassword) {
        Game game = new Game();
        Player player = playerService.getPlayerByNameAndPassword(playerName, playerPassword);
        game.setPlayerX(player);
        return game;
    }

    public Game joinGame(long gameId, String playerName, String playerPassword) {
        Game game = gameRepository.getById(gameId)
                .orElseThrow(() -> new GameNotFoundException(String.format("Game %d not found", gameId)));
        if (game.getGameStatus() != GameStatus.NEW_GAME) {
            throw new GameNotAvailableForRegistrationException(String.format("The game %d has already started!", gameId));
        } else {
            Player joinedPlayer =
                    playerService.getPlayerByNameAndPassword(playerName, playerPassword);
            game.setPlayerO(joinedPlayer);
            game.setGameStatus(GameStatus.MOVE_X);
        }
        return game;
    }

    public Game makeMove(long gameId, int gridPosition, String playerName, String playerPassword) {
        Game game = gameRepository.getById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        assertGameStarted(game);
        assertGameNotFinished(game);

        Player player = playerService.getPlayerByNameAndPassword(playerName, playerPassword);
        assertPlayerRegisteredToGame(game, player);

        //TODO: try to get rid of code duplication
        if ((game.getGameStatus() == GameStatus.MOVE_O) && (player.equals(game.getPlayerX()))) {
            throw new IllegalMoveException("That's not your turn!");
        } else if ((game.getGameStatus() == GameStatus.MOVE_X) && (player.equals(game.getPlayerO()))) {
            throw new IllegalMoveException("That's not your turn!");
        }

        char[] grid = game.getGrid();
        assertCellIsFree(grid, gridPosition);

        grid[gridPosition] = game.getGameStatus().getSymbol();

        verifyIfGameBeenFinished(game);

        if (game.getGameStatus() == GameStatus.MOVE_X) {
            game.setGameStatus(GameStatus.MOVE_O);
        } else if (game.getGameStatus() == GameStatus.MOVE_O) {
            game.setGameStatus(GameStatus.MOVE_X);
        }

        return game;
    }

    //TODO: refactor the method to make it simpler
    private void verifyIfGameBeenFinished(Game game) {
        char[] grid = game.getGrid();
        if ((grid[0] == 'X' && grid[1] == 'X' && grid[2] == 'X') ||
                (grid[0] == 'X' && grid[3] == 'X' && grid[6] == 'X') ||
                (grid[6] == 'X' && grid[7] == 'X' && grid[8] == 'X') ||
                (grid[2] == 'X' && grid[5] == 'X' && grid[8] == 'X') ||
                (grid[1] == 'X' && grid[4] == 'X' && grid[7] == 'X') ||
                (grid[3] == 'X' && grid[4] == 'X' && grid[5] == 'X') ||
                (grid[0] == 'X' && grid[4] == 'X' && grid[8] == 'X') ||
                (grid[2] == 'X' && grid[4] == 'X' && grid[6] == 'X')) {
            game.setGameStatus(GameStatus.X_WON);
            return;
        }

        if ((grid[0] == 'O' && grid[1] == 'O' && grid[2] == 'O') ||
                (grid[0] == 'O' && grid[3] == 'O' && grid[6] == 'O') ||
                (grid[6] == 'O' && grid[7] == 'O' && grid[8] == 'O') ||
                (grid[2] == 'O' && grid[5] == 'O' && grid[8] == 'O') ||
                (grid[1] == 'O' && grid[4] == 'O' && grid[7] == 'O') ||
                (grid[3] == 'O' && grid[4] == 'O' && grid[5] == 'O') ||
                (grid[0] == 'O' && grid[4] == 'O' && grid[8] == 'O') ||
                (grid[2] == 'O' && grid[4] == 'O' && grid[6] == 'O')) {
            game.setGameStatus(GameStatus.O_WON);
            return;
        }

        boolean hasEmptyCells = false;
        for (char c : grid) {
            if (c == 0) {
                hasEmptyCells = true;
                break;
            }
        }

        if (!hasEmptyCells) {
            game.setGameStatus(GameStatus.DRAW);
        }
    }

    private void assertCellIsFree(char[] grid, int gridPosition) {
        if (grid[gridPosition] != 0) {
            throw new IllegalMoveException(String.format("Cell %d is not empty", gridPosition));
        }
    }

    private void assertGameNotFinished(Game game) {
        if (game.getGameStatus().isFinished()) {
            throw new IllegalMoveException("Game has been finished! Start new one.");
        }
    }

    private void assertGameStarted(Game game) {
        if (game.getGameStatus() == GameStatus.NEW_GAME) {
            throw new IllegalMoveException("Game is not started yet!");
        }
    }

    private void assertPlayerRegisteredToGame(Game game, Player player) {
        if (!game.getPlayerX().equals(player) && !game.getPlayerO().equals(player)) {
            throw new GameNotFoundException("Wrong game.");
        }
    }
}
