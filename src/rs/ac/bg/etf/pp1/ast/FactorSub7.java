// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class FactorSub7 extends FactorSub {

    private FactorSub7Start FactorSub7Start;
    private Expr Expr;

    public FactorSub7 (FactorSub7Start FactorSub7Start, Expr Expr) {
        this.FactorSub7Start=FactorSub7Start;
        if(FactorSub7Start!=null) FactorSub7Start.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public FactorSub7Start getFactorSub7Start() {
        return FactorSub7Start;
    }

    public void setFactorSub7Start(FactorSub7Start FactorSub7Start) {
        this.FactorSub7Start=FactorSub7Start;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FactorSub7Start!=null) FactorSub7Start.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FactorSub7Start!=null) FactorSub7Start.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FactorSub7Start!=null) FactorSub7Start.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorSub7(\n");

        if(FactorSub7Start!=null)
            buffer.append(FactorSub7Start.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorSub7]");
        return buffer.toString();
    }
}
