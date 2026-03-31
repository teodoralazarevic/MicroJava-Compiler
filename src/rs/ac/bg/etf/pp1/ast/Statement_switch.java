// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class Statement_switch extends Statement {

    private Switch Switch;
    private Expr Expr;
    private SwitchBodyStart SwitchBodyStart;
    private CaseList CaseList;

    public Statement_switch (Switch Switch, Expr Expr, SwitchBodyStart SwitchBodyStart, CaseList CaseList) {
        this.Switch=Switch;
        if(Switch!=null) Switch.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.SwitchBodyStart=SwitchBodyStart;
        if(SwitchBodyStart!=null) SwitchBodyStart.setParent(this);
        this.CaseList=CaseList;
        if(CaseList!=null) CaseList.setParent(this);
    }

    public Switch getSwitch() {
        return Switch;
    }

    public void setSwitch(Switch Switch) {
        this.Switch=Switch;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public SwitchBodyStart getSwitchBodyStart() {
        return SwitchBodyStart;
    }

    public void setSwitchBodyStart(SwitchBodyStart SwitchBodyStart) {
        this.SwitchBodyStart=SwitchBodyStart;
    }

    public CaseList getCaseList() {
        return CaseList;
    }

    public void setCaseList(CaseList CaseList) {
        this.CaseList=CaseList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Switch!=null) Switch.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(SwitchBodyStart!=null) SwitchBodyStart.accept(visitor);
        if(CaseList!=null) CaseList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Switch!=null) Switch.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(SwitchBodyStart!=null) SwitchBodyStart.traverseTopDown(visitor);
        if(CaseList!=null) CaseList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Switch!=null) Switch.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(SwitchBodyStart!=null) SwitchBodyStart.traverseBottomUp(visitor);
        if(CaseList!=null) CaseList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Statement_switch(\n");

        if(Switch!=null)
            buffer.append(Switch.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SwitchBodyStart!=null)
            buffer.append(SwitchBodyStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CaseList!=null)
            buffer.append(CaseList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Statement_switch]");
        return buffer.toString();
    }
}
