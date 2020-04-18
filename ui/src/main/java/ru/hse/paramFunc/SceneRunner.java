package ru.hse.paramFunc;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.hse.paramfunc.Engine;
import ru.hse.paramfunc.domain.enums.SceneType;

public class SceneRunner {

    private static final SceneRunner instance = new SceneRunner();

    private SceneRunner() {
    }

    public static SceneRunner getInstance() {
        return instance;
    }

    private final static String STYLESHEET_PATH = "ru/hse/paramFunc/";

    public void run(SceneType sceneType, Stage stage) throws Exception {
        switch (sceneType) {
            case MAIN: {
                runMainScene(stage);
                break;
            }
            default: {

            }
        }

    }

    private void runMainScene(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root, 1024, 700, true);
        scene.getStylesheets().add(STYLESHEET_PATH + "main.css");
        Engine.start(scene);
        stage.setScene(scene);
        stage.show();
    }

}
