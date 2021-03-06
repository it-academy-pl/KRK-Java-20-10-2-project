package pl.itacademy.tictac;

import pl.itacademy.tictac.domain.Game;

import java.util.Collection;
import java.util.Optional;

public interface GameRepository {

    void save(Game game);

    Optional<Game> getById(long id);

    Collection<Game> getAll();
}
