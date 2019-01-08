package utils;

import drawing.TableCell;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import mt.Key;
import mt.MT;

import java.io.BufferedWriter;
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
    private List<Label> labels = new ArrayList<>();
    private TableCell currentCell;
    private TextField currentState;
    private int currentPointer;
    private MT loadedMT;

    private BufferedWriter tapeFile;
    private String tapeFileName;

    private int currentIteration = 0;

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

    public MT getLoadedMT() {
        return loadedMT;
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
        updateTapeCells(TAPE_POINTER);
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
        updateTapeCells(TAPE_POINTER);
        currentPointer = TAPE_POINTER;
        if (tapeFileName != null) {
            try {
                tapeFile = new BufferedWriter(new FileWriter(tapeFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean next() {
        if (currentIteration > MAX_ITERATIONS) {
            stop();
            throw new IllegalArgumentException("Too much operations!");
        }
        currentIteration++;
        if (tapeFile!=null) {
            StringBuilder line = new StringBuilder();
            line.append('|');
            loadedMT.getTape().forEach(symbol -> line.append(symbol).append('|'));
            try {
                tapeFile.write(line.toString());
                tapeFile.newLine();
                tapeFile.flush();
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
        updateTapeCells(TAPE_POINTER);
        currentPointer = 0;
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
        currentIteration = 0;
        currentPointer = TAPE_POINTER;
        //setTape();
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
        Button left = new Button();
        left.setPrefSize(TAPE_CELL_SIZE, TAPE_CELL_SIZE);
        left.setLayoutX(0);
        left.setLayoutY(0);
        left.setText("<");
        left.setFont(new Font(12));
        left.setAlignment(Pos.CENTER);
        left.setOnAction(event -> {
            int index = TAPE_CELL_COUNT - 1 + loadedMT.getPointer() - currentPointer;
            if (index - 5 >= 0) {
                currentPointer += 5;
                updateTapeCells(currentPointer);
            }
        });
        tapePane.getChildren().add(left);

        for (int i = 0; i < TAPE_CELL_COUNT; i++) {
            TextField tapeCell = new TextField();
            tapeCell.setPrefSize(TAPE_CELL_SIZE, TAPE_CELL_SIZE);
            tapeCell.setLayoutX(TAPE_CELL_SIZE + i * TAPE_CELL_SIZE);
            tapeCell.setLayoutY(0);
            tapeCell.setEditable(false);
            tapeCell.setCursor(Cursor.HAND);
            tapeCell.setAlignment(Pos.CENTER);
            tape.add(tapeCell);
            tapePane.getChildren().add(tapeCell);

            Label label = new Label();
            label.setPrefWidth(TAPE_CELL_SIZE);
            label.setLayoutX(TAPE_CELL_SIZE + i * TAPE_CELL_SIZE);
            label.setLayoutY(TAPE_CELL_SIZE * 1.2);
            label.setAlignment(Pos.CENTER);
            labels.add(label);
            tapePane.getChildren().add(label);
        }
        tape.get(TAPE_POINTER).setStyle(SELECTED_HEADER_STYLE);

        Button right = new Button();
        right.setPrefSize(TAPE_CELL_SIZE, TAPE_CELL_SIZE);
        right.setLayoutX((TAPE_CELL_COUNT + 1) * TAPE_CELL_SIZE);
        right.setLayoutY(0);
        right.setText(">");
        right.setFont(new Font(12));
        right.setAlignment(Pos.CENTER);
        right.setOnAction(event -> {
            int index = loadedMT.getPointer() - currentPointer;
            if (index + 5 < loadedMT.getTape().size()) {
                currentPointer -= 5;
                updateTapeCells(currentPointer);
            }
        });
        tapePane.getChildren().add(right);
    }

    private void updateTapeCells(Integer pointer) {
        List<Character> tape = loadedMT.getTape();
        for (int i = 0; i < TAPE_CELL_COUNT; i++) {
            int index = i + loadedMT.getPointer() - pointer;
            if (index >= 0 && index < tape.size()) {
                this.tape.get(i).setText(tape.get(index).toString());
                this.labels.get(i).setText(String.valueOf(index));
            } else {
                this.tape.get(i).setText(alphabet[1].toString());
                this.labels.get(i).setText("");
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
            updateTapeCells(TAPE_POINTER);
            currentPointer = TAPE_POINTER;
        }
    };
}
