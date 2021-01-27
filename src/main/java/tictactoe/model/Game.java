package tictactoe.model;

import tictactoe.exceptions.GameHasAlreadyStartedException;
import tictactoe.exceptions.GameNotStartedException;
import tictactoe.exceptions.LobbyIsFullException;
import tictactoe.exceptions.NotEnoughtPlayersInLobbyException;

import java.util.*;

public class Game {
    private final Map<User, Symbol> inGameLobby;
    private final int[][] grid;
    private Symbol currentSymbolPlayed;
    private boolean gameStarted;
    private boolean finish;
    private GameStatus gameStatus;

    public enum Symbol{
        X,
        O
    }

    public Game() {
        this.inGameLobby = new HashMap<>();
        this.grid = new int[3][3];
        this.currentSymbolPlayed = Symbol.O;
        this.gameStarted = false;
        this.finish = false;
        this.gameStatus = GameStatus.IDLE;
    }

    public Symbol getCurrentSymbolPlayed() {
        return currentSymbolPlayed;
    }

    public int[][] getGrid() {
        return grid;
    }

    public boolean isFinished() {
        return finish;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public Map<User, Symbol> getInGameLobby() {
        return Collections.unmodifiableMap(inGameLobby);
    }

    public void join(User user) {
        if(inGameLobby.size() < 2) {
            inGameLobby.put(user, (inGameLobby.size() == 0 ? Symbol.X : Symbol.O));

            gameStatus = GameStatus.NEW_GAME;

        } else {
            throw new LobbyIsFullException();
        }
    }

    public void leave(User user) {
        inGameLobby.remove(user);

        if(gameStarted) {
            user.saveGameResult(-1);
            gameStarted = false;
        }
    }

    public void startGame() {
        if(!gameStarted && (inGameLobby.size() == 2)) {
            gameStarted = true;

            gameStatus = GameStatus.MOVE_X;

        } else if(gameStarted && (inGameLobby.size() == 2)) {
            throw new GameHasAlreadyStartedException();
        } else {
            throw new NotEnoughtPlayersInLobbyException();
        }
    }

    public boolean makeMove(User user, Coordinates coordinates) {
        if(!gameStarted) {
            throw new GameNotStartedException();
        }

        Game.Symbol playerSymbol = inGameLobby.get(user);

        if(gameStarted && currentSymbolPlayed != playerSymbol && grid[coordinates.getY()][coordinates.getX()] == 0) {
            grid[coordinates.getY()][coordinates.getX()] = getPlayersSymbol(playerSymbol);
            currentSymbolPlayed = playerSymbol;

            if(currentSymbolPlayed.equals(Symbol.O)) {
                gameStatus = GameStatus.MOVE_X;
            } else {
                gameStatus = GameStatus.MOVE_O;
            }

            if(winCheck()) {
                endSettledGame(user);
            }

            if(isGridFull()) {
                endDrawGame();
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean isGridFull() {
        for(int[] ints : grid) {
            for(int anInt : ints) {
                if(anInt == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void endSettledGame(User user) {
        finish = true;
        gameStarted = false;

        user.saveGameResult(1);
        for(Map.Entry<User, Symbol> userEntry : inGameLobby.entrySet()) {
            if(!userEntry.getKey().equals(user)) {
                userEntry.getKey().saveGameResult(-1);
            }
        }
    }

    private void endDrawGame() {
        finish = true;
        gameStarted = false;

        for(Map.Entry<User, Symbol> userEntry : inGameLobby.entrySet()) {
            userEntry.getKey().saveGameResult(0);
        }

        gameStatus = GameStatus.DRAW;
    }

    private char getPlayersSymbol(Symbol symbol) {
        if(symbol.equals(Symbol.X)) {
            return 'x';
        }
        return 'o';
    }

    //TODO: upgrade method by deleting repeating code
    private boolean winCheck() {
        ArrayList<Integer> fields = new ArrayList<>();
        //*****************************
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                fields.add(grid[j][i]);
            }
            if(fields.get(0).equals(fields.get(1)) && fields.get(0).equals(fields.get(2)) && !fields.get(0).equals(0)) {
                if(fields.get(0) == 120) {
                    gameStatus = GameStatus.X_WON;
                    System.out.println("x won");
                } else {
                    gameStatus = GameStatus.O_WON;
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
                    gameStatus = GameStatus.X_WON;
                    System.out.println("x won");
                } else {
                    gameStatus = GameStatus.O_WON;
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
                gameStatus = GameStatus.X_WON;
                System.out.println("x won");
            } else {
                gameStatus = GameStatus.O_WON;
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
                gameStatus = GameStatus.X_WON;
                System.out.println("x won");
            } else {
                gameStatus = GameStatus.O_WON;
                System.out.println("o won");
            }
            return true;
        }
        fields.clear();
        //*****************************
        return false;
    }
}
