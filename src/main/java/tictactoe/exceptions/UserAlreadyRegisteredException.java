package tictactoe.exceptions;

public class UserAlreadyRegisteredException extends RuntimeException {
    public UserAlreadyRegisteredException(String name) {
        super(("User " + name + ", already registered."));
    }
}
