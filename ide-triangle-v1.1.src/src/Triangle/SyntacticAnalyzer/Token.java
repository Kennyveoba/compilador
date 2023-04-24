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
    FOR       = 9,
    FUNC      = 10,
    IF        = 11,
    IN        = 12,
    LET       = 13,
    OF        = 14,
    PRIVATE   = 15,
    PROC      = 16,
    REC       = 17,
    RECORD    = 18,
    REPEAT    = 19,
    SKIP      = 20,
    THEN      = 21,
    TIMES     = 22,
    TYPE      = 23,
    UNTIL     = 24, 
    VAR       = 25,
    WHEN      = 26,
    WHILE     = 27,
          
    // punctuation...
    DOT       = 28,
    COLON     = 29,
    SEMICOLON = 30,
    COMMA     = 31,
    BECOMES   = 32,
    IS        = 33,
    BAR       = 34,
    DOLLAR    = 35,
          
    // brackets...
    LPAREN    = 36,
    RPAREN    = 37,
    LBRACKET  = 38,
    RBRACKET  = 39,
    LCURLY    = 40,
    RCURLY    = 41,
          
    // special tokens...  
    EOT       = 42,
    ERROR     = 43;


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
    "when",
    "while",
    ".",
    ":",
    ";",
    ",",
    ":=",
    "~",
    "|" ,
    "$" ,
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