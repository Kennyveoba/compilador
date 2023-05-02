/*
 * @(#)Parser.java                        2.1 2003/10/07
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

import Triangle.ErrorReporter;
import Triangle.AbstractSyntaxTrees.ActualParameter;
import Triangle.AbstractSyntaxTrees.ActualParameterSequence;
import Triangle.AbstractSyntaxTrees.ArrayAggregate;
import Triangle.AbstractSyntaxTrees.ArrayExpression;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoter;
import Triangle.AbstractSyntaxTrees.AssignCommand;
import Triangle.AbstractSyntaxTrees.BinaryExpression;
import Triangle.AbstractSyntaxTrees.CallCommand;
import Triangle.AbstractSyntaxTrees.CallExpression;
import Triangle.AbstractSyntaxTrees.CharacterExpression;
import Triangle.AbstractSyntaxTrees.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.Command;
import Triangle.AbstractSyntaxTrees.ConstActualParameter;
import Triangle.AbstractSyntaxTrees.ConstDeclaration;
import Triangle.AbstractSyntaxTrees.ConstFormalParameter;
import Triangle.AbstractSyntaxTrees.Declaration;
import Triangle.AbstractSyntaxTrees.DoUntilCommand;
import Triangle.AbstractSyntaxTrees.DoWhileCommand;
import Triangle.AbstractSyntaxTrees.DotVname;
import Triangle.AbstractSyntaxTrees.EmptyActualParameterSequence;
import Triangle.AbstractSyntaxTrees.EmptyCommand;
import Triangle.AbstractSyntaxTrees.EmptyFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Expression;
import Triangle.AbstractSyntaxTrees.FieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.ForCommand;
import Triangle.AbstractSyntaxTrees.ForInCommand;
import Triangle.AbstractSyntaxTrees.ForUntilCommand;
import Triangle.AbstractSyntaxTrees.ForWhileCommand;
import Triangle.AbstractSyntaxTrees.FormalParameter;
import Triangle.AbstractSyntaxTrees.FormalParameterSequence;
import Triangle.AbstractSyntaxTrees.FuncActualParameter;
import Triangle.AbstractSyntaxTrees.FuncDeclaration;
import Triangle.AbstractSyntaxTrees.FuncFormalParameter;
import Triangle.AbstractSyntaxTrees.Identifier;
import Triangle.AbstractSyntaxTrees.IfCommand;
import Triangle.AbstractSyntaxTrees.IfExpression;
import Triangle.AbstractSyntaxTrees.IntegerExpression;
import Triangle.AbstractSyntaxTrees.IntegerLiteral;
import Triangle.AbstractSyntaxTrees.LetCommand;
import Triangle.AbstractSyntaxTrees.LetExpression;
import Triangle.AbstractSyntaxTrees.LongIdentifier;
import Triangle.AbstractSyntaxTrees.LongIdentifierComplex;
import Triangle.AbstractSyntaxTrees.LongIdentifierSimple;
import Triangle.AbstractSyntaxTrees.MultipleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleArrayAggregate;
import Triangle.AbstractSyntaxTrees.MultipleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.MultipleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleRecordAggregate;
import Triangle.AbstractSyntaxTrees.Operator;
import Triangle.AbstractSyntaxTrees.PackageDeclaration;
import Triangle.AbstractSyntaxTrees.PackageIdentifier;
import Triangle.AbstractSyntaxTrees.PrivateDeclaration;
import Triangle.AbstractSyntaxTrees.ProcActualParameter;
import Triangle.AbstractSyntaxTrees.ProcDeclaration;
import Triangle.AbstractSyntaxTrees.ProcFormalParameter;
import Triangle.AbstractSyntaxTrees.Program;
import Triangle.AbstractSyntaxTrees.RECDeclaration;
import Triangle.AbstractSyntaxTrees.RecordAggregate;
import Triangle.AbstractSyntaxTrees.RecordExpression;
import Triangle.AbstractSyntaxTrees.RecordTypeDenoter;
import Triangle.AbstractSyntaxTrees.RepeatTimes;
import Triangle.AbstractSyntaxTrees.SequentialCommand;
import Triangle.AbstractSyntaxTrees.SequentialDeclaration;
import Triangle.AbstractSyntaxTrees.SequentialPackage;
import Triangle.AbstractSyntaxTrees.SimpleTypeDenoter;
import Triangle.AbstractSyntaxTrees.SimpleVname;
import Triangle.AbstractSyntaxTrees.SingleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.SingleArrayAggregate;
import Triangle.AbstractSyntaxTrees.SingleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.SingleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.SinglePackage;
import Triangle.AbstractSyntaxTrees.SingleRecordAggregate;
import Triangle.AbstractSyntaxTrees.SubscriptVname;
import Triangle.AbstractSyntaxTrees.TypeDeclaration;
import Triangle.AbstractSyntaxTrees.TypeDenoter;
import Triangle.AbstractSyntaxTrees.UnaryExpression;
import Triangle.AbstractSyntaxTrees.UntilCommand;
import Triangle.AbstractSyntaxTrees.VarActualParameter;
import Triangle.AbstractSyntaxTrees.VarDeclaration;
import Triangle.AbstractSyntaxTrees.VarFormalParameter;
import Triangle.AbstractSyntaxTrees.VariableInitializedDeclaration;
import Triangle.AbstractSyntaxTrees.Vname;
import Triangle.AbstractSyntaxTrees.VnameExpression;
import Triangle.AbstractSyntaxTrees.WhileCommand;

public class Parser {

  private Scanner lexicalAnalyser;
  private ErrorReporter errorReporter;
  private Token currentToken;
  private SourcePosition previousTokenPosition;

  public Parser(Scanner lexer, ErrorReporter reporter) {
    lexicalAnalyser = lexer;
    errorReporter = reporter;
    previousTokenPosition = new SourcePosition();
  }

  // accept checks whether the current token matches tokenExpected.
  // If so, fetches the next token.
  // If not, reports a syntactic error.

  void accept (int tokenExpected) throws SyntaxError {
      
    if (currentToken.kind == tokenExpected) {
      previousTokenPosition = currentToken.position;
      currentToken = lexicalAnalyser.scan();
    } else {
      syntacticError("\"%\" expected here", Token.spell(tokenExpected));
    }
  }

  void acceptIt() {
    previousTokenPosition = currentToken.position;
    currentToken = lexicalAnalyser.scan();
  }

  // start records the position of the start of a phrase.
  // This is defined to be the position of the first
  // character of the first token of the phrase.

  void start(SourcePosition position) {
    position.start = currentToken.position.start;
  }

  // finish records the position of the end of a phrase.
  // This is defined to be the position of the last
  // character of the last token of the phrase.

  void finish(SourcePosition position) {
    position.finish = previousTokenPosition.finish;
  }

  void syntacticError(String messageTemplate, String tokenQuoted) throws SyntaxError {
    SourcePosition pos = currentToken.position;
    errorReporter.reportError(messageTemplate, tokenQuoted, pos);
    throw (new SyntaxError());
  }

  ///////////////////////////////////////////////////////////////////////////////
  //
  // PROGRAMS
  //
  ///////////////////////////////////////////////////////////////////////////////

  /* 
  Modificar Program:
    Program ::= Package-Declaration * Command
  Sahid Rojas
  */
  public Program parseProgram() {

    Program programAST = null;

    previousTokenPosition.start = 0;
    previousTokenPosition.finish = 0;
    currentToken = lexicalAnalyser.scan();
    
    try {
      int packageCounter = 0;
      PackageDeclaration packageDeclarationAST = null;
      PackageDeclaration tempPackageDeclaration = null;
      while(currentToken.kind == Token.PACKAGE){
          tempPackageDeclaration = parsePackageDeclaration();
          if(currentToken.kind != Token.END){
              syntacticError("end expected at the end of package declaration ",
                      currentToken.spelling);
          }else{
              acceptIt();
              packageCounter++;
              if (packageCounter == 1)
                  packageDeclarationAST = tempPackageDeclaration;
              else
                  packageDeclarationAST = new SequentialPackage(packageDeclarationAST, tempPackageDeclaration, previousTokenPosition);
          }

      }
      Command cAST = parseCommand();
      programAST = new Program(packageDeclarationAST, cAST, previousTokenPosition);
      if (currentToken.kind != Token.EOT) {
        syntacticError("\"%\" not expected after end of program",
            currentToken.spelling);
      }
  }
  catch (SyntaxError s) { return null; }
  return programAST;
  }



