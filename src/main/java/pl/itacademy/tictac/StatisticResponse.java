package pl.itacademy.tictac;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.itacademy.tictac.domain.Player;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class StatisticResponse {
    private int gamesPlayed;
    private int wins;
    private int loses;
    private int draws;
}
