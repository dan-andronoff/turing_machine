package utils;

import drawing.TableCell;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mt.MT;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static drawing.DrawingConstants.CELL_HEIGHT;
import static drawing.DrawingConstants.CELL_WIDTH;

public abstract class PaneUtils {

    protected Pane mainPane;
    protected Character[] alphabet;
    protected int stateCount;

    protected TableCell selectedCell;
    protected List<TableCell> cells = new ArrayList<>();
    protected List<TextField> states = new ArrayList<>();
    protected List<TextField> symbols = new ArrayList<>();

    public PaneUtils(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public MT loadAlg(String fileName) {
        MT mt = MT.readAlgorithm(fileName);
        clearTable();
        setForm(mt.getAlphabet(), mt.getCountOfStates());
        mt.getAlg().forEach((key, instruction) -> {
            cells.stream()
                    .filter(cell -> cell.getKey().equals(key))
                    .findFirst()
                    .ifPresent(cell -> cell.setInstruction(instruction));
        });
        return mt;
    }

    public abstract void setForm(Character[] alphabet, int stateCount);

    protected void createInitialTable() {
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
                TableCell cell = new TableCell(i, j, alphabet);
                cell.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                cell.setLayoutY(i * CELL_HEIGHT);
                cell.setLayoutX(j * CELL_WIDTH);
                cell.setOnMouseClicked(onCellClick());
                cell.setEditable(false);
                cell.setCursor(Cursor.HAND);
                cells.add(cell);
                mainPane.getChildren().add(cell);
            }
        }
    }

    protected abstract EventHandler<MouseEvent> onCellClick();

    protected void clearTable() {
        cells.forEach(mainPane.getChildren()::remove);
        states.forEach(mainPane.getChildren()::remove);
        symbols.forEach(mainPane.getChildren()::remove);
        cells.clear();
        states.clear();
        symbols.clear();
    }

    protected void updateNumericChoiceBox(ChoiceBox<Integer> choiceBox, int maxValue) {
        choiceBox.getItems().setAll(IntStream.rangeClosed(1, maxValue).boxed().collect(Collectors.toList()));
    }
}
