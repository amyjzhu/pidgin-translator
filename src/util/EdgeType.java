package util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum EdgeType {
    DEFAULT;

    static Map<String, EdgeType> typeMap = Arrays.stream(EdgeType.values()).collect(Collectors.toMap(EdgeType::name, p -> p));

    public static EdgeType from(String name) {
        return typeMap.get(name);
    }
}
