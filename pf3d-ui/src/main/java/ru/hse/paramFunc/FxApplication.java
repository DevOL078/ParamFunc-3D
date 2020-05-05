package ru.hse.paramFunc;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.hse.paramfunc.settings.AppSettings;

import java.io.IOException;

public class FxApplication extends Application {

    private final static String STYLESHEET_PATH = "ru/hse/paramFunc/";

    @Override
    public void start(Stage stage) throws IOException {
        AppSettings.init();
        System.out.println("App settings have been loaded");
        SceneRunner.getInstance().setMainStage(stage);
        SceneRunner.getInstance().runMainScene();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String getStylesheetPath() {
        return STYLESHEET_PATH;
    }

}
