package ru.hse.paramFunc;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.hse.paramfunc.settings.AppSettings;

import java.io.IOException;
import java.nio.file.Paths;

public class FxApplication extends Application {

    private final static String STYLESHEET_PATH = "ru/hse/paramFunc/";

    @Override
    public void start(Stage stage) throws IOException {
        try {
            AppSettings.init();
            System.out.println("App settings have been loaded");
            SceneRunner.getInstance().setMainStage(stage);
            SceneRunner.getInstance().runMainScene();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Loading settings error");
            alert.setHeaderText("File app.config hasn't been found.");
            alert.setContentText("The config file must be into directory: " +
                    Paths.get(".").toAbsolutePath().toString());
            alert.initModality(Modality.WINDOW_MODAL);
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String getStylesheetPath() {
        return STYLESHEET_PATH;
    }

}
