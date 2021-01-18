package pl.itacademy.tictac.domain;

public class WrongPasswordsException extends RuntimeException {
    public WrongPasswordsException(String message){ super(message);}
}
