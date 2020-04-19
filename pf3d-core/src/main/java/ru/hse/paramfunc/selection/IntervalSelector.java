package ru.hse.paramfunc.selection;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IntervalSelector implements PointSelector {
    @Override
    public List<FunctionPoint> selectPoints(List<FunctionPoint> allPoints, String selectionRule) {
        if (selectionRule == null) {
            throw new IllegalStateException("selectionRule is null");
        }

        String rule = selectionRule.replaceAll(" ", "");
        String[] intervals = rule.split(",");
        List<FunctionPoint> selectedPoints = new ArrayList<>();
        for (String interval : intervals) {
            if (interval.contains("-")) {
                String[] intervalEnds = interval.split("-");
                int intervalStart = Integer.parseInt(intervalEnds[0]);
                int intervalEnd = Integer.parseInt(intervalEnds[1]);
                if (intervalStart > intervalEnd) {
                    throw new IllegalStateException("Invalid interval: " + interval);
                }
                List<FunctionPoint> filteredPoints = allPoints.stream()
                        .filter(p -> p.getT() >= intervalStart && p.getT() <= intervalEnd)
                        .collect(Collectors.toList());
                if(filteredPoints.isEmpty()) {
                    throw new IllegalStateException("Invalid interval: " + interval);
                }
                selectedPoints.addAll(filteredPoints);
            } else {
                int t = Integer.parseInt(interval);
                FunctionPoint point = allPoints.stream()
                        .filter(p -> p.getT() == t)
                        .findAny()
                        .orElseThrow(() -> new IllegalStateException("Point with t equals " + t + " was not found"));
                selectedPoints.add(point);
            }
        }

        return selectedPoints;
    }
}
