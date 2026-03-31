// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class Statement_for extends Statement {

    private For For;
    private DesignatorStatementOpt DesignatorStatementOpt;
    private ForConditionStart ForConditionStart;
    private ConditionOpt ConditionOpt;
    private ForPostExprStart ForPostExprStart;
    private PostfixExpr PostfixExpr;
    private ForBodyStart ForBodyStart;
    private Statement Statement;
    private PostFor PostFor;

    public Statement_for (For For, DesignatorStatementOpt DesignatorStatementOpt, ForConditionStart ForConditionStart, ConditionOpt ConditionOpt, ForPostExprStart ForPostExprStart, PostfixExpr PostfixExpr, ForBodyStart ForBodyStart, Statement Statement, PostFor PostFor) {
        this.For=For;
        if(For!=null) For.setParent(this);
        this.DesignatorStatementOpt=DesignatorStatementOpt;
        if(DesignatorStatementOpt!=null) DesignatorStatementOpt.setParent(this);
        this.ForConditionStart=ForConditionStart;
        if(ForConditionStart!=null) ForConditionStart.setParent(this);
        this.ConditionOpt=ConditionOpt;
        if(ConditionOpt!=null) ConditionOpt.setParent(this);
        this.ForPostExprStart=ForPostExprStart;
        if(ForPostExprStart!=null) ForPostExprStart.setParent(this);
        this.PostfixExpr=PostfixExpr;
        if(PostfixExpr!=null) PostfixExpr.setParent(this);
        this.ForBodyStart=ForBodyStart;
        if(ForBodyStart!=null) ForBodyStart.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.PostFor=PostFor;
        if(PostFor!=null) PostFor.setParent(this);
    }

    public For getFor() {
        return For;
    }

    public void setFor(For For) {
        this.For=For;
    }

    public DesignatorStatementOpt getDesignatorStatementOpt() {
        return DesignatorStatementOpt;
    }

    public void setDesignatorStatementOpt(DesignatorStatementOpt DesignatorStatementOpt) {
        this.DesignatorStatementOpt=DesignatorStatementOpt;
    }

    public ForConditionStart getForConditionStart() {
        return ForConditionStart;
    }

    public void setForConditionStart(ForConditionStart ForConditionStart) {
        this.ForConditionStart=ForConditionStart;
    }

    public ConditionOpt getConditionOpt() {
        return ConditionOpt;
    }

    public void setConditionOpt(ConditionOpt ConditionOpt) {
        this.ConditionOpt=ConditionOpt;
    }

    public ForPostExprStart getForPostExprStart() {
        return ForPostExprStart;
    }

    public void setForPostExprStart(ForPostExprStart ForPostExprStart) {
        this.ForPostExprStart=ForPostExprStart;
    }

    public PostfixExpr getPostfixExpr() {
        return PostfixExpr;
    }

    public void setPostfixExpr(PostfixExpr PostfixExpr) {
        this.PostfixExpr=PostfixExpr;
    }

    public ForBodyStart getForBodyStart() {
        return ForBodyStart;
    }

    public void setForBodyStart(ForBodyStart ForBodyStart) {
        this.ForBodyStart=ForBodyStart;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public PostFor getPostFor() {
        return PostFor;
    }

    public void setPostFor(PostFor PostFor) {
        this.PostFor=PostFor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(For!=null) For.accept(visitor);
        if(DesignatorStatementOpt!=null) DesignatorStatementOpt.accept(visitor);
        if(ForConditionStart!=null) ForConditionStart.accept(visitor);
        if(ConditionOpt!=null) ConditionOpt.accept(visitor);
        if(ForPostExprStart!=null) ForPostExprStart.accept(visitor);
        if(PostfixExpr!=null) PostfixExpr.accept(visitor);
        if(ForBodyStart!=null) ForBodyStart.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(PostFor!=null) PostFor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(For!=null) For.traverseTopDown(visitor);
        if(DesignatorStatementOpt!=null) DesignatorStatementOpt.traverseTopDown(visitor);
        if(ForConditionStart!=null) ForConditionStart.traverseTopDown(visitor);
        if(ConditionOpt!=null) ConditionOpt.traverseTopDown(visitor);
        if(ForPostExprStart!=null) ForPostExprStart.traverseTopDown(visitor);
        if(PostfixExpr!=null) PostfixExpr.traverseTopDown(visitor);
        if(ForBodyStart!=null) ForBodyStart.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(PostFor!=null) PostFor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(For!=null) For.traverseBottomUp(visitor);
        if(DesignatorStatementOpt!=null) DesignatorStatementOpt.traverseBottomUp(visitor);
        if(ForConditionStart!=null) ForConditionStart.traverseBottomUp(visitor);
        if(ConditionOpt!=null) ConditionOpt.traverseBottomUp(visitor);
        if(ForPostExprStart!=null) ForPostExprStart.traverseBottomUp(visitor);
        if(PostfixExpr!=null) PostfixExpr.traverseBottomUp(visitor);
        if(ForBodyStart!=null) ForBodyStart.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(PostFor!=null) PostFor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Statement_for(\n");

        if(For!=null)
            buffer.append(For.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementOpt!=null)
            buffer.append(DesignatorStatementOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForConditionStart!=null)
            buffer.append(ForConditionStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionOpt!=null)
            buffer.append(ConditionOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForPostExprStart!=null)
            buffer.append(ForPostExprStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PostfixExpr!=null)
            buffer.append(PostfixExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForBodyStart!=null)
            buffer.append(ForBodyStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PostFor!=null)
            buffer.append(PostFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Statement_for]");
        return buffer.toString();
    }
}
