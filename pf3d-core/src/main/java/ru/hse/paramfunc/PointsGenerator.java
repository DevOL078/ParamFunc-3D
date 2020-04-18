package ru.hse.paramfunc;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PointsGenerator {

    private static Random random = new Random();
    private final static int POINT_COUNT = 50;

    public static void main(String[] args) throws IOException {
        List<FunctionPoint> points = new ArrayList<>();

        for (int i = 0; i < POINT_COUNT; ++i) {
            int t = i + 1;
            float x = random.nextInt(100) + (float) random.nextDouble();
            float y = random.nextInt(100) + (float) random.nextDouble();
            float z = random.nextInt(100) + (float) random.nextDouble();

            points.add(new FunctionPoint(t, x, y, z));
        }

        List<String> strList = points.stream()
                .sorted(Comparator.comparing(FunctionPoint::getT))
                .map(p -> String.format("%d %f %f %f",
                        p.getT(),
                        p.getOriginalX(),
                        p.getOriginalY(),
                        p.getOriginalZ()))
                .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-SS");
        String filePath = String.format("./etc/test%s.txt", LocalDateTime.now().format(formatter));
        Files.write(Path.of(filePath), strList,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE);
    }

}
