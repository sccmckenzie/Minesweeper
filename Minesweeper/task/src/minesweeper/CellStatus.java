package minesweeper;

public enum CellStatus {
    // unknown
    UNKNOWN('.'), // no mine, but that is not certain to player
    PLANTED('.'), // mine is planted and NOT marked

    // guess
    MARKED('*'), // marker on cell WITH mine
    MISMARKED('*'), // marker on cell WITHOUT mine

    // known
    CLEARED('/'), // no mine, known to player
    TRIPPED('X'); // tripped mine

    char symbol;

    CellStatus(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
