package pl.itacademy.tictac.domain;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message){ super(message);}
}
