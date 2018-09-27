/*
 * Copyright (c) Anton Kondrashkin 2018.
 * This progect created by Anton Kondrashkin. You hardly know him, but he's a very cool guy
 */

package mt;

public class Key {
    private char symbol;
    private int state;
    //Класс обозначает пару: символ алфавита и состояние. Используется для навигации по таблице алгоритма

    @Override
    public String toString() {
        return "Key{" +
                "symbol=" + symbol +
                ", state=" + state +
                '}';
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        Key key = (Key) object;

        if (symbol != key.symbol) return false;
        if (state != key.state) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) symbol;
        result = 31 * result + state;
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

    public Key(char symbol, int state) {

        this.symbol = symbol;
        this.state = state;
    }
}
