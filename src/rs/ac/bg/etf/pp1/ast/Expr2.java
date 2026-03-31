// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class Expr2 extends Expr {

    private Condition Condition;
    private Quest Quest;
    private Expr Expr;
    private TernaryMid TernaryMid;
    private Expr Expr1;

    public Expr2 (Condition Condition, Quest Quest, Expr Expr, TernaryMid TernaryMid, Expr Expr1) {
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.Quest=Quest;
        if(Quest!=null) Quest.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.TernaryMid=TernaryMid;
        if(TernaryMid!=null) TernaryMid.setParent(this);
        this.Expr1=Expr1;
        if(Expr1!=null) Expr1.setParent(this);
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public Quest getQuest() {
        return Quest;
    }

    public void setQuest(Quest Quest) {
        this.Quest=Quest;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public TernaryMid getTernaryMid() {
        return TernaryMid;
    }

    public void setTernaryMid(TernaryMid TernaryMid) {
        this.TernaryMid=TernaryMid;
    }

    public Expr getExpr1() {
        return Expr1;
    }

    public void setExpr1(Expr Expr1) {
        this.Expr1=Expr1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Condition!=null) Condition.accept(visitor);
        if(Quest!=null) Quest.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(TernaryMid!=null) TernaryMid.accept(visitor);
        if(Expr1!=null) Expr1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(Quest!=null) Quest.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(TernaryMid!=null) TernaryMid.traverseTopDown(visitor);
        if(Expr1!=null) Expr1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(Quest!=null) Quest.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(TernaryMid!=null) TernaryMid.traverseBottomUp(visitor);
        if(Expr1!=null) Expr1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expr2(\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Quest!=null)
            buffer.append(Quest.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TernaryMid!=null)
            buffer.append(TernaryMid.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr1!=null)
            buffer.append(Expr1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expr2]");
        return buffer.toString();
    }
}
