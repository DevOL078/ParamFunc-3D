package ru.hse.paramfunc;

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
        subScene.update();

        setAnimationControls(scene);

        CameraController.setUp(subScene, scene);

        root.getChildren().add(subScene);

        Pane spacePane = findSpacePane(scene);

        if (spacePane != null) {
            subScene.widthProperty().bind(spacePane.widthProperty());
            subScene.heightProperty().bind(spacePane.heightProperty());
            addRootToSpacePane(root, spacePane);
        }
    }

    public static SpaceSubScene getSpaceSubScene() {
        return subScene;
    }

    public static void loadFunction(String filePath, String functionName) throws IOException {
        List<FunctionPoint> allPoints = FunctionValues3DParser.getInstance().parse(filePath);
        Function function = new Function(functionName);
        function.setAllPoints(allPoints);
        function.setSelectedPoints(allPoints);
        FunctionStorage.getInstance().addFunction(function);
    }

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

    private static void addRootToSpacePane(Group root, Pane pane) {
        pane.getChildren().add(root);
    }


    private static void setAnimationControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            System.out.println(event.getCode());
            if (event.getCode() == KeyCode.Z) {
                subScene.startCurrentAnimation();
            } else if (event.getCode() == KeyCode.X) {
                subScene.stopCurrentAnimation();
            } else if (event.getCode() == KeyCode.P) {
                subScene.pauseCurrentAnimation();
            } else if (event.getCode() == KeyCode.DIGIT1) {
                subScene.setCurrentAnimation("Flying point");
            } else if (event.getCode() == KeyCode.DIGIT2) {
                subScene.setCurrentAnimation("Dynamic lines");
            }
        });
    }

}
