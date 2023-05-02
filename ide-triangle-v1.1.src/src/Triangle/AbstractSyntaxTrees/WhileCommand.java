package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class WhileCommand extends Command {

    public WhileCommand(Expression eAST, Command cAST, SourcePosition thePosition) {
        super(thePosition);
        E = eAST;
        C = cAST;
    }

    public Object visit(Visitor v, Object o) {
        return v.visitWhileCommand(this, o);
    }

    public Expression E;
    public Command C;
}
