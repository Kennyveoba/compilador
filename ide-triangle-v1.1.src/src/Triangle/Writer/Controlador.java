package Triangle.Writer;

import Triangle.SyntacticAnalyzer.Scanner;
import Triangle.SyntacticAnalyzer.Token;
import java.util.Arrays;

public class Controlador {
    private final Scanner scanner;
    private final Writer2 writerHTML;
    private final static String[] RESERVED_WORDS = {"array", "const", "do", 
        "else", "end", "for", "from", "func", "if", "in", "let", "of", "package", 
        "private", "proc", "rec", "record", "repeat", "skip", "times", "type", 
        "until", "var", "when", "while"};

    public Controlador(Scanner scanner, Writer2 writerHTML) {
        this.scanner = scanner;
        this.writerHTML = writerHTML;
    }

    public void writeHTML(){
        Token currentToken = scanner.scan();
        while (currentToken.kind != Token.EOT) {
            switch (currentToken.kind) {
                case Token.IDENTIFIER:
                    writerHTML.write(currentToken.spelling);
                    break;
                case Token.INTLITERAL:
                    writerHTML.writeColor(currentToken.spelling);
                    break;
                case Token.CHARLITERAL:
                    writerHTML.writeColor(currentToken.spelling);
                    break;
                case Token.COMMENT:
                    writerHTML.writeComment(currentToken.spelling);
                    break;
                case Token.EOL:
                    writerHTML.writeEndOfLine();
                    break;
                case Token.TAB:
                    writerHTML.writeTab();
                    break;                
                default:
                    if (Arrays.asList(RESERVED_WORDS).contains(currentToken.spelling)) {
                        writerHTML.writeReservedWord(currentToken.spelling);
                    } else {
                        writerHTML.write(currentToken.spelling);
                    }
                    break;
            }
            currentToken = scanner.scan();
        }
        writerHTML.TerminarHTML();
    }
}