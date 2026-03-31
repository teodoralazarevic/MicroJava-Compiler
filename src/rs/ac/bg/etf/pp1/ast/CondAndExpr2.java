// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class CondAndExpr2 extends CondAndExpr {

    private CondRelExpr CondRelExpr;

    public CondAndExpr2 (CondRelExpr CondRelExpr) {
        this.CondRelExpr=CondRelExpr;
        if(CondRelExpr!=null) CondRelExpr.setParent(this);
    }

    public CondRelExpr getCondRelExpr() {
        return CondRelExpr;
    }

    public void setCondRelExpr(CondRelExpr CondRelExpr) {
        this.CondRelExpr=CondRelExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondRelExpr!=null) CondRelExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondRelExpr!=null) CondRelExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondRelExpr!=null) CondRelExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondAndExpr2(\n");

        if(CondRelExpr!=null)
            buffer.append(CondRelExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondAndExpr2]");
        return buffer.toString();
    }
}
