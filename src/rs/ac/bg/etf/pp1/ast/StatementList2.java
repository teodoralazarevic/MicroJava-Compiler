// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class StatementList2 extends StatementList {

    public StatementList2 () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementList2(\n");

        buffer.append(tab);
        buffer.append(") [StatementList2]");
        return buffer.toString();
    }
}
