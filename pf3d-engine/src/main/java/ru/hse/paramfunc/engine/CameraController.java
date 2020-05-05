package ru.hse.paramfunc.engine;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;
import org.fxyz3d.geometry.Point3D;
import ru.hse.paramfunc.settings.AppSettings;
import ru.hse.paramfunc.util.Quaternion;
import ru.hse.paramfunc.util.QuaternionUtil;


public class CameraController {

    private static final double SHIFT_ROTATION_COEFF = 10;

    private static PerspectiveCamera camera = new PerspectiveCamera(true);
    private static Scene rootScene;
    private static Group cameraGroup1;
    private static Group cameraGroup2;
    private static SpaceSubScene spaceSubScene;

    private static double oldPosX;
    private static double oldPosY;
    private static double posX;
    private static double posY;

    public static void setUp(SpaceSubScene scene, Scene rootScene) {
        CameraController.rootScene = rootScene;
        spaceSubScene = scene;

        scene.setCamera(camera);
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(200);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);

        cameraGroup1 = new Group();
        cameraGroup1.getChildren().add(camera);

        cameraGroup2 = new Group();
        cameraGroup2.getChildren().add(cameraGroup1);

        Group subSceneRoot = (Group) spaceSubScene.getRoot();
        subSceneRoot.getChildren().add(cameraGroup2);
    }

    public static void setUpForThreeDimSpace() {
        // Устанавливаем камеру в центр пространства
        reset3DPosition();

        double cameraSpeed = AppSettings.cameraSpeedPropertyProperty().get();
        rootScene.setOnScroll(e -> {
            camera.setTranslateZ(camera.getTranslateZ() + cameraSpeed * (e.getDeltaY() > 0 ? 1 : -1));

            Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
            spaceSubScene.onCameraMove(bounds);
        });

        spaceSubScene.setOnMousePressed(event -> {
            oldPosX = event.getSceneX();
            oldPosY = event.getSceneY();
            posX = oldPosX;
            posY = oldPosY;
        });
        spaceSubScene.setOnMouseDragged(event -> {
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
            spaceSubScene.onCameraMove(bounds);
        });
    }

    public static void setUpForTwoDimSpace() {
        reset2DPosition();

        double cameraSpeed = AppSettings.cameraSpeedPropertyProperty().get();
        rootScene.setOnScroll(e -> {
            double newTranslateZ = camera.getTranslateZ() - cameraSpeed * (e.getDeltaY() > 0 ? 1 : -1);
            if(newTranslateZ >= -500 && newTranslateZ <= 0) {
                camera.setTranslateZ(newTranslateZ);
            }
        });
        spaceSubScene.setOnMousePressed(event -> {
            oldPosX = event.getSceneX();
            oldPosY = event.getSceneY();
            posX = oldPosX;
            posY = oldPosY;
        });
        spaceSubScene.setOnMouseDragged(event -> {
            oldPosX = posX;
            oldPosY = posY;
            posX = event.getSceneX();
            posY = event.getSceneY();

            double deltaX = posX - oldPosX;
            double deltaY = posY - oldPosY;

            double newTranslateX = cameraGroup2.getTranslateX() - deltaX * camera.getTranslateZ() / -1200;
            double newTranslateZ = cameraGroup2.getTranslateZ() + deltaY * camera.getTranslateZ() / -1200;

            if(newTranslateX >= 0 && newTranslateX <= 100) {
                cameraGroup2.setTranslateX(newTranslateX);
            }
            if(newTranslateZ >= 0 && newTranslateZ <= 100) {
                cameraGroup2.setTranslateZ(newTranslateZ);
            }
        });
    }

    public static void setTo2DXPosition() {
        reset3DPosition();

        Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
        spaceSubScene.onCameraMove(bounds);
    }

    public static void setTo2DYPosition() {
        reset3DPosition();
        QuaternionUtil.rotateGroup(cameraGroup2, new Quaternion(new Point3D(0, 1, 0), -90));

        Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
        spaceSubScene.onCameraMove(bounds);
    }

    public static void setTo2DZPosition() {
        reset3DPosition();
        QuaternionUtil.rotateGroup(cameraGroup1, new Quaternion(new Point3D(1, 0, 0), -90));

        Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
        spaceSubScene.onCameraMove(bounds);
    }

    public static void setToIsometricPosition() {
        reset3DPosition();
        QuaternionUtil.rotateGroup(cameraGroup2, new Quaternion(new Point3D(0, 1, 0), -45));
        QuaternionUtil.rotateGroup(cameraGroup1, new Quaternion(new Point3D(1, 0, 0), -30));

        Bounds bounds = cameraGroup2.localToScene(cameraGroup2.getBoundsInLocal());
        spaceSubScene.onCameraMove(bounds);
    }

    private static void reset3DPosition() {
        camera.setTranslateZ(-300);
        cameraGroup2.getTransforms().clear();
        cameraGroup1.getTransforms().clear();
        cameraGroup2.setTranslateX(50);
        cameraGroup2.setTranslateY(-50);
        cameraGroup2.setTranslateZ(50);
        QuaternionUtil.rotateGroup(cameraGroup2, new Quaternion(new Point3D(0, 1, 0), -90));
    }

    private static void reset2DPosition() {
        cameraGroup2.getTransforms().clear();
        cameraGroup1.getTransforms().clear();
        camera.setTranslateZ(-200);
        cameraGroup2.setTranslateX(50);
        cameraGroup2.setTranslateZ(50);
        cameraGroup2.setTranslateY(0);
        QuaternionUtil.rotateGroup(cameraGroup2, new Quaternion(new Point3D(1, 0, 0), -90));
    }

}
