package ru.hse.paramfunc.engine;

import javafx.scene.Group;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.element.FunctionHolder;
import ru.hse.paramfunc.storage.FunctionStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PointsGroup extends Group {

    private static final List<FunctionPointsGroup> functionPointsGroups;

    static {
        functionPointsGroups = new ArrayList<>();
    }

    public void update() {
        List<Function> functions = FunctionStorage.getInstance().getFunctions();
        List<FunctionPointsGroup> updatedGroups = new ArrayList<>();
        for (Function function : functions) {
            FunctionPointsGroup functionPointsGroup = functionPointsGroups.stream()
                    .filter(g -> g.getFunctionHolder().getFunction().equals(function))
                    .findAny()
                    .orElseGet(() -> new FunctionPointsGroup(new FunctionHolder(function)));
            updatedGroups.add(functionPointsGroup);
        }
        //Нормализуем все точки
        Map<FunctionHolder, List<FunctionPoint>> valuesPointsMap = updatedGroups.stream()
                .collect(Collectors.toMap(
                        FunctionPointsGroup::getFunctionHolder,
                        g -> g.getFunctionHolder().getFunction().getSelectedPoints()
                ));
        Map<FunctionHolder, List<FunctionPoint>> splinePointsMap = updatedGroups.stream()
                .collect(Collectors.toMap(
                        FunctionPointsGroup::getFunctionHolder,
                        g -> g.getFunctionHolder().getFunction().getSplinePoints()
                ));
        normalizePoints(
                valuesPointsMap.values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()),
                splinePointsMap.values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
        for (FunctionPointsGroup updatedGroup : updatedGroups) {
            updatedGroup.setValuePoints(valuesPointsMap.get(updatedGroup.getFunctionHolder()));
            updatedGroup.setSplinePoints(splinePointsMap.get(updatedGroup.getFunctionHolder()));
            updatedGroup.update();
        }
        //Уничтожаем старые группы
        functionPointsGroups.stream()
                .filter(g -> !updatedGroups.contains(g))
                .forEach(FunctionPointsGroup::destroy);
        functionPointsGroups.clear();
        functionPointsGroups.addAll(updatedGroups);

        super.getChildren().clear();
        super.getChildren().addAll(functionPointsGroups);
    }

    public FunctionHolder getFunctionHolderByFunction(Function function) {
        return functionPointsGroups.stream()
                .filter(g -> g.getFunctionHolder().getFunction().equals(function))
                .findFirst()
                .map(FunctionPointsGroup::getFunctionHolder)
                .orElse(null);
    }

    public List<FunctionHolder> getAllFunctionHolders() {
        return functionPointsGroups.stream()
                .map(FunctionPointsGroup::getFunctionHolder)
                .collect(Collectors.toList());
    }

    private void normalizePoints(List<FunctionPoint> points, List<FunctionPoint> splinePoints) {
        Float minX = null, maxX = null;
        Float minY = null, maxY = null;
        Float minZ = null, maxZ = null;
        for (FunctionPoint point : points) {
            if (minX == null || point.getOriginalX() < minX) {
                minX = point.getOriginalX();
            }
            if (maxX == null || point.getOriginalX() > maxX) {
                maxX = point.getOriginalX();
            }
            if (minY == null || point.getOriginalY() < minY) {
                minY = point.getOriginalY();
            }
            if (maxY == null || point.getOriginalY() > maxY) {
                maxY = point.getOriginalY();
            }
            if (minZ == null || point.getOriginalZ() < minZ) {
                minZ = point.getOriginalZ();
            }
            if (maxZ == null || point.getOriginalZ() > maxZ) {
                maxZ = point.getOriginalZ();
            }
        }
        for (FunctionPoint point : points) {
            float x = (maxX - minX) != 0 ? (point.getOriginalX() - minX) / (maxX - minX) * 100 : 0;
            float y = (maxY - minY) != 0 ? (point.getOriginalY() - minY) / (maxY - minY) * 100 : 0;
            float z = (maxZ - minZ) != 0 ? (point.getOriginalZ() - minZ) / (maxZ - minZ) * 100 : 0;

            point.setSystemX(x);
            point.setSystemY(y);
            point.setSystemZ(z);
        }
        for (FunctionPoint point : splinePoints) {
            float x = (maxX - minX) != 0 ? (point.getOriginalX() - minX) / (maxX - minX) * 100 : 0;
            float y = (maxY - minY) != 0 ? (point.getOriginalY() - minY) / (maxY - minY) * 100 : 0;
            float z = (maxZ - minZ) != 0 ? (point.getOriginalZ() - minZ) / (maxZ - minZ) * 100 : 0;

            point.setSystemX(x);
            point.setSystemY(y);
            point.setSystemZ(z);
        }
    }

}
