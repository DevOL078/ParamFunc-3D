package ru.hse.paramFunc.interpolation.spline;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.util.List;

public interface Spline {
    List<FunctionPoint> calculate(List<FunctionPoint> controlPoints, int pointsNumber);
}
