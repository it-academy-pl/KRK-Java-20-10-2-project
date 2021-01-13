package tictactoe.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tictactoe.model.Coordinates;
import tictactoe.service.GameService;

import java.util.UUID;

@RequestMapping("api/game")
@RestController
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    //TODO: finish mapping and @RequestParam()
    @PostMapping
    public void logInUser(@RequestParam("userName") String userName,@RequestParam("password") String password) {
        gameService.logInUser(userName, password);
    }

    @DeleteMapping
    public void logOutUser(@RequestParam("userName") String userName) {
        gameService.logOutUser(userName);
    }

    @GetMapping
    public void searchForLoggedInUserID(@RequestParam("userName") String userName) {
        gameService.searchForLoggedInUserID(userName);
    }

    @GetMapping
    public void searchForCreatedLobbiesID(@RequestParam("lobbyName") String lobbyName) {
        gameService.searchForCreatedLobbiesID(lobbyName);
    }

    @GetMapping
    public void getUserFromID(@RequestParam("userID") UUID id) {
        gameService.getUserFromID(id);
    }

    @GetMapping
    public void getLobbyFromID(@RequestParam("lobbyID") UUID id) {
        gameService.getLobbyFromID(id);
    }

    @PostMapping
    public void createNewLobby(String lobbyName, String userName) {
        gameService.createNewLobby(lobbyName, userName);
    }

    @PostMapping
    public void createNewLobby(@RequestParam("lobbyName") String lobbyName,@RequestParam("creatorID") UUID creatorID) {
        gameService.createNewLobby(lobbyName, creatorID);
    }

    @PutMapping
    public void jointCreatedLobby(String lobbyName, String userName) {
        gameService.jointCreatedLobby(lobbyName, userName);
    }

    @PutMapping
    public void joinCreatedLobby(UUID lobbyID, UUID playerID) {
        gameService.joinCreatedLobby(lobbyID, playerID);
    }

    @PutMapping
    public void leaveLobby(UUID userID) {
        gameService.leaveLobby(userID);
    }

    @PutMapping
    public void startGame(UUID firstUserID, UUID secondUserID) {
        gameService.startGame(firstUserID, secondUserID);
    }

    @PutMapping
    public void makeMove(UUID userID, Coordinates coordinates) {
        gameService.makeMove(userID, coordinates);
    }

    @GetMapping
    public void printLobbyGrid(UUID lobbyID) {
        gameService.printLobbyGrid(lobbyID);
    }

    @GetMapping
    public void lobbyGameFinished(UUID lobbyID) {
        gameService.lobbyGameFinished(lobbyID);
    }
}