///////////////////////////////////////////////////////////////////////////////
//
// Package Declaration - and Long Identifier
//
///////////////////////////////////////////////////////////////////////////////

/* 
  Agregar:
  PackageDeclaration ::= "package" Package-Identifier "~" 
           Declaration "end"
   
  Sahid Rojas
  */
PackageDeclaration parsePackageDeclaration() throws SyntaxError {  
  PackageDeclaration pacDecAST = null;
  SourcePosition pacDecPos = new SourcePosition();
  start(pacDecPos);
  accept(Token.PACKAGE);
  PackageIdentifier pacIDAST = parsePackageIdentifier();
  accept(Token.IS);
  Declaration dAST = parseDeclaration();
 
  finish(pacDecPos);
  pacDecAST = new SinglePackage(pacIDAST, dAST, pacDecPos);
  return pacDecAST;
}

/* Agregar:
   Package-Identifier ::= Identifier
   Sahid Rojas
*/
PackageIdentifier parsePackageIdentifier() throws SyntaxError {
  PackageIdentifier pacIDAST = null;
  SourcePosition pacIDPos = new SourcePosition();
  start(pacIDPos);
  if (currentToken.kind == Token.IDENTIFIER) {
    previousTokenPosition = currentToken.position;
    String spelling = currentToken.spelling;
    pacIDAST = new PackageIdentifier(spelling, previousTokenPosition);
    currentToken = lexicalAnalyser.scan();
  } else {
    pacIDAST = null;
    syntacticError("a Package-identifier expected here", currentToken.spelling);
  }
  finish(pacIDPos);
  return pacIDAST;
}


