package pl.itacademy.tictac;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.itacademy.tictac.domain.Game;
import pl.itacademy.tictac.domain.GameStatus;
import pl.itacademy.tictac.domain.Player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.itacademy.tictac.domain.GameStatus.*;

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

    //TODO: fix the implementation for make this test green
    @Test
    public void joinGame_notNewGameStatus_throwsGameNotAvailableForRegistrationException() {
        Game game = new Game();
        game.setGameStatus(MOVE_X);
        gameRepository.save(game);
        assertThrows(GameNotAvailableForRegistrationException.class,
                () -> gameService.joinGame(game.getId(), "Jan", "Kowalski1234"));
    }

    @Test
    public void joinGame_newGameStatus_joinsGameAsO_changesGameStatusToMoveX() {
        Game game = new Game();
        gameService.joinGame(123, "Jan", "kowalski1234");
        game.setGameStatus(NEW_GAME);
        assertThat(game.getGameStatus()).isEqualTo(MOVE_X);
//TODO: add the test
    }

    @Test
    public void makeMove_wrongGameId_throwsGameNotFoundException() {
        Game game = new Game();
        GameNotFoundException exception = assertThrows(GameNotFoundException.class,
                () -> game.setGameStatus(MOVE_O));
        assertThat(exception.getMessage()).isNotEqualTo(game.getId());
        //TODO: add the test

    }



}