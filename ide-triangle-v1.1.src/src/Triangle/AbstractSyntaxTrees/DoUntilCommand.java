package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class DoUntilCommand extends Command {
    public DoUntilCommand(Command cAST, Expression eAST, SourcePosition thePosition) {
        super(thePosition);
        C = cAST;
        E = eAST;
    }

    public Object visit(Visitor v, Object o) {
        return v.visitDoUntilLoop(this, o);
    }

    public Expression E;
    public Command C;
}
