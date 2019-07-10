package util;

import parser.ParseException;
import parser.PidginParser;
import parser.parsetree.Expression;
import parser.parsetree.Program;

import java.io.*;

public class ParseAdapter {


    /**
     * Instead of calling methods in the JavaCC (or maybe SableCC) generated
     * parser directly. We use this class as a kind of Stub class to isolate us from
     * direct dependency on the generated parser.
     * <p>
     * In theory this means you should be able to replace the parser
     * with another implementation by editing these methods, and without
     * changing the main implementation for the rest of the compiler.
     * <p>
     * It also has the benefit of not mixing in utility methods with
     * the generated code. We can put different methods for calling the parser
     * (with a file, an inputstream, a String ect. in here).
     * <p>
     * Note: Actually, there is a dependency on the ParseException class generated
     * by JavaCC. To really get "plugability" we should not have this dependency.
     *
     * @author kdvolder
     */
    /**
     * Read input from a File and parse it into an AST representation.
     *
     * @throws IOException for IO-related failures like "file not found" or permission issues
     * @throws ParseException fhen parsing fails
     */
    public static Program parse(File file) throws IOException, ParseException {
        try (FileReader input = new FileReader(file)) {
            return parse(input);
        } //No matter what happens, always close the file!
    }

    /**
     * Read input from a java.io.Reader and parse it into an AST. It is the
     * caller's responsibility to close the Reader.
     *
     * @throws ParseException when parsing fails
     */
    private static Program parse(Reader input) throws ParseException {
        PidginParser parser = new PidginParser(input);
        return parser.Program();
    }

    /**
     * Read input directly from a String and parse it into an AST.
     *
     * @throws ParseException when parsing fails
     */
    public static Program parse(String inputText) throws ParseException {
        return parse(new StringReader(inputText));
    }

    /**
     * Normally we don't need to parse just expressions by themselves. But this
     * is a convenience method, used by the unit tests for creating error messages.
     *
     * @throws ParseException when parsing fails
     */
    public static Expression parseExp(String exp) throws ParseException {
        PidginParser parser = new PidginParser(new StringReader(exp));
        return parser.Expression();
    }

    /**
     * Pretty print an AST node and return the result as a String.
     */
    public static String unparse(Expression node) {
        return node.toString();
        // This assumes that toString on AST nodes is appropriately implemented.
        // If you decided to generate/implement your own AST classes rather than use
        // the ones we provided for you, you may need to implement this unparse method
        // differently.
    }


}
