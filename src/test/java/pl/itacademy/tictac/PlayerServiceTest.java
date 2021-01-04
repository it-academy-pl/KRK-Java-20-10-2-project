package pl.itacademy.tictac;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.itacademy.tictac.domain.Player;

import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceTest {
    private PlayerRepository playerRepository;
    private PlayerService playerService;

    @BeforeEach
    public void setUp(){
        playerRepository = new InMemoryPlayerRepository();
        playerService = new PlayerService(playerRepository);
    }

    @Test
    public void registerPlayer_savesPlayerToRepository(){

        Player player = playerService.registerPlayer("Jan", "Kowalski");
    }

}