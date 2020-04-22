package ru.hse.paramfunc;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import ru.hse.paramfunc.engine.CameraBuilder;
import ru.hse.paramfunc.engine.SpaceSubScene;
import ru.hse.paramfunc.parser.FunctionValues3DParser;

import java.io.IOException;
import java.util.LinkedList;

public class SubSceneEngine {

    private final static String TEST_FILE_PATH = "C:\\Users\\Dmitry\\Desktop\\Diploma\\ParamFunc-3D\\etc\\test13-04-2020_23-31-56.txt";

    private static SpaceSubScene subScene;

    public static void start(Scene scene) throws Exception {
        Group root = new Group();
        subScene = new SpaceSubScene(1024, 726);
        subScene.setUp();

        setAnimationControls(scene);

        CameraBuilder cameraBuilder = new CameraBuilder();
        cameraBuilder.setUp(subScene, scene);

        root.getChildren().add(subScene);

        Pane spacePane = findSpacePane(scene);

        if(spacePane != null) {
            subScene.widthProperty().bind(spacePane.widthProperty());
            subScene.heightProperty().bind(spacePane.heightProperty());
            addRootToSpacePane(root, spacePane);
        }
    }

    public static SpaceSubScene getSpaceSubScene() {
        return subScene;
    }

    public static void loadPoints(String filePath) throws IOException {
        FunctionValues3DParser.getInstance().parse(filePath);
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
