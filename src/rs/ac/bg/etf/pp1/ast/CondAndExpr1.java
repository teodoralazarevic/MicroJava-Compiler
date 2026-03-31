// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class CondAndExpr1 extends CondAndExpr {

    private CondAndExpr CondAndExpr;
    private CondRelExpr CondRelExpr;

    public CondAndExpr1 (CondAndExpr CondAndExpr, CondRelExpr CondRelExpr) {
        this.CondAndExpr=CondAndExpr;
        if(CondAndExpr!=null) CondAndExpr.setParent(this);
        this.CondRelExpr=CondRelExpr;
        if(CondRelExpr!=null) CondRelExpr.setParent(this);
    }

    public CondAndExpr getCondAndExpr() {
        return CondAndExpr;
    }

    public void setCondAndExpr(CondAndExpr CondAndExpr) {
        this.CondAndExpr=CondAndExpr;
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
        if(CondAndExpr!=null) CondAndExpr.accept(visitor);
        if(CondRelExpr!=null) CondRelExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondAndExpr!=null) CondAndExpr.traverseTopDown(visitor);
        if(CondRelExpr!=null) CondRelExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondAndExpr!=null) CondAndExpr.traverseBottomUp(visitor);
        if(CondRelExpr!=null) CondRelExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondAndExpr1(\n");

        if(CondAndExpr!=null)
            buffer.append(CondAndExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondRelExpr!=null)
            buffer.append(CondRelExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondAndExpr1]");
        return buffer.toString();
    }
}
