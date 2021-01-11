package pl.itacademy.tictac;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.itacademy.tictac.domain.Player;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public Player registerPlayer(String name, String password) throws PlayerAlreadyExistsException {
        Player player = new Player(name, password);
        if (player.equals(playerRepository.getByName(name))) {
            throw new PlayerAlreadyExistsException("Player already exists! Try to log in!");
        } else {
            playerRepository.save(player);
            return player;
        }
    }
}
