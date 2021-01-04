package pl.itacademy.tictac;

import pl.itacademy.tictac.domain.Player;

public interface PlayerRepository {
    void save(Player player);

    Player getByName(String playerName);
}
