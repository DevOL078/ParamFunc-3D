package ru.hse.paramfunc.parser;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.io.IOException;
import java.util.List;

public class ParseService {

    private static final ParseService instance = new ParseService();

    private ParseService() {
    }

    public static ParseService getInstance() {
        return instance;
    }

    public List<FunctionPoint> parse(String filePath) throws IOException {
        return new Function3DParser().parse(filePath);
    }

}
