package ru.hse.paramfunc.storage;

import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.selection.SelectionListener;

import java.util.ArrayList;
import java.util.List;

public class FunctionValueStorage {

    // Singleton
    private static FunctionValueStorage instance = new FunctionValueStorage();

    private List<FunctionPoint> allPoints;
    private List<FunctionPoint> selectedPoints;

    private List<SelectionListener> listeners;

    private FunctionValueStorage() {
        this.allPoints = new ArrayList<>();
        this.selectedPoints = new ArrayList<>();
        this.listeners = new ArrayList<>();
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
        if (this.allPoints.size() > 50) {
            this.selectedPoints = this.allPoints.subList(0, 50);
        } else {
            this.selectedPoints = List.copyOf(this.allPoints);
        }
        this.notifyAllListeners();
    }

    public void setSelectedPoints(List<FunctionPoint> points) {
        this.selectedPoints = List.copyOf(points);
        this.notifyAllListeners();
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
        this.selectedPoints.clear();
    }

    public void addListener(SelectionListener listener) {
        if(!this.listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void notifyAllListeners() {
        listeners.forEach(listener -> listener.receive(List.copyOf(this.selectedPoints)));
    }

}