/*
 Agregar:
 parseLongIdentifier Long-Identifier ::= [ Package-Identifier "$" ] Identifier
 Sahid Rojas

*/
LongIdentifier parseLongIdentifier() throws SyntaxError {
  LongIdentifier LI = null;
  SourcePosition LIpos = new SourcePosition();
  start(LIpos);
  PackageIdentifier pacIDAST = null;
  Identifier tempAST = null;
  Identifier idAST = null;
  if (currentToken.kind == Token.IDENTIFIER) {
    previousTokenPosition = currentToken.position;
    String spelling = currentToken.spelling;
    tempAST = new Identifier(spelling, previousTokenPosition); // Create the Identifier before the $ in the LongIdentifier
    currentToken = lexicalAnalyser.scan();

    if (currentToken.kind == Token.DOLLAR) {
      acceptIt();        
      idAST = parseIdentifier();
      pacIDAST = new PackageIdentifier(tempAST); // Create the PackageIdentifier with the Identifier before the $ 
      finish(LIpos);
      LI = new LongIdentifierComplex(pacIDAST, idAST, LIpos);
    } else{ //This case is when is a longIdentifierSimple
      finish(LIpos);
      LI = new LongIdentifierSimple(tempAST, LIpos);
      return LI;

    }
  } else {
    LI = null;
    syntacticError("identifier expected here", "$");
  }
  
  return LI;
}

  ///////////////////////////////////////////////////////////////////////////////
  //
  // LITERALS
  //
  ///////////////////////////////////////////////////////////////////////////////

  // parseIntegerLiteral parses an integer-literal, and constructs
  // a leaf AST to represent it.

  IntegerLiteral parseIntegerLiteral() throws SyntaxError {
    IntegerLiteral IL = null;

    if (currentToken.kind == Token.INTLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      IL = new IntegerLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      IL = null;
      syntacticError("integer literal expected here", "");
    }
    return IL;
  }

  // parseCharacterLiteral parses a character-literal, and constructs a leaf
  // AST to represent it.

  CharacterLiteral parseCharacterLiteral() throws SyntaxError {
    CharacterLiteral CL = null;

    if (currentToken.kind == Token.CHARLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      CL = new CharacterLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      CL = null;
      syntacticError("character literal expected here", "");
    }
    return CL;
  }

  // parseIdentifier parses an identifier, and constructs a leaf AST to
  // represent it.

  Identifier parseIdentifier() throws SyntaxError {
    Identifier I = null;

    if (currentToken.kind == Token.IDENTIFIER) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      I = new Identifier(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      I = null;
      syntacticError("identifier expected here", "");
    }
    return I;
  }  
  
  // parseOperator parses an operator, and constructs a leaf AST to
  // represent it.

  Operator parseOperator() throws SyntaxError {
    Operator O = null;

    if (currentToken.kind == Token.OPERATOR) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      O = new Operator(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      O = null;
      syntacticError("operator expected here", "");
    }
    return O;
  }
  
  // Kenny Vega
  //Secuencial command del if
  // ("|" Expression "then" Command)*
  //     "else" Command "end"         (Resto del if) 
    Command parseRestoDelIf() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);
    if (currentToken.kind == Token.BAR) {
        acceptIt();
        Expression eAST = parseExpression();
        // Verificar si se encuentra la palabra clave "then" después de la expresión
        accept(Token.THEN);
        Command c1AST = parseCommand();
        Command c2AST = parseRestoDelIf();
        finish(commandPos);
        return new IfCommand(eAST, c1AST,c2AST, commandPos);
        
    }
    else if(currentToken.kind == Token.ELSE){
        acceptIt();
        commandAST = parseCommand();
        accept(Token.END);
        finish(commandPos);
        return commandAST;
    }
    else{
        // Si no se encuentra la palabra clave "ELSE o | ", se ha producido un error sintáctico
        syntacticError("ERROR: 'else or | ' expected here ", currentToken.spelling); 
   }
    return commandAST;
  }

  ///////////////////////////////////////////////////////////////////////////////
  //
  // COMMANDS
  //
  ///////////////////////////////////////////////////////////////////////////////

  // parseCommand parses the command, and constructs an AST
  // to represent its phrase structure.

  Command parseCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();

    start(commandPos);
    commandAST = parseSingleCommand();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Command c2AST = parseSingleCommand();
      finish(commandPos);
      commandAST = new SequentialCommand(commandAST, c2AST, commandPos);
    }
    return commandAST;
  }

  /*
        Se elimino las siguientes reglas:
        ::= (vacio)
        | "begin" Command "end"
        | "let" Declaration "in" single-Command
        | "if" Expression "then" single-Command "else" single-Command
        | "while" Expression "do" single-Command
        Sahid Rojas Chacon , Kenny Vega
   */

  Command parseSingleCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);
    switch (currentToken.kind) {

    case Token.IDENTIFIER:
    
      {
        Identifier iAST = null;
        LongIdentifier longI = parseLongIdentifier();

        /*
        Anadir: 
        LongIdentifier 
        parse | V-name ":=" Expression
        Sahid Rojas
        */
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(commandPos);
          commandAST = new CallCommand(longI, apsAST, commandPos); // We modify this line to a longIdentifier the position 1 of the command

        } else {
      
          Vname vAST = parseRestOfVname(longI);
          accept(Token.BECOMES);
          Expression eAST = parseExpression();
          finish(commandPos);
          commandAST = new AssignCommand(vAST, eAST, commandPos);
        }
      }
      break;
    
    // Agregado:
    //| "for" Identifier ":=" Expression ".." Expression
    //    "do" Command "end"
    //| "for" Identifier ":=" Expression ".." Expression
    //    "while" Expression "do" Command "end"
    //| "for" Identifier ":=" Expression ".." Expression
    //    "until" Expression "do" Command "end"
    //Kenny Vega
   
    case Token.FOR:
    { 
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.BECOMES);
        Expression eAST = parseExpression();
        accept(Token.DOTDOT);
        Expression e1AST = parseExpression();
        
        if (currentToken.kind == Token.DO) {
            acceptIt();
            Command cAST = parseCommand();
            accept(Token.END);
            finish(commandPos);
            commandAST = new ForCommand(iAST, eAST, e1AST, cAST, commandPos);
        }
        else if (currentToken.kind == Token.WHILE) {
         
            acceptIt();
            Expression e2AST = parseExpression();
            accept(Token.DO);
            Command cAST = parseCommand();
            accept(Token.END);
            finish(commandPos);
            commandAST = new ForWhileCommand(iAST, eAST, e1AST, e2AST, cAST, commandPos);
        }
        else if (currentToken.kind == Token.UNTIL) {
           
            acceptIt();
            Expression e2AST = parseExpression();
            accept(Token.DO);
            Command cAST = parseCommand();
            accept(Token.END);
            finish(commandPos);
            commandAST = new ForUntilCommand(iAST, eAST, e1AST, e2AST, cAST, commandPos);
        }
        else{
         syntacticError("'do, while or until' expected here ", currentToken.spelling); 
        }
    }
    break;

    //Adicion de:
    //| "repeat" "while" Expression "do" Command "end"
    //| "repeat" "until" Expression "do" Command "end"
    //| "repeat" "do" Command "while" Expression "end"
    //| "repeat" "do" Command "until" Expression "end"
    //| "repeat" Expression "times" "do" Command "end"
    // Kenny Vega
    case Token.REPEAT:
    {
       acceptIt();
       
        if (currentToken.kind == Token.WHILE) {
            acceptIt();
          
            Expression eAST = parseExpression();
            accept(Token.DO);
            Command cAST = parseCommand();
            accept(Token.END);
            finish(commandPos);
            commandAST = new DoWhileCommand(cAST, eAST, commandPos);
        }
        else if (currentToken.kind == Token.UNTIL) {
            acceptIt();
            Expression eAST = parseExpression();
            accept(Token.DO);
            Command cAST = parseCommand();
            accept(Token.END);
            finish(commandPos);
            commandAST = new DoUntilCommand(cAST, eAST, commandPos);
        }
        else if (currentToken.kind == Token.DO) {
            acceptIt();
            Command cAST = parseCommand();
            if (currentToken.kind == Token.WHILE) {
                acceptIt();
                Expression eAST = parseExpression();
                accept(Token.END);
                finish(commandPos);
                commandAST = new DoUntilCommand(cAST, eAST, commandPos);
            } else if (currentToken.kind == Token.UNTIL) {
                acceptIt();
                Expression eAST = parseExpression();
                accept(Token.END);
                finish(commandPos);
                commandAST = new DoUntilCommand(cAST, eAST, commandPos);
            }
        }
        else{ 
    
            Expression eAST = parseExpression();
            if (currentToken.kind == Token.TIMES) {
                acceptIt();
                accept(Token.DO);
                Command cAST = parseCommand();
       
                accept(Token.END);
                finish(commandPos);
                commandAST = new WhileCommand(eAST, cAST, commandPos);
            }
            else{
                //Caso de error
                syntacticError("ERROR: 'times' expected here ", currentToken.spelling); 
            }
        }
     }
    break;

      
        //Agregar:
        //| "let" Declaration "in" Command "end"
        //Kenny Vega
     
      case Token.LET: {
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Command cAST = parseCommand();
        accept(Token.END);
        finish(commandPos);
        commandAST = new LetCommand(dAST, cAST, commandPos);
      }
      break;
      
      
      
     //Kenny Vega
      //| "if" Expression "then" Command + RESTO DEL IF ("|" Expression "then" Command)*
     //                                                    "else" Command "end"
     case Token.IF:
    {
        acceptIt();
        Expression eAST = parseExpression();

        // Verificar si se encuentra la palabra clave "then" después de la expresión
        if (currentToken.kind == Token.THEN) {
            accept(Token.THEN);
            Command c1AST = parseCommand();
            finish(commandPos);
            commandAST = new IfCommand(eAST, c1AST, parseRestoDelIf(), commandPos);
        } else {
            // Si no se encuentra la palabra clave "then", se ha producido un error sintáctico
            syntacticError("ERROR: 'then' expected here ", currentToken.spelling); 
        }
    }
    break;

     /* Se agrego el skip:
            
        Kenny Vega
     */
    case Token.SKIP:
      acceptIt();
      finish(commandPos);
      commandAST = new EmptyCommand(commandPos);
      break;
    
    default:
      syntacticError("\"%\" cannot start a command",
        currentToken.spelling);
      break;

    }

    return commandAST;
  }
  
  


  ///////////////////////////////////////////////////////////////////////////////
  //
  // EXPRESSIONS
  //
  ///////////////////////////////////////////////////////////////////////////////

  Expression parseExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();

    start(expressionPos);

    switch (currentToken.kind) {

      case Token.LET: {
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Expression eAST = parseExpression();
        finish(expressionPos);
        expressionAST = new LetExpression(dAST, eAST, expressionPos);
      }
        break;

      case Token.IF: {
        acceptIt();
        Expression e1AST = parseExpression();
        accept(Token.THEN);
        Expression e2AST = parseExpression();
        accept(Token.ELSE);
        Expression e3AST = parseExpression();
        finish(expressionPos);
        expressionAST = new IfExpression(e1AST, e2AST, e3AST, expressionPos);
      }
        break;

      default:
        expressionAST = parseSecondaryExpression();
        break;
    }
    return expressionAST;
  }

  Expression parseSecondaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    expressionAST = parsePrimaryExpression();
    while (currentToken.kind == Token.OPERATOR) {
      Operator opAST = parseOperator();
      Expression e2AST = parsePrimaryExpression();
      expressionAST = new BinaryExpression(expressionAST, opAST, e2AST,
          expressionPos);
    }
    return expressionAST;
  }

  Expression parsePrimaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    switch (currentToken.kind) {

      case Token.INTLITERAL: {
        IntegerLiteral ilAST = parseIntegerLiteral();
        finish(expressionPos);
        expressionAST = new IntegerExpression(ilAST, expressionPos);
      }
        break;

      case Token.CHARLITERAL: {
        CharacterLiteral clAST = parseCharacterLiteral();
        finish(expressionPos);
        expressionAST = new CharacterExpression(clAST, expressionPos);
      }
        break;

      case Token.LBRACKET: {
        acceptIt();
        ArrayAggregate aaAST = parseArrayAggregate();
        accept(Token.RBRACKET);
        finish(expressionPos);
        expressionAST = new ArrayExpression(aaAST, expressionPos);
      }
        break;

      case Token.LCURLY: {
        acceptIt();
        RecordAggregate raAST = parseRecordAggregate();
        accept(Token.RCURLY);
        finish(expressionPos);
        expressionAST = new RecordExpression(raAST, expressionPos);
      }
        break;
      /*
       Añadir: 
        LongIdentifier
        Sahid Rojas
       */
      case Token.IDENTIFIER: {
        Identifier iAST = null;
        LongIdentifier longI = parseLongIdentifier();

        
        iAST = longI.getSimpleIdentifier();

        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(expressionPos);
          LongIdentifier longIAST = new LongIdentifierSimple(iAST, expressionPos);  
          expressionAST = new CallExpression(longIAST, apsAST, expressionPos);

        } else {
          Vname vAST = parseRestOfVname(longI);
          finish(expressionPos);
          expressionAST = new VnameExpression(vAST, expressionPos);
        }
      }
        break;

      case Token.OPERATOR: {
        Operator opAST = parseOperator();
        Expression eAST = parsePrimaryExpression();
        finish(expressionPos);
        expressionAST = new UnaryExpression(opAST, eAST, expressionPos);
      }
        break;

      case Token.LPAREN:
        acceptIt();
        expressionAST = parseExpression();
        accept(Token.RPAREN);
        break;

      default:
        syntacticError("\"%\" cannot start an expression",
            currentToken.spelling);
        break;

    }
    return expressionAST;
  }

  RecordAggregate parseRecordAggregate() throws SyntaxError {
    RecordAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Identifier iAST = parseIdentifier();
    accept(Token.IS);
    Expression eAST = parseExpression();

    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      RecordAggregate aAST = parseRecordAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleRecordAggregate(iAST, eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleRecordAggregate(iAST, eAST, aggregatePos);
    }
    return aggregateAST;
  }

  ArrayAggregate parseArrayAggregate() throws SyntaxError {
    ArrayAggregate aggregateAST = null; 

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Expression eAST = parseExpression();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ArrayAggregate aAST = parseArrayAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleArrayAggregate(eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleArrayAggregate(eAST, aggregatePos);
    }
    return aggregateAST;
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // VALUE-OR-VARIABLE NAMES
  //
  ///////////////////////////////////////////////////////////////////////////////

  /*
    Modificar:
    V-name ::= [ Package-Identifier "$" ] Var-name
    Var-name ::= Identifier
            | Var-name "." Identifier
            | Var-name "[" Expression "]"
    Sahid Rojas
   */

  Vname parseVname() throws SyntaxError {
    Vname vnameAST = null; 
    LongIdentifier iAST = parseLongIdentifier();
    vnameAST = parseRestOfVname(iAST);
    return vnameAST;
  }

  Vname parseRestOfVname(LongIdentifier identifierAST) throws SyntaxError {

    
    SourcePosition vnamePos = new SourcePosition();
    vnamePos = identifierAST.position;
    
    Vname vAST = new SimpleVname(identifierAST, vnamePos);

    while (currentToken.kind == Token.DOT ||
        currentToken.kind == Token.LBRACKET) {

      if (currentToken.kind == Token.DOT) {
        acceptIt();
        Identifier iAST = parseIdentifier();
        vAST = new DotVname(vAST, iAST, vnamePos);
      } else {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.RBRACKET);
        finish(vnamePos);
        vAST = new SubscriptVname(vAST, eAST, vnamePos);
      }
    }
    return vAST;
  }



