package ru.hse.paramFunc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class MainSceneController {

    @FXML
    private Pane spacePane;

    @FXML
    private ChoiceBox<?> animationChoiceBox;

    @FXML
    private Button cameraXButton;

    @FXML
    private Button cameraYButton;

    @FXML
    private Button cameraZButton;

    @FXML
    private Button cameraIsometricButton;

    @FXML
    private TextField searchXTextField;

    @FXML
    private TextField searchYTextField;

    @FXML
    private TextField searchZTextField;

    @FXML
    private TextField searchTTextField;

    @FXML
    private Button searchButton;

    @FXML
    private CheckBox interpolationCheckBox;

    @FXML
    private ColorPicker controlPointsColorPicker;

    @FXML
    private ColorPicker interpolationPointsColorPicker;

    @FXML
    private ColorPicker linesColorPicker;

    public void initialize() {

    }

}
