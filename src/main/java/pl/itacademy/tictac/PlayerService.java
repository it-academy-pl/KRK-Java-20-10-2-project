package pl.itacademy.tictac;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.itacademy.tictac.domain.Player;
import pl.itacademy.tictac.exception.PlayerAlreadyExistsException;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public Player registerPlayer(String name, String password) {
        Player player = playerRepository.getByName(name);
        if (nonNull(player)) {
            throw new PlayerAlreadyExistsException("Player already exists! Try to log in!");
        }

        player = new Player(name, password);
        playerRepository.save(player);
        return player;
    }
}
