package tictactoe.dataaccessobject;

import org.springframework.stereotype.Repository;
import tictactoe.exceptions.*;
import tictactoe.model.Coordinates;
import tictactoe.model.Lobby;
import tictactoe.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("gameDao")
public class GameDataAccessService implements GameDao {
    private static final List<User> loggedInUsersDB = new ArrayList<>();
    private static final List<Lobby> createdLobbiesDB = new ArrayList<>();

    private final UserDao userDao;

    public GameDataAccessService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean logInUser(String userName, String password) {
        for(User user : userDao.getRegisteredUsers()) {
            if(user.getName().equals(userName)) {
                for(User loggedInUsers : loggedInUsersDB) {
                    if(loggedInUsers.getName().equals(userName)) {
                        throw new UserAlreadyLoggedInException();
                    }
                }

                if(user.logIn(password)) {
                    loggedInUsersDB.add(user);
                    return true;
                } else {
                    throw new WrongPasswordException();
                }
            }
        }
        throw new UserNotRegisteredException();
    }

    @Override
    public boolean logOutUser(String userName) {
        for(User user : userDao.getRegisteredUsers()) {
            if(user.getName().equals(userName)) {
                for(User loggedUser : loggedInUsersDB) {
                    if(loggedUser.getName().equals(userName)) {
                        loggedInUsersDB.remove(user);
                        return true;
                    } else {
                        throw new UserNotLoggedInException();
                    }
                }
            }
        }
        throw new UserNotRegisteredException();
    }

    @Override
    public UUID searchForLoggedInUserID(String userName) {
        Optional<User> foundUser = loggedInUsersDB.stream()
                .filter(user -> user.getName().equals(userName))
                .findFirst();
        //FIXME: fix optional ?
        if(foundUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        return foundUser.get().getId();
    }

    @Override
    public UUID searchForCreatedLobbiesID(String lobbyName) {
        Optional<Lobby> createdLobby = createdLobbiesDB.stream()
                .filter(user -> user.getName().equals(lobbyName))
                .findFirst();

        //FIXME: fix optional ?
        if(createdLobby.isEmpty()) {
            throw new LobbyNotFoundException();
        }
        return createdLobby.get().getId();
    }

    @Override
    public User getUserFromID(UUID id) {
        Optional<User> foundUser = loggedInUsersDB.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
        return foundUser.orElse(foundUser.orElseThrow(UserNotLoggedInException::new));
    }

    @Override
    public Lobby getLobbyFromID(UUID id) {
        Optional<Lobby> foundLobby = createdLobbiesDB.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
        return foundLobby.orElse(foundLobby.orElseThrow(LobbyNotFoundException::new));
    }

    @Override
    public void createNewLobby(String lobbyName, String userName) {
        createNewLobby(lobbyName, searchForLoggedInUserID(userName));
    }

    @Override
    public void createNewLobby(String lobbyName, UUID creatorID) {
        for(Lobby lobby : createdLobbiesDB) {
            if(lobby.getName().equals(lobbyName)) {
                throw new LobbyWithThisNameAlreadyExistsException();
            }
        }
        Lobby newLobby = new Lobby(lobbyName);
        createdLobbiesDB.add(newLobby);
        joinCreatedLobby(newLobby.getId(), creatorID);
    }

    @Override
    public void jointCreatedLobby(String lobbyName, String userName) {
        joinCreatedLobby(searchForCreatedLobbiesID(lobbyName), searchForLoggedInUserID(userName));
    }

    @Override
    public boolean joinCreatedLobby(UUID lobbyID, UUID userID) {
        Optional<User> userOptional = loggedInUsersDB.stream()
                .filter(user -> user.getId().equals(userID))
                .findFirst();

        if(userOptional.isPresent() && userOptional.get().getActiveLobby() != null) {
            throw new UnsupportedOperationException("Player is already in lobby.");
        }

        for(Lobby lobby : createdLobbiesDB) {
            if(lobby.getId().equals(lobbyID) && lobby.getNumberOfPlayers() < 2) {
                lobby.join();
                User user = getUserFromID(userID);
                lobby.getGame().join(user);
                user.setActiveLobby(lobby);
                return true;
            } else if(lobby.getId().equals(lobbyID)) {
                throw new LobbyIsFullException();
            }
        }
        throw new LobbyNotFoundException();
    }

    @Override
    public void leaveLobby(UUID userID) {
        User user = getUserFromID(userID);

        if(user.getActiveLobby() == null) {
            throw new PlayerIsNotInTheLobbyException();
        }

        for(Lobby lobby : createdLobbiesDB) {
            if(lobby.equals(user.getActiveLobby())) {
                lobby.leave();
                lobby.getGame().leave(user);
                user.setActiveLobby(null);
            }
        }
    }

    @Override
    public void startGame(UUID firstUserID, UUID secondUserID) {
        User firstPlayer = getUserFromID(firstUserID);
        User secondPlayer = getUserFromID(secondUserID);


        if(firstPlayer.getActiveLobby().equals(secondPlayer.getActiveLobby()) && firstPlayer.getActiveLobby() != null) {
            for(Lobby lobby : createdLobbiesDB) {
                if(lobby.equals(firstPlayer.getActiveLobby())) {
                    lobby.getGame().startGame();
                    break;
                }
            }
        } else {
            throw new LobbyNotFoundException();
        }
    }

    @Override
    public boolean makeMove(UUID userID, Coordinates coordinates) {
        User user = getUserFromID(userID);

        if(user.getActiveLobby() == null) {
            throw new LobbyNotFoundException();
        }

        boolean moveCompleted = user.getActiveLobby().getGame().makeMove(user, coordinates);
        if(user.getActiveLobby().getGame().isFinish()) {
            System.out.println("Game Over!");
        }
        return moveCompleted;
    }

    @Override
    public void printLobbyGrid(UUID lobbyID) {
        Lobby lobby = getLobbyFromID(lobbyID);

        int[][] grid = lobby.getGame().getGrid();

        for (int[] ints : grid) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    @Override
    public boolean lobbyGameFinished(UUID lobbyID) {
        return getLobbyFromID(lobbyID).getGame().isFinish();
    }


}
