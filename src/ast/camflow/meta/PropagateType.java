package ast.camflow.meta;

import ast.camflow.check.Check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PropagateType {
    NEW,
    PROPAGATE,
    CHECK;

    private static Map<PropagateType, List<Check>> commandList;

    static void addToNew(Check node) {
        add(NEW, node);
    }

    static void addToPropagate(Check node) {
        add(PROPAGATE, node);
    }

    static void addToCheck(Check node) {
        add(CHECK, node);
    }

    private static void add(PropagateType type, Check node) {
        if (commandList == null) {
            commandList = new HashMap<>();
            commandList.put(NEW, new ArrayList<>());
            commandList.put(PROPAGATE, new ArrayList<>());
            commandList.put(CHECK, new ArrayList<>());
        }

        commandList.get(type).add(node);
    }

    public static List<Check> getNodes(PropagateType type) {
        return commandList.get(type);
    }
}
