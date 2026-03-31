// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class CondRelExpr1 extends CondRelExpr {

    private AddopTermList AddopTermList;
    private Relop Relop;
    private AddopTermList AddopTermList1;

    public CondRelExpr1 (AddopTermList AddopTermList, Relop Relop, AddopTermList AddopTermList1) {
        this.AddopTermList=AddopTermList;
        if(AddopTermList!=null) AddopTermList.setParent(this);
        this.Relop=Relop;
        if(Relop!=null) Relop.setParent(this);
        this.AddopTermList1=AddopTermList1;
        if(AddopTermList1!=null) AddopTermList1.setParent(this);
    }

    public AddopTermList getAddopTermList() {
        return AddopTermList;
    }

    public void setAddopTermList(AddopTermList AddopTermList) {
        this.AddopTermList=AddopTermList;
    }

    public Relop getRelop() {
        return Relop;
    }

    public void setRelop(Relop Relop) {
        this.Relop=Relop;
    }

    public AddopTermList getAddopTermList1() {
        return AddopTermList1;
    }

    public void setAddopTermList1(AddopTermList AddopTermList1) {
        this.AddopTermList1=AddopTermList1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AddopTermList!=null) AddopTermList.accept(visitor);
        if(Relop!=null) Relop.accept(visitor);
        if(AddopTermList1!=null) AddopTermList1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AddopTermList!=null) AddopTermList.traverseTopDown(visitor);
        if(Relop!=null) Relop.traverseTopDown(visitor);
        if(AddopTermList1!=null) AddopTermList1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AddopTermList!=null) AddopTermList.traverseBottomUp(visitor);
        if(Relop!=null) Relop.traverseBottomUp(visitor);
        if(AddopTermList1!=null) AddopTermList1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondRelExpr1(\n");

        if(AddopTermList!=null)
            buffer.append(AddopTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Relop!=null)
            buffer.append(Relop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AddopTermList1!=null)
            buffer.append(AddopTermList1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondRelExpr1]");
        return buffer.toString();
    }
}
