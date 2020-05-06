package ru.hse.paramfunc.storage;

import ru.hse.paramFunc.interpolation.SplineProcessor;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.event.EventMediator;
import ru.hse.paramfunc.event.EventType;
import ru.hse.paramfunc.settings.AppSettings;

import java.util.ArrayList;
import java.util.List;

public class FunctionStorage {

    private static final FunctionStorage instance = new FunctionStorage();

    private final List<Function> functionList;

    private FunctionStorage() {
        functionList = new ArrayList<>();
    }

    public static FunctionStorage getInstance() {
        return instance;
    }

    public void addFunction(Function function) {
        functionList.add(function);
        SplineProcessor.calculateAndSaveSpline(function, AppSettings.interpolationPointsCountProperty().get());
        EventMediator.notifyAllListeners(EventType.FUNCTION_LIST_UPDATE);
    }

    public void removeFunction(Function function) {
        functionList.remove(function);
        EventMediator.notifyAllListeners(EventType.FUNCTION_LIST_UPDATE);
    }

    public void updateFunction(Function function) {
        EventMediator.notifyAllListeners(EventType.FUNCTION_LIST_UPDATE);
    }

    public List<Function> getFunctions() {
        return List.copyOf(functionList);
    }

    public void updateSpline(Function function, int pointsNumber) {
        SplineProcessor.calculateAndSaveSpline(function, pointsNumber);
        EventMediator.notifyAllListeners(EventType.FUNCTION_LIST_UPDATE);
    }

}
