package ru.hse.paramFunc.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.hse.paramFunc.FxApplication;
import ru.hse.paramfunc.settings.AppSettings;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class SettingsController {

    @FXML private TextField cameraMovementSpeedTextField;
    @FXML private TextField functionPointsRadiusTextField;
    @FXML private TextField interpolationPointsRadiusTextField;
    @FXML private TextField animationPointsRadiusTextField;
    @FXML private ColorPicker functionPointsColorPicker;
    @FXML private ColorPicker interpolationPointsColorPicker;
    @FXML private ColorPicker animationPointsColorPicker;
    @FXML private ColorPicker functionHighlightingColorPicker;
    @FXML private TextField interpolationPointsCountTextField;
    @FXML private TextField animationSpeedTextField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private static final Pattern DOUBLE_NUMBER_PATTERN = Pattern.compile("([1-9]\\d*.\\d+)|(0.\\d+)|([1-9]\\d*)");
    private static final Pattern INTEGER_NUMBER_PATTERN = Pattern.compile("[1-9]\\d*");

    private Stage stage;

    public SettingsController(Stage ownerStage) {
        this.stage = new Stage();
        this.stage.initModality(Modality.WINDOW_MODAL);
        this.stage.initOwner(ownerStage);
        try {
            FXMLLoader loader = new FXMLLoader(FxApplication.class.getResource("settings.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 400);
            scene.getStylesheets().add(FxApplication.getStylesheetPath() + "main.css");
            this.stage.setScene(scene);
            this.stage.setTitle("App settings");
            this.stage.getIcons().add(new Image(FxApplication.class.getResourceAsStream("logo.png")));
            this.stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        this.stage.show();
    }

    public void initialize() {
        cameraMovementSpeedTextField.setText(String.valueOf(AppSettings.cameraSpeedProperty().get()));
        functionPointsRadiusTextField.setText(String.valueOf(AppSettings.functionPointsRadiusProperty().get()));
        interpolationPointsRadiusTextField.setText(String.valueOf(AppSettings.interpolationPointsRadiusProperty().get()));
        animationPointsRadiusTextField.setText(String.valueOf(AppSettings.animationPointsRadiusProperty().get()));
        functionPointsColorPicker.setValue(AppSettings.functionPointsColorProperty().get());
        interpolationPointsColorPicker.setValue(AppSettings.interpolationPointsColorProperty().get());
        animationPointsColorPicker.setValue(AppSettings.animationPointsColorProperty().get());
        functionHighlightingColorPicker.setValue(AppSettings.functionHighlightingColorProperty().get());
        interpolationPointsCountTextField.setText(String.valueOf(AppSettings.interpolationPointsCountProperty().get()));
        animationSpeedTextField.setText(String.valueOf(AppSettings.animationSpeedProperty().get()));

        // Validation
        cameraMovementSpeedTextField.textProperty().addListener(new ValidationListener(cameraMovementSpeedTextField));
        functionPointsRadiusTextField.textProperty().addListener(new ValidationListener(functionPointsRadiusTextField));
        interpolationPointsRadiusTextField.textProperty().addListener(new ValidationListener(interpolationPointsRadiusTextField));
        animationPointsRadiusTextField.textProperty().addListener(new ValidationListener(animationPointsRadiusTextField));
        interpolationPointsCountTextField.textProperty().addListener(new ValidationListener(interpolationPointsCountTextField));
        animationSpeedTextField.textProperty().addListener(new ValidationListener(animationSpeedTextField));

        saveButton.setOnAction(e -> {
            AppSettings.cameraSpeedProperty().set(Double.parseDouble(cameraMovementSpeedTextField.getText()));
            AppSettings.functionPointsRadiusProperty().set(Double.parseDouble(functionPointsRadiusTextField.getText()));
            AppSettings.interpolationPointsRadiusProperty().set(Double.parseDouble(interpolationPointsRadiusTextField.getText()));
            AppSettings.animationPointsRadiusProperty().set(Double.parseDouble(animationPointsRadiusTextField.getText()));
            AppSettings.functionPointsColorProperty().set(functionPointsColorPicker.getValue());
            AppSettings.interpolationPointsColorProperty().set(interpolationPointsColorPicker.getValue());
            AppSettings.animationPointsColorProperty().set(animationPointsColorPicker.getValue());
            AppSettings.functionHighlightingColorProperty().set(functionHighlightingColorPicker.getValue());
            AppSettings.interpolationPointsCountProperty().set(Integer.parseInt(interpolationPointsCountTextField.getText()));
            AppSettings.animationSpeedProperty().set(Double.parseDouble(animationSpeedTextField.getText()));
            this.stage.close();
        });
        cancelButton.setOnAction(e -> this.stage.close());
    }

    private class ValidationListener implements ChangeListener<String> {

        private TextField textField;

        public ValidationListener(TextField textField) {
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            Pattern textFieldPattern = textField.equals(interpolationPointsCountTextField)
                    ? INTEGER_NUMBER_PATTERN
                    : DOUBLE_NUMBER_PATTERN;

            if(textFieldPattern.matcher(textField.getText()).matches()) {
                textField.getStyleClass().remove("invalid-text-field");
            } else {
                boolean isExists = textField.getStyleClass().stream()
                        .anyMatch(c -> c.equals("invalid-text-field"));
                if(!isExists) {
                    textField.getStyleClass().add("invalid-text-field");
                }
            }

            List<TextField> textFields = List.of(
                    cameraMovementSpeedTextField,
                    functionPointsRadiusTextField,
                    interpolationPointsRadiusTextField,
                    animationPointsRadiusTextField,
                    interpolationPointsCountTextField,
                    animationSpeedTextField);

            Boolean isAllValid = textFields.stream()
                    .map(tf -> tf.equals(interpolationPointsCountTextField)
                            ? INTEGER_NUMBER_PATTERN.matcher(tf.getText()).matches()
                            : DOUBLE_NUMBER_PATTERN.matcher(tf.getText()).matches())
                    .reduce(true, (a, b) -> a && b);
            if (isAllValid) {
                saveButton.setDisable(false);
            } else {
                saveButton.setDisable(true);
            }
        }
    }

}
