package pl.itacademy.tictac;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.itacademy.tictac.domain.Player;

@RequiredArgsConstructor
@Service

public class PlayerService {
    private final PlayerRepository playerRepository;


    public Player registerPlayer(String name, String password) {
        return null;
    }
}
