package ru.hse.paramfunc.provider;

import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionPoint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionFileProvider {

    private static final String localFilePrefix = "./.local";

    public static List<FunctionPoint> getSplinePoints(Function function) {
        List<FunctionPoint> sortedSelectedPoints = function.getSelectedPoints().stream()
                .sorted(Comparator.comparing(FunctionPoint::getT))
                .collect(Collectors.toList());
        if (sortedSelectedPoints.size() > 1) {
            int firstT = sortedSelectedPoints.get(0).getT();
            int lastT = sortedSelectedPoints.get(sortedSelectedPoints.size() - 1).getT();
            try {
                Path filePath = getFilePath(function);
                return Files.lines(filePath)
                        .filter(s -> {
                            String[] parts = s.split(" ");
                            String tStr = parts[0];
                            if (tStr.contains("s")) {
                                int t = Integer.parseInt(tStr.replaceAll("s", ""));
                                return t >= firstT && t < lastT;
                            }
                            return false;
                        })
                        .map(s -> {
                            String[] parts = s.split(" ");
                            int t = Integer.parseInt(parts[0].replaceAll("s", ""));
                            float x = Float.parseFloat(parts[1]);
                            float y = Float.parseFloat(parts[2]);
                            float z = Float.parseFloat(parts[3]);
                            return FunctionPoint.builder()
                                    .t(t)
                                    .originalX(x)
                                    .originalY(y)
                                    .originalZ(z)
                                    .systemX(x)
                                    .systemY(y)
                                    .systemZ(z)
                                    .build();
                        })
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return Collections.emptyList();
        }

        return null;
    }

    public static void saveTmpFile(Function function, List<FunctionPoint> splinePoints) {
        List<String> lines = new ArrayList<>();
        for (FunctionPoint point : function.getAllPoints()) {
            String pointStr = getPointStr(point);
            lines.add(pointStr);
            List<FunctionPoint> splinePointsFiltered = splinePoints.stream()
                    .filter(p -> p.getT() == point.getT())
                    .collect(Collectors.toList());
            lines.addAll(splinePointsFiltered.stream()
                    .map(FunctionFileProvider::getSplinePointStr)
                    .collect(Collectors.toList()));
        }
        try {
            Path filePath = getFilePath(function);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTmpFile(Function function) {
        Path filePath = getFilePath(function);

        try {
            if(Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            System.err.println("File " + filePath + " haven't been deleted");
            e.printStackTrace();
        }
    }

    private static Path getFilePath(Function function) {
        return Paths.get(localFilePrefix, function.getName() + ".fun");
    }

    private static String getPointStr(FunctionPoint point) {
        return String.format("%d %f %f %f", point.getT(), point.getOriginalX(), point.getOriginalY(), point.getOriginalZ());
    }

    private static String getSplinePointStr(FunctionPoint point) {
        return String.format("%ds %f %f %f", point.getT(), point.getOriginalX(), point.getOriginalY(), point.getOriginalZ());
    }

}
