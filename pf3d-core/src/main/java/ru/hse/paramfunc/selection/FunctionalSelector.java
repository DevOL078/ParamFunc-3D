package ru.hse.paramfunc.selection;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.util.*;

public class FunctionalSelector implements PointSelector {
    @Override
    public List<FunctionPoint> selectPoints(List<FunctionPoint> allPoints, String selectionRule) {
        String changedExpression = selectionRule.replaceAll(" ", "");
        IFunction function = new SelectionFunction(changedExpression);
        Set<FunctionPoint> selectedPoints = new HashSet<>();
        int currentSelectedIndex = 0;

        while(currentSelectedIndex < allPoints.size()) {
            int tValueToSelect = function.calculate(currentSelectedIndex);
            Optional<FunctionPoint> pointToSelect = allPoints.stream()
                    .filter(p -> p.getT() == tValueToSelect)
                    .findFirst();
            pointToSelect.ifPresent(selectedPoints::add);

            currentSelectedIndex++;
        }
        return new ArrayList<>(selectedPoints);
    }
}
