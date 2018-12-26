package utils;

import drawing.TableCell;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mt.Key;
import mt.MT;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static drawing.DrawingConstants.*;

public class ModelPaneUtils extends PaneUtils {

    private Pane tapePane;
    private ImageView startModeling;
    private ImageView nextInstruction;
    private ImageView stopModeling;
    private ChoiceBox<Integer> operand1ChoiceBox;
    private ChoiceBox<Integer> operand2ChoiceBox;

    private List<TextField> tape = new ArrayList<>();
    private TableCell currentCell;
    private TextField currentState;
    private MT loadedMT;

    private BufferedWriter tapeFile;
    private String tapeFileName;

    public ModelPaneUtils(Pane mainPane, Pane tapePane, ImageView startModeling, ImageView nextInstruction, ImageView stopModeling,
                          ChoiceBox<Integer> operand1ChoiceBox, ChoiceBox<Integer> operand2ChoiceBox) {
        super(mainPane);
        this.tapePane= tapePane;
        this.startModeling = startModeling;
        this.nextInstruction = nextInstruction;
        this.nextInstruction.setDisable(true);
        this.stopModeling = stopModeling;
        this.stopModeling.setDisable(true);
        this.operand1ChoiceBox = operand1ChoiceBox;
        updateNumericChoiceBox(operand1ChoiceBox, MAX_OPERAND_VALUE);
        this.operand1ChoiceBox.setOnAction(updateOperand);
        this.operand1ChoiceBox.getSelectionModel().selectFirst();
        this.operand2ChoiceBox = operand2ChoiceBox;
        updateNumericChoiceBox(operand2ChoiceBox, MAX_OPERAND_VALUE);
        this.operand2ChoiceBox.setOnAction(updateOperand);
        this.operand2ChoiceBox.getSelectionModel().selectFirst();

        createTapeCells();
    }

    public void setTapeFileName(String tapeFileName) {
        this.tapeFileName = tapeFileName;
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
        loadMT(super.loadAlg(fileName));

        return loadedMT;
    }

    public void loadMT(MT mt) {
        loadedMT = mt;
        alphabet = loadedMT.getAlphabet();
        clearTable();
        setTape();
        updateTapeCells();
        setForm(mt.getAlphabet(), mt.getCountOfStates());
        mt.getAlg().forEach((key, instruction) -> {
            cells.stream()
                    .filter(cell -> cell.getKey().equals(key))
                    .findFirst()
                    .ifPresent(cell -> cell.setInstruction(instruction));
        });
        stop();
        states.forEach(state -> {
            state.setOnMouseClicked(onStateClick);
            state.setCursor(Cursor.HAND);
            state.setEditable(false);
        });
    }

    public void start() {
        startModeling.setDisable(true);
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

        setTape();
        updateTapeCells();
        if (tapeFileName != null) {
            try {
                tapeFile = new BufferedWriter(new FileWriter(tapeFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean next() {
        if (tapeFile!=null) {
            StringBuilder line = new StringBuilder();
            line.append('|');
            loadedMT.getTape().forEach(symbol -> line.append(symbol).append('|'));
            try {
                tapeFile.write(line.toString());
                tapeFile.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        if (loadedMT.getAlg().containsKey(currentKey)) {
            return true;
        } else {
            stop();
            return false;
        }
    }

    public void stop() {
        startModeling.setDisable(false);
        nextInstruction.setDisable(true);
        stopModeling.setDisable(true);

        currentState = states.get(0);
        currentState.setStyle(SELECTED_HEADER_STYLE);
        states.forEach(states -> states.setDisable(false));
        if (currentCell != null) {
            currentCell.setStyle(DEFAULT_STYLE);
        }

        loadedMT.setPointer(0);
        loadedMT.clearIteration();
        setTape();
        //updateTapeCells();

        if (tapeFileName != null) {
            try {
                tapeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        for (int i=0; i<operand1ChoiceBox.getSelectionModel().getSelectedItem(); i++) {
            tape.addLast(operandSymbol);
        }
        tape.addLast(delimiterSymbol);
        for (int i=0; i<operand2ChoiceBox.getSelectionModel().getSelectedItem(); i++) {
            tape.addLast(operandSymbol);
        }
        loadedMT.setTape(tape);
    }

    private EventHandler<ActionEvent> updateOperand = event -> {
        if (loadedMT != null) {
            setTape();
            updateTapeCells();
        }
    };
}
