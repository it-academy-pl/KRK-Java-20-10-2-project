package pl.itacademy.tictac;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.itacademy.tictac.domain.Player;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public Player registerPlayer(String name, String password) {
        Player player = new Player(name,password);
        playerRepository.save(player);
        return player;
    }
}
