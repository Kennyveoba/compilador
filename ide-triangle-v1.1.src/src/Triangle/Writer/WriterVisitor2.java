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
    private Writer2 writerHTML;

    public WriterVisitor2(Scanner scanner, Writer2 writerHTML) {
        this.scanner = scanner;
        this.writerHTML = writerHTML;
    }

    public void writeHTML(){
        currentToken = scanner.scan();
        while (currentToken.kind != Token.EOT) {
            switch (currentToken.kind) {
                
                case Token.CHARLITERAL:
                    writerHTML.writeColor(currentToken.spelling);
                    break;
                    
                case Token.FIN:
                    writerHTML.writeEndOfLine();
                    break;
                    
                case Token.INTLITERAL:
                    writerHTML.writeColor(currentToken.spelling);
                    break;
                
                
                case Token.IDENTIFIER:
                    writerHTML.writeNormal(currentToken.spelling);
                    break;
                
                case Token.TAB:
                    writerHTML.writeTab();
                    break;
                    
                case Token.COMMENT:
                    writerHTML.writeComment(currentToken.spelling);
                    break;
                    
                default:
                   
                    String[] reservedWords = {"array", "const", "do", "else", 
                        "end", "for", "from", "func", "if", "in", "let", "of", 
                        "package", "private", "proc", "rec", "record", "repeat", 
                        "skip", "times", "type", "until", "var", "when", "while"};
                    
                    //Valida si es una palabra reservada o no 
                    if (Arrays.asList(reservedWords).contains(currentToken.spelling)) {
                        writerHTML.writeReservedWord(currentToken.spelling);
                    } else {
                        writerHTML.writeNormal(currentToken.spelling);
}
                    break;
            }
            currentToken = scanner.scan();
        }
        writerHTML.TerminarHTML();
    }
}
