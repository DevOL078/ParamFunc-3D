package ru.hse.paramfunc.storage;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FunctionValueStorage {

    // Singleton
    private static FunctionValueStorage instance = new FunctionValueStorage();

    private final List<FunctionPoint> functionPoints;

    private FunctionValueStorage() { this.functionPoints = new ArrayList<>(); }

    public static FunctionValueStorage getInstance() {
        return instance;
    }

    public void normalize() {
        // TODO
    }

    public List<FunctionPoint> getPoints() {
        return List.copyOf(this.functionPoints);
    }

    public void addAll(FunctionPoint... points) {
        this.functionPoints.addAll(List.of(points));
    }

    public void addAll(Collection<FunctionPoint> points) {
        this.functionPoints.addAll(points);
    }

    public void reset() {
        this.functionPoints.clear();
    }

}
