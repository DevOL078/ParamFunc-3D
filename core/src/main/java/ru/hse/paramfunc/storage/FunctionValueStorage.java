package ru.hse.paramfunc.storage;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.util.ArrayList;
import java.util.List;

public class FunctionValueStorage {

    // Singleton
    private static FunctionValueStorage instance = new FunctionValueStorage();

    private List<FunctionPoint> allPoints;
    private List<FunctionPoint> selectedPoints;

    private FunctionValueStorage() {
        this.allPoints = new ArrayList<>();
        this.selectedPoints = new ArrayList<>();
    }

    public static FunctionValueStorage getInstance() {
        return instance;
    }

    public List<FunctionPoint> getAllPoints() {
        return List.copyOf(this.allPoints);
    }

    public List<FunctionPoint> getSelectedPoints() {
        return List.copyOf(this.selectedPoints);
    }

    public void setAllPoints(List<FunctionPoint> points) {
        this.allPoints = List.copyOf(points);
    }

    public void setSelectedPoints(List<FunctionPoint> points) {
        this.selectedPoints = List.copyOf(points);
    }

//    public void addAll(FunctionPoint... points) {
//        this.allPoints.addAll(List.of(points));
//    }
//
//    public void addAll(Collection<FunctionPoint> points) {
//        this.allPoints.addAll(points);
//    }

    public void reset() {
        this.allPoints.clear();
    }

}
