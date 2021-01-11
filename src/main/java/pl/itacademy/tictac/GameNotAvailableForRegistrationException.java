package pl.itacademy.tictac;

public class GameNotAvailableForRegistrationException extends RuntimeException {

    public GameNotAvailableForRegistrationException(String message) {
        super(message);
    }
}
