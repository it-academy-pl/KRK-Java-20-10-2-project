package pl.itacademy.tictac;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.itacademy.tictac.domain.Player;
import pl.itacademy.tictac.exception.PlayerAlreadyExistsException;
import pl.itacademy.tictac.exception.PlayerNotFoundException;
import pl.itacademy.tictac.exception.WrongPasswordException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Player player = new Player("Jan", "Kowalski");
        playerRepository.save(player);

        PlayerAlreadyExistsException exception = assertThrows(PlayerAlreadyExistsException.class,
                () -> playerService.registerPlayer("Jan", "ABCD123"));
        assertThat(exception.getMessage()).contains("Player already exists");
    }

    @Test
    public void getPlayerByNameAndPassword_playerDoesNotExists_throwsPlayerNotFoundException() {
        PlayerNotFoundException exception = assertThrows(PlayerNotFoundException.class,
                () -> playerService.getPlayerByNameAndPassword("Jan", "Kowlaski"));
        assertThat(exception).hasMessage("Player not found");
    }

    @Test
    public void getPlayerByNameAndPassword_wrongPassword_throwsWrongPasswordsException() {
        Player player = new Player("Jan", "Kowalski1");
        playerRepository.save(player);
        WrongPasswordException exception = assertThrows(WrongPasswordException.class,
                () -> playerService.getPlayerByNameAndPassword("Jan", "Kowalski"));
        assertThat(exception).hasMessage("Wrong password!");
    }

    @Test
    public void getPlayerByNameAndPassword_correctPassword_returnsPlayer() {
        Player jan = new Player("Jan", "k0w@lsk1");
        playerRepository.save(jan);

        Player player = playerService.getPlayerByNameAndPassword("Jan", "k0w@lsk1");
        assertThat(player).isEqualTo(jan);
    }

}