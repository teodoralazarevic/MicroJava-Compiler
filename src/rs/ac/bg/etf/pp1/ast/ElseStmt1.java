// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class ElseStmt1 extends ElseStmt {

    private Else Else;
    private Statement Statement;

    public ElseStmt1 (Else Else, Statement Statement) {
        this.Else=Else;
        if(Else!=null) Else.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public Else getElse() {
        return Else;
    }

    public void setElse(Else Else) {
        this.Else=Else;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Else!=null) Else.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Else!=null) Else.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Else!=null) Else.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ElseStmt1(\n");

        if(Else!=null)
            buffer.append(Else.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ElseStmt1]");
        return buffer.toString();
    }
}
