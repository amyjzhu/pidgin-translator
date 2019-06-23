package accrue.pdg.util;

/**
 * This class represents a position within a source file. It is used to record
 * where each AST node is located in a source file; this is used, for
 * example, for generating error messages.
 * 
 * Largely copied from polyglot.util.Position
 **/
public class Position {

    private String path;
    private String file;
    private String info;

    private int line;
    private int column;

    private int endLine;
    private int endColumn;

    private boolean compilerGenerated = false;

    // Position in characters from the beginning of the containing character
    // stream
    private int offset;
    private int endOffset;

    public static final int UNKNOWN = -1;
    public static final int END_UNUSED = -2;

    /** Get a compiler generated position. */
    public boolean isCompilerGenerated() {
        return compilerGenerated;
    }
    
    public Position(String path, String file, int line, int column, int endLine, int endColumn, int offset,
                    int endOffset) {
        this(path, file, line, column, endLine, endColumn, offset, endOffset, false);
    }

    public Position(String path, String file, int line, int column,
            int endLine, int endColumn, int offset, int endOffset,
            boolean compilerGenerated) {
        this.file = file;
        this.path = path;
        this.line = line;
        this.column = column;
        this.endLine = endLine;
        this.endColumn = endColumn;
        this.offset = offset;
        this.endOffset = endOffset;
        this.compilerGenerated = compilerGenerated;
    }

    public int line() {
        return line;
    }

    public int column() {
        return column;
    }

    public int endLine() {
        if (endLine == UNKNOWN || (line != UNKNOWN && endLine < line)) {
            return line;
        }
        return endLine;
    }

    public int endColumn() {
        if (endColumn == UNKNOWN
                || (column != UNKNOWN && endLine() == line() && endColumn < column)) {
            return column;
        }
        return endColumn;
    }

    public int offset() {
        return offset;
    }

    public int endOffset() {
        if (endOffset == UNKNOWN || (offset != UNKNOWN && endOffset < offset)) {
            return offset;
        }
        return endOffset;
    }

    public String file() {
        return file;
    }

    public String path() {
        return path;
    }

    public String nameAndLineString() {
        // Maybe we should use path here, if it isn't too long...
        String s = path;

        if (s == null || s.length() == 0) {
            s = file;
        }

        if (s == null) {
            s = "unknown file";
        }

        if (line != UNKNOWN) {
            s += ":" + line;
            if (endLine != line && endLine != UNKNOWN && endLine != END_UNUSED) {
                s += "-" + endLine;
            }
        }

        if (info != null) {
            s += " (" + info + ")";
        }

        return s;
    }

    @Override
    public String toString() {
        String s = path;

        if (s == null) {
            s = file;
        }

        if (s == null) {
            s = "unknown file";
        }

        if (line != UNKNOWN) {
            s += ":" + line;

            if (column != UNKNOWN) {
                s += "," + column;
                if (line == endLine && endColumn != UNKNOWN
                        && endColumn != END_UNUSED) {
                    s += "-" + endColumn;
                }
                if (line != endLine && endColumn != UNKNOWN
                        && endColumn != END_UNUSED) {
                    s += "-" + endLine + "," + endColumn;
                }
            }
        }

        if (info != null) {
            s += " (" + info + ")";
        }

        return s;
    }
}
