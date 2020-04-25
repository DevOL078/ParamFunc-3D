package ru.hse.paramFunc.interpolation.spline;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.util.ArrayList;
import java.util.List;

import static ru.hse.paramFunc.interpolation.util.MatrixUtils.multiplyByNumber;
import static ru.hse.paramFunc.interpolation.util.MatrixUtils.sum;

public class CatmullRomSpline implements Spline {

    private final static double KNOT_PARAMETRIZATION_POWER = 0.5;
    private final static int POINTS_NUMBER = 10;

    @Override
    public List<FunctionPoint> calculate(List<FunctionPoint> controlPoints) {
        List<FunctionPoint> splinePoints = new ArrayList<>();
        for (int i = 0; i < controlPoints.size() - 3; ++i) {
            List<FunctionPoint> partSplinePoints = calculateSplinePart(
                    controlPoints.get(i),
                    controlPoints.get(i + 1),
                    controlPoints.get(i + 2),
                    controlPoints.get(i + 3));
            splinePoints.addAll(partSplinePoints);
        }
        if(controlPoints.size() > 2) {
            // Добавляем точки между первой и второй точками, предпоследней и последней.
            FunctionPoint first = controlPoints.get(0);
            FunctionPoint second = controlPoints.get(1);
            FunctionPoint third = controlPoints.get(2);
            FunctionPoint preFirst = FunctionPoint.builder()
                    .t(-1)
                    .systemX(2 * first.getOriginalX() - second.getOriginalX())
                    .systemY(2 * first.getOriginalY() - second.getOriginalY())
                    .systemZ(2 * first.getOriginalZ() - second.getOriginalZ())
                    .originalX(2 * first.getOriginalX() - second.getOriginalX())
                    .originalY(2 * first.getOriginalY() - second.getOriginalY())
                    .originalZ(2 * first.getOriginalZ() - second.getOriginalZ())
                    .build();
            List<FunctionPoint> firstSplinePoints = calculateSplinePart(
                    preFirst,
                    first,
                    second,
                    third);
            splinePoints.addAll(firstSplinePoints);

            FunctionPoint beforePreLast = controlPoints.get(controlPoints.size() - 3);
            FunctionPoint preLast = controlPoints.get(controlPoints.size() - 2);
            FunctionPoint last = controlPoints.get(controlPoints.size() - 1);
            FunctionPoint afterLast = FunctionPoint.builder()
                    .t(last.getT())
                    .systemX(2 * last.getOriginalX() - preLast.getOriginalX())
                    .systemY(2 * last.getOriginalY() - preLast.getOriginalY())
                    .systemZ(2 * last.getOriginalZ() - preLast.getOriginalZ())
                    .originalX(2 * last.getOriginalX() - preLast.getOriginalX())
                    .originalY(2 * last.getOriginalY() - preLast.getOriginalY())
                    .originalZ(2 * last.getOriginalZ() - preLast.getOriginalZ())
                    .build();
            List<FunctionPoint> lastSplinePoints = calculateSplinePart(
                    beforePreLast,
                    preLast,
                    last,
                    afterLast);
            splinePoints.addAll(lastSplinePoints);
        }



        return splinePoints;
    }

    private List<FunctionPoint> calculateSplinePart(FunctionPoint p0,
                                                    FunctionPoint p1,
                                                    FunctionPoint p2,
                                                    FunctionPoint p3) {
        double[][] m0 = convertToMatrix(p0);
        double[][] m1 = convertToMatrix(p1);
        double[][] m2 = convertToMatrix(p2);
        double[][] m3 = convertToMatrix(p3);

        double t0 = 0;
        double t1 = calculateT(t0, m0[0], m1[0]);
        double t2 = calculateT(t1, m1[0], m2[0]);
        double t3 = calculateT(t2, m2[0], m3[0]);

        List<FunctionPoint> splinePoints = new ArrayList<>();
        for (double t = t1; t < t2; t += ((t2 - t1) / POINTS_NUMBER)) {
            double[][] a1 = sum(multiplyByNumber(m0, (t1 - t) / (t1 - t0)), multiplyByNumber(m1, (t - t0) / (t1 - t0)));
            double[][] a2 = sum(multiplyByNumber(m1, (t2 - t) / (t2 - t1)), multiplyByNumber(m2, (t - t1) / (t2 - t1)));
            double[][] a3 = sum(multiplyByNumber(m2, (t3 - t) / (t3 - t2)), multiplyByNumber(m3, (t - t2) / (t3 - t2)));

            double[][] b1 = sum(multiplyByNumber(a1, (t2 - t) / (t2 - t0)), multiplyByNumber(a2, (t - t0) / (t2 - t0)));
            double[][] b2 = sum(multiplyByNumber(a2, (t3 - t) / (t3 - t1)), multiplyByNumber(a3, (t - t1) / (t3 - t1)));

            double[][] c = sum(multiplyByNumber(b1, (t2 - t) / (t2 - t1)), multiplyByNumber(b2, (t - t1) / (t2 - t1)));

            splinePoints.add(FunctionPoint.builder()
                    .t(p1.getT())
                    .systemX((float) c[0][0])
                    .systemY((float) c[0][1])
                    .systemZ((float) c[0][2])
                    .originalX((float) c[0][0])
                    .originalY((float) c[0][1])
                    .originalZ((float) c[0][2])
                    .build());
        }

        return splinePoints;
    }

    private double[][] convertToMatrix(FunctionPoint point) {
        return new double[][]{
                {point.getOriginalX(), point.getOriginalY(), point.getOriginalZ()}
        };
    }

    private double calculateT(double tPrev, double[] v1, double[] v2) {
        double distance = Math.pow(
                Math.pow(v2[0] - v1[0], 2) +
                        Math.pow(v2[1] - v1[1], 2) +
                        Math.pow(v2[2] - v1[2], 2),
                0.5);
        return Math.pow(distance, KNOT_PARAMETRIZATION_POWER) + tPrev;
    }

}
