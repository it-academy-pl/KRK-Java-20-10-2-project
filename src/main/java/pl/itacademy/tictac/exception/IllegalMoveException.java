package pl.itacademy.tictac.exception;

public class IllegalMoveException extends RuntimeException {
    public IllegalMoveException(String message) {
        super(message);
    }
}
