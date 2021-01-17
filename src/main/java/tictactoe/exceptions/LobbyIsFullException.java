package tictactoe.exceptions;

public class LobbyIsFullException extends RuntimeException {
    public LobbyIsFullException() {
        super("Unable to join: lobby is full.");
    }
}
