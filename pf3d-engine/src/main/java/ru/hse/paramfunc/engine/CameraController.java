package ru.hse.paramfunc.engine;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import org.fxyz3d.geometry.Point3D;
import ru.hse.paramfunc.util.Quaternion;
import ru.hse.paramfunc.util.QuaternionUtil;


public class CameraController {

    private static final double SHIFT_ROTATION_COEFF = 10;

    private static PerspectiveCamera camera = new PerspectiveCamera(true);;
    private static Group cameraGroup1;
    private static Group cameraGroup2;
    private static SpaceSubScene spaceSubScene;

    private static double oldPosX;
    private static double oldPosY;
    private static double posX;
    private static double posY;

    public static void setUp(SpaceSubScene scene, Scene rootScene) {
        spaceSubScene = scene;
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
        cameraGroup2.setTranslateZ(50);
        resetPosition();

        Group sceneRoot = (Group) scene.getRoot();
        sceneRoot.getChildren().add(cameraGroup2);

        rootScene.setOnScroll(e -> {
            camera.setTranslateZ(camera.getTranslateZ() + 10 * (e.getDeltaY() > 0 ? 1 : -1));

            Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
            scene.onCameraMove(bounds);
        });

        scene.setOnMousePressed(event -> {
            oldPosX = event.getSceneX();
            oldPosY = event.getSceneY();
            posX = oldPosX;
            posY = oldPosY;
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

    public static void setTo2DXPosition() {
        resetPosition();

        Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
        spaceSubScene.onCameraMove(bounds);
    }

    public static void setTo2DYPosition() {
        resetPosition();
        QuaternionUtil.rotateGroup(cameraGroup2, new Quaternion(new Point3D(0, 1, 0), -90));

        Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
        spaceSubScene.onCameraMove(bounds);
    }

    public static void setTo2DZPosition() {
        resetPosition();
        QuaternionUtil.rotateGroup(cameraGroup1, new Quaternion(new Point3D(1, 0, 0), -90));

        Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
        spaceSubScene.onCameraMove(bounds);
    }

    public static void setToIsometricPosition() {
        resetPosition();
        QuaternionUtil.rotateGroup(cameraGroup2, new Quaternion(new Point3D(0, 1, 0), -45));
        QuaternionUtil.rotateGroup(cameraGroup1, new Quaternion(new Point3D(1, 0, 0), -30));

        Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
        spaceSubScene.onCameraMove(bounds);
    }

    private static void resetPosition() {
        cameraGroup2.getTransforms().clear();
        cameraGroup1.getTransforms().clear();
        QuaternionUtil.rotateGroup(cameraGroup2, new Quaternion(new Point3D(0, 1, 0), -90));
    }

}
