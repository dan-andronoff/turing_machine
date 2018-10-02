package controller;

import static drawing.DrawingConstants.*;

import drawing.TableCell;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainFormController {

    @FXML
    private Pane mainPane;
    private TableCell selectedCell;
    private List<TableCell> cells = new ArrayList<>();
    private List<TextField> states = new ArrayList<>();
    private List<TextField> symbols = new ArrayList<>();

    @FXML
    public void initialize() {
        createInitialTable(INITIAL_ALPHABET, INITIAL_STATE_COUNT);
    }

    private void createInitialTable(char[] alphabet, int stateCount) {
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
                TableCell cell = new TableCell(i, j, alphabet[i - 1]);
                cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setLayoutY(i * CELL_HEIGHT);
                cell.setLayoutX(j * CELL_WIDTH);
                //choose table cell on mouse clicked
                cell.setOnMouseClicked((event -> {
                    TableCell tableCell = (TableCell) event.getSource();
                    symbols.get(tableCell.getI()).setStyle(SELECTED_HEADER_STYLE);
                    states.get(tableCell.getJ()).setStyle(SELECTED_HEADER_STYLE);
                    if (selectedCell != null) {
                        if (tableCell.getI() != selectedCell.getI()) {
                            symbols.get(selectedCell.getI()).setStyle(HEADER_STYLE);
                        }
                        if (tableCell.getJ() != selectedCell.getJ()) {
                            states.get(selectedCell.getJ()).setStyle(HEADER_STYLE);
                        }
                    }
                    selectedCell = tableCell;
                }));
                cells.add(cell);
                mainPane.getChildren().add(cell);
            }
        }
    }

    @FXML
    private void deleteColumn() {
        if (selectedCell != null) {
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
            symbols.get(selectedCell.getI()).setStyle(HEADER_STYLE);
            selectedCell = null;
        }
    }

    @FXML
    private void insertColumnRight() {
        if (selectedCell != null) {
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
            for (int i = 1; i <= INITIAL_ALPHABET.length; i++) {
                //TODO fix setting initial alphabet
                TableCell cell = new TableCell(i, j + 1, INITIAL_ALPHABET[i - 1]);
                cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setLayoutY(i * CELL_HEIGHT);
                cell.setLayoutX((j + 1) * CELL_WIDTH);
                //choose table cell on mouse clicked
                cell.setOnMouseClicked((event -> {
                    TableCell tableCell = (TableCell) event.getSource();
                    symbols.get(tableCell.getI()).setStyle(SELECTED_HEADER_STYLE);
                    states.get(tableCell.getJ()).setStyle(SELECTED_HEADER_STYLE);
                    if (selectedCell != null) {
                        if (tableCell.getI() != selectedCell.getI()) {
                            symbols.get(selectedCell.getI()).setStyle(HEADER_STYLE);
                        }
                        if (tableCell.getJ() != selectedCell.getJ()) {
                            states.get(selectedCell.getJ()).setStyle(HEADER_STYLE);
                        }
                    }
                    selectedCell = tableCell;
                }));
                cells.add(cell);
                mainPane.getChildren().add(cell);
            }
        }
    }

    @FXML
    private void insertColumnLeft() {
        if (selectedCell != null) {
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
            for (int i = 1; i <= INITIAL_ALPHABET.length; i++) {
                //TODO fix setting initial alphabet
                TableCell cell = new TableCell(i, j, INITIAL_ALPHABET[i - 1]);
                cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setLayoutY(i * CELL_HEIGHT);
                cell.setLayoutX(j * CELL_WIDTH);
                //choose table cell on mouse clicked
                cell.setOnMouseClicked((event -> {
                    TableCell tableCell = (TableCell) event.getSource();
                    symbols.get(tableCell.getI()).setStyle(SELECTED_HEADER_STYLE);
                    states.get(tableCell.getJ()).setStyle(SELECTED_HEADER_STYLE);
                    if (selectedCell != null) {
                        if (tableCell.getI() != selectedCell.getI()) {
                            symbols.get(selectedCell.getI()).setStyle(HEADER_STYLE);
                        }
                        if (tableCell.getJ() != selectedCell.getJ()) {
                            states.get(selectedCell.getJ()).setStyle(HEADER_STYLE);
                        }
                    }
                    selectedCell = tableCell;
                }));
                cells.add(cell);
                mainPane.getChildren().add(cell);
            }
        }
    }
}
