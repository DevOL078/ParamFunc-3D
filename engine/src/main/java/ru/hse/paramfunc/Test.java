package ru.hse.paramfunc;

public class Test {

//    private final static String TEST_FILE_PATH = "./etc/test13-04-2020_23-31-56.txt";
//
//    private SpaceSubScene subScene;
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        loadPoints();
//
//        Group root = new Group();
//        Scene scene = new Scene(root, 1024, 726, true);
//        subScene = new SpaceSubScene(1024, 726);
//        subScene.setUp();
//
//        setAnimationControls(scene);
//
//        CameraBuilder cameraBuilder = new CameraBuilder();
//        cameraBuilder.setUp(subScene, scene);
//
//        root.getChildren().add(subScene);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    private void loadPoints() throws IOException {
//        FunctionValues3DParser.getInstance().parse(TEST_FILE_PATH);
//    }
//
//    private void setAnimationControls(Scene scene) {
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
//
//    public static void main(String[] args) {
//        launch(args);
//    }

}
