package ru.hse.paramfunc.contract;

import javafx.scene.input.MouseEvent;
import ru.hse.paramfunc.domain.FunctionPoint;

public interface MouseEventListener {

    void receive(MouseEvent event, FunctionPoint target);

}
