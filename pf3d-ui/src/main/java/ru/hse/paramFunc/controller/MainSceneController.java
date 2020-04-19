package ru.hse.paramFunc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import ru.hse.paramFunc.SceneRunner;
import ru.hse.paramFunc.animation.AnimationStorage;
import ru.hse.paramfunc.SubSceneEngine;
import ru.hse.paramfunc.domain.enums.SceneType;

public class MainSceneController {

    @FXML
    private MenuItem allPointsMenuItem;

    @FXML
    private MenuItem selectPointsMenuItem;

    @FXML
    private Pane spacePane;

    @FXML
    private ChoiceBox<String> animationChoiceBox;

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
        AnimationStorage.getAnimations().forEach(animation -> {
            animationChoiceBox.getItems().add(animation.getName());
        });
        animationChoiceBox.setOnAction(e -> {
            String name = animationChoiceBox.getSelectionModel().getSelectedItem();
            SubSceneEngine.getSpaceSubScene().setCurrentAnimation(name);
        });

        allPointsMenuItem.setOnAction(e -> {
            try {
                SceneRunner.getInstance().run(SceneType.ALL_POINTS);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        selectPointsMenuItem.setOnAction(e -> {
            try {
                SceneRunner.getInstance().run(SceneType.SELECTION);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

}
