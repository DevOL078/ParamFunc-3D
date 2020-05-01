package ru.hse.paramFunc.interpolation;

import ru.hse.paramFunc.interpolation.spline.CatmullRomSpline;
import ru.hse.paramFunc.interpolation.spline.Spline;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.storage.FunctionFileProvider;

import java.util.List;

public class SplineProcessor {

    public static void calculateAndSaveSpline(Function function, int pointsNumber) {
        Spline spline = new CatmullRomSpline();
        List<FunctionPoint> splinePoints = spline.calculate(function.getAllPoints(), pointsNumber);
        FunctionFileProvider.saveTmpFile(function, splinePoints);
    }

}
