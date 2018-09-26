package mt;

public class Instruction{
    private char symbol;
    private int state;
    private char movement;

    @java.lang.Override
    public java.lang.String toString() {
        return "Instruction{" +
                "symbol=" + symbol +
                ", state=" + state +
                ", movement=" + movement +
                '}';
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        Instruction that = (Instruction) object;

        if (symbol != that.symbol) return false;
        if (state != that.state) return false;
        if (movement != that.movement) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) symbol;
        result = 31 * result + state;
        result = 31 * result + (int) movement;
        return result;
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

    public char getMovement() {
        return movement;
    }

    public void setMovement(char movement) {
        this.movement = movement;
    }

    public Instruction(char symbol, int state, char movement) {
        this.symbol = symbol;
        this.state = state;
        this.movement = movement;
    }
}