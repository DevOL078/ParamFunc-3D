package ru.hse.paramfunc.storage;

import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.selection.SelectionListener;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionValueStorage {

    // Singleton
    private static FunctionValueStorage instance = new FunctionValueStorage();

    private static final Path CUMULATIVE_FILE_PATH = Paths.get("./cumulative-points.txt");
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

    public List<FunctionPoint> getSplinePointsForSelectedPoints() {
        List<FunctionPoint> sortedSelectedPoints = this.selectedPoints.stream()
                .sorted(Comparator.comparing(FunctionPoint::getT))
                .collect(Collectors.toList());
        if(sortedSelectedPoints.size() > 1) {
            int firstT = sortedSelectedPoints.get(0).getT();
            int lastT = sortedSelectedPoints.get(sortedSelectedPoints.size() - 1).getT();
            try {
                List<FunctionPoint> splinePoints = Files.lines(CUMULATIVE_FILE_PATH)
                        .filter(s -> {
                            String[] parts = s.split(" ");
                            String tStr = parts[0];
                            if (tStr.contains("s")) {
                                int t = Integer.parseInt(tStr.replaceAll("s", ""));
                                return t >= firstT && t < lastT;
                            }
                            return false;
                        })
                        .map(s -> {
                            String[] parts = s.split(" ");
                            int t = Integer.parseInt(parts[0].replaceAll("s", ""));
                            float x = Float.parseFloat(parts[1]);
                            float y = Float.parseFloat(parts[2]);
                            float z = Float.parseFloat(parts[3]);
                            return FunctionPoint.builder()
                                    .t(t)
                                    .originalX(x)
                                    .originalY(y)
                                    .originalZ(z)
                                    .systemX(x)
                                    .systemY(y)
                                    .systemZ(z)
                                    .build();
                        })
                        .collect(Collectors.toList());
                return splinePoints;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return Collections.emptyList();
        }

        return null;
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

    public void createCumulativeFile(List<FunctionPoint> allPoints, List<FunctionPoint> splinePoints) {
        List<String> lines = new ArrayList<>();
        for (FunctionPoint point : allPoints) {
            String pointStr = getPointStr(point);
            lines.add(pointStr);
            List<FunctionPoint> splinePointsFiltered = splinePoints.stream()
                    .filter(p -> p.getT() == point.getT())
                    .collect(Collectors.toList());
            lines.addAll(splinePointsFiltered.stream()
                    .map(this::getSplinePointStr)
                    .collect(Collectors.toList()));
        }
        try {
            Files.write(CUMULATIVE_FILE_PATH, lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPointStr(FunctionPoint point) {
        return String.format("%d %f %f %f", point.getT(), point.getOriginalX(), point.getOriginalY(), point.getOriginalZ());
    }

    private String getSplinePointStr(FunctionPoint point) {
        return String.format("%ds %f %f %f", point.getT(), point.getOriginalX(), point.getOriginalY(), point.getOriginalZ());
    }

}
