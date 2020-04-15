package ru.hse.paramfunc.parser;

import lombok.Data;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.storage.FunctionValueStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FunctionValues3DParser implements Parser {

    private final static FunctionValues3DParser instance = new FunctionValues3DParser();

    private FunctionValues3DParser() {
    }

    public static FunctionValues3DParser getInstance() {
        return instance;
    }

    @Override
    public void parse(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        List<FunctionPoint> points = new ArrayList<>();
        Files.lines(path)
                .forEach(s -> {
                    String[] parts = s.split(" ");
                    if (parts.length != 4) {
                        throw new IllegalStateException("Illegal file format");
                    }

                    int t = Integer.parseInt(parts[0]);
                    float x = Float.parseFloat(parts[1]);
                    float y = Float.parseFloat(parts[2]);
                    float z = Float.parseFloat(parts[3]);

                    NormalizePoint normalizePoint = new NormalizePoint(x, y, z);

                    points.add(FunctionPoint.builder()
                            .t(t)
                            .systemX(x)
                            .systemY(y)
                            .systemZ(z)
                            .originalX(x)
                            .originalY(y)
                            .originalZ(z)
                            .build());
                });
        normalizePoints(points);
        points.forEach(System.out::println);
        FunctionValueStorage.getInstance().addAll(points);
    }

    private void normalizePoints(List<FunctionPoint> points) {
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
    }

    @Data
    private static class NormalizePoint {
        private float x;
        private float y;
        private float z;

        public NormalizePoint(float x, float y, float z) {
            float length = (float) Math.sqrt(x * x + y * y + z * z);
            this.x = x / length;
            this.y = y / length;
            this.z = z / length;
        }
    }

}
