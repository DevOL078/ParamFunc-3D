package ru.hse.paramFunc;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.hse.paramfunc.domain.enums.SceneType;

public class FxApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneRunner.getInstance().setMainStage(stage);
        SceneRunner.getInstance().run(SceneType.MAIN);
    }

    public static void main(String[] args) {
        launch(args);
    }

}