package ru.hse.paramFunc.controller;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import ru.hse.paramFunc.SceneRunner;
import ru.hse.paramFunc.animation.AnimationStorage;
import ru.hse.paramfunc.SubSceneEngine;
import ru.hse.paramfunc.contract.MouseEventListener;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.domain.enums.SceneType;
import ru.hse.paramfunc.engine.SpaceSubScene;

import java.io.File;

public class MainSceneController implements MouseEventListener {

    @FXML
    private MenuItem loadFileMenuItem;

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

    @FXML
    private Label fpsLabel;

    @FXML
    private Button playButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button stopButton;

    @FXML
    private Label pointInfoLabel;

    @FXML
    private VBox functionsVBox;

    public void initialize() {
        AnimationStorage.getAnimations().forEach(animation -> {
            animationChoiceBox.getItems().add(animation.getName());
        });
        animationChoiceBox.setOnAction(e -> {
            String name = animationChoiceBox.getSelectionModel().getSelectedItem();
            SubSceneEngine.getSpaceSubScene().setCurrentAnimation(name);
        });

        loadFileMenuItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File loadedFile = fileChooser.showOpenDialog(SceneRunner.getInstance().getMainStage());
            if (loadedFile != null) {
                try {
                    SubSceneEngine.loadFunction(loadedFile.getAbsolutePath());
                    resetScene();
                    SubSceneEngine.start(SceneRunner.getInstance().getMainStage().getScene());
                    SubSceneEngine.getSpaceSubScene().addMouseEventListener(this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
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

        fpsLabel.textProperty().bind(SpaceSubScene.getFpsProperty().asString("%.1f"));

        playButton.setOnAction(e -> SubSceneEngine.getSpaceSubScene().startCurrentAnimation());
        pauseButton.setOnAction(e -> SubSceneEngine.getSpaceSubScene().pauseCurrentAnimation());
        stopButton.setOnAction(e -> SubSceneEngine.getSpaceSubScene().stopCurrentAnimation());

        interpolationCheckBox.setOnAction(e -> {
//            SubSceneEngine.getSpaceSubScene().setSplinePointsVisible(interpolationCheckBox.isSelected());
        });

        // Пример создания меню Functions
        Accordion functionsAccordion = new Accordion();
        TitledPane modelTitledPane = new TitledPane();
        modelTitledPane.setText("Model Data");
        modelTitledPane.getStyleClass().add("custom-titled-pane");
        VBox modelVBox = new VBox();
        GridPane modelGridPane = new GridPane();
        RowConstraints r0 = new RowConstraints();
        RowConstraints r1 = new RowConstraints();
        RowConstraints r2 = new RowConstraints();
        r0.setVgrow(Priority.SOMETIMES);
        r1.setVgrow(Priority.SOMETIMES);
        r2.setVgrow(Priority.SOMETIMES);
        ColumnConstraints c0 = new ColumnConstraints();
        ColumnConstraints c1 = new ColumnConstraints();
        c0.setHgrow(Priority.SOMETIMES);
        c1.setHgrow(Priority.SOMETIMES);
        modelGridPane.getRowConstraints().clear();
        modelGridPane.getColumnConstraints().clear();
        modelGridPane.getRowConstraints().addAll(r0, r1, r2);
        modelGridPane.getColumnConstraints().addAll(c0, c1);
        Label label = new Label("Animation");
        label.getStyleClass().add("inspector-label");
        modelGridPane.getChildren().add(label);
        GridPane.setColumnIndex(label, 0);
        GridPane.setHalignment(label, HPos.LEFT);

        modelVBox.getChildren().add(modelGridPane);
        modelTitledPane.setContent(modelVBox);
        functionsAccordion.getPanes().add(modelTitledPane);
        functionsVBox.getChildren().add(functionsAccordion);
    }

    private void resetScene() {
        animationChoiceBox.setValue(null);
    }

    @Override
    public void receive(MouseEvent event, FunctionPoint target) {
        if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            pointInfoLabel.setText(String.format("T: %d\nX: %.3f\nY: %.3f\nZ: %.3f",
                    target.getT(),
                    target.getOriginalX(),
                    target.getOriginalY(),
                    target.getOriginalZ()));
            pointInfoLabel.setVisible(true);
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            pointInfoLabel.setText("");
            pointInfoLabel.setVisible(false);
        }
    }
}
