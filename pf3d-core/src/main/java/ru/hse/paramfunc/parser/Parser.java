package ru.hse.paramfunc.parser;

import ru.hse.paramfunc.domain.FunctionPoint;

import java.io.IOException;
import java.util.List;

public interface Parser {
    List<FunctionPoint> parse(String filePath) throws IOException;
}
