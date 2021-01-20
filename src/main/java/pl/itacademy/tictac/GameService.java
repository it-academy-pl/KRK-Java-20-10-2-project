package pl.itacademy.tictac;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.itacademy.tictac.domain.Game;
import pl.itacademy.tictac.domain.GameStatus;
import pl.itacademy.tictac.domain.Player;
import pl.itacademy.tictac.exception.GameNotAvailableForRegistrationException;
import pl.itacademy.tictac.exception.GameNotFoundException;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;

    public Game createGame(String playerName, String playerPassword) {
        Game game = new Game();
        gameRepository.save(game);
        Player player = playerService.getPlayerByNameAndPassword(playerName, playerPassword);
        game.setPlayerX(player);
        return game;
    }

    public void joinGame(long id, String playerName, String playerPassword) {


        if(id != gameRepository.getById(id).get().getId()) {
            throw new GameNotFoundException(String.format("Game %d not found", id));
        } else if(gameRepository.getById(id).get().getGameStatus() != GameStatus.NEW_GAME) {
            throw new GameNotAvailableForRegistrationException(String.format("The game %d has already started!", id));
        } else {
            Player joinedPlayer = new Player(playerName, playerPassword);
            gameRepository.getById(id).get().setPlayerO(joinedPlayer);
            gameRepository.getById(id).get().setGameStatus(GameStatus.MOVE_X);
        }
    }
}
