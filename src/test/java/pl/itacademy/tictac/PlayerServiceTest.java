package pl.itacademy.tictac;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import pl.itacademy.tictac.domain.Player;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PlayerServiceTest {
    private PlayerRepository playerRepository;
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        playerRepository = new InMemoryPlayerRepository();
        playerService = new PlayerService(playerRepository);
    }

    @Test
    public void registerPlayer_savesPlayerToRepository() {
        Player player = playerService.registerPlayer("Jan", "Kowalski");
        Player expectedPlayer = new Player("Jan", "Kowalski");
        assertThat(player).isEqualTo(expectedPlayer);
        assertThat(playerRepository.getByName("Jan")).isEqualTo(expectedPlayer);
    }

    @Test
    public void registerPlayer_playerNameAlreadyExists_throwPlayerAlreadyExistsException() {

        PlayerAlreadyExistsException exception = (PlayerAlreadyExistsException.class),
        () ->
    }

    @Test
    public void getPlayerByNameAndPassword_playerDoesNotExists_throwsPlayerNotFoundException() {

    }

    @Test
    public void getPlayerByNameAndPassword_wrongPassword_throwsWrongPasswordsException() {

    }

    @Test
    public void getPlayerByNameAndPassword_correctPassword_returnsPlayer() {

    }

}