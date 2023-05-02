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

 public final class Token extends Object {
 
   public int kind;
   public String spelling;
   public SourcePosition position;
 
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
           currentKind++;
         }
       }
     } else
       this.kind = kind;
 
     this.spelling = spelling;
     this.position = position;
 
   }
 
   public static String spell(int kind) {
     return tokenTable[kind];
   }
 
   public String toString() {
     return "Kind=" + kind + ", spelling=" + spelling +
         ", position=" + position;
   }
    
   // Se añadio las nuevas palabras reservadas y tokens tambien se elimino Begin
   // Kenny Vega 
 
   public static final int
 
    // literals, identifiers, operators...
    INTLITERAL = 0,
    CHARLITERAL = 1,
    IDENTIFIER = 2,
    OPERATOR = 3,

    // reserved words - must be in alphabetical order...
    ARRAY = 4,
    CONST = 5,
    DO = 6,
    ELSE = 7,
    END = 8,
    FOR = 9,
    FROM = 10,
    FUNC = 11,
    IF = 12,
    IN = 13,
    LET = 14,
    OF = 15,
    PACKAGE = 16,
    PRIVATE = 17,
    PROC = 18,
    REC = 19,
    RECORD = 20,       
    REPEAT = 21,
    SKIP = 22,
    THEN = 23,
    TIMES = 24,
    TYPE = 25,
    UNTIL = 26,
    VAR = 27,
    WHILE = 28,

    // punctuation...
    DOLLAR = 29,
    BAR = 30,
    DOTDOT = 31,
    DOT = 32,
    COLON = 33,
    SEMICOLON = 34,
    COMMA = 35,
    BECOMES = 36,
    IS = 37,

    // brackets...
    LPAREN = 38,
    RPAREN = 39,
    LBRACKET = 40,
    RBRACKET = 41,
    LCURLY = 42,
    RCURLY = 43,

    // special tokens...
    EOT = 44,
    ERROR = 45,
    COMMENT = 46,
    FIN = 47,
    TAB = 48;

 
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
    "from",
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
    "skip", 
    "then",
    "times",
    "type",
    "until", 
    "var",
    "while", 
    "$",
    "|", 
    "..", 
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
    "",
    "<error>",
    ""
   };
 
   private final static int firstReservedWord = Token.ARRAY,
       lastReservedWord = Token.WHILE;
 
 }
