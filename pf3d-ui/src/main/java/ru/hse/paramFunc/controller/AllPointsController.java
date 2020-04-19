package ru.hse.paramFunc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.hse.paramFunc.SceneRunner;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.domain.enums.SceneType;
import ru.hse.paramfunc.storage.FunctionValueStorage;

public class AllPointsController {

    @FXML
    private Button exitButton;

    @FXML
    private TableView<FunctionPoint> tableView;

    @FXML
    private TableColumn<FunctionPoint, Integer> tColumn;

    @FXML
    private TableColumn<FunctionPoint, Float> xColumn;

    @FXML
    private TableColumn<FunctionPoint, Float> yColumn;

    @FXML
    private TableColumn<FunctionPoint, Float> zColumn;

    public void initialize() {
        tColumn.setCellValueFactory(new PropertyValueFactory<>("t"));
        xColumn.setCellValueFactory(new PropertyValueFactory<>("originalX"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("originalY"));
        zColumn.setCellValueFactory(new PropertyValueFactory<>("originalZ"));
        tColumn.minWidthProperty().bind(tableView.widthProperty().divide(4));
        xColumn.minWidthProperty().bind(tableView.widthProperty().divide(4));
        yColumn.minWidthProperty().bind(tableView.widthProperty().divide(4));
        zColumn.minWidthProperty().bind(tableView.widthProperty().divide(4));
        tColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        xColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        yColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        zColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        tableView.getItems().addAll(FunctionValueStorage.getInstance().getAllPoints());

        exitButton.setOnAction(e -> {
            SceneRunner.getInstance().stop(SceneType.ALL_POINTS);
        });
    }

}
