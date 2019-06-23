package test.unit;

import junit.framework.TestCase;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.graph.PDGFactory;

public class LLVMJSON extends TestCase {
    public void testJSON() {
        try {
            PDGFactory.graphFromJSONFile("tests/llvm.guess.json", false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
