package mt;

import java.io.Serializable;
import java.util.Objects;

public class Instruction implements Serializable {

    private Character symbol;
    private Integer state;
    private Movement movement;

    public Instruction(Character symbol, Integer state, Movement movement) {
        this.symbol = symbol;
        this.state = state;
        this.movement = movement;
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

    @Override
    public String toString() {
        return symbol + " " + state + " " + movement;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Instruction instruction = (Instruction) object;

        return Objects.equals(symbol, instruction.symbol) && Objects.equals(state, instruction.getState())
                && movement == instruction.movement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, state, movement);
    }
}
