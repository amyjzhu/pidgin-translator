package util;

import parser.parsetree.Argument;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum EdgeType implements Argument {
    DEFAULT;

    static Map<String, EdgeType> typeMap = Arrays.stream(EdgeType.values()).collect(Collectors.toMap(EdgeType::name, p -> p));

    public static EdgeType from(String name) {
        return typeMap.get(name);
    }
}
