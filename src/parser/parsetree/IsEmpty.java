package parser.parsetree;

import java.util.List;

public class IsEmpty {

    List<Expression> toTest;

    public IsEmpty(List<Expression> toTest) {
        this.toTest = toTest;
    }

    public List<Expression> getToTest() {
        return toTest;
    }
}
