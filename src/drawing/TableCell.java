package drawing;

import javafx.scene.control.TextField;

import java.util.Optional;

public class TableCell extends TextField {
    private int i;
    private int j;

    private Character symbol;
    private Integer state;
    private Character movement;

    public TableCell(int i, int j) {
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

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public Character getSymbol() {
        return symbol;
    }

    public void setSymbol(Character symbol) {
        this.symbol = symbol;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Character getMovement() {
        return movement;
    }

    public void setMovement(Character movement) {
        this.movement = movement;
    }

    public void setInstruction() {
        StringBuilder instruction = new StringBuilder();
        Optional.ofNullable(symbol).ifPresent(instruction::append);
        instruction.append(" ");
        Optional.ofNullable(state).ifPresent(instruction::append);
        instruction.append(" ");
        Optional.ofNullable(movement).ifPresent(instruction::append);
        super.setText(instruction.toString());
    }

    public void clear(TableCell tableCell) {
        symbol = null;
        state = null;
        movement = null;
        setInstruction();
    }
}

