package pl.itacademy.tictac;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.itacademy.tictac.domain.Game;
import pl.itacademy.tictac.domain.Player;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.itacademy.tictac.domain.GameStatus.NEW_GAME;

class GameServiceTest {

    private GameService gameService;
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;

    @BeforeEach
    public void setUp() {
        gameRepository = new InMemoryGameRepository();
        playerRepository = new InMemoryPlayerRepository();
        gameService = new GameService(gameRepository, playerRepository);
    }

    @Test
    public void createGame_existingPlayer_newGameCreatedWithPlayerX() {
        Player player = new Player("Jan", "kowalski");
        playerRepository.save(player);

        Game game = gameService.createGame("Jan");

        assertThat(game.getGameStatus()).isEqualTo(NEW_GAME);
        assertThat(game.getPlayerX()).isEqualTo(player);
    }

}