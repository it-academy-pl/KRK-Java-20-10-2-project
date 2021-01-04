package data;

import java.util.Objects;

public class Player implements Comparable<Player> {
    private final String name;
    private final Password password;
    private final Score playerScore;
    private String activeLobby;


    public Player(String name, String password) {
        this.name = name;
        this.password = new Password(password);
        this.playerScore = new Score();
        this.activeLobby = null;
    }

    public String getName() {
        return name;
    }

    public boolean logIn(String password) {
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
                throw new IllegalArgumentException("-1, 0 or 1 was expected.");
        }
    }

    public Score getPlayerScore() {
        return new Score(playerScore.getGamesPlayed(), playerScore.getWins(), playerScore.getDraws(), playerScore.getLoses());
    }

    public String getActiveLobby() {
        return activeLobby;
    }

    public void setActiveLobby(String activeLobby) {
        this.activeLobby = activeLobby;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) && Objects.equals(password, player.password) && Objects.equals(playerScore, player.playerScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, playerScore);
    }

    @Override
    public int compareTo(Player o) {
        return o.getName().compareTo(name);
    }
}
