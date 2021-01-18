package pl.itacademy.tictac;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.itacademy.tictac.domain.Game;
import pl.itacademy.tictac.domain.Player;
import pl.itacademy.tictac.exception.GameNotAvailableForRegistrationException;
import pl.itacademy.tictac.exception.GameNotFoundException;
import pl.itacademy.tictac.exception.IllegalMoveException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.itacademy.tictac.domain.GameStatus.*;

class GameServiceTest {

    private GameService gameService;
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;

    @BeforeEach
    public void setUp() {
        gameRepository = new InMemoryGameRepository();
        playerRepository = new InMemoryPlayerRepository();
        PlayerService playerService = new PlayerService(playerRepository);
        gameService = new GameService(gameRepository, playerService);
    }

    @Test
    void createGame_existingPlayer_newGameCreatedWithPlayerX() {
        Player player = new Player("Jan", "kowalski");
        playerRepository.save(player);

        Game game = gameService.createGame("Jan", "kowalski");

        assertThat(game.getGameStatus()).isEqualTo(NEW_GAME);
        assertThat(game.getPlayerX()).isEqualTo(player);
    }

    @Test
    void joinGame_gameIdDoesNotExists_throwsGameNotFoundException() {
        GameNotFoundException exception = assertThrows(GameNotFoundException.class,
                () -> gameService.joinGame(42, "Jan", "Kowalski123"));
        assertThat(exception.getMessage()).contains("42");
    }

    @Test
    void joinGame_notNewGameStatus_throwsGameNotAvailableForRegistrationException() {
        Game game = new Game();
        game.setGameStatus(MOVE_X);
        gameRepository.save(game);

        long gameId = game.getId();
        GameNotAvailableForRegistrationException exception = assertThrows(GameNotAvailableForRegistrationException.class,
                () -> gameService.joinGame(gameId, "Jan", "Kowalski"));
        assertThat(exception).hasMessage(String.format("The game %d has already started!", game.getId()));
    }

    @Test
    void joinGame_newGameStatus_joinsGameAsO_changesGameStatusToMoveX() {
        Game game = new Game();
        gameRepository.save(game);

        Player playerO = new Player("Eva", "Nowak");
        playerRepository.save(playerO);

        gameService.joinGame(game.getId(), "Eva", "Nowak");

        assertThat(game.getGameStatus()).isEqualTo(MOVE_X);
        assertThat(game.getPlayerO()).isEqualTo(playerO);
    }

