package pl.itacademy.tictac;

import org.springframework.stereotype.Component;
import pl.itacademy.tictac.domain.Player;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryPlayerRepository implements PlayerRepository {
    private final Map<String, Player> players = new HashMap<>();

    @Override
    public void save(Player player) {
        players.put(player.getName(), player);
    }

    @Override
    public Player getByName(String playerName) {
        return players.get(playerName);
    }
}
