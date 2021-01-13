package pl.itacademy.tictac;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.itacademy.tictac.domain.Game;
import pl.itacademy.tictac.domain.GameStatus;
import pl.itacademy.tictac.domain.Player;
import pl.itacademy.tictac.exception.PlayerNotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;

    public Game createGame(String playerName, String playerPassword) {
        Game game = new Game();
        Player player = playerService.getPlayerByNameAndPassword(playerName, playerPassword);
        game.setPlayerX(player);
        return game;
    }

    public void joinGame(long id, String playerName, String playerPassword) {
        if(gameRepository.getById(id).isEmpty()) {
            throw new GameNotFoundException(String.format("Game %d not found", id));
        }else if(gameRepository.getById(id).get().getGameStatus() != GameStatus.NEW_GAME){
            throw new GameNotAvailableForRegistrationException(String.format("Game not available to join"));
        }else{
            Player joinedPlayer = new Player(playerName,playerPassword);
            gameRepository.getById(id).get().setPlayerO(joinedPlayer);
            gameRepository.getById(id).get().setGameStatus(GameStatus.MOVE_X);
        }

    }
}
