package controller;

import static drawing.DrawingConstants.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mt.DefaultAlgorithm;
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

        modelPaneUtils = new ModelPaneUtils(modelPane, tapePane, startModeling, nextInstruction, stopModeling,
                operand1ChoiceBox, operand2ChoiceBox);

        baseAlgorithmChoiceBox.getItems().addAll(DefaultAlgorithm.values());
        baseAlgorithmChoiceBox.setOnAction(event -> {
            modelPaneUtils.loadMT(baseAlgorithmChoiceBox.getSelectionModel().getSelectedItem().getMt());
        });
        baseAlgorithmChoiceBox.getSelectionModel().selectFirst();
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
    private ImageView startModeling;
    @FXML
    private ImageView nextInstruction;
    @FXML
    private ImageView stopModeling;

    @FXML
    private ChoiceBox<DefaultAlgorithm> baseAlgorithmChoiceBox;
    @FXML
    private CheckBox isSaveTape;
    @FXML
    private ChoiceBox<Integer> operand1ChoiceBox;
    @FXML
    private ChoiceBox<Integer> operand2ChoiceBox;

    private ModelPaneUtils modelPaneUtils;
    private VisualizationMode visualizationMode = VisualizationMode.STEP_BY_STEP;
    private Integer interval = 1;

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

    @FXML
    private void getVisualizationMode() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/model_settings_form.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Задать режим визуализации");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            ModelSettingsFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMode(visualizationMode);
            controller.setInterval(interval);
            dialogStage.showAndWait();

            if (controller.isModeChosen()) {
                visualizationMode = controller.getMode();
                interval = controller.getInterval();
                switch (visualizationMode) {
                    case STEP_BY_STEP:
                        stopModeling.setVisible(true);
                        nextInstruction.setVisible(true);
                        break;
                    case NO_VISUALIZATION:
                        stopModeling.setVisible(false);
                        nextInstruction.setVisible(false);
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void saveTape() {
        if (isSaveTape.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Сохранение трассы:");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовый файл", "*.txt"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                modelPaneUtils.setTapeFileName(file.getPath());
            } else {
                isSaveTape.setSelected(false);
            }
        } else {
            modelPaneUtils.setTapeFileName(null);
        }
    }

    //Left menu panel

    @FXML
    private void startModeling() {
        switch (visualizationMode) {
            case STEP_BY_STEP:
                modelPaneUtils.start();
                break;
            case TIMER:
                modelPaneUtils.start();

                break;
            case NO_VISUALIZATION:
                modelPaneUtils.start();
                while (modelPaneUtils.next());
                break;
        }

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
