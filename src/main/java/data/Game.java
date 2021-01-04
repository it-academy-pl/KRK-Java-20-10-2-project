package data;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private final Map<Player, Symbol> lobby;
    private int[][] grid;
    private Symbol currentSymbolPlayed;
    private boolean gameStarted;
    private boolean finish;

    enum Symbol{
        X,
        O
    }

    public Game() {
        this.lobby = new HashMap<>();
        this.grid = new int[3][3];
        this.currentSymbolPlayed = null;
        gameStarted = false;
        finish = false;
    }

    public void join(Player player, Symbol symbol) {
        if(lobby.size() < 3) {
            lobby.put(player, symbol);
        } else {
            throw new IndexOutOfBoundsException("Lobby is already full");
        }
    }

    public void leave(Player player) {
        lobby.remove(player);

        if(gameStarted) {
            player.saveGameResult(-1);
            gameStarted = false;
        }
    }

    public void startGame() {
        if(!gameStarted && (lobby.size() == 2)) {
            gameStarted = true;
        } else if(gameStarted && (lobby.size() == 2)) {
            throw new UnsupportedOperationException("Game already began.");
        } else {
            throw new UnsupportedOperationException("Not enough players.");
        }
    }

    public boolean makeMove(Player player,Coordinates coordinates) {
        Symbol playerSymbol = lobby.get(player);
        if(gameStarted && currentSymbolPlayed != playerSymbol && grid[coordinates.getX()][coordinates.getY()] == 0) {
            grid[coordinates.getX()][coordinates.getY()] = getPlayersSymbol(playerSymbol);
            //check if game has ended
        }

        return false;
    }

    private char getPlayersSymbol(Symbol symbol) {
        if(symbol == Symbol.X) {
            return 'x';
        }
        return 'o';
    }

    private boolean winCheck() {
        //check if game is won
        return false;
    }
}
