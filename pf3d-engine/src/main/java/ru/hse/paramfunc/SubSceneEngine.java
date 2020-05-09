package ru.hse.paramfunc;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import ru.hse.paramfunc.engine.CameraController;
import ru.hse.paramfunc.engine.SpaceSubScene;

import java.util.LinkedList;

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

}
