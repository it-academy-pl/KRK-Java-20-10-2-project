package tictactoe.exceptions;

public class GameNotStartedException extends RuntimeException {
    public GameNotStartedException() {
        super("Game not started.");
    }
}
