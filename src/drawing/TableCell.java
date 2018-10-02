package drawing;

import javafx.scene.control.TextField;

public class TableCell extends TextField {
    private int i;
    private int j;
    private char c;

    public TableCell(int i, int j, char c) {
        super();
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public char getC() {
        return c;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }
}

