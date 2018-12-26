package utils;

import controller.SymbolsFormController;
import drawing.TableCell;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mt.MT;
import mt.Movement;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static drawing.DrawingConstants.*;

public class ConstructorPaneUtils extends PaneUtils {

    private ChoiceBox<Integer> stateCountChoiceBox;
    private ChoiceBox<Character> alphabetChoiceBox;
    private ChoiceBox<Integer> stateChoiceBox;
    private ChoiceBox<Movement> movementChoiceBox;

    public ConstructorPaneUtils(Pane constructionPane, ChoiceBox<Integer> stateCountChoiceBox, ChoiceBox<Character> alphabetChoiceBox,
                                ChoiceBox<Integer> stateChoiceBox, ChoiceBox<Movement> movementChoiceBox) {
        super(constructionPane);
        this.stateCountChoiceBox = stateCountChoiceBox;
        this.alphabetChoiceBox = alphabetChoiceBox;
        this.stateChoiceBox = stateChoiceBox;
        this.movementChoiceBox = movementChoiceBox;
    }

    @Override
    public void setForm(Character[] alphabet, int stateCount) {
        this.stateCount = stateCount;
        this.alphabet = alphabet;

        stateCountChoiceBox.getItems().setAll(IntStream.rangeClosed(1, MAX_STATES).boxed().collect(Collectors.toList()));
        stateCountChoiceBox.getSelectionModel().select(stateCount - 1);
        alphabetChoiceBox.getItems().setAll(Arrays.asList(alphabet));
        updateNumericChoiceBox(stateChoiceBox, stateCount);
        movementChoiceBox.getItems().setAll(Movement.values());

        removeSelection();
        createInitialTable();
    }

    //Handlers

    public void onChangeStateCount() {
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
        removeSelection();
    }

    public void saveAlg(String fileName) {
        MT mt = new MT(alphabet, stateCount);
        cells.stream()
                .filter(cell -> cell.getState() != null || cell.getSymbol() != null || cell.getMovement() != null)
                .forEach(cell -> mt.addInstruction(cell.getKey(), cell.getInstruction()));
        mt.writeAlgorithm(fileName);
    }

    public void setAlphabet() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/symbols_form.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Задать алфавит");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            SymbolsFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAlphabet(alphabet);
            dialogStage.showAndWait();

