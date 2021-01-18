package pl.itacademy.tictac.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GameStatus {
    NEW_GAME(false, (char) 0),
    MOVE_X(false, 'X'),
    MOVE_O(false, 'O'),
    DRAW(true, (char) 0),
    X_WON(true, (char) 0),
    O_WON(true, (char) 0);

    private final boolean finished;
    private final char symbol;
}
