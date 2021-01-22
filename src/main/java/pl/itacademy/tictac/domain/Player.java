package pl.itacademy.tictac.domain;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import pl.itacademy.tictac.StatisticResponse;

@Value
public class Player {
    private final String name;
    private final String password;
}
