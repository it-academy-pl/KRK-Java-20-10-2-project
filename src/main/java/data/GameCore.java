package data;

import java.util.ArrayList;
import java.util.List;

public class GameCore {
    private final List<Player> loggedInPlayers;
    private final List<Player> gameLobbyList;


    public GameCore() {
        loggedInPlayers = new ArrayList<>();
        gameLobbyList = new ArrayList<>();
    }

    public void registerNewPlayer(String name, String password) {
        PlayerDatabase.getInstance().registerNewPlayer(name, password);
    }

    public void singInPlayer(String name, String password) throws IllegalAccessException {
        for(Player player : PlayerDatabase.getInstance().getRegisteredPlayers()) {
            if(player.getName().equals(name)) {
                for(Player loggedPlayer : loggedInPlayers) {
                    if(loggedPlayer.getName().compareTo(name) == 0) {
                        throw new UnsupportedOperationException("Player already logged in.");
                    }
                }

                if(player.logIn(password)) {
                    loggedInPlayers.add(player);
                } else {
                    throw new IllegalAccessException("Wrong password");
                }
            } else {
                throw new UnsupportedOperationException("Player doesn't exist.");
            }
        }
    }

    public void singOutPlayer(String name) {
        for(Player player : PlayerDatabase.getInstance().getRegisteredPlayers()) {
            if(player.getName().equals(name)) {
                for(Player loggedPlayer : loggedInPlayers) {
                    if(loggedPlayer.getName().equals(name)) {
                        loggedInPlayers.remove(player);
                    } else {
                        throw new UnsupportedOperationException("Player not logged in.");
                    }
                }
            } else {
                throw new UnsupportedOperationException("Player doesn't exist.");
            }
        }
    }

    public void createNewGame(String lobbyName) {
        //add to list, auto join, set active lobby name
        //in future move lobby name from player to some Map
    }

    public void joinCreatedGame() {
        //search for games, join
    }

    public void leaveGame() {
        //look for what lobby you are in, and if leave
    }

    public void play() {
        //here: -make move -check if end (>if all places full >if any won(checked inside))
        //all the magic
    }
}
