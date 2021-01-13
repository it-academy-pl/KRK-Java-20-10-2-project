package pl.itacademy.tictac.exception;

public class WrongPasswordsException extends RuntimeException {
    public WrongPasswordsException(String message){ super(message);}
}
