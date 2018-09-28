/*
 * Copyright (c) Anton Kondrashkin 2018.
 * This progect created by Anton Kondrashkin. You hardly know him, but he's a very cool guy
 */

package mt;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MT {
    private ArrayList<Character> tape;
    private HashMap<Key,Instruction> alg;
    private ArrayList<Character> alphabet;

    private int pointer = 0; //указатель на текущий элемент в ленте
    private Key currentState;//текущиее состояние
    private int countOfStates = 3;

    // конструктор без параметров - для самого простого алгоритма с тремя символами
    public MT(){
        tape = new ArrayList<>();
        alphabet = new ArrayList<>();

        //Заполняем алфавит по умолчанию
        this.alphabet.add('*');
        this.alphabet.add(',');

        this.alphabet.add(' ');
        pointer = 0;
        currentState = new Key('*',1);
        alg = new HashMap<>();

        // Заполняем таблицу алгоритма пустыми ячейками
        /*for(char c: alphabet){
            for(int i=1; i<=countOfStates; i++){
                if(i!=countOfStates)
                    alg.put(new Key(c,i),null);// 1 - всегда начальное состояние
                else
                    alg.put(new Key(c,-1),null);// -1 - финальное состояние
            }
        }*/

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

    //Метод для добавления одной тройки алгоритма по состоянию и символу алфавита
    public void addInstruction(Key key, Instruction value){
        alg.put(key,value);
    }

    //Получить инструкцию целиком на основе символа и состояния
    public Instruction getInstruction(char symbol, int state){
        Set<Key> keys = alg.keySet();
        for (Key k:keys){
            if(k.getSymbol()==symbol && k.getState()== state)
                return alg.get(k);
        }
        return null;
    }

    //Получить инструкцию целиком на основе ключа
    public Instruction getInstruction(Key key){
        return alg.get(key);
    }

    //Получить ТЕКУЩУЮ инструкцию
    public Instruction getCurrentInstruction(){
        return alg.get(currentState);
    }

    // устанавливается текущиее состояние и буква. Проще говоря - позиция в таблице
    public void setCurrentState(char symbol, int state) {
        this.currentState.setSymbol(symbol);
        this.currentState.setState(state);
    }

    // Добавление алгоритма целиком
    public void setAlg(HashMap<Key, Instruction> alg) {
        this.alg = alg;
    }

    //Метод предназначен для определения того, есть ли алгоритм с таким названием. Рекомендуется использовать перед записью
    public boolean algExists(String nameOfAlgorithm){
        File f =new File(nameOfAlgorithm+".mt");
        return f.exists();
    }

    //Метод для записи в файл. В качестве параметра принимает имя алгоритма. Оно же - название файла
    public void writeAlgorithm(String nameOfAlgorithm) throws IOException {
        File f =new File(nameOfAlgorithm+".mt");
        if (!f.exists())
            f.createNewFile();
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(alg);
        oos.close();
        fos.close();
    }

    //Метод для чтения из файла. В качестве параметра принимает имя алгоритма. Оно же - название файла
    public void readAlgorithm(String nameOfAlgorithm) throws IOException {
        File f =new File(nameOfAlgorithm+".mt");
        if (!f.exists())
            throw new FileNotFoundException();
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        try {
            alg = (HashMap<Key, Instruction>)ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        fis.close();
        ois.close();
    }


}
