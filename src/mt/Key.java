package mt;

import java.io.Serializable;
import java.util.Objects;

public class Key implements Serializable {

    private Character symbol;
    private Integer state;

    public Key() {
    }

    public Key(Character symbol, Integer state) {
        this.symbol = symbol;
        this.state = state;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return symbol + " " + state;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Key key = (Key) object;

        return Objects.equals(symbol, key.symbol) && Objects.equals(state, key.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, state);
    }
}
