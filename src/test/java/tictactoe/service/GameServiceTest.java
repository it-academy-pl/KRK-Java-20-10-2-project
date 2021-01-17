package tictactoe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tictactoe.dataaccessobject.GameDao;
import tictactoe.dataaccessobject.GameDataAccessService;
import tictactoe.dataaccessobject.UserDao;
import tictactoe.dataaccessobject.UserDataAccessService;
import tictactoe.exceptions.GameNotStartedException;
import tictactoe.exceptions.LobbyIsFullException;
import tictactoe.exceptions.LobbyNotFoundException;
import tictactoe.model.*;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameServiceTest {

    private UserDao userDao;
    private GameDao gameDao;

    @BeforeEach
    public void setUp() {
        userDao = new UserDataAccessService();
        gameDao = new GameDataAccessService(userDao);
    }

    //FIXME: singleton class is blocking me from executing test all at once
    @Test
    public void createNewLobby_existingUser_createNewLobbyForGameWithUser() {
        User user = new User("Jan", "kowalski");

        userDao.registerNewUser(user);
        gameDao.logInUser("Jan", "kowalski");
        gameDao.createNewLobby("JanGranie", user.getId());

        UUID lobbyID = gameDao.searchForCreatedLobbiesID("JanGranie");
        Lobby lobby = gameDao.getLobbyFromID(lobbyID);

        assertThat(lobby.getGame().getGameStatus()).isEqualTo(GameStatus.NEW_GAME);

        User testUser = null;
        for(Map.Entry<User, Game.Symbol> mapEntry : lobby.getGame().getInGameLobby().entrySet()) {
            if(mapEntry.getValue().equals(Game.Symbol.X)) {
                testUser = mapEntry.getKey();
            }
        }

        assertThat(testUser).isEqualTo(user);
    }

    @Test
    public void joinCreatedLobby_lobbyIdDoesNotMatchWithExistingLobbies_lobbyNotFoundException() {
        User user = new User("Jan", "kowalski");
        userDao.registerNewUser(user);
        gameDao.logInUser("Jan", "kowalski");

        UUID fakeLobbyID = UUID.randomUUID();
        UUID userID = user.getId();

        LobbyNotFoundException exception = assertThrows(LobbyNotFoundException.class,
                () -> gameDao.joinCreatedLobby(fakeLobbyID, userID));
        assertThat(exception).hasMessage("Lobby not found.");
    }

    @Test
    public void joinCreatedLobby_lobbyIsFull_throwsLobbyIsFullException() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");
        User ninjaUser = new User("Bajo", "jajo123");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);
        userDao.registerNewUser(ninjaUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");
        gameDao.logInUser("Bajo", "jajo123");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());
        gameDao.startGame(firstUser.getId(), secondUser.getId());

        LobbyIsFullException exception = assertThrows(LobbyIsFullException.class,
                () -> gameDao.jointCreatedLobby("QuickGame", "Bajo"));
        assertThat(exception).hasMessage("Unable to join: lobby is full.");
    }

    @Test
    public void makeMove_userXMakeMove_changeGameStatusToMoveO() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());
        gameDao.startGame(firstUser.getId(), secondUser.getId());

        gameDao.makeMove(firstUser.getId(), new Coordinates(0, 0));

        UUID lobbyID = gameDao.searchForCreatedLobbiesID("QuickGame");
        Lobby lobby = gameDao.getLobbyFromID(lobbyID);

        assertThat(lobby.getGame().getGameStatus()).isEqualTo(GameStatus.MOVE_O);

        User testUser = null;
        for(Map.Entry<User, Game.Symbol> mapEntry : lobby.getGame().getInGameLobby().entrySet()) {
            if(mapEntry.getValue().equals(Game.Symbol.X)) {
                testUser = mapEntry.getKey();
            }
        }
        assertThat(testUser).isEqualTo(firstUser);
    }

    @Test
    public void makeMove_gameLobbyDoesNotExist_throwsLobbyNotFoundException() {
        User user = new User("Janusz", "omegalol");
        userDao.registerNewUser(user);
        gameDao.logInUser("Janusz", "omegalol");

        LobbyNotFoundException exception = assertThrows(LobbyNotFoundException.class,
                () -> gameDao.makeMove(user.getId(), new Coordinates(0,0)));
        assertThat(exception).hasMessage("Lobby not found.");
    }

    @Test
    public void makeMove_wrongPlaceOnGrid_returnsFalse() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());
        gameDao.startGame(firstUser.getId(), secondUser.getId());

        gameDao.makeMove(firstUser.getId(), new Coordinates(0, 0));

        assertThat(gameDao.makeMove(secondUser.getId(), new Coordinates(0,0))).isEqualTo(false);
    }

    @Test
    public void makeMove_coordinatesOutOfScope_returnsFalse() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());
        gameDao.startGame(firstUser.getId(), secondUser.getId());

        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> gameDao.makeMove(firstUser.getId(), new Coordinates(3,0)));
        assertThat(exception).hasMessageContaining("Index");
    }

    //TODO: Implement test cases together with GameService method implementation
    @Test
    public void makeMove_wrongPlayerTurn_returnsFalse() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());
        gameDao.startGame(firstUser.getId(), secondUser.getId());

        gameDao.makeMove(firstUser.getId(), new Coordinates(0, 0));

        assertThat(gameDao.makeMove(firstUser.getId(), new Coordinates(1,0))).isEqualTo(false);

        UUID lobbyID = gameDao.searchForCreatedLobbiesID("QuickGame");
        Lobby lobby = gameDao.getLobbyFromID(lobbyID);
        assertThat(lobby.getGame().getGameStatus()).isEqualTo(GameStatus.MOVE_O);

        Game.Symbol testUserSymbol = null;
        for(Map.Entry<User, Game.Symbol> mapEntry : lobby.getGame().getInGameLobby().entrySet()) {
            if(mapEntry.getKey().equals(firstUser)) {
                testUserSymbol = mapEntry.getValue();
            }
        }
        assertThat(testUserSymbol).isEqualTo(Game.Symbol.X);
    }

    @Test
    public void makeMove_gameNotStarted_throwsGameNotStartedException() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());

        GameNotStartedException exception = assertThrows(GameNotStartedException.class,
                () -> gameDao.makeMove(firstUser.getId(), new Coordinates(0, 0)));
        assertThat(exception).hasMessage("Game not started.");
    }

    @Test
    public void makeMove_gameFinished_throwsGameNotStartedException() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());
        gameDao.startGame(firstUser.getId(), secondUser.getId());

        gameDao.makeMove(firstUser.getId(), new Coordinates(0,0));
        gameDao.makeMove(secondUser.getId(), new Coordinates(0,1));
        gameDao.makeMove(firstUser.getId(), new Coordinates(1,0));
        gameDao.makeMove(secondUser.getId(), new Coordinates(1,1));
        gameDao.makeMove(firstUser.getId(), new Coordinates(2,0));

        GameNotStartedException exception = assertThrows(GameNotStartedException.class,
                () -> gameDao.makeMove(secondUser.getId(), new Coordinates(2,1)));
        assertThat(exception).hasMessage("Game not started.");

        UUID lobbyID = gameDao.searchForCreatedLobbiesID("QuickGame");
        Lobby lobby = gameDao.getLobbyFromID(lobbyID);
        assertThat(lobby.getGame().getGameStatus()).isEqualTo(GameStatus.X_WON);
    }

    @Test
    public void makeMove_legalMoveX_gameStatusChangesToAnotherPlayer() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());
        gameDao.startGame(firstUser.getId(), secondUser.getId());

        gameDao.makeMove(firstUser.getId(), new Coordinates(0,0));

        UUID lobbyID = gameDao.searchForCreatedLobbiesID("QuickGame");
        Lobby lobby = gameDao.getLobbyFromID(lobbyID);
        assertThat(lobby.getGame().getGameStatus()).isEqualTo(GameStatus.MOVE_O);
    }

    @Test
    public void makeMove_legalMoveO_gameStatusChangesToAnotherPlayer() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());
        gameDao.startGame(firstUser.getId(), secondUser.getId());

        gameDao.makeMove(firstUser.getId(), new Coordinates(0,0));
        gameDao.makeMove(secondUser.getId(), new Coordinates(0,1));

        UUID lobbyID = gameDao.searchForCreatedLobbiesID("QuickGame");
        Lobby lobby = gameDao.getLobbyFromID(lobbyID);
        assertThat(lobby.getGame().getGameStatus()).isEqualTo(GameStatus.MOVE_X);
    }

    @Test
    public void makeMove_legalMove_gameCanBeFinishedWithDraw() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());
        gameDao.startGame(firstUser.getId(), secondUser.getId());

        gameDao.makeMove(firstUser.getId(), new Coordinates(0,0));
        gameDao.makeMove(secondUser.getId(), new Coordinates(0,1));
        gameDao.makeMove(firstUser.getId(), new Coordinates(1,0));
        gameDao.makeMove(secondUser.getId(), new Coordinates(1,1));
        gameDao.makeMove(firstUser.getId(), new Coordinates(0,2));
        gameDao.makeMove(secondUser.getId(), new Coordinates(2,0));
        gameDao.makeMove(firstUser.getId(), new Coordinates(1,2));
        gameDao.makeMove(secondUser.getId(), new Coordinates(2,2));
        gameDao.makeMove(firstUser.getId(), new Coordinates(2,1));

        UUID lobbyID = gameDao.searchForCreatedLobbiesID("QuickGame");
        Lobby lobby = gameDao.getLobbyFromID(lobbyID);
        assertThat(lobby.getGame().getGameStatus()).isEqualTo(GameStatus.DRAW);
    }

    @Test
    public void makeMove_legalMove_gameCanBeFinishedWithXWon() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());
        gameDao.startGame(firstUser.getId(), secondUser.getId());

        gameDao.makeMove(firstUser.getId(), new Coordinates(0,0));
        gameDao.makeMove(secondUser.getId(), new Coordinates(0,1));
        gameDao.makeMove(firstUser.getId(), new Coordinates(1,0));
        gameDao.makeMove(secondUser.getId(), new Coordinates(1,1));
        gameDao.makeMove(firstUser.getId(), new Coordinates(2,0));

        UUID lobbyID = gameDao.searchForCreatedLobbiesID("QuickGame");
        Lobby lobby = gameDao.getLobbyFromID(lobbyID);
        assertThat(lobby.getGame().getGameStatus()).isEqualTo(GameStatus.X_WON);
    }

    @Test
    public void makeMove_legalMove_gameCanBeFinishedWithOWon() {
        User firstUser = new User("Janusz", "omegalol");
        User secondUser = new User("Janina", "dziew34");

        userDao.registerNewUser(firstUser);
        userDao.registerNewUser(secondUser);

        gameDao.logInUser("Janusz", "omegalol");
        gameDao.logInUser("Janina", "dziew34");

        gameDao.createNewLobby("QuickGame", firstUser.getId());
        gameDao.jointCreatedLobby("QuickGame", secondUser.getName());
        gameDao.startGame(firstUser.getId(), secondUser.getId());

        gameDao.makeMove(firstUser.getId(), new Coordinates(0,0));
        gameDao.makeMove(secondUser.getId(), new Coordinates(0,1));
        gameDao.makeMove(firstUser.getId(), new Coordinates(1,0));
        gameDao.makeMove(secondUser.getId(), new Coordinates(1,1));
        gameDao.makeMove(firstUser.getId(), new Coordinates(2,2));
        gameDao.makeMove(secondUser.getId(), new Coordinates(2,1));

        UUID lobbyID = gameDao.searchForCreatedLobbiesID("QuickGame");
        Lobby lobby = gameDao.getLobbyFromID(lobbyID);
        assertThat(lobby.getGame().getGameStatus()).isEqualTo(GameStatus.O_WON);
    }
}