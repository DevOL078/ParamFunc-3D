package ru.hse.paramFunc.controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private TabPane selectionTabPane;

    @FXML
    private Tab selectionIntervalTab;

    @FXML
    private Tab selectionFunctionalTab;

    @FXML
    private TextArea intervalSelectionTextArea;

    @FXML
    private TextArea functionalSelectionTextArea;

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
            Tab selectedTab = selectionTabPane.getSelectionModel().getSelectedItem();
            if(selectedTab.equals(selectionIntervalTab)) {
                selectPoints(SelectionType.INTERVAL, intervalSelectionTextArea.getText());
            } else if (selectedTab.equals(selectionFunctionalTab)) {
                selectPoints(SelectionType.FUNCTIONAL, functionalSelectionTextArea.getText());
            }
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

    private void selectPoints(SelectionType selectionType, String expression) {
        List<FunctionPoint> selectedPoints = SelectionService
                .selectPoints(selectionType, expression);
        this.selectedPoints.clear();
        this.selectedPoints.addAll(selectedPoints);
    }

}
