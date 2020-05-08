package ru.hse.paramfunc.selection;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunctionalSelector implements PointSelector {
    @Override
    public List<FunctionPoint> selectPoints(List<FunctionPoint> allPoints, String selectionRule) {
        String changedExpression = selectionRule.replaceAll(" ", "");
        IFunction function = new SelectionFunction(changedExpression);
        Set<FunctionPoint> selectedPoints = new HashSet<>();
        int currentSelectedIndex = 0;
        //Чтобы как-то ограничить цикл и не ставить while(true)
        while(currentSelectedIndex < allPoints.size()) {
            int indexToSelect = function.calculate(currentSelectedIndex);
            if(indexToSelect >= 0 && indexToSelect < allPoints.size()) {
                selectedPoints.add(allPoints.get(indexToSelect));
            }

            currentSelectedIndex++;
        }
        return new ArrayList<>(selectedPoints);
    }
}
