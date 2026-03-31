// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class ActParList2 extends ActParList {

    private ActParStart ActParStart;

    public ActParList2 (ActParStart ActParStart) {
        this.ActParStart=ActParStart;
        if(ActParStart!=null) ActParStart.setParent(this);
    }

    public ActParStart getActParStart() {
        return ActParStart;
    }

    public void setActParStart(ActParStart ActParStart) {
        this.ActParStart=ActParStart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActParStart!=null) ActParStart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParStart!=null) ActParStart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParStart!=null) ActParStart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParList2(\n");

        if(ActParStart!=null)
            buffer.append(ActParStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParList2]");
        return buffer.toString();
    }
}
