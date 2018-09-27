/*
 * Copyright (c) Anton Kondrashkin 2018.
 * This progect created by Anton Kondrashkin. You hardly know him, but he's a very cool guy
 */

package mt;

import java.util.ArrayList;
import java.util.HashMap;

public class MT {
    private ArrayList<Character> tape;
    private HashMap<Key,Instruction> alg;
    private ArrayList<Character> alphabet;

    private int pointer = 0; //указатель на текущий элемент в ленте
    private Key currentState;//текущиее состояние
    private int countOfStates = 3;

    // устанавливается текущиее состояние и буква. Проще говоря - позиция в таблице
    public void setCurrentState(char symbol, int state) {
        this.currentState.setSymbol(symbol);
        this.currentState.setState(state);
    }

    // конструктор без параметров - для самого простого алгоритма с тремя символами
    public MT(){
        tape = new ArrayList<>();

        //Заполняем алфавит по умолчанию
        this.alphabet.add('*');
        this.alphabet.add(',');
        this.alphabet.add(' ');
        pointer = 0;
        currentState = new Key('*',1);
        alg = new HashMap<>();

        // Заполняем таблицу алгоритма пустыми ячейками
        for(char c: alphabet){
            for(int i=1; i<=countOfStates; i++){
                if(i!=countOfStates)
                    alg.put(new Key(c,i),null);// 1 - всегда начальное состояние
                else
                    alg.put(new Key(c,-1),null);// -1 - финальное состояние
            }
        }

    }

    //Добавить в алфавит символы, причём их число может быть переменным
    //При попытке добавить уже имеющийся символ он будет игнорироваться
    public void addAlphabetSymbol(char ...symbols) throws IndexOutOfBoundsException{
        for(Character c: symbols){
            if(!this.alphabet.contains(c))
            {
                if(alphabet.size()==5)
                    throw new IndexOutOfBoundsException("Нельзя добавить больше символов!");

                //Добавляем символ в алфавит
                this.alphabet.add(c);

                //Добавляем строчку в таблицу алгоритма
                for(int i=1; i<=countOfStates; i++)
                    if(i!=countOfStates)
                        alg.put(new Key(c,i),null);
                    else
                        alg.put(new Key(c,-1),null);

            }
        }
    }

    public void addState() throws IndexOutOfBoundsException{
        if(countOfStates == 10)
            throw new IndexOutOfBoundsException("Нельзя добавить больше состояний!");

        for(char c: alphabet){
            alg.put(new Key(c,countOfStates),null);// Добавляем новый столбец состояния
            }
        countOfStates++;

    }

    //Получить алфавит МТ
    public ArrayList<Character> getAlphabet() {
        return alphabet;
    }

    //Получить текущую ленту МТ. Все действия алгоритма отражаются на этой ленте
    public ArrayList<Character> getTape() {
        return tape;
    }

    //Получить алгоритм МТ
    public HashMap<Key, Instruction> getAlg() {
        return alg;
    }

    //Узнать текущий указатель
    public int getPointer() {
        return pointer;
    }

    // Узнать текущее состояние и символ алфавита
    public Key getCurrentState() {
        return currentState;
    }
}
