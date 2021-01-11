package pl.itacademy.tictac.exception;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String message) {
        super(message);
    }
}
