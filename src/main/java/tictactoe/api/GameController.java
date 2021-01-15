package tictactoe.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tictactoe.model.Coordinates;
import tictactoe.service.GameService;

import java.util.UUID;

@RequestMapping("/game")
@RestController
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/sing")
    public void logInUser(@RequestParam("userName") String userName,@RequestParam("password") String password) {
        gameService.logInUser(userName, password);
    }

    @DeleteMapping("/sing")
    public void logOutUser(@RequestParam("userName") String userName) {
        gameService.logOutUser(userName);
    }

    @GetMapping("/search/userid")
    public ResponseEntity<SearchUserIDResponse> searchForLoggedInUserID(@RequestParam("userName") String userName) {
        UUID userID = gameService.searchForLoggedInUserID(userName);
        return ResponseEntity.ok(SearchUserIDResponse.fromID(userID));
    }

    @GetMapping("/search/lobbyid")
    public void searchForCreatedLobbiesID(@RequestParam("lobbyName") String lobbyName) {
        gameService.searchForCreatedLobbiesID(lobbyName);
    }

    @GetMapping("/get/userid")
    public void getUserFromID(@RequestParam("userID") UUID id) {
        gameService.getUserFromID(id);
    }

    @GetMapping("/get/lobbyid")
    public void getLobbyFromID(@RequestParam("lobbyID") UUID id) {
        gameService.getLobbyFromID(id);
    }

    @PostMapping("/createlobby/withuser")
    public void createNewLobby(@RequestParam("lobbyName") String lobbyName,@RequestParam("userName") String userName) {
        gameService.createNewLobby(lobbyName, userName);
    }

    @PostMapping("/createlobby/withuserid")
    public void createNewLobby(@RequestParam("lobbyName") String lobbyName,@RequestParam("creatorID") UUID creatorID) {
        gameService.createNewLobby(lobbyName, creatorID);
    }

    @PutMapping("/joinlobby/withuser")
    public void jointCreatedLobby(@RequestParam("lobbyName") String lobbyName,@RequestParam("userName") String userName) {
        gameService.jointCreatedLobby(lobbyName, userName);
    }

    @PutMapping("/joinlobby/withuserid")
    public void joinCreatedLobby(@RequestParam("lobbyID") UUID lobbyID,@RequestParam("userID") UUID userID) {
        gameService.joinCreatedLobby(lobbyID, userID);
    }

    @DeleteMapping(path = "{id}")
    public void leaveLobby(@PathVariable("id") UUID userID) {
        gameService.leaveLobby(userID);
    }

    @PutMapping("/start")
    public void startGame(@RequestParam("firstUserID") UUID firstUserID,@RequestParam("secondUserID") UUID secondUserID) {
        gameService.startGame(firstUserID, secondUserID);
    }

    @PutMapping("/move")
    public void makeMove(@RequestParam("userID") UUID userID,@RequestBody Coordinates coordinates) {
        gameService.makeMove(userID, coordinates);
    }

    @GetMapping("/grid")
    public void printLobbyGrid(@RequestParam("lobbyID") UUID lobbyID) {
        gameService.printLobbyGrid(lobbyID);
    }

    @GetMapping("/status")
    public void lobbyGameFinished(@RequestParam("lobbyID") UUID lobbyID) {
        gameService.lobbyGameFinished(lobbyID);
    }

    //FIXME: doesn't show anything
    private static class SearchUserIDResponse {
        private final UUID userID;

        public SearchUserIDResponse(UUID userID) {
            this.userID = userID;
        }

        public static SearchUserIDResponse fromID(UUID userID) {
            return new GameController.SearchUserIDResponse(
                    userID
            );
        }
    }
}
