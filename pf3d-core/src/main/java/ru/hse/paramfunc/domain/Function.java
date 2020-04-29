package ru.hse.paramfunc.domain;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.hse.paramfunc.storage.FunctionFileProvider;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Function {

    @NonNull
    private String name;
    private List<FunctionPoint> allPoints;
    private List<FunctionPoint> selectedPoints;

    public List<FunctionPoint> getSplinePoints() {
        return FunctionFileProvider.getSplinePoints(this);
    }

}
