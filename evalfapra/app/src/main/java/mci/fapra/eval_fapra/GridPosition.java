package mci.fapra.eval_fapra;

public class GridPosition {
    private final int column;
    private final int row;

    public GridPosition(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
