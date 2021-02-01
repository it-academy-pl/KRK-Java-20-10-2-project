package pl.itacademy.tictac;

import org.springframework.stereotype.Component;
import pl.itacademy.tictac.domain.Game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryGameRepository implements GameRepository {
    private final Map<Long, Game> games = new HashMap<>();

    @Override
    public void save(Game game) {
        games.put(game.getId(), game);
    }

    @Override
    public Optional<Game> getById(long id) {
        return Optional.ofNullable(games.get(id));
    }

    @Override
    public Collection<Game> getAll() {
        return games.values();
    }
}
