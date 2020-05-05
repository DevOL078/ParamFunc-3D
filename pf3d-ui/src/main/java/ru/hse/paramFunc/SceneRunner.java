package ru.hse.paramFunc;

import javafx.stage.Stage;
import ru.hse.paramFunc.controller.*;
import ru.hse.paramfunc.domain.Function;


public class SceneRunner {

    private static final SceneRunner instance = new SceneRunner();

    private SceneRunner() {
    }

    public static SceneRunner getInstance() {
        return instance;
    }

    private Stage mainStage;

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public void runMainScene() {
        MainSceneController controller = new MainSceneController(this.mainStage);
        controller.showStage();
    }

    public void runSelectionScene(Stage ownerStage, Function function) {
        SelectionController controller = new SelectionController(ownerStage, function);
        controller.showStage();
    }

    public void runFunctionsScene() {
        FunctionsController controller = new FunctionsController(this.mainStage);
        controller.showStage();
    }

    public void runFunctionSettingsScene(Stage ownerStage, Function function) {
        FunctionSettingsController controller = new FunctionSettingsController(ownerStage, function);
        controller.showStage();
    }

    public void runAppSettingsScene() {
        SettingsController controller = new SettingsController(this.mainStage);
        controller.showStage();
    }

}
