package ru.hse.paramFunc.controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.hse.paramFunc.SceneRunner;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.domain.enums.SceneType;
import ru.hse.paramfunc.domain.enums.SelectionType;
import ru.hse.paramfunc.selection.SelectionService;
import ru.hse.paramfunc.storage.FunctionValueStorage;

import java.util.ArrayList;
import java.util.List;

public class SelectionController {

    @FXML
    private TextArea intervalSelectionTextArea;

    @FXML
    private Button selectButton;

    @FXML
    private TableView<FunctionPoint> selectionTableView;

    @FXML
    private TableColumn<FunctionPoint, Integer> tColumn;

    @FXML
    private TableColumn<FunctionPoint, Float> xColumn;

    @FXML
    private TableColumn<FunctionPoint, Float> yColumn;

    @FXML
    private TableColumn<FunctionPoint, Float> zColumn;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button closeButton;

    private ObservableList<FunctionPoint> selectedPoints;

    public void initialize() {
        tColumn.setCellValueFactory(new PropertyValueFactory<>("t"));
        xColumn.setCellValueFactory(new PropertyValueFactory<>("originalX"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("originalY"));
        zColumn.setCellValueFactory(new PropertyValueFactory<>("originalZ"));
        tColumn.minWidthProperty().bind(selectionTableView.widthProperty().divide(4));
        xColumn.minWidthProperty().bind(selectionTableView.widthProperty().divide(4));
        yColumn.minWidthProperty().bind(selectionTableView.widthProperty().divide(4));
        zColumn.minWidthProperty().bind(selectionTableView.widthProperty().divide(4));
        tColumn.prefWidthProperty().bind(selectionTableView.widthProperty().divide(4));
        xColumn.prefWidthProperty().bind(selectionTableView.widthProperty().divide(4));
        yColumn.prefWidthProperty().bind(selectionTableView.widthProperty().divide(4));
        zColumn.prefWidthProperty().bind(selectionTableView.widthProperty().divide(4));

        this.selectedPoints = FXCollections.observableArrayList(FunctionValueStorage.getInstance().getSelectedPoints());

        ListProperty<FunctionPoint> selectedPointsProperty = new SimpleListProperty<>(this.selectedPoints);

        selectionTableView.itemsProperty().bind(selectedPointsProperty);

        selectButton.setOnAction(e -> {
            List<FunctionPoint> selectedPoints = SelectionService
                    .selectPoints(SelectionType.INTERVAL, intervalSelectionTextArea.getText());
            this.selectedPoints.clear();
            this.selectedPoints.addAll(selectedPoints);
        });

        saveButton.setOnAction(e -> {
            FunctionValueStorage.getInstance().setSelectedPoints(new ArrayList<>(this.selectedPoints));
            SceneRunner.getInstance().stop(SceneType.SELECTION);
        });

        clearButton.setOnAction(e -> {
            this.selectedPoints.clear();
        });

        closeButton.setOnAction(e -> {
            SceneRunner.getInstance().stop(SceneType.SELECTION);
        });
    }

}
