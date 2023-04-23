/*
 * @(#)Token.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle.SyntacticAnalyzer;


final class Token extends Object {

  protected int kind;
  protected String spelling;
  protected SourcePosition position;

  public Token(int kind, String spelling, SourcePosition position) {

    if (kind == Token.IDENTIFIER) {
      int currentKind = firstReservedWord;
      boolean searching = true;

      while (searching) {
        int comparison = tokenTable[currentKind].compareTo(spelling);
        if (comparison == 0) {
          this.kind = currentKind;
          searching = false;
        } else if (comparison > 0 || currentKind == lastReservedWord) {
          this.kind = Token.IDENTIFIER;
          searching = false;
        } else {
          currentKind ++;
        }
      }
    } else
      this.kind = kind;

    this.spelling = spelling;
    this.position = position;

  }

  public static String spell (int kind) {
    return tokenTable[kind];
  }

  public String toString() {
    return "Kind=" + kind + ", spelling=" + spelling +
      ", position=" + position;
  }

  // Token classes...

  public static final int

    INTLITERAL  = 0,
    CHARLITERAL = 1,
    IDENTIFIER  = 2,
    OPERATOR    = 3,
    ARRAY     = 4,
    CONST     = 5,
    DO        = 6,
    ELSE      = 7,
    END       = 8,   
    FOR       = 9,
    FUNC      = 10,
    IF        = 11,
    IN        = 12,
    LET       = 13,
    OF        = 14,
    PACKAGE   = 15,
    PRIVATE   = 16,
    PROC      = 17,
    REC       = 18,
    RECORD    = 19,
    REPEAT    = 20,
    THEN      = 21,
    TIMES     = 22,
    TYPE      = 23,
    UNTIL     = 24, 
    VAR       = 25,
    WHEN      = 26,
    WHILE     = 27,
    DOT       = 28,
    COLON     = 29,
    SEMICOLON = 30,
    COMMA     = 31,
    BECOMES   = 32,
    IS        = 33,
    BAR       = 34,
    LPAREN    = 35,
    RPAREN    = 36,
    LBRACKET  = 37,
    RBRACKET  = 38,
    LCURLY    = 39,
    RCURLY    = 40,
    EOT       = 41,
    ERROR     = 42;


  private static String[] tokenTable = new String[] {
    "<int>",
    "<char>",
    "<identifier>",
    "<operator>",
    "array",
    "const",
    "do",
    "else",
    "end",
    "for",
    "func",
    "if",
    "in",
    "let",
    "of",
    "package",
    "private",
    "proc",
    "rec",
    "record",
    "repeat",
    "then",
    "times",
    "type",
    "until", 
    "var",
    "when",
    "while",
    ".",
    ":",
    ";",
    ",",
    ":=",
    "~",
    "|" ,
    "(",
    ")",
    "[",
    "]",
    "{",
    "}",
    "",
    "<error>"

  };

  private final static int	firstReservedWord = Token.ARRAY,
  				lastReservedWord  = Token.WHILE;

}
