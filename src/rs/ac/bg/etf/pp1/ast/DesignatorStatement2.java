// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatement2 extends DesignatorStatement {

    private Designator Designator;
    private ActParList ActParList;

    public DesignatorStatement2 (Designator Designator, ActParList ActParList) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.ActParList=ActParList;
        if(ActParList!=null) ActParList.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public ActParList getActParList() {
        return ActParList;
    }

    public void setActParList(ActParList ActParList) {
        this.ActParList=ActParList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(ActParList!=null) ActParList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(ActParList!=null) ActParList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(ActParList!=null) ActParList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatement2(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParList!=null)
            buffer.append(ActParList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatement2]");
        return buffer.toString();
    }
}
