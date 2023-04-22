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

    // literals, identifiers, operators...
    INTLITERAL  = 0,
    CHARLITERAL = 1,
    IDENTIFIER  = 2,
    OPERATOR    = 3,

    // reserved words - must be in alphabetical order...
    ARRAY     = 4,
    CONST     = 5,
    DO        = 6,
    ELSE      = 7,
    END       = 8,
    FUNC      = 9,
    IF        = 10,
    IN        = 11,
    LET       = 12,
    OF        = 13,
    PROC      = 14,
    RECORD    = 15,
    REPEAT    = 16,
    THEN      = 17,
    TYPE      = 18,
    UNTIL     = 19, 
    VAR       = 20,
    WHILE     = 21,

    // punctuation...
    DOT       = 22,
    COLON     = 23,
    SEMICOLON = 24,
    COMMA     = 25,
    BECOMES   = 26,
    IS        = 27,

    // brackets...
    LPAREN    = 28,
    RPAREN    = 29,
    LBRACKET  = 30,
    RBRACKET  = 31,
    LCURLY    = 32,
    RCURLY    = 33,

    // special tokens...
    ERROR     = 34,
    BAR     = 35;

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
    "func",
    "if",
    "in",
    "let",
    "of",
    "proc",
    "record",
    "repeat",
    "then",
    "type",
    "until", 
    "var",
    "while",
    ".",
    ":",
    ";",
    ",",
    ":=",
    "~",
    "(",
    ")",
    "[",
    "]",
    "{",
    "}",
    "<error>",
    "|"
  };

  private final static int  firstReservedWord = Token.ARRAY,
                lastReservedWord  = Token.WHILE;

}