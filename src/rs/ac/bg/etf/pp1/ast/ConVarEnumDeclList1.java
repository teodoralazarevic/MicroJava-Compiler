// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class ConVarEnumDeclList1 extends ConVarEnumDeclList {

    private ConVarEnumDeclList ConVarEnumDeclList;
    private ConstDeclList ConstDeclList;

    public ConVarEnumDeclList1 (ConVarEnumDeclList ConVarEnumDeclList, ConstDeclList ConstDeclList) {
        this.ConVarEnumDeclList=ConVarEnumDeclList;
        if(ConVarEnumDeclList!=null) ConVarEnumDeclList.setParent(this);
        this.ConstDeclList=ConstDeclList;
        if(ConstDeclList!=null) ConstDeclList.setParent(this);
    }

    public ConVarEnumDeclList getConVarEnumDeclList() {
        return ConVarEnumDeclList;
    }

    public void setConVarEnumDeclList(ConVarEnumDeclList ConVarEnumDeclList) {
        this.ConVarEnumDeclList=ConVarEnumDeclList;
    }

    public ConstDeclList getConstDeclList() {
        return ConstDeclList;
    }

    public void setConstDeclList(ConstDeclList ConstDeclList) {
        this.ConstDeclList=ConstDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConVarEnumDeclList!=null) ConVarEnumDeclList.accept(visitor);
        if(ConstDeclList!=null) ConstDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConVarEnumDeclList!=null) ConVarEnumDeclList.traverseTopDown(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConVarEnumDeclList!=null) ConVarEnumDeclList.traverseBottomUp(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConVarEnumDeclList1(\n");

        if(ConVarEnumDeclList!=null)
            buffer.append(ConVarEnumDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclList!=null)
            buffer.append(ConstDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConVarEnumDeclList1]");
        return buffer.toString();
    }
}
