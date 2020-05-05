package ru.hse.paramfunc.settings;

import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.io.*;

public class AppSettings {

    private static String configFilePath = "./app.config";
    private static SortedProperties props;

    private static DoubleProperty cameraSpeedProperty;
    private static DoubleProperty functionPointsRadiusProperty;
    private static DoubleProperty interpolationPointsRadiusProperty;
    private static DoubleProperty animationPointsRadiusProperty;
    private static ObjectProperty<Color> functionPointsColorProperty;
    private static ObjectProperty<Color> interpolationPointsColorProperty;
    private static ObjectProperty<Color> animationPointsColorProperty;
    private static ObjectProperty<Color> functionHighlightingColorProperty;
    private static IntegerProperty interpolationPointsCountProperty;
    private static DoubleProperty animationSpeedProperty;

    public static void init() throws IOException {
        props = new SortedProperties();
        try (InputStream is = new FileInputStream(configFilePath)) {
            props.load(is);
        }

        cameraSpeedProperty = new SimpleDoubleProperty(Double.parseDouble(props.getProperty("app.camera.speed")));
        functionPointsRadiusProperty = new SimpleDoubleProperty(Double.parseDouble(props.getProperty("app.function.radius")));
        interpolationPointsRadiusProperty = new SimpleDoubleProperty(Double.parseDouble(props.getProperty("app.interpolation.radius")));
        animationPointsRadiusProperty = new SimpleDoubleProperty(Double.parseDouble(props.getProperty("app.animation.radius")));
        functionPointsColorProperty = new SimpleObjectProperty<>(Color.valueOf(props.getProperty("app.function.color")));
        interpolationPointsColorProperty = new SimpleObjectProperty<>(Color.valueOf(props.getProperty("app.interpolation.color")));
        animationPointsColorProperty = new SimpleObjectProperty<>(Color.valueOf(props.getProperty("app.animation.color")));
        functionHighlightingColorProperty = new SimpleObjectProperty<>(Color.valueOf(props.getProperty("app.highlighting.color")));
        interpolationPointsCountProperty = new SimpleIntegerProperty(Integer.parseInt(props.getProperty("app.interpolation.one-step.count")));
        animationSpeedProperty = new SimpleDoubleProperty(Double.parseDouble(props.getProperty("app.animation.speed")));

        cameraSpeedProperty.addListener((observableValue, number, t1) -> {
            props.setProperty("app.camera.speed", String.valueOf(t1));
            storeConfigs();
        });
        functionPointsRadiusProperty.addListener((observableValue, number, t1) -> {
            props.setProperty("app.function.radius", String.valueOf(t1));
            storeConfigs();
        });
        interpolationPointsRadiusProperty.addListener((observableValue, number, t1) -> {
            props.setProperty("app.interpolation.radius", String.valueOf(t1));
            storeConfigs();
        });
        animationPointsRadiusProperty.addListener((observableValue, number, t1) -> {
            props.setProperty("app.animation.radius", String.valueOf(t1));
            storeConfigs();
        });
        functionPointsColorProperty.addListener((observableValue, number, t1) -> {
            props.setProperty("app.function.color", t1.toString());
            storeConfigs();
        });
        interpolationPointsColorProperty.addListener((observableValue, number, t1) -> {
            props.setProperty("app.interpolation.color", t1.toString());
            storeConfigs();
        });
        animationPointsColorProperty.addListener((observableValue, number, t1) -> {
            props.setProperty("app.animation.color", t1.toString());
            storeConfigs();
        });
        functionHighlightingColorProperty.addListener((observableValue, number, t1) -> {
            props.setProperty("app.highlighting.color", t1.toString());
            storeConfigs();
        });
        interpolationPointsCountProperty.addListener((observableValue, number, t1) -> {
            props.setProperty("app.interpolation.one-step.count", String.valueOf(t1));
            storeConfigs();
        });
        animationSpeedProperty.addListener((observableValue, number, t1) -> {
            props.setProperty("app.animation.speed", String.valueOf(t1));
            storeConfigs();
        });
    }

    private static void storeConfigs() {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(configFilePath)
                )
        )) {
            props.store(writer);
            System.out.println("New configurations saved");
        } catch (IOException e) {
            System.err.println("Store error");
            e.printStackTrace();
        }
    }

    public static DoubleProperty cameraSpeedProperty() {
        return cameraSpeedProperty;
    }

    public static DoubleProperty functionPointsRadiusProperty() {
        return functionPointsRadiusProperty;
    }

    public static DoubleProperty interpolationPointsRadiusProperty() {
        return interpolationPointsRadiusProperty;
    }

    public static DoubleProperty animationPointsRadiusProperty() {
        return animationPointsRadiusProperty;
    }

    public static ObjectProperty<Color> functionPointsColorProperty() {
        return functionPointsColorProperty;
    }

    public static ObjectProperty<Color> interpolationPointsColorProperty() {
        return interpolationPointsColorProperty;
    }

    public static ObjectProperty<Color> animationPointsColorProperty() {
        return animationPointsColorProperty;
    }

    public static ObjectProperty<Color> functionHighlightingColorProperty() {
        return functionHighlightingColorProperty;
    }

    public static IntegerProperty interpolationPointsCountProperty() {
        return interpolationPointsCountProperty;
    }

    public static DoubleProperty animationSpeedProperty() {
        return animationSpeedProperty;
    }

}
