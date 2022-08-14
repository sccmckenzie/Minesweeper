package minesweeper;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class MineField {
    private final CellStatus[][] field;
    private final int numMines;
    private int numUnarmed;
    private final Deque<List<Integer>> deque = new ArrayDeque<List<Integer>>();


    public MineField(int dim, int numMines) {
        this.field = new CellStatus[dim][dim];
        this.numMines = numMines;
        this.numUnarmed = numMines;

        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                this.field[x][y] = CellStatus.UNKNOWN;
            }
        }
    }

    public void printMineField() {
        // header
        System.out.print(" |");
        for (int h0 = 0; h0 < this.field.length; h0++) {
            System.out.print(h0 + 1);
        }
        System.out.print("|");
        System.out.println();
        System.out.print("-|");
        for (int h1 = 0; h1 < this.field.length; h1++) {
            System.out.print('-');
        }
        System.out.print("|");
        System.out.println();


        for (int x = 0; x < this.field.length; x++) {
            System.out.print(x + 1);
            System.out.print('|');
            for (int y = 0; y < this.field.length; y++) {
                CellStatus status = this.field[x][y];

                if (status == CellStatus.CLEARED) {
                    if (numMinesAdjacent(x, y) > 0) {
                        System.out.print(numMinesAdjacent(x, y));
                    } else {
                        System.out.print(status.getSymbol());
                    }
                } else {
                    System.out.print(status.getSymbol());
                }
            }
            System.out.print('|');
            System.out.println();
        }

        System.out.print("-|");
        for (int h2 = 0; h2 < this.field.length; h2++) {
            System.out.print('-');
        }
        System.out.print("|");
        System.out.println();
    }

    public void plantMines() {
        Random random = new Random();
        int x;
        int y;

        while (numUnarmed > 0) {
            x = random.nextInt(this.field.length);
            y = random.nextInt(this.field.length);


            switch (this.field[x][y]) {
                case UNKNOWN:
                    this.field[x][y] = CellStatus.PLANTED;
                    numUnarmed--;
                    break;
                case PLANTED:
                    break;
                case MARKED:
                    break;
                case MISMARKED:
                    this.field[x][y] = CellStatus.MARKED;
                    numUnarmed--;
                    break;
                case CLEARED:
                    break;
                case TRIPPED:
                    break;
            }
        }
    }

    public void tripAllMines() {
        for (int x = 0; x < this.field.length; x++) {
            for (int y = 0; y < this.field.length; y++) {
                if (this.field[x][y] == CellStatus.PLANTED) {
                    this.field[x][y] = CellStatus.TRIPPED;
                }
            }
        }
    }


    public int numMinesAdjacent(int x, int y) {
        int out = 0;

        int[] offset = {-1, 0, 1};

        for (int xi : offset) {
            for (int yi : offset) {
                if (!(xi == 0 && yi == 0)) {
                    try {
                        if (this.field[x + xi][y + yi] == CellStatus.PLANTED || this.field[x + xi][y + yi] == CellStatus.MARKED) {
                            out++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {

                    }
                }
            }
        }
        return out;
    }

    public void markCell(int x, int y) {
        CellStatus status = this.field[x][y];

        // if cell is already CLEARED, it should not toggle to MARKED
        switch (status) {
            case UNKNOWN:
                this.field[x][y] = CellStatus.MISMARKED;
                break;
            case PLANTED:
                this.field[x][y] = CellStatus.MARKED;
                break;
            case MARKED:
                this.field[x][y] = CellStatus.PLANTED;
                break;
            case MISMARKED:
                this.field[x][y] = CellStatus.UNKNOWN;
                break;
            default:
                break;
        }
    }

    public void surveyCell (int x, int y) throws Exception {
        CellStatus status = this.field[x][y];
        if (status == CellStatus.PLANTED) {
            throw new Exception("Unexpected PLANTED cell passed to surveyCell");
        }

        this.field[x][y] = CellStatus.CLEARED;

        if (this.numUnarmed > 0) {
            this.plantMines();
        }

        if (numMinesAdjacent(x, y) == 0) {
            int[] offset = {-1, 0, 1};
            for (int dx : offset) {
                for (int dy : offset) {
                    try {
                        if (!(dx == 0 && dy == 0)) {
                            if (!this.deque.contains(List.of(x + dx, y + dy))) {
                                if (this.field[x + dx][y + dy] != CellStatus.CLEARED) {
                                    deque.add(List.of(x + dx, y + dy));
                                }
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {

                    }

                }
            }
        }
    }

    public void exploreCell(int x, int y) throws Exception {
        CellStatus status = this.field[x][y];

        if (status == CellStatus.PLANTED) {
            tripAllMines();
        } else {
            deque.add(List.of(x, y));
            while(!deque.isEmpty()) {
                List<Integer> coordinates = deque.pop();
                surveyCell(coordinates.get(0), coordinates.get(1));
            }
        }
    }

    public boolean checkVictory() throws Exception {
        int numMarked = 0;
        int numMismarked = 0;
        int numCleared = 0;


        for (int x = 0; x < this.field.length; x++) {
            for (int y = 0; y < this.field.length; y++) {
               CellStatus status = this.field[x][y];

               switch (status) {
                   case TRIPPED:
                       throw new Exception("You stepped on a mine and failed!");
                   case MARKED:
                       numMarked++;
                       break;
                   case MISMARKED:
                       numMismarked++;
                       break;
                   case CLEARED:
                       numCleared++;
                       break;
               }
            }
        }

        if (numMarked == numMines && numMismarked == 0) {
            return true;
        } else return numCleared == this.field.length * this.field.length - numMines;
    }
}
