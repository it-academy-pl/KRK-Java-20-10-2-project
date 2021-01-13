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
import static pl.itacademy.tictac.domain.GameStatus.MOVE_X;
import static pl.itacademy.tictac.domain.GameStatus.NEW_GAME;

class GameServiceTest {

    private GameService gameService;
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        gameRepository = new InMemoryGameRepository();
        playerRepository = new InMemoryPlayerRepository();
        playerService = new PlayerService(playerRepository);
        gameService = new GameService(gameRepository, playerService);
    }

    @Test
    public void createGame_existingPlayer_newGameCreatedWithPlayerX() {
        Player player = new Player("Jan", "kowalski");
        playerRepository.save(player);

        Game game = gameService.createGame("Jan", "kowalski");

        assertThat(game.getGameStatus()).isEqualTo(NEW_GAME);
        assertThat(game.getPlayerX()).isEqualTo(player);
    }

    @Test
    public void joinGame_gameIdDoesNotExists_throwsGameNotFoundException() {
        GameNotFoundException exception = assertThrows(GameNotFoundException.class,
                () -> gameService.joinGame(42, "Jan", "Kowalski123"));
        assertThat(exception.getMessage()).contains("42");
    }

    @Test
    public void joinGame_notNewGameStatus_throwsGameNotAvailableForRegistrationException() {
        Game game = new Game();
        game.setGameStatus(MOVE_X);
        gameRepository.save(game);
        GameNotAvailableForRegistrationException exception = assertThrows(GameNotAvailableForRegistrationException.class,
                () -> gameService.joinGame(game.getId(), "Jan", "Kowalski"));
        assertThat(exception).hasMessage("The game 2 has already started!");
    }

    @Test
    public void joinGame_newGameStatus_joinsGameAsO_changesGameStatusToMoveX() {
        Game game = new Game();
        gameRepository.save(game);

        Player playerO = new Player("Eva", "Nowak");
        playerRepository.save(playerO);

        gameService.joinGame(game.getId(), "Eva", "Nowak");

        assertThat(game.getGameStatus()).isEqualTo(MOVE_X);
        assertThat(game.getPlayerO()).isEqualTo(playerO);
    }

    @Test
    public void makeMove_wrongGameId_throwsGameNotFoundException() {
        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> gameService.makeMove(42, 0, "Jan", "password"));
        assertThat(exception.getMessage()).contains("Game not found");
    }

    @Test
    public void makeMove_wrongPlaceOnGrid_throwsIllegalMoveException() {
        Game game = new Game();
        gameRepository.save(game);
        Player playerO = new Player("Jan", "Kowalski");
        Player playerX = new Player("Ewa", "Nowak");
        playerRepository.save(playerO);
        playerRepository.save(playerX);
        game.setPlayerO(playerO);
        game.setPlayerX(playerX);

        char[] grid = game.getGrid();

        grid[0] = 'X';

        IllegalMoveException exception =
                assertThrows(IllegalMoveException.class, () -> gameService.makeMove(game.getId(), 0, "Jan", "Kowalski"));
        assertThat(exception).hasMessage("Cell 0 is not empty");


    }

    @Test
    public void makeMove_wrongPlayerTurn_throwsIllegalMoveException() {

    }

    @Test
    public void makeMove_gameNotStarted_throwsIllegalMoveException() {

    }

    @Test
    public void makeMove_gameFinished_throwsIllegalMoveException() {

    }

    @Test
    public void makeMove_legalMove_gameStatusChangesToAnotherPlayer() {

    }

    @Test
    public void makeMove_legalMove_gameCanBeFinishedWithDraw() {

    }

    @Test
    public void makeMove_legalMove_gameCanBeFinishedWithXWon() {

    }

    @Test
    public void makeMove_legalMove_gameCanBeFinishedWithOWon() {

    }
}