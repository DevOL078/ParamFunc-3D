package ru.hse.paramfunc.selection;

import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.domain.enums.SelectionType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SelectionService {

    private static final Map<SelectionType, PointSelector> selectorMap;

    static {
        selectorMap = new HashMap<>();
        selectorMap.put(SelectionType.INTERVAL, new IntervalSelector());
        selectorMap.put(SelectionType.FUNCTIONAL, new FunctionalSelector());
    }

    public static List<FunctionPoint> selectPoints(Function function, SelectionType selectionType, String expression) {
        List<FunctionPoint> selectedPoints;
        try {
            selectedPoints = selectorMap.get(selectionType)
                    .selectPoints(function.getAllPoints(), expression);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Selection error: " + e.getMessage());
        }

        return selectedPoints.stream()
                .sorted(Comparator.comparing(FunctionPoint::getT))
                .collect(Collectors.toList());
    }

}
