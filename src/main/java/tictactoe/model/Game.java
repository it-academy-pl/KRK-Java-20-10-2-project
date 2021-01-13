package tictactoe.model;

import tictactoe.exceptions.GameHasAlreadyStartedException;
import tictactoe.exceptions.GameNotStartedException;
import tictactoe.exceptions.LobbyIsFullException;
import tictactoe.exceptions.NotEnoughtPlayersInLobbyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private final Map<User, Symbol> lobby;
    private final int[][] grid;
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

    public void join(User user) {
        if(lobby.size() < 2) {
            lobby.put(user, (lobby.size() == 0 ? Symbol.X : Symbol.O));
        } else {
            throw new LobbyIsFullException();
        }
    }

    public void leave(User user) {
        lobby.remove(user);

        if(gameStarted) {
            user.saveGameResult(-1);
            gameStarted = false;
        }
    }

    public void startGame() {
        if(!gameStarted && (lobby.size() == 2)) {
            gameStarted = true;
        } else if(gameStarted && (lobby.size() == 2)) {
            throw new GameHasAlreadyStartedException();
        } else {
            throw new NotEnoughtPlayersInLobbyException();
        }
    }

    public boolean makeMove(User user, Coordinates coordinates) {
        if(!gameStarted) {
            throw new GameNotStartedException();
        }

        boolean freeSpaceFlag = false;
        for(int[] ints : grid) {
            if(freeSpaceFlag) {
                break;
            }
            for(int anInt : ints) {
                if(anInt == 0) {
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

        Symbol playerSymbol = lobby.get(user);
        if(gameStarted && currentSymbolPlayed != playerSymbol && grid[coordinates.getX()][coordinates.getY()] == 0) {
            grid[coordinates.getY()][coordinates.getX()] = getPlayersSymbol(playerSymbol);
            currentSymbolPlayed = playerSymbol;
            if(winCheck()) {
                finish = true;
                gameStarted = false;
                endSettledGame(user);
            }
            return true;
        }
        return false;
    }

    private void endSettledGame(User user) {
        user.saveGameResult(1);
        for(Map.Entry<User, Symbol> userEntry : lobby.entrySet()) {
            if(!userEntry.getKey().equals(user)) {
                userEntry.getKey().saveGameResult(-1);
            }
        }
    }

    private void endDrawGame() {
        for(Map.Entry<User, Symbol> userEntry : lobby.entrySet()) {
            userEntry.getKey().saveGameResult(0);
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
                if(fields.get(0) == 120) {
                    System.out.println("x won");
                } else {
                    System.out.println("o won");
                }
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
                if(fields.get(0) == 120) {
                    System.out.println("x won");
                } else {
                    System.out.println("o won");
                }
                return true;
            }
            fields.clear();
        }
        //*****************************
        for(int i = 0; i < 3; i++) {
            fields.add(grid[i][i]);
        }
        if(fields.get(0).equals(fields.get(1)) && fields.get(0).equals(fields.get(2)) && !fields.get(0).equals(0)) {
            if(fields.get(0) == 120) {
                System.out.println("x won");
            } else {
                System.out.println("o won");
            }
            return true;
        }
        fields.clear();
        //*****************************
        for(int i = 0; i < 3; i++) {
            fields.add(grid[i][2-i]);
        }
        if(fields.get(0).equals(fields.get(1)) && fields.get(0).equals(fields.get(2)) && !fields.get(0).equals(0)) {
            if(fields.get(0) == 120) {
                System.out.println("x won");
            } else {
                System.out.println("o won");
            }
            return true;
        }
        fields.clear();
        //*****************************
        return false;
    }
}
