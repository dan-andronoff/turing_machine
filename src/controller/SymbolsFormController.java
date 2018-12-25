package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Arrays;

public class SymbolsFormController {

    @FXML
    private ChoiceBox<Integer> alphabetSize;
    @FXML
    private TextField one;
    @FXML
    private TextField eraser;
    @FXML
    private TextField separator;
    @FXML
    private TextField firstAdditionSymbol;
    @FXML
    private TextField secondAdditionSymbol;

    private Stage dialogStage;
    private boolean isAlphabetChosen;

    @FXML
    public void initialize() {
        alphabetSize.getItems().addAll(3, 4, 5);
        alphabetSize.setOnAction(disableTextField);
    }

    public void setAlphabet(Character[] characters) {
        alphabetSize.getSelectionModel().select(Integer.valueOf(characters.length));
        switch (characters.length) {
            case 5:
                secondAdditionSymbol.setText(characters[4].toString());
            case 4:
                firstAdditionSymbol.setText(characters[3].toString());
            case 3:
                eraser.setText(characters[2].toString());
                separator.setText(characters[1].toString());
                one.setText(characters[0].toString());
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isAlphabetChosen() {
        return isAlphabetChosen;
    }

    public Character[] getAlphabet() {
        Character[] alphabet = new Character[alphabetSize.getSelectionModel().getSelectedItem()];
        switch (alphabet.length) {
            case 5:
                alphabet[4] = secondAdditionSymbol.getText().charAt(0);
            case 4:
                alphabet[3] = firstAdditionSymbol.getText().charAt(0);
            case 3:
                alphabet[2] = eraser.getText().charAt(0);
                alphabet[1] = separator.getText().charAt(0);
                alphabet[0] = one.getText().charAt(0);
        }
        return alphabet;
    }

    @FXML
    private void chooseAlphabet() {
        isAlphabetChosen = true;
        dialogStage.close();
    }

    private EventHandler<ActionEvent> disableTextField = e -> {
        switch (alphabetSize.getSelectionModel().getSelectedItem()) {
            case 3:
                firstAdditionSymbol.setDisable(true);
                secondAdditionSymbol.setDisable(true);
                break;
            case 4:
                firstAdditionSymbol.setDisable(false);
                secondAdditionSymbol.setDisable(true);
                break;
            case 5:
                firstAdditionSymbol.setDisable(false);
                secondAdditionSymbol.setDisable(false);
                break;
        }
    };
}
