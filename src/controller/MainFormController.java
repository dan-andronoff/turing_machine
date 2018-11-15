package controller;

import static drawing.DrawingConstants.*;

import drawing.TableCell;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mt.Instruction;
import mt.Key;
import mt.MT;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainFormController {

    @FXML
    private Pane mainPane;

    private TableCell selectedCell;
    private List<TableCell> cells = new ArrayList<>();
    private List<TextField> states = new ArrayList<>();
    private List<TextField> symbols = new ArrayList<>();

    @FXML
    private ChoiceBox<Integer> stateCountChoiceBox;
    @FXML
    private ChoiceBox<Character> alphabetChoiceBox;
    @FXML
    private ChoiceBox<Integer> stateChoiceBox;
    @FXML
    private ChoiceBox<Character> movementChoiceBox;

    private Character[] alphabet;
    private int stateCount;

    @FXML
    public void initialize() {
        alphabet = INITIAL_ALPHABET;
        stateCount = INITIAL_STATE_COUNT;

        stateCountChoiceBox.getItems().setAll(IntStream.rangeClosed(1, MAX_STATES).boxed().collect(Collectors.toList()));
        stateCountChoiceBox.getSelectionModel().selectLast();
        alphabetChoiceBox.getItems().setAll(Arrays.asList(alphabet));
        updateStateChoiceBox(stateCount);
        movementChoiceBox.getItems().setAll('Н', 'П', 'Л');

        createInitialTable(alphabet, stateCount);
    }

    //Top menu panel

    @FXML
    private void onChangeStateCount() {
        Optional.ofNullable(selectedCell).ifPresent((selectedCell) -> {
            symbols.get(selectedCell.getI()).setStyle(DEFAULT_STYLE);
            states.get(selectedCell.getJ()).setStyle(DEFAULT_STYLE);
        });
        while (stateCount > stateCountChoiceBox.getSelectionModel().getSelectedItem()) {
            selectedCell = cells.stream()
                    .filter(cell -> cell.getJ() == stateCount)
                    .findFirst()
                    .orElse(null);
            deleteColumn();
        }
        while (stateCount < stateCountChoiceBox.getSelectionModel().getSelectedItem()) {
            selectedCell = cells.stream()
                    .filter(cell -> cell.getJ() == stateCount)
                    .findFirst()
                    .orElse(null);
            insertColumnRight();
        }
        Optional.ofNullable(selectedCell).ifPresent(selectedCell -> selectedCell.setStyle(DEFAULT_STYLE));
        selectedCell = null;
    }

    @FXML
    private void saveAlg() {
        MT mt = new MT(stateCount);
        cells.stream()
                .filter(cell -> cell.getState() != null && cell.getSymbol() != null && cell.getMovement() != null)
                .forEach(cell -> {
                    Key key = new Key(alphabet[cell.getI() - 1], cell.getJ());
                    Instruction instruction = new Instruction(cell.getSymbol(), cell.getState(), cell.getMovement());
                    mt.addInstruction(key, instruction);
                });
        System.out.println(mt.getAlg());
    }

    //Left menu panel

    @FXML
    private void deleteColumn() {
        if (selectedCell != null && stateCount > MIN_STATES) {
            int j = selectedCell.getJ();
            Iterator<TableCell> cellsIterator = cells.iterator();
            //move cells
            while (cellsIterator.hasNext()) {
                TableCell current = cellsIterator.next();
                if (current.getJ() == j) {
                    cellsIterator.remove();
                    mainPane.getChildren().remove(current);
                } else {
                    if (current.getJ() > j) {
                        current.setJ(current.getJ() - 1);
                        current.setLayoutX(current.getJ() * CELL_WIDTH);
                    }
                }
            }
            //move state row
            mainPane.getChildren().remove(states.get(j));
            states.remove(j);
            for (int m = j; m < states.size(); m++) {
                states.get(m).setLayoutX(m * CELL_WIDTH);
                states.get(m).setText(String.valueOf(m));
            }
            //remove cell selection 
            symbols.get(selectedCell.getI()).setStyle(DEFAULT_STYLE);

            updateStateChoiceBox(--stateCount);
            updateCellsStatesAfterRemoving(selectedCell.getJ());
            selectedCell = null;
        }
    }

    @FXML
    private void insertColumnRight() {
        if (selectedCell != null && stateCount < MAX_STATES) {
            int j = selectedCell.getJ();
            //move cells
            for (TableCell cell : cells) {
                if (cell.getJ() > j) {
                    cell.setJ(cell.getJ() + 1);
                    cell.setLayoutX(cell.getJ() * CELL_WIDTH);
                }
            }
            //create state cell
            TextField stateCell = new TextField();
            stateCell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
            stateCell.setLayoutY(0);
            stateCell.setLayoutX((j + 1) * CELL_WIDTH);
            stateCell.setText(String.valueOf(j + 1));
            stateCell.setDisable(true);
            states.add(j + 1, stateCell);
            mainPane.getChildren().add(stateCell);
            //move state row
            for (int m = j + 2; m < states.size(); m++) {
                states.get(m).setLayoutX(m * CELL_WIDTH);
                states.get(m).setText(String.valueOf(m));
            }
            //create cells
            for (int i = 1; i <= alphabet.length; i++) {
                TableCell cell = new TableCell(i, j + 1);
                cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setLayoutY(i * CELL_HEIGHT);
                cell.setLayoutX((j + 1) * CELL_WIDTH);
                //choose table cell on mouse clicked
                cell.setOnMouseClicked(onCellClick);
                cell.setEditable(false);
                cell.setCursor(Cursor.HAND);
                cells.add(cell);
                mainPane.getChildren().add(cell);
            }

            updateStateChoiceBox(++stateCount);
            updateCellsStatesAfterInserting(selectedCell.getJ());
        }
    }

    @FXML
    private void insertColumnLeft() {
        if (selectedCell != null && stateCount < MAX_STATES) {
            int j = selectedCell.getJ();
            //move cells
            for (TableCell cell : cells) {
                if (cell.getJ() >= j) {
                    cell.setJ(cell.getJ() + 1);
                    cell.setLayoutX(cell.getJ() * CELL_WIDTH);
                }
            }
            //create state cell
            TextField stateCell = new TextField();
            stateCell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
            stateCell.setLayoutY(0);
            stateCell.setLayoutX((j) * CELL_WIDTH);
            stateCell.setText(String.valueOf(j));
            stateCell.setDisable(true);
            states.add(j, stateCell);
            mainPane.getChildren().add(stateCell);
            //move state row
            for (int m = j; m < states.size(); m++) {
                states.get(m).setLayoutX(m * CELL_WIDTH);
                states.get(m).setText(String.valueOf(m));
            }
            //create cells
            for (int i = 1; i <= alphabet.length; i++) {
                TableCell cell = new TableCell(i, j);
                cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setLayoutY(i * CELL_HEIGHT);
                cell.setLayoutX(j * CELL_WIDTH);
                //choose table cell on mouse clicked
                cell.setOnMouseClicked(onCellClick);
                cell.setEditable(false);
                cell.setCursor(Cursor.HAND);
                cells.add(cell);
                mainPane.getChildren().add(cell);
            }

            updateStateChoiceBox(++stateCount);
            updateCellsStatesAfterInserting(selectedCell.getJ() - 2);
        }
    }

    @FXML
    private void clearCell() {
        Optional.ofNullable(selectedCell).ifPresent(selectedCell::clear);
    }

    //Pane elements controllers

    @FXML
    public void onAddInstruction() {
        if (selectedCell != null) {
            selectedCell.setSymbol(alphabetChoiceBox.getSelectionModel().getSelectedItem());
            selectedCell.setState(stateChoiceBox.getSelectionModel().getSelectedItem());
            selectedCell.setMovement(movementChoiceBox.getSelectionModel().getSelectedItem());
            selectedCell.setInstruction();
        }
    }

    private void updateStateChoiceBox(int stateCount) {
        stateChoiceBox.getItems().setAll(IntStream.rangeClosed(1, stateCount).boxed().collect(Collectors.toList()));
    }

    private void updateCellsStatesAfterRemoving(int removedState) {
        cells.stream()
                .filter(cell -> cell.getState() != null)
                .forEach(cell -> {
                    if (cell.getState() > removedState) {
                        cell.setState(cell.getState() - 1);
                        cell.setInstruction();
                    }
                    if (cell.getState() == removedState) {
                        cell.setState(null);
                        cell.setInstruction();
                    }
                });
    }

    private void updateCellsStatesAfterInserting(int insertedState) {
        cells.stream()
                .filter(cell -> cell.getState() != null)
                .forEach(cell -> {
                    if (cell.getState() > insertedState) {
                        cell.setState(cell.getState() + 1);
                        cell.setInstruction();
                    }
                });
    }

    private void createInitialTable(Character[] alphabet, int stateCount) {
        TextField header = new TextField();
        header.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
        header.setLayoutY(0);
        header.setLayoutX(0);
        header.setText("/");
        header.setDisable(true);
        symbols.add(0, header);
        states.add(0, header);
        mainPane.getChildren().add(header);

        //create alphabet column
        for (int i = 1; i <= alphabet.length; i++) {
            TextField cell = new TextField();
            cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
            cell.setLayoutY(i * CELL_HEIGHT);
            cell.setLayoutX(0);
            cell.setText(String.valueOf(alphabet[i - 1]));
            cell.setDisable(true);
            symbols.add(i, cell);
            mainPane.getChildren().add(cell);
        }

        //create state row
        for (int j = 1; j <= stateCount; j++) {
            TextField cell = new TextField();
            cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
            cell.setLayoutY(0);
            cell.setLayoutX(j * CELL_WIDTH);
            cell.setText(String.valueOf(j));
            cell.setDisable(true);
            states.add(j, cell);
            mainPane.getChildren().add(cell);
        }

        //create other cells
        for (int i = 1; i <= alphabet.length; i++) {
            for (int j = 1; j <= stateCount; j++) {
                TableCell cell = new TableCell(i, j);
                cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setLayoutY(i * CELL_HEIGHT);
                cell.setLayoutX(j * CELL_WIDTH);
                cell.setOnMouseClicked(onCellClick);
                cell.setEditable(false);
                cell.setCursor(Cursor.HAND);
                cells.add(cell);
                mainPane.getChildren().add(cell);
            }
        }
    }

    private final EventHandler<MouseEvent> onCellClick = event -> {
        TableCell tableCell = (TableCell) event.getSource();
        symbols.get(tableCell.getI()).setStyle(SELECTED_HEADER_STYLE);
        states.get(tableCell.getJ()).setStyle(SELECTED_HEADER_STYLE);
        if (selectedCell != null) {
            if (tableCell.getI() != selectedCell.getI()) {
                symbols.get(selectedCell.getI()).setStyle(DEFAULT_STYLE);
            }
            if (tableCell.getJ() != selectedCell.getJ()) {
                states.get(selectedCell.getJ()).setStyle(DEFAULT_STYLE);
            }
        }
        alphabetChoiceBox.getSelectionModel().clearSelection();
        stateChoiceBox.getSelectionModel().clearSelection();
        movementChoiceBox.getSelectionModel().clearSelection();
        Optional.ofNullable(tableCell.getSymbol()).ifPresent(alphabetChoiceBox.getSelectionModel()::select);
        Optional.ofNullable(tableCell.getState()).ifPresent(stateChoiceBox.getSelectionModel()::select);
        Optional.ofNullable(tableCell.getMovement()).ifPresent(movementChoiceBox.getSelectionModel()::select);
        selectedCell = tableCell;
    };
}
