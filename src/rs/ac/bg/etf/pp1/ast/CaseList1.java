// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class CaseList1 extends CaseList {

    private Case Case;
    private CaseList CaseList;

    public CaseList1 (Case Case, CaseList CaseList) {
        this.Case=Case;
        if(Case!=null) Case.setParent(this);
        this.CaseList=CaseList;
        if(CaseList!=null) CaseList.setParent(this);
    }

    public Case getCase() {
        return Case;
    }

    public void setCase(Case Case) {
        this.Case=Case;
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
        if(Case!=null) Case.accept(visitor);
        if(CaseList!=null) CaseList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Case!=null) Case.traverseTopDown(visitor);
        if(CaseList!=null) CaseList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Case!=null) Case.traverseBottomUp(visitor);
        if(CaseList!=null) CaseList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CaseList1(\n");

        if(Case!=null)
            buffer.append(Case.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CaseList!=null)
            buffer.append(CaseList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CaseList1]");
        return buffer.toString();
    }
}
