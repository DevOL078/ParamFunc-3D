package ru.hse.paramfunc.selection;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.util.List;

public interface SelectionListener {

    void receive(List<FunctionPoint> selectedPoints);

}
