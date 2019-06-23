package test.unit;

import junit.framework.TestCase;

/**
 * Load a file and then compare the results.
 */
public class TestFileLoading extends TestCase {

    /**
     * Load a .json file generated by infoflow and check that it is read in
     * correctly by comparing the .dot files from this and infoflow
     */
    public void testLoad() {
        Helper.testAndLoad("test.programs.Loop");
    }
}
