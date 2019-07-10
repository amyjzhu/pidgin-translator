package parser;


import util.ParseAdapter;

public class BasicParseTest {

    @Test
    public void testBasic() throws ParseException {
        ParseAdapter.parse("hello");
    }
}
