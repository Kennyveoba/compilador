package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public abstract class LongIdentifier extends AST{
    
    public LongIdentifier (SourcePosition position) {
        super (position);
    }
    
    public Identifier getSimpleIdentifier(){

        Identifier iAST = null;    

        if (this.getClass() == LongIdentifierComplex.class) {
          iAST = ((LongIdentifierComplex) this).I;
        } else {
          iAST = ((LongIdentifierSimple) this).I;
        }

        return iAST;
    }

    
}
