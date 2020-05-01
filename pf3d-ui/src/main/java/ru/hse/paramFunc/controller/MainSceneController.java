package ru.hse.paramFunc.controller;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import ru.hse.paramFunc.FxApplication;
import ru.hse.paramFunc.SceneRunner;
import ru.hse.paramFunc.animation.Animation;
import ru.hse.paramFunc.animation.AnimationStorage;
import ru.hse.paramfunc.SubSceneEngine;
import ru.hse.paramfunc.contract.MouseEventListener;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.domain.enums.SceneType;
import ru.hse.paramfunc.element.FunctionHolder;
import ru.hse.paramfunc.engine.SpaceSubScene;
import ru.hse.paramfunc.listener.Listener;
import ru.hse.paramfunc.storage.FunctionStorage;

import java.io.File;
import java.util.List;

public class MainSceneController implements MouseEventListener, Listener {

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
        FunctionStorage.getInstance().addListener(this);

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

    @Override
    public void receive() {
        //Обновление меню Functions
        functionsVBox.getChildren().clear();
        List<Function> functions = FunctionStorage.getInstance().getFunctions();
        Accordion functionsAccordion = new Accordion();
        for (Function function : functions) {
            functionsAccordion.getPanes().add(createTitledPaneForFunction(function));
        }
        functionsVBox.getChildren().add(functionsAccordion);

    }

