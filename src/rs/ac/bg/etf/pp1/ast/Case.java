// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class Case implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private CaseNumber CaseNumber;
    private CaseBodyStart CaseBodyStart;
    private StatementList StatementList;

    public Case (CaseNumber CaseNumber, CaseBodyStart CaseBodyStart, StatementList StatementList) {
        this.CaseNumber=CaseNumber;
        if(CaseNumber!=null) CaseNumber.setParent(this);
        this.CaseBodyStart=CaseBodyStart;
        if(CaseBodyStart!=null) CaseBodyStart.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public CaseNumber getCaseNumber() {
        return CaseNumber;
    }

    public void setCaseNumber(CaseNumber CaseNumber) {
        this.CaseNumber=CaseNumber;
    }

    public CaseBodyStart getCaseBodyStart() {
        return CaseBodyStart;
    }

    public void setCaseBodyStart(CaseBodyStart CaseBodyStart) {
        this.CaseBodyStart=CaseBodyStart;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CaseNumber!=null) CaseNumber.accept(visitor);
        if(CaseBodyStart!=null) CaseBodyStart.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CaseNumber!=null) CaseNumber.traverseTopDown(visitor);
        if(CaseBodyStart!=null) CaseBodyStart.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CaseNumber!=null) CaseNumber.traverseBottomUp(visitor);
        if(CaseBodyStart!=null) CaseBodyStart.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Case(\n");

        if(CaseNumber!=null)
            buffer.append(CaseNumber.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CaseBodyStart!=null)
            buffer.append(CaseBodyStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Case]");
        return buffer.toString();
    }
}
