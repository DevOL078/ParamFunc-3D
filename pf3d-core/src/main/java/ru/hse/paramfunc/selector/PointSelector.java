package ru.hse.paramfunc.selector;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.util.List;

public interface PointSelector {

    List<FunctionPoint> selectPoints(List<FunctionPoint> allPoints, String selectionRule);

}
