package pl.itacademy.tictac;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException (String message){
        super(message);
    }
}
