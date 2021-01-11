package data;

import java.util.Scanner;

public class AppRawDemo {

    /**
     * 0.12 alpha functionality test
     * @author Karol Kurbiel
     * updated on 2021/01/11
     */

    public static void main(String[] args) {

        //*** 1. create new account
        GameCore.getInstance().registerNewPlayer("Ryszard","Ryszard123");
        GameCore.getInstance().registerNewPlayer("Marzena","marzenkabuziaczek");

        //*** 2. sing in into account
        try {
            GameCore.getInstance().singInPlayer("Ryszard", "Ryszard123");
            GameCore.getInstance().singInPlayer("Marzena", "marzenkabuziaczek");
        } catch(IllegalAccessException e) {
            System.out.println(e.getMessage());
        }

        //*** 3. create lobby
        GameCore.getInstance().createNewLobby("Konskie Zaloty", "Ryszard");

        //*** 4. join lobby
        GameCore.getInstance().joinCreatedLobby("Konskie Zaloty", "Marzena");

        //*** 5. start game
        GameCore.getInstance().startGame("Ryszard", "Marzena");

        //*** 6. play game
        boolean quit = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Tic Tac Toe");
        int chose;
        int coordinatesX;
        int coordinatesY;

        boolean isXmoving = true;

        while(!quit) {
            System.out.println("press:");
            System.out.println("0 - to quit; 1 - to enter coordinates");

            chose = scanner.nextInt();
            scanner.nextLine();
            switch(chose) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    System.out.println("Now is " + (isXmoving ? "X " : "O ") + "turn.");
                    System.out.println("Enter coordinates (range 0-2): ");
                    System.out.print("\tx: ");
                    coordinatesX = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("\ty: ");
                    coordinatesY = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println(GameCore.getInstance().makeMoveInActiveGame((isXmoving ? "Ryszard" : "Marzena"), new Coordinates(coordinatesX, coordinatesY)));
                    isXmoving ^= true;
                    break;
            }

            GameCore.getInstance().displayGrid("Marzena");
            if(GameCore.getInstance().gameEnd("Marzena")) {
                quit = true;
            }
        }

        // NEED TO FIX:
        // done- after each coordinates placed need to display grid
        // done- check if make move works correctly
        // done- break from while loop if any player wins
        // - if tried to make move on occupied place again, same symbol have chance
    }
}
