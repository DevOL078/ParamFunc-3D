package ru.hse.paramFunc.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.hse.paramFunc.FxApplication;
import ru.hse.paramfunc.SubSceneEngine;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionHolder;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class FunctionSettingsController {

    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private TextField valuesRadiusTextField;
    @FXML private ColorPicker valuesColorPicker;
    @FXML private ColorPicker interpolationColorPicker;
    @FXML private TextField interpolationRadiusTextField;
    @FXML private TextField interpolationPointsNumberTextField;
    @FXML private ColorPicker animationColorPicker;
    @FXML private TextField animationRadiusTextField;
    @FXML private TextField animationTimeTextField;

    private static final Pattern DOUBLE_NUMBER_PATTERN = Pattern.compile("([1-9]\\d*.\\d+)|(0.\\d+)|([1-9]\\d*)");
    private static final Pattern INTEGER_NUMBER_PATTERN = Pattern.compile("[1-9]\\d*");

    private Stage stage;
    private FunctionHolder functionHolder;

    private ObjectProperty<Color> valuesColorProperty;
    private StringProperty valuesRadiusProperty;
    private ObjectProperty<Color> interpolationColorProperty;
    private StringProperty interpolationRadiusProperty;
    private StringProperty interpolationPointsNumberProperty;
    private ObjectProperty<Color> animationColorProperty;
    private StringProperty animationRadiusProperty;
    private StringProperty animationTimeProperty;

    public FunctionSettingsController(Stage ownerStage, Function function) {
        this.functionHolder = SubSceneEngine.getSpaceSubScene().getFunctionHolderByFunction(function);
        this.stage = new Stage();
        this.stage.initModality(Modality.WINDOW_MODAL);
        this.stage.initOwner(ownerStage);
        try {
            FXMLLoader loader = new FXMLLoader(FxApplication.class.getResource("function-settings.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 400);
            scene.getStylesheets().add(FxApplication.getStylesheetPath() + "main.css");
            this.stage.setScene(scene);
            this.stage.setTitle(function.getName() + ": Settings");
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
        valuesColorProperty = new SimpleObjectProperty<>(
                this.functionHolder.valuesColorProperty().get());
        valuesRadiusProperty = new SimpleStringProperty(
                String.valueOf(this.functionHolder.valuesRadiusProperty().get()));
        interpolationColorProperty = new SimpleObjectProperty<>(
                this.functionHolder.interpolationColorProperty().get());
        interpolationRadiusProperty = new SimpleStringProperty(
                String.valueOf(this.functionHolder.interpolationRadiusProperty().get()));
        interpolationPointsNumberProperty = new SimpleStringProperty(
                String.valueOf(this.functionHolder.interpolationPointsNumberProperty().get()));
        animationColorProperty = new SimpleObjectProperty<>(
                this.functionHolder.animationColorProperty().get());
        animationRadiusProperty = new SimpleStringProperty(
                String.valueOf(this.functionHolder.animationRadiusProperty().get()));
        animationTimeProperty = new SimpleStringProperty(
                String.valueOf(this.functionHolder.animationTimeProperty().get().toMillis()));

        valuesColorPicker.valueProperty().bindBidirectional(valuesColorProperty);
        valuesRadiusTextField.textProperty().bindBidirectional(valuesRadiusProperty);
        interpolationColorPicker.valueProperty().bindBidirectional(interpolationColorProperty);
        interpolationRadiusTextField.textProperty().bindBidirectional(interpolationRadiusProperty);
        interpolationPointsNumberTextField.textProperty().bindBidirectional(interpolationPointsNumberProperty);
        animationColorPicker.valueProperty().bindBidirectional(animationColorProperty);
        animationRadiusTextField.textProperty().bindBidirectional(animationRadiusProperty);
        animationTimeTextField.textProperty().bindBidirectional(animationTimeProperty);

        valuesRadiusTextField.textProperty().addListener(
                new ValidationListener(valuesRadiusTextField));
        interpolationRadiusTextField.textProperty().addListener(
                new ValidationListener(interpolationRadiusTextField));
        interpolationPointsNumberTextField.textProperty().addListener(
                new ValidationListener(interpolationPointsNumberTextField));
        animationRadiusTextField.textProperty().addListener(
                new ValidationListener(animationRadiusTextField));
        animationTimeTextField.textProperty().addListener(
                new ValidationListener(animationTimeTextField));

        saveButton.setOnAction(e -> {
            this.functionHolder.valuesColorProperty().set(this.valuesColorProperty.get());
            this.functionHolder.valuesRadiusProperty().set(
                    Double.parseDouble(this.valuesRadiusProperty.get()));
            this.functionHolder.interpolationColorProperty().set(this.interpolationColorProperty.get());
            this.functionHolder.interpolationRadiusProperty().set(
                    Double.parseDouble(this.interpolationRadiusProperty.get()));
            this.functionHolder.interpolationPointsNumberProperty().set(
                    Integer.parseInt(this.interpolationPointsNumberProperty.get()));
            this.functionHolder.animationColorProperty().set(this.animationColorProperty.get());
            this.functionHolder.animationRadiusProperty().set(
                    Double.parseDouble(this.animationRadiusProperty.get()));
            this.functionHolder.animationTimeProperty().set(
                    Duration.millis(Double.parseDouble(this.animationTimeProperty.get())));
            this.stage.close();
        });
        cancelButton.setOnAction(e -> {
            this.stage.close();
        });
    }

    private class ValidationListener implements ChangeListener<String> {

        private TextField textField;

        public ValidationListener(TextField textField) {
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            Pattern textFieldPattern = textField.equals(interpolationPointsNumberTextField)
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
                    valuesRadiusTextField,
                    interpolationRadiusTextField,
                    interpolationPointsNumberTextField,
                    animationRadiusTextField,
                    animationTimeTextField);

            Boolean isAllValid = textFields.stream()
                    .map(tf -> tf.equals(interpolationPointsNumberTextField)
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
