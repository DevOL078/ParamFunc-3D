package ru.hse.paramfunc.parser;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Function3DParser implements Parser {

    @Override
    public List<FunctionPoint> parse(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        List<FunctionPoint> points = new ArrayList<>();
        Files.lines(path)
                .forEach(s -> {
                    s = s.replaceAll("[\"']", "");
                    String[] parts = s.split(" |,");
                    if (parts.length != 4) {
                        throw new IllegalStateException("Illegal file format");
                    }

                    int t = Integer.parseInt(parts[0]);
                    float x = Float.parseFloat(parts[1]);
                    float y = Float.parseFloat(parts[2]);
                    float z = Float.parseFloat(parts[3]);

                    FunctionPoint point = FunctionPoint.builder()
                            .t(t)
                            .systemX(x)
                            .systemY(y)
                            .systemZ(z)
                            .originalX(x)
                            .originalY(y)
                            .originalZ(z)
                            .build();
                    System.out.println("Loaded " + point);

                    points.add(point);
                });
        return points;
    }

}
