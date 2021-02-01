package pl.itacademy.tictac;

import lombok.Data;

@Data
public class GameStatistic {
    private int won;
    private int draw;
    private int lost;

    public void incrementWon() {
        won++;
    }

    public void incrementLost() {
        lost++;
    }

    public void incrementDraw() {
        draw++;
    }
}
