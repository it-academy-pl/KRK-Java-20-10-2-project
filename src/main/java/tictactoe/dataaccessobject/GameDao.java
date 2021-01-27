package tictactoe.dataaccessobject;

import tictactoe.model.Coordinates;
import tictactoe.model.Lobby;
import tictactoe.model.User;

import java.util.UUID;

public interface GameDao {
    boolean logInUser(String userName, String password);

    boolean logOutUser(String userName); //expand to get by ID

    UUID searchForLoggedInUserID(String userName);
    UUID searchForCreatedLobbiesID(String lobbyName);
    User getUserFromID(UUID id);
    Lobby getLobbyFromID(UUID id);

    void createNewLobby(String lobbyName, String userName);
    void createNewLobby(String lobbyName, UUID creatorID);

    void jointCreatedLobby(String lobbyName, String userName);
    boolean joinCreatedLobby(UUID lobbyID, UUID userID);

    void leaveLobby(UUID userID);

    void startGame(UUID firstUserID, UUID secondUserID);

    boolean makeMove(UUID userID, Coordinates coordinates);

    void printLobbyGrid(UUID lobbyID);

    boolean lobbyGameFinished(UUID lobbyID);

    //TODO:
//    - add switch symbols
//    - something conected to play again which will call method above
}
