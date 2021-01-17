package tictactoe.exceptions;

public class LobbyNotFoundException extends RuntimeException {
    public LobbyNotFoundException() {
        super("Lobby not found.");
    }
}
