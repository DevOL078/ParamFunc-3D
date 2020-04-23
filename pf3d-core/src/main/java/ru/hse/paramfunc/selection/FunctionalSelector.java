package ru.hse.paramfunc.selection;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.util.ArrayList;
import java.util.List;

public class FunctionalSelector implements PointSelector {
    @Override
    public List<FunctionPoint> selectPoints(List<FunctionPoint> allPoints, String selectionRule) {
        String changedExpression = selectionRule.replaceAll(" ", "");
        Function function = new SelectionFunction(changedExpression);
        List<FunctionPoint> selectedPoints = new ArrayList<>();
        int currentSelectedIndex = 0;
        //Чтобы как-то ограничить цикл и не ставить while(true)
        while(currentSelectedIndex < allPoints.size()) {
            int indexToSelect = function.calculate(currentSelectedIndex + 1) - 1;
            if(indexToSelect >= 0 && indexToSelect < allPoints.size()) {
                selectedPoints.add(allPoints.get(indexToSelect));
            } else {
                System.err.println("Point with index " + indexToSelect + " was not found");
                break;
            }

            currentSelectedIndex++;
        }
        return selectedPoints;
    }
}
