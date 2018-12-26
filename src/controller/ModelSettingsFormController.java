package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import static controller.VisualizationMode.STEP_BY_STEP;
import static controller.VisualizationMode.TIMER;

public class ModelSettingsFormController {

    @FXML
    private ChoiceBox<VisualizationMode> mode;
    @FXML
    private Label intervalLabel;
    @FXML
    private ChoiceBox<Integer> intervalChoiceBox;

    private Stage dialogStage;
    private boolean isModeChosen;

    @FXML
    public void initialize() {
        mode.setOnAction(event -> {
            boolean isTimerMode = mode.getSelectionModel().getSelectedItem()==TIMER;
            intervalLabel.setVisible(isTimerMode);
            intervalChoiceBox.setVisible(isTimerMode);
        });
        mode.getItems().addAll(VisualizationMode.values());

        intervalChoiceBox.getItems().addAll(1,2,3,4,5);
    }

    public void setMode(VisualizationMode visualizationMode) {
        mode.getSelectionModel().select(visualizationMode);
    }

    public void setInterval(Integer interval) {
        intervalChoiceBox.getSelectionModel().select(interval);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isModeChosen() {
        return isModeChosen;
    }

    public VisualizationMode getMode() {
       return mode.getSelectionModel().getSelectedItem();
    }

    public Integer getInterval() {
        return intervalChoiceBox.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void chooseMode() {
        isModeChosen = true;
        dialogStage.close();
    }
}
