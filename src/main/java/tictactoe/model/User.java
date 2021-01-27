package tictactoe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class User {
    private final String name;
    private final Password password;
    private final UUID id;
    private final Score playerScore;
    private Lobby activeLobby;

    public User(@JsonProperty("name") String name,
                @JsonProperty("password") String password) {
        this.name = name;
        this.password = new Password(password);
        this.id = UUID.randomUUID();
        this.playerScore = new Score();
        this.activeLobby = null;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public boolean logIn(String password) {
        // possibility to improvement -> player cannot play if not loggedIn <- this needs to by signalized by new boolean field
        return this.password.letMeIn(password);
    }

    public void saveGameResult(int result) {
        switch(result) {
            case -1:
                playerScore.addLose();
                break;
            case 0:
                playerScore.addDraw();
                break;
            case 1:
                playerScore.addWin();
                break;
            default:
                throw new IllegalArgumentException("-1, 0 or 1 was expected while saving players score.");
        }
    }

    public Score getUserScore() {
        return new Score(playerScore.getGamesPlayed(), playerScore.getWins(), playerScore.getDraws(), playerScore.getLoses());
    }

    public Lobby getActiveLobby() {
        return activeLobby;
    }

    public void setActiveLobby(Lobby activeLobby) {
        this.activeLobby = activeLobby;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, id);
    }
}
