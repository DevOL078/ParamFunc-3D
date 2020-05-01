package ru.hse.paramFunc.controller;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.hse.paramFunc.FxApplication;
import ru.hse.paramfunc.SubSceneEngine;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.element.FunctionHolder;
import ru.hse.paramfunc.storage.FunctionStorage;

import java.io.IOException;

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
    @FXML private TextField animationSpeedTextField;

    private Stage stage;
    private FunctionHolder functionHolder;

    private ObjectProperty<Color> valuesColorProperty;
    private StringProperty valuesRadiusProperty;
    private ObjectProperty<Color> interpolationColorProperty;
    private StringProperty interpolationRadiusProperty;
    private StringProperty interpolationPointsNumberProperty;

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

        valuesColorPicker.valueProperty().bindBidirectional(valuesColorProperty);
        valuesRadiusTextField.textProperty().bindBidirectional(valuesRadiusProperty);
        interpolationColorPicker.valueProperty().bindBidirectional(interpolationColorProperty);
        interpolationRadiusTextField.textProperty().bindBidirectional(interpolationRadiusProperty);
        interpolationPointsNumberTextField.textProperty().bindBidirectional(interpolationPointsNumberProperty);

        saveButton.setOnAction(e -> {
            this.functionHolder.valuesColorProperty().set(this.valuesColorProperty.get());
            this.functionHolder.valuesRadiusProperty().set(Double.parseDouble(this.valuesRadiusProperty.get()));
            this.functionHolder.interpolationColorProperty().set(this.interpolationColorProperty.get());
            this.functionHolder.interpolationRadiusProperty().set(Double.parseDouble(this.interpolationRadiusProperty.get()));
            this.functionHolder.interpolationPointsNumberProperty().set(
                    Integer.parseInt(this.interpolationPointsNumberProperty.get()));
            this.stage.close();
        });
        cancelButton.setOnAction(e -> {
            this.stage.close();
        });
    }

}
