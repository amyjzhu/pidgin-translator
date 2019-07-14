package parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.ParseAdapter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ExampleTests {

    public static final String TESTS_FOLDER = "test/programs/";

    @Test
    public void runall() {
        File folder = new File(TESTS_FOLDER);
        File[] files = folder.listFiles();
        if (files != null) {
            Arrays.stream(files).forEach(f -> {
                try {
                    ParseAdapter.parse(new File(TESTS_FOLDER + f.getName()));
                } catch (IOException e) {
                    Assertions.fail();
                } catch (ParseException e) {
                    Assertions.fail(e);
                }
            });
        } else {
            System.out.println("No tests run.");
        }
    }
}
