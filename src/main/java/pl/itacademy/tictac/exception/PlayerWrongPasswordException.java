package pl.itacademy.tictac.exception;

public class PlayerWrongPasswordException extends RuntimeException {
    public PlayerWrongPasswordException(String message) {
        super (message);
    }
}
