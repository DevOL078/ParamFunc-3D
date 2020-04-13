package ru.hse.paramfunc.engine;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import org.fxyz3d.geometry.Point3D;
import ru.hse.paramfunc.util.Quaternion;
import ru.hse.paramfunc.util.QuaternionUtil;


public class CameraBuilder {

    private final double SHIFT_ROTATION_COEFF = 10;

    private PerspectiveCamera camera;
    private Group cameraGroup1;
    private Group cameraGroup2;

    private double oldPosX;
    private double oldPosY;
    private double posX;
    private double posY;

    public CameraBuilder() {
        this.camera = new PerspectiveCamera(true);
    }

    public void setUp(SpaceSubScene scene, Scene rootScene) {
        scene.setCamera(camera);
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-300);

        cameraGroup1 = new Group();
        cameraGroup1.getChildren().add(camera);

        cameraGroup2 = new Group();
        cameraGroup2.getChildren().add(cameraGroup1);
        // Устанавливаем камеры в центр пространства
        cameraGroup2.setTranslateX(50);
        cameraGroup2.setTranslateY(-50);
        cameraGroup2.setTranslateZ(-50);

        Group sceneRoot = (Group) scene.getRoot();
        sceneRoot.getChildren().add(cameraGroup2);

        scene.setOnMousePressed(event -> {
            oldPosX = event.getSceneX();
            oldPosY = event.getSceneY();
            posX = oldPosX;
            posY = oldPosY;
        });

        rootScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W) {
                camera.setTranslateZ(camera.getTranslateZ() + 10);
            } else if (event.getCode() == KeyCode.S) {
                camera.setTranslateZ(camera.getTranslateZ() - 10);
            }

            Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
            scene.onCameraMove(bounds);
        });

        scene.setOnMouseDragged(event -> {
            oldPosX = posX;
            oldPosY = posY;
            posX = event.getSceneX();
            posY = event.getSceneY();

            double deltaX = posX - oldPosX;
            double deltaY = posY - oldPosY;

            Quaternion qX;
            Quaternion qY;

            if (event.isShiftDown()) {
                qX = new Quaternion(new Point3D(0, 1, 0), deltaX * -0.1 * SHIFT_ROTATION_COEFF);
                qY = new Quaternion(new Point3D(1, 0, 0), deltaY * 0.1 * SHIFT_ROTATION_COEFF);
            } else {
                qX = new Quaternion(new Point3D(0, 1, 0), deltaX * -0.1);
                qY = new Quaternion(new Point3D(1, 0, 0), deltaY * 0.1);
            }

            QuaternionUtil.rotateGroup(cameraGroup2, qX);
            QuaternionUtil.rotateGroup(cameraGroup1, qY);

            Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
            scene.onCameraMove(bounds);
        });
    }
}
