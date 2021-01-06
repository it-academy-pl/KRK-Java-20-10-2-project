package data;

import java.util.ArrayList;
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
        this.currentSymbolPlayed = Symbol.O;
        gameStarted = false;
        finish = false;
    }

    public Symbol getCurrentSymbolPlayed() {
        return currentSymbolPlayed;
    }

    public int[][] getGrid() {
        return grid;
    }

    public boolean isFinish() {
        return finish;
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
        if(!gameStarted) {
            throw new UnsupportedOperationException("Game not started!");
        }

        boolean freeSpaceFlag = false;
        for(int[] ints : grid) {
            for(int anInt : ints) {
                if (anInt == 0) {
                    freeSpaceFlag = true;
                    break;
                }
            }
        }
        if(!freeSpaceFlag) {
            finish = true;
            gameStarted = false;
            endDrawGame();
            return true;
        }

        Symbol playerSymbol = lobby.get(player);
        if(gameStarted && currentSymbolPlayed != playerSymbol && grid[coordinates.getX()][coordinates.getY()] == 0) {
            grid[coordinates.getX()][coordinates.getY()] = getPlayersSymbol(playerSymbol);
            if(winCheck()) {
                finish = true;
                gameStarted = false;
                endSettledGame(player);
            }
            return true;
        }
        return false;
    }

    private void endSettledGame(Player winner) {
        winner.saveGameResult(1);
        for(Map.Entry<Player, Symbol> playerEntry : lobby.entrySet()) {
            if(!playerEntry.getKey().equals(winner)) {
                playerEntry.getKey().saveGameResult(-1);
            }
        }
    }

    private void endDrawGame() {
        for(Map.Entry<Player, Symbol> playerEntry : lobby.entrySet()) {
            playerEntry.getKey().saveGameResult(0);
        }
    }

    private char getPlayersSymbol(Symbol symbol) {
        if(symbol == Symbol.X) {
            return 'x';
        }
        return 'o';
    }

    private boolean winCheck() {
        ArrayList<Integer> fields = new ArrayList<>();
        //*****************************
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                fields.add(grid[j][i]);
            }
            if(fields.get(0).equals(fields.get(1)) && fields.get(0).equals(fields.get(2)) && !fields.get(0).equals(0)) {
                return true;
            }
            fields.clear();
        }
        //*****************************
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                fields.add(grid[i][j]);
            }
            if(fields.get(0).equals(fields.get(1)) && fields.get(0).equals(fields.get(2)) && !fields.get(0).equals(0)) {
                return true;
            }
            fields.clear();
        }
        //*****************************
        for(int i = 0; i < 3; i++) {
            fields.add(grid[i][i]);
        }
        if(fields.get(0).equals(fields.get(1)) && fields.get(0).equals(fields.get(2)) && !fields.get(0).equals(0)) {
            return true;
        }
        fields.clear();
        //*****************************
        for(int i = 0; i < 3; i++) {
            fields.add(grid[i][2-i]);
        }
        if(fields.get(0).equals(fields.get(1)) && fields.get(0).equals(fields.get(2)) && !fields.get(0).equals(0)) {
            return true;
        }
        fields.clear();
        //*****************************
        return false;
    }
}
