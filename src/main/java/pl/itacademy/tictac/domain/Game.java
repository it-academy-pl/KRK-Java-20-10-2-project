package pl.itacademy.tictac.domain;

import lombok.Data;

@Data
public class Game {
    private static long nextId = 0;
    private final long id;
    private final char[] grid;
    private Player playerX;
    private Player playerO;
    private GameStatus gameStatus;

    public Game() {
        this.id = nextId++;
        grid = new char[9];
        gameStatus = GameStatus.NEW_GAME;
    }
}
