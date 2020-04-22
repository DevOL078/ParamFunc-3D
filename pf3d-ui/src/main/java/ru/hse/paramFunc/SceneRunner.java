package ru.hse.paramFunc;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.hse.paramfunc.SubSceneEngine;
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

    public void run(SceneType sceneType) throws Exception {
        switch (sceneType) {
            case MAIN: {
                runMainScene();
                break;
            }
            case ALL_POINTS: {
                runAllPointsScene();
                break;
            }
            case SELECTION: {
                runSelectionScene();
                break;
            }
            default: {

            }
        }
    }

    public void stop(SceneType sceneType) {
        switch (sceneType) {
            case ALL_POINTS: {
                stopAllPointsScene();
                break;
            }
            case SELECTION: {
                stopSelectionScene();
                break;
            }
        }
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    private void runMainScene() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root, 1024, 700, true);
        scene.getStylesheets().add(STYLESHEET_PATH + "main.css");
        mainStage.setScene(scene);
        mainStage.show();
    }

    private void runAllPointsScene() throws IOException {
        searchStage = new Stage();
        searchStage.initModality(Modality.WINDOW_MODAL);
        searchStage.initOwner(mainStage);
        Parent root = FXMLLoader.load(getClass().getResource("all-points.fxml"));
        Scene scene = new Scene(root, 600, 400);
        searchStage.setScene(scene);
        searchStage.show();
    }

    private void runSelectionScene() throws IOException {
        selectionStage = new Stage();
        selectionStage.initModality(Modality.WINDOW_MODAL);
        selectionStage.initOwner(mainStage);
        Parent root = FXMLLoader.load(getClass().getResource("selection.fxml"));
        Scene scene = new Scene(root, 600, 400);
        selectionStage.setScene(scene);
        selectionStage.show();
    }

    private void stopAllPointsScene() {
        searchStage.close();
    }

    private void stopSelectionScene() {
        selectionStage.close();
    }

}
