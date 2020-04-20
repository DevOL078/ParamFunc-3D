package ru.hse.paramfunc.selection;

import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.domain.enums.SelectionType;
import ru.hse.paramfunc.storage.FunctionValueStorage;

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

    public static List<FunctionPoint> selectPoints(SelectionType selectionType, String rule) {
        List<FunctionPoint> selectedPoints;
        try {
            switch (selectionType) {
                case INTERVAL: {
                    selectedPoints = selectorMap.get(selectionType)
                            .selectPoints(FunctionValueStorage.getInstance().getAllPoints(), rule);
                    break;
                }
                default: {
                    throw new UnsupportedOperationException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Selection error");
        }

        return selectedPoints.stream()
                .sorted(Comparator.comparing(FunctionPoint::getT))
                .collect(Collectors.toList());
    }

}
