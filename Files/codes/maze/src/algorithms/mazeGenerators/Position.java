package algorithms.mazeGenerators;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRowIndex() {
        return row;
    }

    public int getColumnIndex() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String toString() {
        return "{" + row + "," + col + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return (row == position.row && col == position.col);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

}