    @Test
    void makeMove_wrongGameId_throwsGameNotFoundException() {
        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> gameService.makeMove(42, 0, "Jan", "password"));
        assertThat(exception.getMessage()).contains("Game not found");
    }

    @Test
    void makeMove_wrongPlaceOnGrid_throwsIllegalMoveException() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerO = new Player("Jan", "Kowalski");
        Player playerX = new Player("Ewa", "Nowak");
        playerRepository.save(playerO);
        playerRepository.save(playerX);
        game.setPlayerO(playerO);
        game.setPlayerX(playerX);
        game.setGameStatus(MOVE_O);

        char[] grid = game.getGrid();
        grid[0] = 'X';

        long gameId = game.getId();
        IllegalMoveException exception =
                assertThrows(IllegalMoveException.class, () -> gameService.makeMove(gameId, 0, "Jan", "Kowalski"));
        assertThat(exception).hasMessage("Cell 0 is not empty");
    }

    @Test
    void makeMove_playerNotParticipatingInTheGame_throwsGameNotFoundException() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerO = new Player("Jan", "Kowalski");
        Player playerX = new Player("Ewa", "Nowak");
        playerRepository.save(playerO);
        playerRepository.save(playerX);
        game.setPlayerO(playerO);
        game.setPlayerX(playerX);

        Player cheater = new Player("Ryszard", "Ryszard123");
        playerRepository.save(cheater);
        game.setGameStatus(MOVE_X);

        long gameId = game.getId();
        GameNotFoundException exception =
                assertThrows(GameNotFoundException.class, () -> gameService.makeMove(gameId, 0, "Ryszard", "Ryszard123"));
        assertThat(exception).hasMessage("Wrong game.");
    }

    @Test
    void makeMove_playerO_gameWithPlayerXState_throwsIllegalMoveException() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerX = new Player("Jan", "Jan123");
        Player playerO = new Player("Ewa", "Ewa123");
        playerRepository.save(playerX);
        playerRepository.save(playerO);
        game.setPlayerX(playerX);
        game.setPlayerO(playerO);

        game.setGameStatus(MOVE_X);
        long gameId = game.getId();
        IllegalMoveException exception_wrongMoveO =
                assertThrows(IllegalMoveException.class,
                        () -> gameService.makeMove(gameId, 0, "Ewa", "Ewa123"));
        assertThat(exception_wrongMoveO).hasMessage("That's not your turn!");
    }

    @Test
    void makeMove_playerX_gameWithPlayerOState_throwsIllegalMoveException() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerX = new Player("Jan", "Jan123");
        Player playerO = new Player("Ewa", "Ewa123");
        playerRepository.save(playerX);
        playerRepository.save(playerO);
        game.setPlayerX(playerX);
        game.setPlayerO(playerO);

        game.setGameStatus(MOVE_O);
        long gameId = game.getId();
        IllegalMoveException exception_wrongMoveX =
                assertThrows(IllegalMoveException.class,
                        () -> gameService.makeMove(gameId, 0, "Jan", "Jan123"));
        assertThat(exception_wrongMoveX).hasMessage("That's not your turn!");
    }

    @Test
    void makeMove_gameNotStarted_throwsIllegalMoveException() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerX = new Player("Jan", "Jan123");
        playerRepository.save(playerX);
        game.setPlayerX(playerX);

        long gameId = game.getId();
        IllegalMoveException exception =
                assertThrows(IllegalMoveException.class,
                        () -> gameService.makeMove(gameId, 0, "Jan", "Jan123"));
        assertThat(exception).hasMessage("Game is not started yet!");

        assertThat(game.getGameStatus()).isNotEqualTo(MOVE_O);
        assertThat(game.getGameStatus()).isNotEqualTo(MOVE_X);
    }

    @Test
    void makeMove_gameFinished_throwsIllegalMoveException() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerX = new Player("Jan", "Jan123");
        playerRepository.save(playerX);
        game.setPlayerX(playerX);

        long gameId = game.getId();
        game.setGameStatus(O_WON);
        IllegalMoveException exception_Y_Won =
                assertThrows(IllegalMoveException.class,
                        () -> gameService.makeMove(gameId, 0, "Jan", "Jan123"));
        assertThat(exception_Y_Won).hasMessage("Game has been finished! Start new one.");

        game.setGameStatus(X_WON);
        IllegalMoveException exception_X_Won =
                assertThrows(IllegalMoveException.class,
                        () -> gameService.makeMove(gameId, 0, "Jan", "Jan123"));
        assertThat(exception_X_Won).hasMessage("Game has been finished! Start new one.");

        game.setGameStatus(DRAW);
        IllegalMoveException exception_Draw =
                assertThrows(IllegalMoveException.class,
                        () -> gameService.makeMove(gameId, 0, "Jan", "Jan123"));
        assertThat(exception_Draw).hasMessage("Game has been finished! Start new one.");
    }

    @Test
    void makeMove_legalMoveX_gameStatusChangesToAnotherPlayer() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerX = new Player("Jan", "Jan123");
        playerRepository.save(playerX);
        game.setPlayerX(playerX);

        game.setGameStatus(MOVE_X);
        gameService.makeMove(game.getId(), 0, "Jan", "Jan123");
        assertThat(game.getGameStatus()).isEqualTo(MOVE_O);
    }

    @Test
    void makeMove_legalMoveO_gameStatusChangesToAnotherPlayer() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerX = new Player("Jan", "Jan123");
        Player playerO = new Player("Ewa", "Ewa123");
        playerRepository.save(playerX);
        playerRepository.save(playerO);
        game.setPlayerX(playerX);
        game.setPlayerO(playerO);

        game.setGameStatus(MOVE_O);
        gameService.makeMove(game.getId(), 0, "Ewa", "Ewa123");
        assertThat(game.getGameStatus()).isEqualTo(MOVE_X);
    }

    @Test
    void makeMove_legalMove_gameCanBeFinishedWithDraw() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerX = new Player("Jan", "Jan123");
        Player playerO = new Player("Ewa", "Ewa123");
        playerRepository.save(playerX);
        playerRepository.save(playerO);
        game.setPlayerX(playerX);
        game.setPlayerO(playerO);
        game.setGameStatus(MOVE_X);

        gameService.makeMove(game.getId(), 0, "Jan", "Jan123");
        gameService.makeMove(game.getId(), 1, "Ewa", "Ewa123");
        gameService.makeMove(game.getId(), 2, "Jan", "Jan123");
        gameService.makeMove(game.getId(), 3, "Ewa", "Ewa123");
        gameService.makeMove(game.getId(), 4, "Jan", "Jan123");
        gameService.makeMove(game.getId(), 6, "Ewa", "Ewa123");
        gameService.makeMove(game.getId(), 5, "Jan", "Jan123");
        gameService.makeMove(game.getId(), 8, "Ewa", "Ewa123");
        gameService.makeMove(game.getId(), 7, "Jan", "Jan123");
        assertThat(game.getGameStatus()).isEqualTo(DRAW);
    }

    @Test
    void makeMove_legalMove_gameCanBeFinishedWithXWon() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerX = new Player("Jan", "Jan123");
        Player playerO = new Player("Ewa", "Ewa123");
        playerRepository.save(playerX);
        playerRepository.save(playerO);
        game.setPlayerX(playerX);
        game.setPlayerO(playerO);
        game.setGameStatus(MOVE_X);

        gameService.makeMove(game.getId(), 0, "Jan", "Jan123");
        gameService.makeMove(game.getId(), 4, "Ewa", "Ewa123");
        gameService.makeMove(game.getId(), 1, "Jan", "Jan123");
        gameService.makeMove(game.getId(), 5, "Ewa", "Ewa123");
        gameService.makeMove(game.getId(), 2, "Jan", "Jan123");
        assertThat(game.getGameStatus()).isEqualTo(X_WON);
    }

    @Test
    void makeMove_legalMove_gameCanBeFinishedWithOWon() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerX = new Player("Jan", "Jan123");
        Player playerO = new Player("Ewa", "Ewa123");
        playerRepository.save(playerX);
        playerRepository.save(playerO);
        game.setPlayerX(playerX);
        game.setPlayerO(playerO);
        game.setGameStatus(MOVE_O);

        gameService.makeMove(game.getId(), 0, "Ewa", "Ewa123");
        gameService.makeMove(game.getId(), 4, "Jan", "Jan123");
        gameService.makeMove(game.getId(), 1, "Ewa", "Ewa123");
        gameService.makeMove(game.getId(), 5, "Jan", "Jan123");
        gameService.makeMove(game.getId(), 2, "Ewa", "Ewa123");
        assertThat(game.getGameStatus()).isEqualTo(O_WON);
    }
}