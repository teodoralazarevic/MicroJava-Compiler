// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class CondOrExpr2 extends CondOrExpr {

    private CondAndExpr CondAndExpr;

    public CondOrExpr2 (CondAndExpr CondAndExpr) {
        this.CondAndExpr=CondAndExpr;
        if(CondAndExpr!=null) CondAndExpr.setParent(this);
    }

    public CondAndExpr getCondAndExpr() {
        return CondAndExpr;
    }

    public void setCondAndExpr(CondAndExpr CondAndExpr) {
        this.CondAndExpr=CondAndExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondAndExpr!=null) CondAndExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondAndExpr!=null) CondAndExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondAndExpr!=null) CondAndExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondOrExpr2(\n");

        if(CondAndExpr!=null)
            buffer.append(CondAndExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondOrExpr2]");
        return buffer.toString();
    }
}
