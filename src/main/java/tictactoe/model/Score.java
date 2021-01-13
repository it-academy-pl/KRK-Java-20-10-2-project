package tictactoe.model;

public class Score {
    private long gamesPlayed;
    private long wins;
    private long draws;
    private long loses;

    public Score() {
        this(0,0,0,0);
    }

    public Score(long gamesPlayed, long wins, long draws, long loses) {
        this.gamesPlayed = gamesPlayed;
        this.wins = wins;
        this.draws = draws;
        this.loses = loses;
    }

    public long getGamesPlayed() {
        return gamesPlayed;
    }

    public long getWins() {
        return wins;
    }

    public long getDraws() {
        return draws;
    }

    public long getLoses() {
        return loses;
    }

    public void addWin() {
        gamesPlayed += 1;
        wins += 1;
    }

    public void addLose() {
        gamesPlayed += 1;
        loses += 1;
    }

    public void addDraw() {
        gamesPlayed += 1;
        draws += 1;
    }
}
