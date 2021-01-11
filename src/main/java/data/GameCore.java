package data;

import java.util.ArrayList;
import java.util.List;

public class GameCore {
    public static final GameCore instance = new GameCore();
    private final List<Player> loggedInPlayers;
    private final List<Lobby> gameLobbyList;

    public static GameCore getInstance() {
        return instance;
    }


    private GameCore() {
        loggedInPlayers = new ArrayList<>();
        gameLobbyList = new ArrayList<>();
    }

    public void registerNewPlayer(String name, String password) {
        PlayerDatabase.getInstance().registerNewPlayer(name, password);
    }

    public void singInPlayer(String playerName, String password) throws IllegalAccessException {
        for(Player player : PlayerDatabase.getInstance().getRegisteredPlayers()) {
            if(player.getName().equals(playerName)) {
                for(Player loggedPlayer : loggedInPlayers) {
                    if(loggedPlayer.getName().equals(playerName)) {
                        throw new UnsupportedOperationException("Player already logged in.");
                    }
                }

                if(player.logIn(password)) {
                    loggedInPlayers.add(player);
                } else {
                    throw new IllegalAccessException("Wrong password.");
                }
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
            }
        }
    }

    public void createNewLobby(String newLobbyName, String creatorName) {

        for(Lobby lobby : gameLobbyList) {
            if(lobby.getName().equals(newLobbyName)) {
                throw new UnsupportedOperationException("Lobby with this name already exist.");
            }
        }
        gameLobbyList.add(new Lobby(newLobbyName));
        joinCreatedLobby(newLobbyName, creatorName);
    }

    public void joinCreatedLobby(String lobbyName, String playerName) {

        Player player = searchForPlayer(playerName);

        if(player.getActiveLobby() != null) {
            throw new UnsupportedOperationException("Player is already in lobby.");
        }

        for(Lobby lobby : gameLobbyList) {
            if(lobby.getName().equals(lobbyName) && lobby.getNumberOfPlayers() < 2) {
                lobby.join();
                lobby.getGame().join(player);
                player.setActiveLobby(lobby);
            } else if(lobby.getName().equals(lobbyName)) {
                throw new UnsupportedOperationException("Lobby is full.");
            }
        }
    }

    public void leaveLobby(Player player) {
        if(player.getActiveLobby() == null) {
            throw new UnsupportedOperationException("Player didn't join any lobby.");
        }

        for(Lobby lobby : gameLobbyList) {
            if(lobby.equals(player.getActiveLobby())) {
                lobby.leave();
                lobby.getGame().leave(player);
                player.setActiveLobby(null);
            }
        }
    }

    public void startGame(String firstPlayerName, String secondPlayerName) {
        Player firstPlayer = searchForPlayer(firstPlayerName);
        Player secondPlayer = searchForPlayer(secondPlayerName);


        if(firstPlayer.getActiveLobby().equals(secondPlayer.getActiveLobby()) && firstPlayer.getActiveLobby() != null) {
            for(Lobby lobby : gameLobbyList) {
                if(lobby.equals(firstPlayer.getActiveLobby())) {

                    lobby.getGame().startGame();
                    break;
                }
            }
        } else {
            throw new UnsupportedOperationException("Lobby mismatch.");
        }
    }

    public boolean makeMoveInActiveGame(String playerName, Coordinates coordinates) {
        Player player = searchForPlayer(playerName);

        boolean moveCompleted = player.getActiveLobby().getGame().makeMove(player, coordinates);
        if(player.getActiveLobby().getGame().isFinish()) {
            System.out.println("Game Over!");
        }
        return moveCompleted;
    }

    private Player searchForPlayer(String name) {
        for(Player player : loggedInPlayers) {
            if(player.getName().equals(name)) {
                return player;
            }
        }
        throw new UnsupportedOperationException("Player not singed in.");
    }

    public void displayGrid(String playerName) {
        Lobby lobby = searchForPlayer(playerName).getActiveLobby();

        int[][] grid = lobby.getGame().getGrid();

        for (int[] ints : grid) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    public boolean gameEnd(String playerName) {
        Lobby lobby = searchForPlayer(playerName).getActiveLobby();
        return lobby.getGame().isFinish();
    }
}
