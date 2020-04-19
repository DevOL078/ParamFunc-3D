package ru.hse.paramFunc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.domain.enums.SelectionType;
import ru.hse.paramfunc.selection.SelectionListener;
import ru.hse.paramfunc.selection.SelectionService;
import ru.hse.paramfunc.storage.FunctionValueStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SelectionController implements SelectionListener {

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
    private Button resetButton;

    @FXML
    private Button returnButton;

    public void initialize() {
        FunctionValueStorage.getInstance().addListener(this);

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

        selectionTableView.getItems().addAll(FunctionValueStorage.getInstance().getSelectedPoints());

        selectButton.setOnAction(e -> {
            SelectionService.selectPoints(SelectionType.INTERVAL, intervalSelectionTextArea.getText());
        });
    }

    @Override
    public void receive(List<FunctionPoint> selectedPoints) {
        selectionTableView.getItems().clear();
        List<FunctionPoint> sortedSelectedPoints = selectedPoints.stream()
                .sorted(Comparator.comparing(FunctionPoint::getT))
                .collect(Collectors.toList());
        selectionTableView.getItems().addAll(sortedSelectedPoints);
    }

}
