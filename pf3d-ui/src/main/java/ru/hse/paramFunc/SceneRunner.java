package ru.hse.paramFunc;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.hse.paramFunc.controller.MainSceneController;
import ru.hse.paramFunc.controller.SelectionController;
import ru.hse.paramfunc.SubSceneEngine;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.enums.SceneType;

import java.io.IOException;


public class SceneRunner {

    private static final SceneRunner instance = new SceneRunner();

    private SceneRunner() {
    }

    public static SceneRunner getInstance() {
        return instance;
    }

    private final static String STYLESHEET_PATH = "ru/hse/paramFunc/";
    private Stage mainStage;
    private Stage searchStage;
    private Stage selectionStage;

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public void runMainScene() {
        MainSceneController mainSceneController = new MainSceneController(this.mainStage);
        mainSceneController.showStage();
    }

//    public void runAllPointsScene() throws IOException {
//        searchStage = new Stage();
//        searchStage.initModality(Modality.WINDOW_MODAL);
//        searchStage.initOwner(mainStage);
//        Parent root = FXMLLoader.load(getClass().getResource("all-points.fxml"));
//        Scene scene = new Scene(root, 600, 400);
//        searchStage.setScene(scene);
//        searchStage.show();
//    }

    public void runSelectionScene(Function function) {
        SelectionController controller = new SelectionController(this.mainStage, function);
        controller.showStage();
    }

}
