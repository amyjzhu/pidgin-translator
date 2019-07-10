package util;

import parser.parsetree.Argument;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum NodeType implements Argument {
    DEFAULT;

    static Map<String, NodeType> typeMap = Arrays.stream(NodeType.values()).collect(Collectors.toMap(NodeType::name, p -> p));

    public static NodeType from(String name) {
        return typeMap.get(name);
    }
}
