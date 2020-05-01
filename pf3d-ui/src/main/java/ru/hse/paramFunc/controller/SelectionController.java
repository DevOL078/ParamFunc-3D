package ru.hse.paramFunc.controller;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Builder;
import lombok.Data;
import ru.hse.paramFunc.FxApplication;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.domain.enums.SelectionType;
import ru.hse.paramfunc.selection.SelectionService;
import ru.hse.paramfunc.storage.FunctionStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectionController {

    @FXML private TabPane selectionTabPane;
    @FXML private Tab selectionIntervalTab;
    @FXML private Tab selectionFunctionalTab;
    @FXML private TextArea intervalSelectionTextArea;
    @FXML private TextArea functionalSelectionTextArea;
    @FXML private Button selectButton;
    @FXML private TableView<FunctionValue> selectionTableView;
    @FXML private Button saveButton;
    @FXML private Button clearButton;
    @FXML private Button closeButton;

    private Stage stage;
    private Function function;
    private List<FunctionValue> functionValues;
    private List<FunctionValue> selectedFunctionValues;

    public SelectionController(Stage ownerStage, Function function) {
        this.function = function;
        this.stage = new Stage();
        this.stage.initModality(Modality.WINDOW_MODAL);
        this.stage.initOwner(ownerStage);
        try {
            FXMLLoader loader = new FXMLLoader(FxApplication.class.getResource("selection.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 400);
            this.stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return this.stage;
    }

    public void showStage() {
        this.stage.show();
    }

    public void initialize() {
        TableColumn<FunctionValue, CheckBox> selectedColumn = new TableColumn<>("Selected");
        TableColumn<FunctionValue, Integer> tColumn = new TableColumn<>("T");
        TableColumn<FunctionValue, Float> xColumn = new TableColumn<>("X");
        TableColumn<FunctionValue, Float> yColumn = new TableColumn<>("Y");
        TableColumn<FunctionValue, Float> zColumn = new TableColumn<>("Z");
        tColumn.setCellValueFactory(new PropertyValueFactory<>("t"));
        xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("y"));
        zColumn.setCellValueFactory(new PropertyValueFactory<>("z"));
        selectedColumn.minWidthProperty().bind(selectionTableView.widthProperty().divide(9));
        tColumn.minWidthProperty().bind(selectionTableView.widthProperty().divide(9).multiply(2));
        xColumn.minWidthProperty().bind(selectionTableView.widthProperty().divide(9).multiply(2));
        yColumn.minWidthProperty().bind(selectionTableView.widthProperty().divide(9).multiply(2));
        zColumn.minWidthProperty().bind(selectionTableView.widthProperty().divide(9).multiply(2));
        selectedColumn.prefWidthProperty().bind(selectionTableView.widthProperty().divide(9));
        tColumn.prefWidthProperty().bind(selectionTableView.widthProperty().divide(9).multiply(2));
        xColumn.prefWidthProperty().bind(selectionTableView.widthProperty().divide(9).multiply(2));
        yColumn.prefWidthProperty().bind(selectionTableView.widthProperty().divide(9).multiply(2));
        zColumn.prefWidthProperty().bind(selectionTableView.widthProperty().divide(9).multiply(2));
        selectedColumn.setStyle("-fx-alignment: CENTER;");
        tColumn.setStyle("-fx-alignment: CENTER;");
        xColumn.setStyle("-fx-alignment: CENTER;");
        yColumn.setStyle("-fx-alignment: CENTER;");
        zColumn.setStyle("-fx-alignment: CENTER;");

        selectedColumn.setEditable(true);
        selectedColumn.setCellValueFactory(f -> {
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().bindBidirectional(f.getValue().selectedProperty);
            return new SimpleObjectProperty<>(checkBox);
        });

        selectionTableView.getColumns().addAll(
                selectedColumn,
                tColumn,
                xColumn,
                yColumn,
                zColumn
        );

        List<Integer> selectedTs = function.getSelectedPoints().stream()
                .map(FunctionPoint::getT)
                .collect(Collectors.toList());
        this.functionValues = this.function.getAllPoints().stream()
                .map(p -> FunctionValue.builder()
                        .selectedProperty(new SimpleBooleanProperty(selectedTs.contains(p.getT())))
                        .t(p.getT())
                        .x(p.getOriginalX())
                        .y(p.getOriginalY())
                        .z(p.getOriginalZ())
                        .build())
                .collect(Collectors.toList());
        this.selectedFunctionValues = this.functionValues.stream()
                .filter(v -> selectedTs.contains(v.getT()))
                .collect(Collectors.toList());
        selectionTableView.getItems().addAll(this.functionValues);
        this.functionValues.forEach(v -> v.selectedProperty.addListener((observableValue, aBoolean, t1) -> {
            if(t1) {
                selectedFunctionValues.add(v);
            } else {
                selectedFunctionValues.remove(v);
            }
        }));

        selectButton.setOnAction(e -> {
            Tab selectedTab = selectionTabPane.getSelectionModel().getSelectedItem();
            if (selectedTab.equals(selectionIntervalTab)) {
                selectPoints(SelectionType.INTERVAL, intervalSelectionTextArea.getText());
            } else if (selectedTab.equals(selectionFunctionalTab)) {
                selectPoints(SelectionType.FUNCTIONAL, functionalSelectionTextArea.getText());
            }
        });

        saveButton.setOnAction(e -> {
            List<Integer> selectedTList = this.selectedFunctionValues.stream()
                    .map(FunctionValue::getT)
                    .collect(Collectors.toList());
            List<FunctionPoint> selectedPoints = function.getAllPoints().stream()
                    .filter(p -> selectedTList.contains(p.getT()))
                    .collect(Collectors.toList());
            FunctionStorage.getInstance().setSelectedPoints(this.function, selectedPoints);
            this.stage.close();
        });

        clearButton.setOnAction(e -> {
            this.functionValues.forEach(v -> v.selectedProperty.set(false));
            this.selectedFunctionValues.clear();
        });

        closeButton.setOnAction(e -> {
            this.stage.close();
        });
    }

    private void selectPoints(SelectionType selectionType, String expression) {
        List<FunctionPoint> selectedPoints = SelectionService
                .selectPoints(this.function, selectionType, expression);
        List<Integer> selectedTs = selectedPoints.stream()
                .map(FunctionPoint::getT)
                .collect(Collectors.toList());
        this.selectedFunctionValues.clear();
        List<FunctionValue> localSelectedValues = this.functionValues.stream()
                .filter(v -> selectedTs.contains(v.getT()))
                .collect(Collectors.toList());
        this.selectedFunctionValues.addAll(localSelectedValues);
        localSelectedValues.forEach(v -> v.selectedProperty.set(true));
    }

    @Data
    @Builder
    public static class FunctionValue {
        private BooleanProperty selectedProperty;
        private Integer t;
        private Float x;
        private Float y;
        private Float z;
    }

}
