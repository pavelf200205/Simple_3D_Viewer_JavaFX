package com.example.cg4_1;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.net.URL;

public class CG4 extends Application {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    PerspectiveCamera camera;
    private double anchorX, anchorZ;
    private double anchorAngleX = -60;
    private double anchorAngleZ = 30;
    Button WireframeOn;
    Button WireframeOff;
    private final DoubleProperty angleX = new SimpleDoubleProperty(anchorAngleX);
    private final DoubleProperty angleZ = new SimpleDoubleProperty(anchorAngleZ);
    private MeshView mesh;

    @Override
    public void start(Stage primaryStage) {
        Group group = new Group();
        StlMeshImporter stlMeshImporter = new StlMeshImporter();
        try {
            URL modelUrl = this.getClass().getResource("eiffel_tower.stl");
            stlMeshImporter.read(modelUrl);
            mesh = new MeshView(stlMeshImporter.getImport());
            group.getChildren().add(mesh);
        }
        catch (ImportException e) {

        }

        camera = new PerspectiveCamera();
        camera.setNearClip(0.01);
        camera.setFarClip(500);
        camera.getTransforms().add(new Translate(-WIDTH/2., -HEIGHT/2.-25, 1000));
        Scene scene = new Scene(group, WIDTH, HEIGHT, true);
        scene.setCamera(camera);

        initMouseControl(group, scene);
        initKeyboardControl(group, scene);

        primaryStage.setTitle("CG4");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initKeyboardControl(Group group, Scene scene) {
        scene.setOnKeyPressed(e-> {
            if(e.getCode().equals(KeyCode.W)) {
                mesh.setCullFace(CullFace.NONE);
                mesh.setDrawMode(DrawMode.LINE);
            } else if(e.getCode().equals(KeyCode.S)) {
                mesh.setCullFace(CullFace.BACK);
                mesh.setDrawMode(DrawMode.FILL);
            }
        });
    }

    private void initMouseControl(Group group, Scene scene) {
        Rotate xRotate;
        Rotate zRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                zRotate = new Rotate(0, Rotate.Z_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        zRotate.angleProperty().bind(angleZ);
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorZ = event.getSceneY();

            anchorAngleX = angleX.get();
            anchorAngleZ = angleZ.get();

        });
        scene.setOnMouseDragged(event -> {
            if(event.isPrimaryButtonDown()) {
                angleX.set(anchorAngleX - (anchorZ - event.getSceneY())/4);
                angleZ.set(anchorAngleZ +  (anchorX - event.getSceneX())/4);
            } else if (event.isMiddleButtonDown()) {

                camera.setTranslateX(camera.getTranslateX() - (event.getSceneX()- anchorX)/4);
                camera.setTranslateY(camera.getTranslateY() - (event.getSceneY()- anchorZ)/4);
                anchorX = event.getSceneX();
                anchorZ = event.getSceneY();
                event.consume();
            }
        });

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            camera.translateZProperty().set(camera.getTranslateZ()+event.getDeltaY()/8);

//                group.setScaleX(group.getScaleX() * Math.pow(1.005, event.getDeltaY()));
//                group.setScaleY(group.getScaleY() * Math.pow(1.005, event.getDeltaY()));
//                group.setScaleZ(group.getScaleZ() * Math.pow(1.005, event.getDeltaY()));

        });
    }


    public static void main(String[] args) {
        launch(args);
    }

}