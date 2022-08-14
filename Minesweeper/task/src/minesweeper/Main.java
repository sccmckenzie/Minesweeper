package minesweeper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many mines do you want on the field?");
        int numMines = scanner.nextInt();
        var mineField = new MineField(9, numMines);

        int x;
        int y;
        String cmd;
        boolean exitFlag = false;

        try {
            while(!exitFlag) {
                mineField.printMineField();
                if (mineField.checkVictory()) {
                    System.out.println("Congratulations! You found all the mines!");
                    exitFlag = true;
                } else {
                    System.out.print("Set/unset mines marks or claim a cell as free:");
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                    cmd = scanner.next().trim();

                    if (cmd.equals("free")) {
                        mineField.exploreCell(y - 1, x - 1);
                    } else if (cmd.equals("mine")) {
                        mineField.markCell(y - 1, x - 1);
                    } else {
                        throw new Exception("Unrecognized command.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }
}
