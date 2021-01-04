package pl.itacademy.tictac;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.itacademy.tictac.domain.Game;
import pl.itacademy.tictac.domain.Player;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public Game createGame(String playerName) {
        Game game = new Game();
        Player player = playerRepository.getByName(playerName);
        game.setPlayerX(player);
        return game;
    }
}
