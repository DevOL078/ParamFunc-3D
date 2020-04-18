package ru.hse.paramFunc;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.hse.paramfunc.domain.enums.SceneType;

import java.io.IOException;

public class SceneRunner {

    private static final SceneRunner instance = new SceneRunner();
    private SceneRunner() {}
    public static SceneRunner getInstance() {
        return instance;
    }

    private final static String STYLESHEET_PATH = "ru/hse/paramFunc/";

    public void run(SceneType sceneType, Stage stage) throws IOException {
        switch (sceneType) {
            case MAIN: {
                runMainScene(stage);
                break;
            }
            default: {

            }
        }

    }

    private void runMainScene(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root, 1024, 700);
        scene.getStylesheets().add(STYLESHEET_PATH + "main.css");
        stage.setScene(scene);
        stage.show();
    }

}
