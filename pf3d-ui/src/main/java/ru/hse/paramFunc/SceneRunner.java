package ru.hse.paramFunc;

import javafx.stage.Stage;
import ru.hse.paramFunc.controller.FunctionsController;
import ru.hse.paramFunc.controller.MainSceneController;
import ru.hse.paramFunc.controller.SelectionController;
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
        MainSceneController mainSceneController = new MainSceneController(this.mainStage);
        mainSceneController.showStage();
    }

    public void runSelectionScene(Function function) {
        SelectionController controller = new SelectionController(this.mainStage, function);
        controller.showStage();
    }

    public void runFunctionsScene() {
        FunctionsController functionsController = new FunctionsController(this.mainStage);
        functionsController.showStage();
    }

}
