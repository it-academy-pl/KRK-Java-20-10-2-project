package tictactoe.model;

import java.util.Objects;
import java.util.UUID;

public class Lobby {
    private final String name;
    private final UUID id;
    private int numberOfPlayers;
    private final Game game;

    public Lobby(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.numberOfPlayers = 0;
        this.game = new Game();
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Game getGame() {
        return game;
    }

    public void join() {
        numberOfPlayers += 1;
    }

    public void leave() {
        numberOfPlayers -= 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return numberOfPlayers == lobby.numberOfPlayers && Objects.equals(name, lobby.name) && Objects.equals(game, lobby.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, numberOfPlayers, game);
    }
}