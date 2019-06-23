//From root:
//java -jar lib/JFlex.jar -d src/accrue/parser cup/PidginQL.jflex 
package accrue.parser;

import java_cup.runtime.Symbol;
import accrue.pdg.node.PDGNodeType;
import accrue.pdg.PDGEdgeType;

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
    "backwardSlice"              { return symbol(BACKWARD_SLICE); }
    "shortestPath"               { return symbol(SHORTEST_PATH); }
    "removeNodes"                { return symbol(REMOVE_NODES); }
    "removeEdges"                { return symbol(REMOVE_EDGES); }
    "selectNodes"                { return symbol(SELECT_NODES); }
    "selectEdges"                { return symbol(SELECT_EDGES); }    
    "forExpression"              { return symbol(FOR_EXPRESSION); }
    "forProcedure"               { return symbol(FOR_PROCEDURE); }
    "removeGuardedBy"            { return symbol(REMOVE_GUARDED_BY); }
    "removeGuardedByBool"        { return symbol(REMOVE_GUARDED_BY_BOOL); }
    "removeGuardedByPC"          { return symbol(REMOVE_GUARDED_BY_PC); }
    "findPCNodes"                { return symbol(FIND_PC_NODES); }
    "removeControlDeps" 	     { return symbol(REMOVE_GUARDED_BY_PC); }

    /* policy assertion */
    "is empty"                   { return symbol(IS_EMPTY); }
    
    /* cheats */
    "nodeByID"                   { return symbol(NODE_BY_ID); }
    "position"                   { return symbol(POSITION); }
    "nodesInContext"             { return symbol(NODES_IN_CONTEXT); }
    "restrict"                   { return symbol(RESTRICT); }

    /* node types */
    "LOCAL"                      { return symbol(NODE_TYPE, PDGNodeType.LOCAL); }
    "OUTPUT_NODE"                { return symbol(NODE_TYPE, PDGNodeType.OUTPUT); }
    "OTHER_EXPRESSION"           { return symbol(NODE_TYPE, PDGNodeType.OTHER_EXPRESSION); }
    "FORMAL_ASSIGNMENT"          { return symbol(NODE_TYPE, PDGNodeType.FORMAL_ASSIGNMENT); }
    "BASE_VALUE"                 { return symbol(NODE_TYPE, PDGNodeType.BASE_VALUE); }
    "EXIT_PC_SUMMARY"            { return symbol(NODE_TYPE, PDGNodeType.EXIT_PC_SUMMARY); }
    "ENTRY_PC_SUMMARY"           { return symbol(NODE_TYPE, PDGNodeType.ENTRY_PC_SUMMARY); }
    "BOOLEAN_TRUE_PC"            { return symbol(NODE_TYPE, PDGNodeType.BOOLEAN_TRUE_PC); }
    "BOOLEAN_FALSE_PC"           { return symbol(NODE_TYPE, PDGNodeType.BOOLEAN_FALSE_PC); }
    "PC_MERGE"                   { return symbol(NODE_TYPE, PDGNodeType.PC_MERGE); }
    "PC_OTHER"                   { return symbol(NODE_TYPE, PDGNodeType.PC_OTHER); }
    "FORMAL_SUMMARY"             { return symbol(NODE_TYPE, PDGNodeType.FORMAL_SUMMARY); }
    "THIS"                       { return symbol(NODE_TYPE, PDGNodeType.THIS); }
    "EXIT_SUMMARY"               { return symbol(NODE_TYPE, PDGNodeType.EXIT_SUMMARY); }
    "ABSTRACT_LOCATION"          { return symbol(NODE_TYPE, PDGNodeType.ABSTRACT_LOCATION); }
    "EXIT_PC_JOIN"               { return symbol(NODE_TYPE, PDGNodeType.EXIT_PC_JOIN); }
    "EXIT_ASSIGNMENT"            { return symbol(NODE_TYPE, PDGNodeType.EXIT_ASSIGNMENT); }
    
    /* edge types */
    "EXP"                        { return symbol(EDGE_TYPE, PDGEdgeType.EXP); }
    "COPY"                       { return symbol(EDGE_TYPE, PDGEdgeType.COPY); }
    "INPUT"                      { return symbol(EDGE_TYPE, PDGEdgeType.INPUT); }
    "OUTPUT_EDGE"                { return symbol(EDGE_TYPE, PDGEdgeType.OUTPUT); }
    "MERGE"                      { return symbol(EDGE_TYPE, PDGEdgeType.MERGE); }
    "IMPLICIT"                   { return symbol(EDGE_TYPE, PDGEdgeType.IMPLICIT); }
    "POINTER"                    { return symbol(EDGE_TYPE, PDGEdgeType.POINTER); }
    "TRUE"                       { return symbol(EDGE_TYPE, PDGEdgeType.TRUE); }
    "FALSE"                      { return symbol(EDGE_TYPE, PDGEdgeType.FALSE); }
    "MISSING"                    { return symbol(EDGE_TYPE, PDGEdgeType.MISSING); }
    "CONJUNCTION"                { return symbol(EDGE_TYPE, PDGEdgeType.CONJUNCTION); }
    "SWITCH"                     { return symbol(EDGE_TYPE, PDGEdgeType.SWITCH); }

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

