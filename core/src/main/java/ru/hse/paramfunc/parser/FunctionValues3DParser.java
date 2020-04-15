package ru.hse.paramfunc.parser;

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
        points.forEach(System.out::println);
        FunctionValueStorage.getInstance().addAll(points);
    }

}
