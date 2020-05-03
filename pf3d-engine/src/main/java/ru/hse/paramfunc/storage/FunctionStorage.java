package ru.hse.paramfunc.storage;

import ru.hse.paramFunc.interpolation.SplineProcessor;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.listener.Listener;
import ru.hse.paramfunc.listener.Notifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionStorage implements Notifier {

    private static final FunctionStorage instance = new FunctionStorage();
    private final static int DEFAULT_SPLINE_POINTS_NUMBER = 10;

    private final List<Function> functionList;

    private FunctionStorage() {
        functionList = new ArrayList<>();
    }

    public static FunctionStorage getInstance() {
        return instance;
    }

    public void addFunction(Function function) {
        functionList.add(function);
        SplineProcessor.calculateAndSaveSpline(function, DEFAULT_SPLINE_POINTS_NUMBER);
        notifyListeners();
    }

    public void removeFunction(Function function) {
        functionList.remove(function);
        notifyListeners();
    }

    public void setSelectedPoints(Function function, List<FunctionPoint> functionPoints) {
        function.setSelectedPoints(functionPoints);
        notifyListeners();
    }

    public List<Function> getFunctions() {
        return List.copyOf(functionList);
    }

    public int getDefaultSplinePointsNumber() {
        return DEFAULT_SPLINE_POINTS_NUMBER;
    }

    public void updateSpline(Function function, int pointsNumber) {
        SplineProcessor.calculateAndSaveSpline(function, pointsNumber);
        notifyListeners();
    }

    @Override
    public void notifyListeners() {
        // Оповещаем подписчиков в обратном порядке, чтобы контроллер узнал о событии последним
        List<Listener> copyListeners = new ArrayList<>(listeners);
        Collections.reverse(copyListeners);
        copyListeners.forEach(Listener::receive);
    }

}