            if (controller.isAlphabetChosen()) {
                updateAlphabetStates(controller.getAlphabet(), alphabet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteColumn() {
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
                        current.moveLeft();
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

            updateNumericChoiceBox(stateChoiceBox, --stateCount);
            updateCellsStatesAfterRemoving(selectedCell.getJ());
            removeSelection();
        }
    }

    public void insertColumnRight() {
        if (selectedCell != null && stateCount < MAX_STATES) {
            int j = selectedCell.getJ();
            //move cells
            for (TableCell cell : cells) {
                if (cell.getJ() > j) {
                    cell.moveRight();
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
                TableCell cell = new TableCell(i, j + 1, alphabet);
                cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setLayoutY(i * CELL_HEIGHT);
                cell.setLayoutX((j + 1) * CELL_WIDTH);
                //choose table cell on mouse clicked
                cell.setOnMouseClicked(onCellClick());
                cell.setEditable(false);
                cell.setCursor(Cursor.HAND);
                cells.add(cell);
                mainPane.getChildren().add(cell);
            }

            updateNumericChoiceBox(stateCountChoiceBox, ++stateCount);
            updateCellsStatesAfterInserting(selectedCell.getJ());
        }
    }

    public void insertColumnLeft() {
        if (selectedCell != null && stateCount < MAX_STATES) {
            int j = selectedCell.getJ();
            //move cells
            for (TableCell cell : cells) {
                if (cell.getJ() >= j) {
                    cell.moveRight();
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
                TableCell cell = new TableCell(i, j, alphabet);
                cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setLayoutY(i * CELL_HEIGHT);
                cell.setLayoutX(j * CELL_WIDTH);
                //choose table cell on mouse clicked
                cell.setOnMouseClicked(onCellClick());
                cell.setEditable(false);
                cell.setCursor(Cursor.HAND);
                cells.add(cell);
                mainPane.getChildren().add(cell);
            }

            updateNumericChoiceBox(stateCountChoiceBox, ++stateCount);
            updateCellsStatesAfterInserting(selectedCell.getJ() - 2);
        }
    }

    public void clearCell() {
        Optional.ofNullable(selectedCell).ifPresent(TableCell::clear);
    }

    public void onAddInstruction() {
        if (selectedCell != null) {
            selectedCell.setSymbol(alphabetChoiceBox.getSelectionModel().getSelectedItem());
            selectedCell.setState(stateChoiceBox.getSelectionModel().getSelectedItem());
            selectedCell.setMovement(movementChoiceBox.getSelectionModel().getSelectedItem());
            selectedCell.setInstruction();
        }
    }

    @Override
    protected EventHandler<MouseEvent> onCellClick() {
        return event -> {
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

    private void updateCellsStatesAfterRemoving(int removedState) {
        cells.stream()
                .filter(cell -> cell.getState() != null)
                .forEach(cell -> {
                    if (cell.getState() == removedState) {
                        cell.setState(null);
                        cell.setInstruction();
                    } else if (cell.getState() > removedState) {
                        cell.setState(cell.getState() - 1);
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

    private void updateAlphabetStates(Character[] newAlphabet, Character[] oldAlphabet) {
        Optional.ofNullable(selectedCell).ifPresent((selectedCell) -> {
            symbols.get(selectedCell.getI()).setStyle(DEFAULT_STYLE);
            states.get(selectedCell.getJ()).setStyle(DEFAULT_STYLE);
        });
        int index = 0;
        while (index < newAlphabet.length && index < oldAlphabet.length) {
            updateSymbol(newAlphabet[index], oldAlphabet[index], index + 1);
            index++;
        }
        while (newAlphabet.length > oldAlphabet.length && index < newAlphabet.length) {
            addSymbol(newAlphabet[index], index + 1);
            index++;
        }
        List<TextField> symbolsTextFieldsToRemove = new ArrayList<>();
        while (oldAlphabet.length > newAlphabet.length && index < oldAlphabet.length) {
            symbolsTextFieldsToRemove.add(removeSymbol(oldAlphabet[index], index + 1));
            index++;
        }
        symbols.removeAll(symbolsTextFieldsToRemove);
        alphabet = newAlphabet;
    }

    private void updateSymbol(Character newSymbol, Character oldSymbol, int row) {
        alphabetChoiceBox.getItems().remove(oldSymbol);
        alphabetChoiceBox.getItems().add(newSymbol);
        symbols.get(row).setText(newSymbol.toString());
        cells.stream()
                .filter(cell -> Objects.equals(cell.getSymbol(), oldSymbol))
                .forEach(cell -> {
                    cell.setSymbol(newSymbol);
                    cell.setInstruction();
                });
    }

    private TextField removeSymbol(Character symbol, int row) {
        alphabetChoiceBox.getItems().remove(symbol);
        TextField symbolTextField = symbols.get(row);
        mainPane.getChildren().remove(symbolTextField);
        List<TableCell> cellsToRemove = cells.stream()
                .filter(cell -> cell.getI() == row)
                .collect(Collectors.toList());
        cells.removeAll(cellsToRemove);
        mainPane.getChildren().removeAll(cellsToRemove);
        cells.stream()
                .filter(cell -> Objects.equals(cell.getSymbol(), symbol))
                .forEach(cell -> {
                    cell.setSymbol(null);
                    cell.setInstruction();
                });
        return symbolTextField;
    }

    private void addSymbol(Character symbol, int row) {
        alphabetChoiceBox.getItems().add(symbol);
        TextField symbolCell = new TextField();
        symbolCell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
        symbolCell.setLayoutY(CELL_HEIGHT * row);
        symbolCell.setLayoutX(0);
        symbolCell.setText(symbol.toString());
        symbolCell.setDisable(true);
        symbols.add(row, symbolCell);
        mainPane.getChildren().add(symbolCell);
        for (int j = 1; j <= stateCount; j++) {
            TableCell cell = new TableCell(row, j, alphabet);
            cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
            cell.setLayoutY(row * CELL_HEIGHT);
            cell.setLayoutX(j * CELL_WIDTH);
            cell.setOnMouseClicked(onCellClick());
            cell.setEditable(false);
            cell.setCursor(Cursor.HAND);
            cells.add(cell);
            mainPane.getChildren().add(cell);
        }
    }

    private void removeSelection() {
        Optional.ofNullable(selectedCell).ifPresent(selectedCell -> selectedCell.setStyle(DEFAULT_STYLE));
        selectedCell = null;
        stateChoiceBox.getSelectionModel().clearSelection();
        alphabetChoiceBox.getSelectionModel().clearSelection();
        movementChoiceBox.getSelectionModel().clearSelection();
    }
}
