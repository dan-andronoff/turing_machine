package mt;

import java.io.*;
import java.util.*;

import static drawing.DrawingConstants.MAX_ITERATIONS;

public class MT implements Serializable {

    private HashMap<Key, Instruction> alg = new HashMap<>();

    private List<Character> alphabet;
    private int countOfStates;

    private transient int pointer;
    private transient Integer currentState;
    private transient LinkedList<Character> tape = new LinkedList<>();

    public MT(Character[] alphabet, int countOfStates) {
        this.countOfStates = countOfStates;
        this.alphabet = Arrays.asList(alphabet);
        tape.add(alphabet[1]);
    }

    public List<Character> getTape() {
        return tape;
    }

    public void setTape(LinkedList<Character> tape) {
        this.tape = tape;
    }

    public HashMap<Key, Instruction> getAlg() {
        return alg;
    }

    public Character[] getAlphabet() {
        return alphabet.toArray(new Character[]{});
    }

    public int getCountOfStates() {
        return countOfStates;
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public Integer getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Integer state) {
        currentState = state;
    }

    public void addInstruction(Key key, Instruction value) {
        alg.put(key, value);
    }

    public void writeAlgorithm(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
        } catch (Exception e) {
            throw new RuntimeException("Can't save mt to file", e);
        }
    }

    public static MT readAlgorithm(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (MT) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Can't load mt from file", e);
        }
    }

    public Key next() {
        char currentSymbol = tape.get(pointer);
        Key currentKey = new Key(currentSymbol, currentState);
        Instruction instruction = alg.get(currentKey);
        if (instruction.getSymbol() != null) {
            tape.set(pointer, instruction.getSymbol());
        }
        switch (instruction.getMovement()) {
            case RIGHT:
                pointer++;
                if (pointer == tape.size()) {
                    tape.addLast(alphabet.get(1));
                }
                break;
            case LEFT:
                pointer--;
                if (pointer == -1) {
                    tape.addFirst(' ');
                    pointer = 0;
                }
                break;
        }
        currentState = instruction.getState();
        return new Key(tape.get(pointer), currentState);
    }
}
