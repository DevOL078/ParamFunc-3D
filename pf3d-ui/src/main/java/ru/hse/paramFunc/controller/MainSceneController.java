package ru.hse.paramFunc.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.hse.paramFunc.FxApplication;
import ru.hse.paramFunc.SceneRunner;
import ru.hse.paramFunc.animation.AnimationStorage;
import ru.hse.paramfunc.SubSceneEngine;
import ru.hse.paramfunc.domain.Animation;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionHolder;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.engine.CameraController;
import ru.hse.paramfunc.engine.SpaceSubScene;
import ru.hse.paramfunc.event.EventListener;
import ru.hse.paramfunc.event.EventMediator;
import ru.hse.paramfunc.event.EventType;
import ru.hse.paramfunc.parser.FunctionValues3DParser;
import ru.hse.paramfunc.storage.FunctionStorage;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainSceneController implements EventListener {

    @FXML private MenuItem loadFileMenuItem;
    @FXML private MenuItem exitMenuItem;
    @FXML private Menu functionsMenu;
    @FXML private Menu settingsMenu;
    @FXML private Pane spacePane;
    @FXML private TitledPane functionsTitledPane;
    @FXML private TitledPane cameraTitledPane;
    @FXML private TitledPane navigationTitledPane;
    @FXML private Button cameraXButton;
    @FXML private Button cameraYButton;
    @FXML private Button cameraZButton;
    @FXML private Button cameraIsometricButton;
    @FXML private TextField searchXTextField;
    @FXML private TextField searchYTextField;
    @FXML private TextField searchZTextField;
    @FXML private TextField searchTTextField;
    @FXML private Button searchButton;
    @FXML private Label fpsLabel;
    @FXML private Label pointInfoLabel;
    @FXML private VBox functionsVBox;
    @FXML private ProgressIndicator progressIndicator;

    private Stage stage;
    private String functionName;

    private static final int MAX_FUNCTION_VALUES = 1000;
    private static final Pattern DOUBLE_NUMBER_PATTERN = Pattern.compile("([1-9]\\d*.\\d+)|(0.\\d+)|([1-9]\\d*)|0|");
    private static final Pattern INTEGER_NUMBER_PATTERN = Pattern.compile("([1-9]\\d*)|0|");

    public MainSceneController(Stage primaryStage) {
        this.stage = primaryStage;
        try {
            FXMLLoader loader = new FXMLLoader(FxApplication.class.getResource("main.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 1024, 700, true);
            scene.getStylesheets().add(FxApplication.getStylesheetPath() + "main.css");
            this.stage.setScene(scene);
            this.stage.setTitle("Parametrically defined Functions Visualizer 3D");
            this.stage.getIcons().add(new Image(FxApplication.class.getResourceAsStream("logo.png")));
            SubSceneEngine.start(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        this.stage.show();
    }

    public void initialize() {
        EventMediator.addListener(EventType.FUNCTION_LIST_UPDATE, this);
        EventMediator.addListener(EventType.MOUSE_ENTERED, this);
        EventMediator.addListener(EventType.MOUSE_EXITED, this);

        loadFileMenuItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File loadedFile = fileChooser.showOpenDialog(SceneRunner.getInstance().getMainStage());
            if (loadedFile != null) {
                showDialogWindow();
                if (this.functionName != null) {
                    progressIndicator.setVisible(true);
                    try {
                        Task<Void> task = new Task<>() {
                            @Override
                            protected Void call() throws Exception {
                                List<FunctionPoint> allPoints = FunctionValues3DParser.getInstance().parse(loadedFile.getAbsolutePath());
                                Function function = new Function(functionName);
                                function.setAllPoints(allPoints);
                                function.setSelectedPoints(allPoints);

                                Platform.runLater(() -> {
                                    AtomicBoolean isCancel = new AtomicBoolean(false);
                                    while (function.getSelectedPoints().size() > MAX_FUNCTION_VALUES && !isCancel.get()) {
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setTitle("Warning: To many function values");
                                        alert.setHeaderText("The loaded function has too many values: " + function.getSelectedPoints().size());
                                        alert.setContentText("You should select a subset of function values for loading.\n" +
                                                "Maximal count of values: " + MAX_FUNCTION_VALUES);
                                        alert.initModality(Modality.WINDOW_MODAL);
                                        alert.initOwner(stage);
                                        alert.getButtonTypes().add(ButtonType.CANCEL);
                                        alert.setResultConverter(buttonType -> {
                                            if (buttonType == ButtonType.CANCEL) {
                                                isCancel.set(true);
                                            }
                                            return buttonType;
                                        });
                                        alert.showAndWait();

                                        if (!isCancel.get()) {
                                            SceneRunner.getInstance().runSelectionScene(stage, function);
                                        }
                                    }

                                    if (!isCancel.get()) {
                                        function.setAllPoints(function.getSelectedPoints());
                                        FunctionStorage.getInstance().addFunction(function);
                                    }
                                    progressIndicator.setVisible(false);
                                });
                                functionName = null;
                                return null;
                            }
                        };
                        Thread thread = new Thread(task);
                        thread.setDaemon(true);
                        thread.start();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
        exitMenuItem.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit alert");
            alert.setHeaderText("Do you want to exit?");
            alert.setContentText("All loaded data will be cleared.");
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(stage);
            alert.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    this.stage.close();
                }
                return buttonType;
            });
            alert.showAndWait();
        });
        Label functionsLabel = new Label("Functions");
        functionsLabel.setOnMouseClicked(e -> SceneRunner.getInstance().runFunctionsScene());
        functionsMenu.setGraphic(functionsLabel);

        Label settingsLabel = new Label("Settings");
        settingsLabel.setOnMouseClicked(e -> SceneRunner.getInstance().runAppSettingsScene());
        settingsMenu.setGraphic(settingsLabel);

        searchButton.setOnAction(e -> {
            String tSearch = searchTTextField.getText();
            String xSearch = searchXTextField.getText();
            String ySearch = searchYTextField.getText();
            String zSearch = searchZTextField.getText();
            SubSceneEngine.getSpaceSubScene().findPoints(
                    tSearch.isEmpty() ? null : Integer.parseInt(tSearch),
                    xSearch.isEmpty() ? null : Double.parseDouble(xSearch),
                    ySearch.isEmpty() ? null : Double.parseDouble(ySearch),
                    zSearch.isEmpty() ? null : Double.parseDouble(zSearch));
            searchTTextField.setText("");
            searchXTextField.setText("");
            searchYTextField.setText("");
            searchZTextField.setText("");
        });
        searchButton.setDisable(true);

        searchTTextField.textProperty().addListener(new ValidationListener(searchTTextField));
        searchYTextField.textProperty().addListener(new ValidationListener(searchYTextField));
        searchZTextField.textProperty().addListener(new ValidationListener(searchZTextField));
        searchXTextField.textProperty().addListener(new ValidationListener(searchXTextField));

        cameraXButton.setOnAction(e -> CameraController.setTo2DXPosition());
        cameraYButton.setOnAction(e -> CameraController.setTo2DYPosition());
        cameraZButton.setOnAction(e -> CameraController.setTo2DZPosition());
        cameraIsometricButton.setOnAction(e -> CameraController.setToIsometricPosition());

        fpsLabel.textProperty().bind(SpaceSubScene.getFpsProperty().asString("%.1f"));
    }

    private void showDialogWindow() {
        Stage dialogStage = new Stage();

        GridPane root = new GridPane();
        root.setStyle("-fx-background-color: #57575C;");
        RowConstraints r0 = new RowConstraints();
        RowConstraints r1 = new RowConstraints();
        r0.setPercentHeight(50);
        r1.setPercentHeight(50);
        ColumnConstraints c0 = new ColumnConstraints();
        ColumnConstraints c1 = new ColumnConstraints();
        c0.setPercentWidth(30);
        c1.setPercentWidth(70);
        root.getRowConstraints().clear();
        root.getColumnConstraints().clear();
        root.getRowConstraints().addAll(r0, r1);
        root.getColumnConstraints().addAll(c0, c1);

        Label label = new Label("Function name");
        label.getStyleClass().add("inspector-label");
        GridPane.setValignment(label, VPos.CENTER);
        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setRowIndex(label, 0);
        GridPane.setColumnIndex(label, 0);

        TextField textField = new TextField();
        GridPane.setMargin(textField, new Insets(10));
        GridPane.setValignment(textField, VPos.CENTER);
        GridPane.setHalignment(textField, HPos.LEFT);
        GridPane.setRowIndex(textField, 0);
        GridPane.setColumnIndex(textField, 1);

        GridPane buttonGridPane = new GridPane();
        RowConstraints r0B = new RowConstraints();
        ColumnConstraints c0B = new ColumnConstraints();
        ColumnConstraints c1B = new ColumnConstraints();
        c0B.setPercentWidth(50);
        c1B.setPercentWidth(50);
        buttonGridPane.getRowConstraints().clear();
        buttonGridPane.getColumnConstraints().clear();
        buttonGridPane.getRowConstraints().addAll(r0B);
        buttonGridPane.getColumnConstraints().addAll(c0B, c1B);
        GridPane.setRowIndex(buttonGridPane, 1);
        GridPane.setColumnSpan(buttonGridPane, 2);

        Button loadButton = new Button("Load");
        loadButton.setFont(Font.font(15));
        loadButton.setPrefHeight(30);
        loadButton.setPrefWidth(67);
        loadButton.getStyleClass().add("inspector-button");
        loadButton.setOnAction(e -> {
            this.functionName = textField.getText();
            dialogStage.close();
        });
        GridPane.setValignment(loadButton, VPos.CENTER);
        GridPane.setHalignment(loadButton, HPos.RIGHT);
        GridPane.setColumnIndex(loadButton, 0);
        GridPane.setMargin(loadButton, new Insets(0, 10, 0, 0));

        Button cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.font(15));
        cancelButton.setPrefHeight(30);
        cancelButton.setPrefWidth(67);
        cancelButton.getStyleClass().add("inspector-button");
        cancelButton.setOnAction(e -> dialogStage.close());
        GridPane.setValignment(cancelButton, VPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.LEFT);
        GridPane.setColumnIndex(cancelButton, 1);
        GridPane.setMargin(cancelButton, new Insets(0, 0, 0, 10));

        buttonGridPane.getChildren().addAll(loadButton, cancelButton);
        root.getChildren().addAll(label, textField, buttonGridPane);

        // Validation
        loadButton.setDisable(true);
        textField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null && !t1.isBlank()) {
                loadButton.setDisable(false);
            } else {
                loadButton.setDisable(true);
            }
        });

        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(this.stage);
        Scene scene = new Scene(root, 300, 130);
        scene.getStylesheets().add(FxApplication.getStylesheetPath() + "main.css");
        dialogStage.setTitle("Input function name");
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);
        dialogStage.getIcons().add(new Image(FxApplication.class.getResourceAsStream("logo.png")));
        dialogStage.showAndWait();
    }

    @Override
    public void receive(EventType eventType, Object... args) {
        if (eventType == EventType.FUNCTION_LIST_UPDATE) {
            updateMenu();
        } else if (eventType == EventType.MOUSE_ENTERED) {
            showInfo((FunctionPoint) args[0], (FunctionHolder) args[1]);
        } else if (eventType == EventType.MOUSE_EXITED) {
            clearInfo();
        }
    }

    private void showInfo(FunctionPoint target, FunctionHolder functionHolder) {
        String functionName = functionHolder.getFunction().getName();
        pointInfoLabel.setText(String.format("T: %d\nX: %.3f\nY: %.3f\nZ: %.3f\nFunction: %s",
                target.getT(),
                target.getOriginalX(),
                target.getOriginalY(),
                target.getOriginalZ(),
                functionName));
        pointInfoLabel.setVisible(true);
    }

    private void clearInfo() {
        pointInfoLabel.setText("");
        pointInfoLabel.setVisible(false);
    }

    private void updateMenu() {
        List<Function> functions = FunctionStorage.getInstance().getFunctions();
        Accordion functionsAccordion;
        if (this.functionsVBox.getChildren().isEmpty()) {
            functionsAccordion = new Accordion();
            this.functionsVBox.getChildren().add(functionsAccordion);
        } else {
            functionsAccordion = (Accordion) this.functionsVBox.getChildren().get(0);
        }
        for (Function function : functions) {
            Optional<TitledPane> paneOpt = functionsAccordion.getPanes().stream()
                    .filter(p -> p.getText().equals(function.getName()))
                    .findFirst();
            if (paneOpt.isEmpty()) {
                functionsAccordion.getPanes().add(createTitledPaneForFunction(function));
            }
        }
        //Удаляем старые TitledPane
        List<String> functionNames = functions.stream()
                .map(Function::getName)
                .collect(Collectors.toList());
        List<TitledPane> deletedPanes = functionsAccordion.getPanes().stream()
                .filter(p -> !functionNames.contains(p.getText()))
                .collect(Collectors.toList());
        functionsAccordion.getPanes().removeAll(deletedPanes);
        this.functionsVBox.setFillWidth(true);

        this.functionsTitledPane.setExpanded(false);
        this.cameraTitledPane.setExpanded(false);
        this.navigationTitledPane.setExpanded(false);
        if (functions.isEmpty()) {
            this.functionsTitledPane.setCollapsible(false);
            this.cameraTitledPane.setCollapsible(false);
            this.navigationTitledPane.setCollapsible(false);
        } else {
            this.functionsTitledPane.setCollapsible(true);
            this.navigationTitledPane.setCollapsible(true);
            if (SubSceneEngine.getSpaceSubScene().is3DCoordinateSystem()) {
                this.cameraTitledPane.setCollapsible(true);
            } else {
                this.cameraTitledPane.setCollapsible(false);
            }
        }
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
        RowConstraints r4 = new RowConstraints();
        ColumnConstraints c0 = new ColumnConstraints();
        c0.setMinWidth(10);
        c0.setPrefWidth(80);
        c0.maxWidthProperty().bind(c0.prefWidthProperty());
        ColumnConstraints c1 = new ColumnConstraints();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().addAll(r0, r1, r2, r3, r4);
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
        playButton.setDisable(true);
        GridPane.setHalignment(playButton, HPos.RIGHT);
        GridPane.setValignment(playButton, VPos.CENTER);

        // Pause animation button
        Button pauseButton = new Button();
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
        pauseButton.setDisable(true);
        GridPane.setColumnIndex(pauseButton, 1);
        GridPane.setHalignment(pauseButton, HPos.CENTER);
        GridPane.setValignment(pauseButton, VPos.CENTER);

        //Stop animation button
        Button stopButton = new Button();
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
        stopButton.setDisable(true);
        GridPane.setColumnIndex(stopButton, 2);
        GridPane.setHalignment(stopButton, HPos.LEFT);
        GridPane.setValignment(stopButton, VPos.CENTER);
        animationButtonsPane.getChildren().addAll(playButton, pauseButton, stopButton);

        animationChoiceBox.valueProperty().addListener((observableValue, animation, t1) -> {
            if (t1 != null) {
                playButton.setDisable(false);
            } else {
                playButton.setDisable(true);
            }
            pauseButton.setDisable(true);
            stopButton.setDisable(true);
        });
        playButton.setOnAction(e -> {
            functionHolder.startAnimationCallback().call();
            playButton.setDisable(true);
            pauseButton.setDisable(false);
            stopButton.setDisable(false);
        });
        pauseButton.setOnAction(e -> {
            functionHolder.pauseAnimationCallback().call();
            playButton.setDisable(false);
            pauseButton.setDisable(true);
            stopButton.setDisable(false);
        });
        stopButton.setOnAction(e -> {
            functionHolder.stopAnimationCallback().call();
            playButton.setDisable(false);
            pauseButton.setDisable(true);
            stopButton.setDisable(true);
        });

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

        //Visible label
        Label visibleLabel = new Label("Visible");
        visibleLabel.getStyleClass().add("inspector-label");
        GridPane.setRowIndex(visibleLabel, 3);
        GridPane.setColumnIndex(visibleLabel, 0);
        GridPane.setHalignment(visibleLabel, HPos.LEFT);
        GridPane.setMargin(visibleLabel, new Insets(10, 0, 0, 0));
        gridPane.getChildren().add(visibleLabel);

        //Visible checkbox
        CheckBox visibleCheckBox = new CheckBox();
        visibleCheckBox.setSelected(true);
        visibleCheckBox.getStyleClass().add("inspector-checkbox");
        functionHolder.visibleProperty().bind(visibleCheckBox.selectedProperty());
        GridPane.setRowIndex(visibleCheckBox, 3);
        GridPane.setColumnIndex(visibleCheckBox, 1);
        GridPane.setMargin(visibleCheckBox, new Insets(10, 0, 0, 0));
        gridPane.getChildren().add(visibleCheckBox);

        //Select points button
        Button selectPointsButton = new Button("Select points");
        selectPointsButton.getStyleClass().add("inspector-button");
        selectPointsButton.setOnAction(e -> {
            SceneRunner.getInstance().runSelectionScene(this.stage, function);
            FunctionStorage.getInstance().updateFunction(function);
        });
        GridPane.setRowIndex(selectPointsButton, 4);
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

    private class ValidationListener implements ChangeListener<String> {

        private TextField textField;

        public ValidationListener(TextField textField) {
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            Pattern textFieldPattern = textField.equals(searchTTextField)
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
                    searchTTextField,
                    searchXTextField,
                    searchYTextField,
                    searchZTextField);

            Boolean isAllValid = textFields.stream()
                    .map(tf -> tf.equals(searchTTextField)
                            ? INTEGER_NUMBER_PATTERN.matcher(tf.getText()).matches()
                            : DOUBLE_NUMBER_PATTERN.matcher(tf.getText()).matches())
                    .reduce(true, (a, b) -> a && b);
            Boolean isAllEmpty = textFields.stream()
                    .map(tf -> tf.getText().isEmpty())
                    .reduce(true, (a, b) -> a && b);
            if (isAllValid && !isAllEmpty) {
                searchButton.setDisable(false);
            } else {
                searchButton.setDisable(true);
            }
        }
    }

}
