// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class Condition1 extends Condition {

    private CondOrExpr CondOrExpr;

    public Condition1 (CondOrExpr CondOrExpr) {
        this.CondOrExpr=CondOrExpr;
        if(CondOrExpr!=null) CondOrExpr.setParent(this);
    }

    public CondOrExpr getCondOrExpr() {
        return CondOrExpr;
    }

    public void setCondOrExpr(CondOrExpr CondOrExpr) {
        this.CondOrExpr=CondOrExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondOrExpr!=null) CondOrExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondOrExpr!=null) CondOrExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondOrExpr!=null) CondOrExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Condition1(\n");

        if(CondOrExpr!=null)
            buffer.append(CondOrExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Condition1]");
        return buffer.toString();
    }
}
