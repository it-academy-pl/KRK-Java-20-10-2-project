package pl.itacademy.tictac.domain;

public class PlayerAlreadyExistsException extends RuntimeException{
    public PlayerAlreadyExistsException(String message) {super(message);
    }
}
