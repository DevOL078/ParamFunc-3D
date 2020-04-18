package ru.hse.paramfunc.parser;

import java.io.IOException;

public interface Parser {
    void parse(String filePath) throws IOException;
}
