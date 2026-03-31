// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private MethodName MethodName;
    private FormParsList FormParsList;
    private VarDeclListMeth VarDeclListMeth;
    private StatementList StatementList;

    public MethodDecl (MethodName MethodName, FormParsList FormParsList, VarDeclListMeth VarDeclListMeth, StatementList StatementList) {
        this.MethodName=MethodName;
        if(MethodName!=null) MethodName.setParent(this);
        this.FormParsList=FormParsList;
        if(FormParsList!=null) FormParsList.setParent(this);
        this.VarDeclListMeth=VarDeclListMeth;
        if(VarDeclListMeth!=null) VarDeclListMeth.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public MethodName getMethodName() {
        return MethodName;
    }

    public void setMethodName(MethodName MethodName) {
        this.MethodName=MethodName;
    }

    public FormParsList getFormParsList() {
        return FormParsList;
    }

    public void setFormParsList(FormParsList FormParsList) {
        this.FormParsList=FormParsList;
    }

    public VarDeclListMeth getVarDeclListMeth() {
        return VarDeclListMeth;
    }

    public void setVarDeclListMeth(VarDeclListMeth VarDeclListMeth) {
        this.VarDeclListMeth=VarDeclListMeth;
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
        if(MethodName!=null) MethodName.accept(visitor);
        if(FormParsList!=null) FormParsList.accept(visitor);
        if(VarDeclListMeth!=null) VarDeclListMeth.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodName!=null) MethodName.traverseTopDown(visitor);
        if(FormParsList!=null) FormParsList.traverseTopDown(visitor);
        if(VarDeclListMeth!=null) VarDeclListMeth.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodName!=null) MethodName.traverseBottomUp(visitor);
        if(FormParsList!=null) FormParsList.traverseBottomUp(visitor);
        if(VarDeclListMeth!=null) VarDeclListMeth.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(MethodName!=null)
            buffer.append(MethodName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsList!=null)
            buffer.append(FormParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListMeth!=null)
            buffer.append(VarDeclListMeth.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}
