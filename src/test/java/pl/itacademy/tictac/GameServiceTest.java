package pl.itacademy.tictac;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.itacademy.tictac.domain.Game;
import pl.itacademy.tictac.domain.GameStatus;
import pl.itacademy.tictac.domain.Player;
import pl.itacademy.tictac.exception.GameNotAvailableForRegistrationException;
import pl.itacademy.tictac.exception.GameNotFoundException;

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


    @Test
    public void joinGame_notNewGameStatus_throwsGameNotAvailableForRegistrationException() {
        Game game = new Game();
        game.setGameStatus(MOVE_X);
        gameRepository.save(game);
        GameNotAvailableForRegistrationException exception = assertThrows(GameNotAvailableForRegistrationException.class,
                () -> gameService.joinGame(game.getId(), "Jan", "Kowalski"));
        assertThat(exception).hasMessage("Game not available to join");
    }

    @Test
    public void joinGame_newGameStatus_joinsGameAsO_changesGameStatusToMoveX() {
        Game game = new Game();
        gameRepository.save(game);
        Player joinedPlayer = new Player("Jan", "Kowalski");
        gameService.joinGame(game.getId(),"Jan", "Kowalski");
        assertThat(joinedPlayer).isEqualTo(game.getPlayerO());
        assertThat(game.getGameStatus()).isEqualTo(MOVE_X);

    }

    @Test
    public void makeMove_wrongGameId_throwsGameNotFoundException() {
       Game game = new Game();
       gameRepository.save(game);

       Player playerX = new Player("Damian", "Brodek");
       Player playerO = new Player("Jan", "Kowalski");

       playerRepository.save(playerX);
       playerRepository.save(playerO);

       game.setPlayerX(playerX);
       game.setPlayerO(playerO);

       GameNotFoundException exception = new GameNotFoundException("Wrong game id provided1");
       assertThrows(GameNotFoundException.class, () -> gameService.makeMove(42));
       assertThat(exception.getMessage()).contains("Wrong game id");

    }

}