/*
 * Copyright (c) Anton Kondrashkin 2018.
 * This progect created by Anton Kondrashkin. You hardly know him, but he's a very cool guy
 */

package mt;

import java.io.Serializable;
import java.util.Objects;

public class Key implements Serializable {
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

        Key key = (Key) object;

        return (symbol == key.symbol) && (state == key.state);
    }

    public int hashCode() {
        return Objects.hash(symbol, state);
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
