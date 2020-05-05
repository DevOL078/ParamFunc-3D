package ru.hse.paramfunc;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.engine.CameraController;
import ru.hse.paramfunc.engine.SpaceSubScene;
import ru.hse.paramfunc.parser.FunctionValues3DParser;
import ru.hse.paramfunc.storage.FunctionStorage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SubSceneEngine {

    private static SpaceSubScene subScene;

    public static void start(Scene scene) throws Exception {
        Group root = new Group();
        subScene = new SpaceSubScene(1024, 726);

        CameraController.setUp(subScene, scene);

        root.getChildren().add(subScene);

        Pane spacePane = findSpacePane(scene);

        if (spacePane != null) {
            subScene.widthProperty().bind(spacePane.widthProperty());
            subScene.heightProperty().bind(spacePane.heightProperty());
            spacePane.getChildren().add(root);
        }
    }

    public static SpaceSubScene getSpaceSubScene() {
        return subScene;
    }

//    public static void loadFunction(String filePath, String functionName) throws IOException {
//        System.out.println("Start loadFunction");
//        List<FunctionPoint> allPoints = FunctionValues3DParser.getInstance().parse(filePath);
//        Function function = new Function(functionName);
//        System.out.println("Created new function: " + functionName);
//        function.setAllPoints(allPoints);
//        System.out.println("All points");
//        function.setSelectedPoints(allPoints);
//        System.out.println("Selected points");
//        Platform.runLater(() -> FunctionStorage.getInstance().addFunction(function));
//        System.out.println("Add function");
//    }

    private static Pane findSpacePane(Scene scene) {
        Pane sceneRoot = (Pane) scene.getRoot();
        LinkedList<Pane> queue = new LinkedList<>();
        queue.push(sceneRoot);

        while (!queue.isEmpty()) {
            Pane currentPane = queue.poll();
            for (Node child : currentPane.getChildren()) {
                if (child.getId() != null && child.getId().equals("spacePane")) {
                    return (Pane) child;
                } else if (child instanceof Pane) {
                    queue.push((Pane) child);
                }
            }
        }

        return null;
    }

//    private static void setAnimationControls(Scene scene) {
//        scene.setOnKeyPressed(event -> {
//            System.out.println(event.getCode());
//            if (event.getCode() == KeyCode.Z) {
//                subScene.startCurrentAnimation();
//            } else if (event.getCode() == KeyCode.X) {
//                subScene.stopCurrentAnimation();
//            } else if (event.getCode() == KeyCode.P) {
//                subScene.pauseCurrentAnimation();
//            } else if (event.getCode() == KeyCode.DIGIT1) {
//                subScene.setCurrentAnimation("Flying point");
//            } else if (event.getCode() == KeyCode.DIGIT2) {
//                subScene.setCurrentAnimation("Dynamic lines");
//            }
//        });
//    }

}
