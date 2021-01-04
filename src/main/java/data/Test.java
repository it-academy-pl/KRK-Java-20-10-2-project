package data;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        Password password = new Password("Karolkfhduhfsdbfdsofibasdfojahsd213214234$#@#@#$niujh");
        System.out.println(password.letMeIn("Karolkfhduhfsdbfdsofibasdfojahsd213214234$#@#@#$niujh"));

        char[][] grid = new char[3][3];
        System.out.println(Arrays.deepToString(grid));
        char test = 0;
        System.out.println((int)grid[0][0]);
        System.out.println((int)test);

        test = 'x';
        char test1 = 'y';

        int turboTest = test;
        int turboTest1 = test1;

        System.out.println(turboTest + " " + turboTest1);
    }
}
