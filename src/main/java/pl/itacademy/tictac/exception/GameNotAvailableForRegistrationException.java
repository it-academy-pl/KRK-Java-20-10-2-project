package pl.itacademy.tictac.exception;

public class GameNotAvailableForRegistrationException extends RuntimeException {

    public GameNotAvailableForRegistrationException(String message) {
        super(message);
    }
}
