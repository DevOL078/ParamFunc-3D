package ru.hse.paramfunc;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.hse.paramfunc.engine.CameraBuilder;
import ru.hse.paramfunc.engine.SpaceSubScene;

public class Test extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 1024, 726, true);
        SpaceSubScene subScene = new SpaceSubScene(1024, 726);
        subScene.setUp();

        CameraBuilder cameraBuilder = new CameraBuilder();
        cameraBuilder.setUp(subScene, scene);

//        scene.setOnKeyPressed(event -> {
//            double change = 1.0;
//            if (event.isShiftDown()) {
//                change = 20.0;
//            }
//            KeyCode keycode = event.getCode();
//            Camera camera = subScene.getCamera();
//            if (keycode == KeyCode.W) {
//                camera.setTranslateZ(camera.getTranslateZ() + change);
//            }
//            if (keycode == KeyCode.S) {
//                camera.setTranslateZ(camera.getTranslateZ() - change);
//            }
//            if (keycode == KeyCode.A) {
//                camera.setTranslateX(camera.getTranslateX() - change);
//            }
//            if (keycode == KeyCode.D) {
//                camera.setTranslateX(camera.getTranslateX() + change);
//            }
//            if (keycode == KeyCode.E) {
//                camera.setTranslateY(camera.getTranslateY() - change);
//            }
//            if (keycode == KeyCode.Q) {
//                camera.setTranslateY(camera.getTranslateY() + change);
//            }
//            System.out.println(camera.getTranslateX() + " " + camera.getTranslateY() + " " + camera.getTranslateZ());
//        });


        root.getChildren().add(subScene);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
