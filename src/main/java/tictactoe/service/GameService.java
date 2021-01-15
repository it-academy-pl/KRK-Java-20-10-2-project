package tictactoe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import tictactoe.dataaccessobject.GameDao;
import tictactoe.model.Coordinates;
import tictactoe.model.Lobby;
import tictactoe.model.User;

import java.util.UUID;

@Service
public class GameService {
    private final GameDao gameDao;

    @Autowired
    public GameService(@Qualifier("gameDao") GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public boolean logInUser(String userName, String password) {
        return gameDao.logInUser(userName, password);
    }

    public boolean logOutUser(String userName) {
        return gameDao.logOutUser(userName);
    }

    public UUID searchForLoggedInUserID(String userName) {
        return gameDao.searchForLoggedInUserID(userName);
    }

    public UUID searchForCreatedLobbiesID(String lobbyName) {
        return gameDao.searchForCreatedLobbiesID(lobbyName);
    }

    public User getUserFromID(UUID id) {
        return gameDao.getUserFromID(id);
    }

    public Lobby getLobbyFromID(UUID id) {
        return gameDao.getLobbyFromID(id);
    }

    public void createNewLobby(String lobbyName, String userName) {
        gameDao.createNewLobby(lobbyName, userName);
    }

    public void createNewLobby(String lobbyName, UUID creatorID) {
        gameDao.createNewLobby(lobbyName, creatorID);
    }

    public void jointCreatedLobby(String lobbyName, String userName) {
        gameDao.jointCreatedLobby(lobbyName, userName);
    }

    public boolean joinCreatedLobby(UUID lobbyID, UUID userID) {
        return gameDao.joinCreatedLobby(lobbyID, userID);
    }

    public void leaveLobby(UUID userID) {
        gameDao.leaveLobby(userID);
    }

    public void startGame(UUID firstUserID, UUID secondUserID) {
        gameDao.startGame(firstUserID, secondUserID);
    }

    public boolean makeMove(UUID userID, Coordinates coordinates) {
        return gameDao.makeMove(userID, coordinates);
    }

    public void printLobbyGrid(UUID lobbyID) {
        gameDao.printLobbyGrid(lobbyID);
    }

    public boolean lobbyGameFinished(UUID lobbyID) {
        return gameDao.lobbyGameFinished(lobbyID);
    }
}
