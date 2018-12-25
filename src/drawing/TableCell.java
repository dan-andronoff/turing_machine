package drawing;

import javafx.scene.control.TextField;
import mt.Instruction;
import mt.Key;
import mt.Movement;

import java.util.Optional;

import static drawing.DrawingConstants.CELL_WIDTH;

public class TableCell extends TextField {

    private int i;
    private Character[] alphabet;
    private int j;

    private Character symbol;
    private Integer state;
    private Movement movement;

    public TableCell(int i, int j, Character[] alphabet) {
        super();
        this.i = i;
        this.j = j;
        this.alphabet = alphabet;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void moveLeft() {
        setLayoutX(getLayoutX() - CELL_WIDTH);
        j--;
    }

    public void moveRight() {
        setLayoutX(getLayoutX() + CELL_WIDTH);
        j++;
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

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public void clear() {
        symbol = null;
        state = null;
        movement = null;
        setInstruction();
    }

    public Instruction getInstruction() {
        return new Instruction(symbol, state, movement);
    }

    public Key getKey() {
        return new Key(alphabet[i - 1], j);
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

    public void setInstruction(Instruction instruction) {
        symbol = instruction.getSymbol();
        state = instruction.getState();
        movement = instruction.getMovement();
        setInstruction();
    }
}