///////////////////////////////////////////////////////////////////////////////
//
// DECLARATIONS
//
///////////////////////////////////////////////////////////////////////////////
  
   /*
    Modificar:  
    ::= compound-Declaration 
    |   Declaration ";" compound-Declaration
  
    Sahid Rojas
   */

  Declaration parseDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    declarationAST = parseCompoundDeclaration();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Declaration d2AST = parseCompoundDeclaration();
      finish(declarationPos);
      declarationAST = new SequentialDeclaration(declarationAST, d2AST,
          declarationPos);
    }
    return declarationAST;
  }
  
 

  Declaration parseSingleDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    
    switch (currentToken.kind) {

      case Token.CONST: {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new ConstDeclaration(iAST, eAST, declarationPos);
      }
      break;
      
      

    /*
       Agregar:
        | "var" Identifier ":=" Expression
        Sahid Rojas
    */
    case Token.VAR:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        switch(currentToken.kind){
          case Token.COLON:
            {
              acceptIt();
              TypeDenoter tAST = parseTypeDenoter();
              finish(declarationPos);
              declarationAST = new VarDeclaration(iAST, tAST, declarationPos);
            }
            break;
          case Token.BECOMES:
            {
              acceptIt();
              Expression eAST = parseExpression();
              finish(declarationPos);
              declarationAST = new VariableInitializedDeclaration(iAST, eAST,declarationPos); 
            }
            break;
          default:
            syntacticError("\"%\" is not a valid declaration",
              currentToken.spelling);
            break;              
        }
      }
        break;

    /*
       Modificar
        Proc-Func
       | "proc" Identifier "(" Formal-Parameter-Sequence ")"
        
       | "func" Identifier "(" Formal-Parameter-Sequence ")"
        ":" Type-denoter "~" Expression
         "~" Command "end"
        
        Sahid Rojas
    */
    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.IS);
        Command cAST = parseCommand();
        accept(Token.END);
        finish(declarationPos);
        declarationAST = new ProcDeclaration(iAST, fpsAST, cAST, declarationPos);
      }
      break;
      
      

      case Token.FUNC: {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new FuncDeclaration(iAST, fpsAST, tAST, eAST,
            declarationPos);
      }
        break;

      case Token.TYPE: {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        TypeDenoter tAST = parseTypeDenoter();
        finish(declarationPos);
        declarationAST = new TypeDeclaration(iAST, tAST, declarationPos);
      }
        break;

    default:
      syntacticError("\"%\" cannot start a declaration",
        currentToken.spelling);
      break;
      
    }
    
    return declarationAST;
  }
  
  /*
    Agregar: 
    compound-Declaration
        ::= single-Declaration
        | "rec" Proc-Funcs "end"
        | "private" Declaration "in" Declaration "end"
    Sahid Rojas
  */
  
  Declaration parseCompoundDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error
    
    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    
    switch(currentToken.kind){
      case Token.CONST:
      case Token.VAR:
      case Token.PROC:
      case Token.FUNC:
      case Token.TYPE:
        {
          declarationAST = parseSingleDeclaration();
        }
        break;
      case Token.REC:
        {
          acceptIt();
          Declaration pfsDeclaration = parse_ProcFuncs_Declaration();
          accept(Token.END);
          finish(declarationPos);
          declarationAST = new RECDeclaration(pfsDeclaration, declarationPos);
        }
        break;
      case Token.PRIVATE:
        {
          acceptIt();
          Declaration d1AST = parseDeclaration();
          accept(Token.IN);
          Declaration d2AST = parseDeclaration();
          accept(Token.END);
          finish(declarationPos);
          declarationAST = new PrivateDeclaration(d1AST, d2AST, declarationPos);
        }
        break;
      default:
      syntacticError("\"%\" cannot start a compound declaration",
        currentToken.spelling);
      break;
    }
    return declarationAST;
  }
  
  /*
    Agregar:
    ProcFunc
        ::= "proc" Identifier "(" Formal-Parameter-Sequence ")"
             "~" Command "end"
        | "func" Identifier "(" Formal-Parameter-Sequence ")"
             ":" Type-denoter "~" Expression
     Sahid Rojas
  */
  
  Declaration parse_ProcFunc_Declaration() throws SyntaxError{
    Declaration declarationAST = null; // in case there's a syntactic error
    SourcePosition declarationPos = new SourcePosition();
    
    start(declarationPos);
    
    switch(currentToken.kind){
      case Token.PROC:
      case Token.FUNC:
        {
          declarationAST = parseSingleDeclaration();
        }
        break;
      default:
      syntacticError("\"%\" cannot start neither a process nor a function declaration",
        currentToken.spelling);
      break;
    }
    
    return declarationAST;
  }
  
  /*
    Agregar:
    ProcFuncs 
        ::= Proc-Func ("|" Proc-Func)+
     Sahid Rojas
  */
  
  Declaration parse_ProcFuncs_Declaration() throws SyntaxError{
    Declaration declarationAST = null; // in case there's a syntactic error
    SourcePosition declarationPos = new SourcePosition();
    
    start(declarationPos);
    
    declarationAST = parse_ProcFunc_Declaration();
    accept(Token.BAR);
    Declaration pf2AST = parse_ProcFunc_Declaration();
    
    declarationAST = new SequentialDeclaration(declarationAST, pf2AST, declarationPos);
    
    while(currentToken.kind == Token.BAR){
      acceptIt();
      Declaration pfAuxAST = parse_ProcFunc_Declaration();
      finish(declarationPos);
      declarationAST = new SequentialDeclaration(declarationAST, pfAuxAST, declarationPos);
    }
    return declarationAST;
  }
  
  

  ///////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETERS
  //
  ///////////////////////////////////////////////////////////////////////////////

  FormalParameterSequence parseFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST;

    SourcePosition formalsPos = new SourcePosition();

    start(formalsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(formalsPos);
      formalsAST = new EmptyFormalParameterSequence(formalsPos);

    } else {
      formalsAST = parseProperFormalParameterSequence();
    }
    return formalsAST;
  }

  FormalParameterSequence parseProperFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST = null; // in case there's a syntactic error;

    SourcePosition formalsPos = new SourcePosition();
    start(formalsPos);
    FormalParameter fpAST = parseFormalParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FormalParameterSequence fpsAST = parseProperFormalParameterSequence();
      finish(formalsPos);
      formalsAST = new MultipleFormalParameterSequence(fpAST, fpsAST,
          formalsPos);

    } else {
      finish(formalsPos);
      formalsAST = new SingleFormalParameterSequence(fpAST, formalsPos);
    }
    return formalsAST;
  }

  FormalParameter parseFormalParameter() throws SyntaxError {
    FormalParameter formalAST = null; // in case there's a syntactic error;

    SourcePosition formalPos = new SourcePosition();
    start(formalPos);

    switch (currentToken.kind) {

      case Token.IDENTIFIER: {
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new ConstFormalParameter(iAST, tAST, formalPos);
      }
        break;

      case Token.VAR: {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new VarFormalParameter(iAST, tAST, formalPos);
      }
        break;

      case Token.PROC: {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        finish(formalPos);
        formalAST = new ProcFormalParameter(iAST, fpsAST, formalPos);
      }
        break;

      case Token.FUNC: {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new FuncFormalParameter(iAST, fpsAST, tAST, formalPos);
      }
        break;

      default:
        syntacticError("\"%\" cannot start a formal parameter",
            currentToken.spelling);
        break;

    }
    return formalAST;
  }

  ActualParameterSequence parseActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST;

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(actualsPos);
      actualsAST = new EmptyActualParameterSequence(actualsPos);

    } else {
      actualsAST = parseProperActualParameterSequence();
    }
    return actualsAST;
  }

  ActualParameterSequence parseProperActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST = null; // in case there's a syntactic error

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    ActualParameter apAST = parseActualParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ActualParameterSequence apsAST = parseProperActualParameterSequence();
      finish(actualsPos);
      actualsAST = new MultipleActualParameterSequence(apAST, apsAST,
          actualsPos);
    } else {
      finish(actualsPos);
      actualsAST = new SingleActualParameterSequence(apAST, actualsPos);
    }
    return actualsAST;
  }

  ActualParameter parseActualParameter() throws SyntaxError {
    ActualParameter actualAST = null; // in case there's a syntactic error

    SourcePosition actualPos = new SourcePosition();

    start(actualPos);

    switch (currentToken.kind) {

      case Token.IDENTIFIER:
      case Token.INTLITERAL:
      case Token.CHARLITERAL:
      case Token.OPERATOR:
      case Token.LET:
      case Token.IF:
      case Token.LPAREN:
      case Token.LBRACKET:
      case Token.LCURLY: {
        Expression eAST = parseExpression();
        finish(actualPos);
        actualAST = new ConstActualParameter(eAST, actualPos);
      }
        break;

      case Token.VAR: {
        acceptIt();
        Vname vAST = parseVname();
        finish(actualPos);
        actualAST = new VarActualParameter(vAST, actualPos);
      }
        break;

      case Token.PROC: {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new ProcActualParameter(iAST, actualPos);
      }
        break;

      case Token.FUNC: {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new FuncActualParameter(iAST, actualPos);
      }
        break;

      default:
        syntacticError("\"%\" cannot start an actual parameter",
            currentToken.spelling);
        break;

    }
    return actualAST;
  }

  ///////////////////////////////////////////////////////////////////////////////
  //
  // TYPE-DENOTERS
  //
  ///////////////////////////////////////////////////////////////////////////////
  
  /*
    Modificar
        Type-denoter ::= Long-Identifier
            | "array" Integer-Literal "of" Type-denoter
            | "record" Record-Type-denoter "end"
      Sahid Rojas
   */
  TypeDenoter parseTypeDenoter() throws SyntaxError {
    TypeDenoter typeAST = null; // in case there's a syntactic error
    SourcePosition typePos = new SourcePosition();

    start(typePos);

    switch (currentToken.kind) {

      case Token.IDENTIFIER: {
        LongIdentifier iAST = parseLongIdentifier();
        finish(typePos);
        typeAST = new SimpleTypeDenoter(iAST, typePos);
      }
        break;

      case Token.ARRAY: {
        acceptIt();
        IntegerLiteral ilAST = parseIntegerLiteral();
        accept(Token.OF);
        TypeDenoter tAST = parseTypeDenoter();
        finish(typePos);
        typeAST = new ArrayTypeDenoter(ilAST, tAST, typePos);
      }
        break;

      case Token.RECORD: {
        acceptIt();
        FieldTypeDenoter fAST = parseFieldTypeDenoter();
        accept(Token.END);
        finish(typePos);
        typeAST = new RecordTypeDenoter(fAST, typePos);
      }
        break;

      default:
        syntacticError("\"%\" cannot start a type denoter",
            currentToken.spelling);
        break;

    }
    return typeAST;
  }

  FieldTypeDenoter parseFieldTypeDenoter() throws SyntaxError {
    FieldTypeDenoter fieldAST = null; // in case there's a syntactic error

    SourcePosition fieldPos = new SourcePosition();

    start(fieldPos);
    Identifier iAST = parseIdentifier();
    accept(Token.COLON);
    TypeDenoter tAST = parseTypeDenoter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FieldTypeDenoter fAST = parseFieldTypeDenoter();
      finish(fieldPos);
      fieldAST = new MultipleFieldTypeDenoter(iAST, tAST, fAST, fieldPos);
    } else {
      finish(fieldPos);
      fieldAST = new SingleFieldTypeDenoter(iAST, tAST, fieldPos);
    }
    return fieldAST;
  }
}