    private TitledPane createTitledPaneForFunction(Function function) {
        FunctionHolder functionHolder = SubSceneEngine.getSpaceSubScene().getFunctionHolderByFunction(function);
        List<Animation> functionAnimations = AnimationStorage.getCopyAnimations();

        TitledPane functionTitledPane = new TitledPane();
        functionTitledPane.setText(function.getName());
        functionTitledPane.getStyleClass().add("custom-titled-pane");
        functionTitledPane.expandedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                functionHolder.focusProperty().set(true);
            } else {
                functionHolder.focusProperty().set(false);
            }
        });
        VBox vBox = new VBox();

        //Создание Grid-контейнера
        GridPane gridPane = new GridPane();
        RowConstraints r0 = new RowConstraints();
        RowConstraints r1 = new RowConstraints();
        RowConstraints r2 = new RowConstraints();
        RowConstraints r3 = new RowConstraints();
        ColumnConstraints c0 = new ColumnConstraints();
        c0.setMinWidth(10);
        c0.setPrefWidth(80);
        c0.maxWidthProperty().bind(c0.prefWidthProperty());
        ColumnConstraints c1 = new ColumnConstraints();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().addAll(r0, r1, r2, r3);
        gridPane.getColumnConstraints().addAll(c0, c1);

        //Animation label
        Label animationLabel = new Label("Animation");
        animationLabel.getStyleClass().add("inspector-label");
        gridPane.getChildren().add(animationLabel);
        GridPane.setRowIndex(animationLabel, 0);
        GridPane.setColumnIndex(animationLabel, 0);
        GridPane.setHalignment(animationLabel, HPos.LEFT);

        //Animation choiceBox
        ChoiceBox<Animation> animationChoiceBox = new ChoiceBox<>();
        animationChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Animation animation) {
                return animation.getName();
            }

            @Override
            public Animation fromString(String s) {
                return functionAnimations.stream()
                        .filter(a -> a.getName().equals(s))
                        .findFirst()
                        .orElseThrow();
            }
        });
        functionAnimations.forEach(a -> animationChoiceBox.getItems().add(a));
        functionHolder.animationProperty().bind(animationChoiceBox.valueProperty());
        animationChoiceBox.getStyleClass().add("inspector-button");
        animationChoiceBox.setPrefWidth(150);
        animationChoiceBox.maxWidth(Integer.MAX_VALUE);
        gridPane.getChildren().add(animationChoiceBox);
        GridPane.setMargin(animationChoiceBox, new Insets(0, 10, 0, 0));
        GridPane.setRowIndex(animationChoiceBox, 0);
        GridPane.setColumnIndex(animationChoiceBox, 1);
        GridPane.setHalignment(animationChoiceBox, HPos.LEFT);

        //Animation buttons gridPane
        GridPane animationButtonsPane = new GridPane();
        RowConstraints abr0 = new RowConstraints();
        abr0.setMinHeight(10);
        abr0.setPrefHeight(40);
        ColumnConstraints abc0 = new ColumnConstraints();
        abc0.setMinWidth(10);
        abc0.setPrefWidth(100);
        ColumnConstraints abc1 = new ColumnConstraints();
        abc1.setMinWidth(10);
        abc1.setPrefWidth(50);
        ColumnConstraints abc2 = new ColumnConstraints();
        abc2.setMinWidth(10);
        abc2.setPrefWidth(100);
        animationButtonsPane.getRowConstraints().clear();
        animationButtonsPane.getColumnConstraints().clear();
        animationButtonsPane.getRowConstraints().addAll(abr0);
        animationButtonsPane.getColumnConstraints().addAll(abc0, abc1, abc2);
        GridPane.setRowIndex(animationButtonsPane, 1);
        GridPane.setColumnSpan(animationButtonsPane, 2);
        gridPane.getChildren().add(animationButtonsPane);

        // Play animation button
        Button playButton = new Button();
        playButton.setOnAction(e -> functionHolder.startAnimationCallback().call());
        ImageView playImageView = new ImageView(FxApplication.class.getResource("play.png").toString());
        playImageView.setViewport(new Rectangle2D(20.0, 20.0, 25.0, 25.0));
        playImageView.setFitHeight(10);
        playImageView.setFitWidth(10);
        playImageView.setPickOnBounds(true);
        playImageView.setPreserveRatio(true);
        playButton.setGraphic(playImageView);
        playButton.getStyleClass().add("inspector-button");
        playButton.setMinHeight(20);
        playButton.setMinWidth(20);
        playButton.setPrefHeight(20);
        playButton.setPrefWidth(20);
        GridPane.setHalignment(playButton, HPos.RIGHT);
        GridPane.setValignment(playButton, VPos.CENTER);

        // Pause animation button
        Button pauseButton = new Button();
        pauseButton.setOnAction(e -> functionHolder.pauseAnimationCallback().call());
        ImageView pauseImageView = new ImageView(FxApplication.class.getResource("pause.png").toString());
        pauseImageView.setViewport(new Rectangle2D(20.0, 20.0, 25.0, 25.0));
        pauseImageView.setFitHeight(10);
        pauseImageView.setFitWidth(10);
        pauseButton.setGraphic(pauseImageView);
        pauseButton.getStyleClass().add("inspector-button");
        pauseButton.setMinHeight(20);
        pauseButton.setMinWidth(20);
        pauseButton.setPrefHeight(20);
        pauseButton.setPrefWidth(20);
        GridPane.setColumnIndex(pauseButton, 1);
        GridPane.setHalignment(pauseButton, HPos.CENTER);
        GridPane.setValignment(pauseButton, VPos.CENTER);

        //Stop animation button
        Button stopButton = new Button();
        stopButton.setOnAction(e -> functionHolder.stopAnimationCallback().call());
        ImageView stopImageView = new ImageView(FxApplication.class.getResource("stop.png").toString());
        stopImageView.setViewport(new Rectangle2D(20.0, 20.0, 25.0, 25.0));
        stopImageView.setFitHeight(10);
        stopImageView.setFitWidth(10);
        stopButton.setGraphic(stopImageView);
        stopButton.getStyleClass().add("inspector-button");
        stopButton.setMinHeight(20);
        stopButton.setMinWidth(20);
        stopButton.setPrefHeight(20);
        stopButton.setPrefWidth(20);
        GridPane.setColumnIndex(stopButton, 2);
        GridPane.setHalignment(stopButton, HPos.LEFT);
        GridPane.setValignment(stopButton, VPos.CENTER);
        animationButtonsPane.getChildren().addAll(playButton, pauseButton, stopButton);

        //Interpolation label
        Label interpolationLabel = new Label("Interpolation");
        interpolationLabel.getStyleClass().add("inspector-label");
        GridPane.setRowIndex(interpolationLabel, 2);
        GridPane.setColumnIndex(interpolationLabel, 0);
        GridPane.setHalignment(interpolationLabel, HPos.LEFT);
        gridPane.getChildren().add(interpolationLabel);

        //Interpolation checkBox
        CheckBox interpolationCheckBox = new CheckBox();
        interpolationCheckBox.getStyleClass().add("inspector-checkbox");
        functionHolder.interpolationShownProperty().bind(interpolationCheckBox.selectedProperty());
        GridPane.setRowIndex(interpolationCheckBox, 2);
        GridPane.setColumnIndex(interpolationCheckBox, 1);
        gridPane.getChildren().add(interpolationCheckBox);

        //Select points button
        Button selectPointsButton = new Button("Select points");
        selectPointsButton.getStyleClass().add("inspector-button");
        GridPane.setRowIndex(selectPointsButton, 3);
        GridPane.setColumnIndex(selectPointsButton, 0);
        GridPane.setColumnSpan(selectPointsButton, 2);
        GridPane.setHalignment(selectPointsButton, HPos.CENTER);
        GridPane.setValignment(selectPointsButton, VPos.CENTER);
        GridPane.setMargin(selectPointsButton, new Insets(10, 0, 0, 0));
        gridPane.getChildren().add(selectPointsButton);

        vBox.getChildren().add(gridPane);
        functionTitledPane.setContent(vBox);
        return functionTitledPane;
    }
}
