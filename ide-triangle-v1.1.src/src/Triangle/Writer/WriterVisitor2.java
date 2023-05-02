package Triangle.Writer;

import Triangle.SyntacticAnalyzer.Scanner;
import Triangle.SyntacticAnalyzer.Token;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;

public class WriterVisitor2 {
    private Scanner scanner;
    private Token currentToken;
    private Writer2 Writer2;

    public WriterVisitor2(Scanner scanner, Writer2 writerHTML) {
        this.scanner = scanner;
        this.Writer2 = Writer2;
    }

    public void writeHTML(){
        currentToken = scanner.scan();
        while (currentToken.kind != Token.EOT) {
            switch (currentToken.kind) {
                
                case Token.CHARLITERAL:
                    Writer2.writeColor(currentToken.spelling);
                    break;
                    
                case Token.FIN:
                    Writer2.writeEndOfLine();
                    break;
                    
                case Token.INTLITERAL:
                    Writer2.writeColor(currentToken.spelling);
                    break;
                
                
                case Token.IDENTIFIER:
                    Writer2.write(currentToken.spelling);
                    break;
                
                case Token.TAB:
                    Writer2.writeTab();
                    break;
                    
                case Token.COMMENT:
                    Writer2.writeComment(currentToken.spelling);
                    break;
                    
                default:
                   
                    String[] reservedWords = {"array", "const", "do", "else", 
                        "end", "for", "from", "func", "if", "in", "let", "of", 
                        "package", "private", "proc", "rec", "record", "repeat", 
                        "skip", "times", "type", "until", "var", "when", "while"};
                    
                    //Valida si es una palabra reservada o no 
                    if (Arrays.asList(reservedWords).contains(currentToken.spelling)) {
                        Writer2.writeReservedWord(currentToken.spelling);
                    } else {
                        Writer2.write(currentToken.spelling);
}
                    break;
            }
            currentToken = scanner.scan();
        }
        Writer2.TerminarHTML();
    }
}
