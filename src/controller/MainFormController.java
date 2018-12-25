package controller;

import static drawing.DrawingConstants.*;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import mt.Movement;
import utils.ConstructorPaneUtils;
import utils.ModelPaneUtils;

import java.io.*;

public class MainFormController {

    @FXML
    public void initialize() {
        constructorPaneUtils = new ConstructorPaneUtils(constructionPane, stateCountChoiceBox,
                alphabetChoiceBox, stateChoiceBox, movementChoiceBox);
        constructorPaneUtils.setForm(INITIAL_ALPHABET, INITIAL_STATE_COUNT);

        modelPaneUtils = new ModelPaneUtils(modelPane, tapePane, nextInstruction, stopModeling);
    }

    //
    //Constructor Tab
    //

    @FXML
    private Pane constructionPane;
    private ConstructorPaneUtils constructorPaneUtils;

    @FXML
    private ChoiceBox<Integer> stateCountChoiceBox;
    @FXML
    private ChoiceBox<Character> alphabetChoiceBox;
    @FXML
    private ChoiceBox<Integer> stateChoiceBox;
    @FXML
    private ChoiceBox<Movement> movementChoiceBox;

    //Top menu panel

    @FXML
    private void onChangeStateCount() {
        constructorPaneUtils.onChangeStateCount();
    }

    @FXML
    private void saveAlg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранение алгоритма:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл алгоритма", "*.mt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
          constructorPaneUtils.saveAlg(file.getPath());
        }
    }

    @FXML
    private void loadAlg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузка алгоритма:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл алгоритма", "*.mt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
           constructorPaneUtils.loadAlg(file.getPath());
        }
    }

    @FXML
    private void setAlphabet() {
        constructorPaneUtils.setAlphabet();
    }

    //Left menu panel

    @FXML
    private void deleteColumn() {
        constructorPaneUtils.deleteColumn();
    }

    @FXML
    private void insertColumnRight() {
        constructorPaneUtils.insertColumnRight();
    }

    @FXML
    private void insertColumnLeft() {
      constructorPaneUtils.insertColumnLeft();
    }

    @FXML
    private void clearCell() {
       constructorPaneUtils.clearCell();
    }

    //Pane elements controllers

    @FXML
    public void onAddInstruction() {
        constructorPaneUtils.onAddInstruction();
    }

    //
    //Model Tab
    //

    @FXML
    private Pane modelPane;
    @FXML
    private Pane tapePane;

    @FXML
    private ImageView nextInstruction;
    @FXML
    private ImageView stopModeling;

    private ModelPaneUtils modelPaneUtils;

    //Top menu panel

    @FXML
    private void loadAlgToModel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузка алгоритма:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл алгоритма", "*.mt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
           modelPaneUtils.loadAlg(file.getPath());
        }
    }

    //Left menu panel

    @FXML
    private void startModeling() {
        modelPaneUtils.start();
    }

    @FXML
    private void stopModeling() {
        modelPaneUtils.stop();
    }

    @FXML
    private void nextInstruction() {
        modelPaneUtils.next();
    }
}
