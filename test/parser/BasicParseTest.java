package parser;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.ParseAdapter;

import java.util.Arrays;

public class BasicParseTest {

    public void parse(String text) {
        System.out.println("Parsing:");
        System.out.println(text);
        try {
            ParseAdapter.parse(text);
        } catch (ParseException e) {
            //Assertions.fail("Parsing failed at " + e.currentToken + ". Tokenimage is: " + Arrays.toString(e.tokenImage));
            Assertions.fail(e);
        }
    }

    private void parseFail(String text) {
        try {
            ParseAdapter.parse(text);
        } catch (ParseException e) {

        }
    }

    @Test
    public void testBasic() throws ParseException {
        parse("let x = pdg.forwardSlice(a) in x is empty");
    }
}
