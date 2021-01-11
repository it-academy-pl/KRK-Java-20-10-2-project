package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerDatabase {
    private static final PlayerDatabase instance = new PlayerDatabase();
    private final List<Player> registeredPlayers;

    public static PlayerDatabase getInstance() {
        return instance;
    }

    private PlayerDatabase() {
        this.registeredPlayers = new ArrayList<>();
    }

    public List<Player> getRegisteredPlayers() {
        return Collections.unmodifiableList(registeredPlayers);
    }

    public void registerNewPlayer(String name, String password) {
        for(Player player : registeredPlayers) {
            if(player.getName().equals(name)) {
                throw new UnsupportedOperationException("Player already registered");
            }
        }
        registeredPlayers.add(new Player(name,password));
    }

    public void removePlayer(String name) {
        registeredPlayers.removeIf(player -> player.getName().equals(name));
    }
}
