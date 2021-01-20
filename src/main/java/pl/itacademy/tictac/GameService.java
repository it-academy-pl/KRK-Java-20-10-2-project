package pl.itacademy.tictac;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.itacademy.tictac.domain.Game;
import pl.itacademy.tictac.domain.GameStatus;
import pl.itacademy.tictac.domain.Player;
import pl.itacademy.tictac.exception.GameNotAvailableForRegistrationException;
import pl.itacademy.tictac.exception.GameNotFoundException;
import pl.itacademy.tictac.exception.IllegalMoveException;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
//TODO: add and implement methods playAgain and getStats (create StatisticResponse class for that)
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;

    public Game createGame(String playerName, String playerPassword) {
        Player player = playerService.getPlayerByNameAndPassword(playerName, playerPassword);

        Game game = new Game();
        game.setPlayerX(player);
        gameRepository.save(game);
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
        //ROW
        char[][] grid2d = new char[3][3];
        grid2d[0] = Arrays.copyOfRange(grid, 0, 3);
        grid2d[1] = Arrays.copyOfRange(grid, 3, 6);
        grid2d[2] = Arrays.copyOfRange(grid, 6, 9);
        for (int i = 0; i < 3; i++) {
            if (grid2d[i][0] == grid2d[i][1] && grid2d[i][1] == grid2d[i][2] && grid2d[i][0] == 'X') {
                game.setGameStatus(GameStatus.X_WON);
            } else if (grid2d[i][0] == grid2d[i][1] && grid2d[i][1] == grid2d[i][2] && grid2d[i][0] == 'O') {
                game.setGameStatus(GameStatus.O_WON);
            }
        }
        //COLUMN
        for (int j = 0; j < 3; j++) {
            if (grid2d[0][j] == grid2d[1][j] && grid2d[1][j] == grid2d[2][j] && grid2d[0][j] == 'X') {
                game.setGameStatus(GameStatus.X_WON);
            } else if (grid2d[0][j] == grid2d[1][j] && grid2d[1][j] == grid2d[2][j] && grid2d[0][j] == 'O') {
                game.setGameStatus(GameStatus.O_WON);
            }
        }
        //DIAGONAL
        if (grid2d[0][0] == grid2d[1][1] && grid2d[1][1] == grid2d[2][2] && grid2d[0][0] == 'X') {
            game.setGameStatus(GameStatus.X_WON);
        } else if (grid2d[0][0] == grid2d[1][1] && grid2d[1][1] == grid2d[2][2] && grid2d[0][0] == 'O') {
            game.setGameStatus(GameStatus.O_WON);
        }
//        if ((grid2d[0] == 'X' && grid2d[1] == 'X' && grid2d[2] == 'X') ||
//                (grid2d[0] == 'X' && grid2d[3] == 'X' && grid2d[6] == 'X') ||
//                (grid2d[6] == 'X' && grid2d[7] == 'X' && grid2d[8] == 'X') ||
//                (grid2d[2] == 'X' && grid2d[5] == 'X' && grid2d[8] == 'X') ||
//                (grid2d[1] == 'X' && grid2d[4] == 'X' && grid2d[7] == 'X') ||
//                (grid2d[3] == 'X' && grid2d[4] == 'X' && grid2d[5] == 'X') ||
//                (grid2d[0] == 'X' && grid2d[4] == 'X' && grid2d[8] == 'X') ||
//                (grid2d[2] == 'X' && grid2d[4] == 'X' && grid2d[6] == 'X')) {
//            game.setGameStatus(GameStatus.X_WON);
//            return;
//        }
//
//        if ((grid2d[0] == 'O' && grid2d[1] == 'O' && grid2d[2] == 'O') ||
//                (grid2d[0] == 'O' && grid2d[3] == 'O' && grid2d[6] == 'O') ||
//                (grid2d[6] == 'O' && grid2d[7] == 'O' && grid2d[8] == 'O') ||
//                (grid2d[2] == 'O' && grid2d[5] == 'O' && grid2d[8] == 'O') ||
//                (grid2d[1] == 'O' && grid2d[4] == 'O' && grid2d[7] == 'O') ||
//                (grid2d[3] == 'O' && grid2d[4] == 'O' && grid2d[5] == 'O') ||
//                (grid2d[0] == 'O' && grid2d[4] == 'O' && grid2d[8] == 'O') ||
//                (grid2d[2] == 'O' && grid2d[4] == 'O' && grid2d[6] == 'O')) {
//            game.setGameStatus(GameStatus.O_WON);
//            return;
//        }

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
