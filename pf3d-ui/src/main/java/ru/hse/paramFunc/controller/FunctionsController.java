package ru.hse.paramFunc.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.hse.paramFunc.FxApplication;
import ru.hse.paramFunc.SceneRunner;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.listener.Listener;
import ru.hse.paramfunc.storage.FunctionStorage;

import java.io.IOException;
import java.util.List;

public class FunctionsController implements Listener {

    @FXML private Button closeButton;
    @FXML private TableView<Function> functionsTableView;

    private Stage stage;

    public FunctionsController(Stage ownerStage) {
        this.stage = new Stage();
        this.stage.initModality(Modality.WINDOW_MODAL);
        this.stage.initOwner(ownerStage);
        try {
            FXMLLoader loader = new FXMLLoader(FxApplication.class.getResource("functions.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 400);
            scene.getStylesheets().add(FxApplication.getStylesheetPath() + "main.css");
            this.stage.setScene(scene);
            this.stage.setTitle("Functions");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        this.stage.show();
    }

    public void initialize() {
        this.closeButton.setOnAction(e -> this.stage.close());

        this.initTable();
        this.updateTableData();
    }

    private void initTable() {
        FunctionStorage.getInstance().addListener(this);

        TableColumn<Function, String> nameColumn = new TableColumn<>("Function name");
        TableColumn<Function, Integer> allPointsColumn = new TableColumn<>("All points");
        TableColumn<Function, Integer> selectedColumn = new TableColumn<>("Selected points");
        TableColumn<Function, Button> pointsActionColumn = new TableColumn<>();
        TableColumn<Function, Button> settingsActionColumn = new TableColumn<>();
        TableColumn<Function, Button> removeActionColumn = new TableColumn<>();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPointsColumn.setCellValueFactory(f -> new SimpleObjectProperty<>(f.getValue().getAllPoints().size()));
        selectedColumn.setCellValueFactory(f -> new SimpleObjectProperty<>(f.getValue().getSelectedPoints().size()));
        pointsActionColumn.setCellValueFactory(f -> {
            Button button = new Button();
            button.getStyleClass().add("inspector-button");
            button.setPrefWidth(20);
            button.setPrefHeight(20);
            ImageView imageView = new ImageView(FxApplication.class.getResource("points.png").toString());
            imageView.setViewport(new Rectangle2D(6, 6, 25, 25));
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            button.setGraphic(imageView);
            button.setOnAction(e -> SceneRunner.getInstance().runSelectionScene(this.stage, f.getValue()));
            return new SimpleObjectProperty<>(button);
        });
        settingsActionColumn.setCellValueFactory(f -> {
            Button button = new Button();
            button.getStyleClass().add("inspector-button");
            button.setPrefWidth(20);
            button.setPrefHeight(20);
            ImageView imageView = new ImageView(FxApplication.class.getResource("settings.png").toString());
            imageView.setViewport(new Rectangle2D(4, 4, 31, 32));
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            button.setGraphic(imageView);
            button.setOnAction(e -> SceneRunner.getInstance().runFunctionSettingsScene(this.stage, f.getValue()));
            return new SimpleObjectProperty<>(button);
        });
        removeActionColumn.setCellValueFactory(f -> {
            Button button = new Button();
            button.getStyleClass().add("inspector-button");
            button.setPrefWidth(20);
            button.setPrefHeight(20);
            ImageView imageView = new ImageView(FxApplication.class.getResource("remove.png").toString());
            imageView.setViewport(new Rectangle2D(7, 4, 24, 29));
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            button.setGraphic(imageView);
            button.setOnAction(e -> FunctionStorage.getInstance().removeFunction(f.getValue()));
            return new SimpleObjectProperty<>(button);
        });

        nameColumn.prefWidthProperty().bind(functionsTableView.widthProperty().divide(10).multiply(3));
        allPointsColumn.prefWidthProperty().bind(functionsTableView.widthProperty().divide(10).multiply(2));
        selectedColumn.prefWidthProperty().bind(functionsTableView.widthProperty().divide(10).multiply(2));
        pointsActionColumn.prefWidthProperty().bind(functionsTableView.widthProperty().divide(10));
        settingsActionColumn.prefWidthProperty().bind(functionsTableView.widthProperty().divide(10));
        removeActionColumn.prefWidthProperty().bind(functionsTableView.widthProperty().divide(10));

        nameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        allPointsColumn.setStyle("-fx-alignment: CENTER;");
        selectedColumn.setStyle("-fx-alignment: CENTER;");
        pointsActionColumn.setStyle("-fx-alignment: CENTER;");
        settingsActionColumn.setStyle("-fx-alignment: CENTER;");
        removeActionColumn.setStyle("-fx-alignment: CENTER;");

        functionsTableView.getColumns().addAll(
                nameColumn,
                allPointsColumn,
                selectedColumn,
                pointsActionColumn,
                settingsActionColumn,
                removeActionColumn
        );
        functionsTableView.getStyleClass().add("functions-table");
    }

    private void updateTableData() {
        functionsTableView.getItems().clear();
        List<Function> functions = FunctionStorage.getInstance().getFunctions();
        functionsTableView.getItems().addAll(functions);
    }

    @Override
    public void receive() {
        this.updateTableData();
    }
}
