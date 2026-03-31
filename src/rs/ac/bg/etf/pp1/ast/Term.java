// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class Term implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private MoreTerm MoreTerm;

    public Term (MoreTerm MoreTerm) {
        this.MoreTerm=MoreTerm;
        if(MoreTerm!=null) MoreTerm.setParent(this);
    }

    public MoreTerm getMoreTerm() {
        return MoreTerm;
    }

    public void setMoreTerm(MoreTerm MoreTerm) {
        this.MoreTerm=MoreTerm;
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
        if(MoreTerm!=null) MoreTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MoreTerm!=null) MoreTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MoreTerm!=null) MoreTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Term(\n");

        if(MoreTerm!=null)
            buffer.append(MoreTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Term]");
        return buffer.toString();
    }
}
