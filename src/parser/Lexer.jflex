//From root:
//java -jar lib/JFlex.jar -d src/accrue/parser cup/PidginQL.jflex
package accrue.parser;

import java_cup.runtime.Symbol;
import accrue.query.expression.NodeType;
import accrue.query.expression.EdgeType;

%%

%class PidginLexer
%cup
%implements sym
%line
%column
%state STRING

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int sym) {
    return new Symbol(sym, yyline+1, yycolumn+1);
  }

  private Symbol symbol(int sym, Object val) {
    return new Symbol(sym, yyline+1, yycolumn+1, val);
  }

  private void error(String message) {
    throw new RuntimeException("Invalid token at line " + (yyline+1) + ", column " + (yycolumn+1) + ": " + message);
  }
%}

new_line = \r|\n|\r\n;
white_space = {new_line} | [ \t\f]
int_literal = [0-9]+

input_character       = [^\r\n]
traditional_comment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
end_of_line_comment     = "//" {input_character}* {new_line}

digit = [0-9]
letter = [A-Za-z]

comment = {traditional_comment} | {end_of_line_comment}

%%

<YYINITIAL> {
    /* Query keywords */
    "let"                        { return symbol(LET); }
    "in"                         { return symbol(IN); }

    /* Expression keywords */
    "pdg"                        { return symbol(PDG); }
    "pgm"			             { return symbol(PDG); }
    "and"|"AND"                  { return symbol(AND); }
    "or"|"OR"                    { return symbol(OR); }

    /* Primitive Expression keywords */
    "forwardSlice"               { return symbol(FORWARD_SLICE); }

    /* policy assertion */
    "is empty"                   { return symbol(IS_EMPTY); }

    /* cheats */
    "nodeByID"                   { return symbol(NODE_BY_ID); }
    "position"                   { return symbol(POSITION); }
    "nodesInContext"             { return symbol(NODES_IN_CONTEXT); }
    "restrict"                   { return symbol(RESTRICT); }

    /* TODO -- add more! */
    /* node types */
    "DEFAULT_NODE"               { return symbol(NODE_TYPE, NodeType.DEFAULT); }


    /* edge types */
    "DEFAULT_EDGE"               { return symbol(EDGE_TYPE, EdgeType.DEFAULT); }


    /* literals */
    \"                           { string.setLength(0); yybegin(STRING); }
    {int_literal}                { return symbol(INT_LITERAL, yytext()); }


    /* separators */
    ","               { return symbol(COMMA); }
    "("               { return symbol(LPAR); }
    ")"               { return symbol(RPAR); }
    "."               { return symbol(DOT); }
    "="               { return symbol(EQUALS); }

    /* boolean conjuncts */
    "!"               { return symbol(BOOL_NOT); }
    "&&"              { return symbol(BOOL_AND); }
    "||"              { return symbol(BOOL_OR); }
    "["               { return symbol(LBRACK); }
    "]"               { return symbol(RBRACK); }

    /* comments */
    {white_space}                { /* ignore */ }
    {comment}                    { /* ignore */ }

    /* identifiers */
    {letter}({letter}|{digit})*  { return symbol(IDENTIFIER, yytext().intern()); }
}

<STRING> {
  \"                             { yybegin(YYINITIAL);
                                   return symbol(sym.STRING_LITERAL,
                                   string.toString()); }
  [^\n\r\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}


/* error fallback */
.|\n                             { error("Illegal character <"+ yytext()+">"); }

