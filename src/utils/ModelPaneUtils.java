package utils;

import drawing.TableCell;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mt.Key;
import mt.MT;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static drawing.DrawingConstants.*;

public class ModelPaneUtils extends PaneUtils {

    private Pane tapePane;
    private ImageView nextInstruction;
    private ImageView stopModeling;

    private List<TextField> tape = new ArrayList<>();
    private TableCell currentCell;
    private TextField currentState;
    private MT loadedMT;

    private int operand1 = 2;
    private int operand2 = 3;

    public ModelPaneUtils(Pane mainPane, Pane tapePane, ImageView nextInstruction, ImageView stopModeling) {
        super(mainPane);
        this.tapePane= tapePane;
        this.nextInstruction = nextInstruction;
        this.nextInstruction.setDisable(true);
        this.stopModeling = stopModeling;
        this.stopModeling.setDisable(true);
        createTapeCells();
    }

    @Override
    public void setForm(Character[] alphabet, int stateCount) {
        this.stateCount = stateCount;
        this.alphabet = alphabet;

        createInitialTable();
        states.remove(0);
    }

    //Handlers

    @Override
    public MT loadAlg(String fileName) {
        loadedMT = super.loadAlg(fileName);
        alphabet = loadedMT.getAlphabet();
        stop();
        states.forEach(state -> {
            state.setOnMouseClicked(onStateClick);
            state.setCursor(Cursor.HAND);
            state.setEditable(false);
        });
        return loadedMT;
    }

    public void start() {
        nextInstruction.setDisable(false);
        stopModeling.setDisable(false);

        currentState.setStyle(DEFAULT_STYLE);
        states.forEach(states -> {
            states.setDisable(true);
            states.setStyle(DEFAULT_STYLE);
        });

        cells.stream()
                .filter(cell -> cell.getKey().equals(
                        new Key(loadedMT.getTape().get(loadedMT.getPointer()), Integer.valueOf(currentState.getText()))))
                .findFirst()
                .ifPresent(cell -> {
                    cell.setStyle(SELECTED_HEADER_STYLE);
                    currentCell = cell;
                });
        loadedMT.setCurrentState(Integer.valueOf(currentState.getText()));
    }

    public void next() {
        Key currentKey = loadedMT.next();
        cells.stream()
                .filter(cell -> cell.getKey().equals(currentKey))
                .findFirst()
                .ifPresent(cell -> {
                    currentCell.setStyle(DEFAULT_STYLE);
                    cell.setStyle(SELECTED_HEADER_STYLE);
                    currentCell = cell;
                });
        updateTapeCells();
    }

    public void stop() {
        nextInstruction.setDisable(true);
        stopModeling.setDisable(true);

        currentState = states.get(0);
        currentState.setStyle(SELECTED_HEADER_STYLE);
        states.forEach(states -> states.setDisable(false));
        if (currentCell != null) {
            currentCell.setStyle(DEFAULT_STYLE);
        }

        loadedMT.setPointer(0);
        setTape();
        updateTapeCells();
    }

    @Override
    protected EventHandler<MouseEvent> onCellClick() {
        return event -> {};
    }

    private void createTapeCells() {
        for (int i = 0; i < TAPE_CELL_COUNT; i++) {
            TextField tapeCell = new TextField();
            tapeCell.setPrefSize(TAPE_CELL_SIZE, TAPE_CELL_SIZE);
            tapeCell.setLayoutX(i * TAPE_CELL_SIZE);
            tapeCell.setLayoutY(0);
            tapeCell.setEditable(false);
            tapeCell.setCursor(Cursor.HAND);
            tape.add(tapeCell);
            tapePane.getChildren().add(tapeCell);
        }
        tape.get(TAPE_POINTER).setStyle(SELECTED_HEADER_STYLE);
    }

    private void updateTapeCells() {
        List<Character> tape = loadedMT.getTape();
        for (int i = 0; i < TAPE_CELL_COUNT; i++) {
            int index = i + loadedMT.getPointer() - TAPE_POINTER;
            if (index >= 0 && index < tape.size()) {
                this.tape.get(i).setText(tape.get(index).toString());
            } else {
                this.tape.get(i).setText(alphabet[1].toString());
            }
        }
    }

    private EventHandler<MouseEvent> onStateClick = event -> {
        currentState.setStyle(DEFAULT_STYLE);
        currentState = (TextField) event.getSource();
        currentState.setStyle(SELECTED_HEADER_STYLE);
        loadedMT.setCurrentState(Integer.valueOf(currentState.getText()));
    };

    private void setTape() {
        LinkedList<Character> tape = new LinkedList<>();
        Character operandSymbol = alphabet[0];
        Character delimiterSymbol = alphabet[2];
        for (int i=0; i<operand1; i++) {
            tape.addLast(operandSymbol);
        }
        tape.addLast(delimiterSymbol);
        for (int i=0; i<operand2; i++) {
            tape.addLast(operandSymbol);
        }
        loadedMT.setTape(tape);
    }
}
