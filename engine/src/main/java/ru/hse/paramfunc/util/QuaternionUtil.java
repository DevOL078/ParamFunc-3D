package ru.hse.paramfunc.util;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;

public class QuaternionUtil {

    public static void rotateGroup(Group group, Quaternion q) {
        double mag = q.X() * q.X() + q.Y() * q.Y() + q.Z() * q.Z();

        final double EPS = 1.0e-12;

        if (mag > EPS) {
            mag = Math.sqrt(mag);
            double invMag = 1.0 / mag;

            double pX = q.X() * invMag;
            double pY = q.Y() * invMag;
            double pZ = q.Z() * invMag;
            double angle = 2.0 * Math.atan2(mag, q.S()) * 180 / Math.PI;
            Rotate rotate = new Rotate(angle, new Point3D(pX, pY, pZ));
            group.getTransforms().add(rotate);
        }
    }

}
