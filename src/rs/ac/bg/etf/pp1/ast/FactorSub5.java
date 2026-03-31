// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class FactorSub5 extends FactorSub {

    private Integer B1;

    public FactorSub5 (Integer B1) {
        this.B1=B1;
    }

    public Integer getB1() {
        return B1;
    }

    public void setB1(Integer B1) {
        this.B1=B1;
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
        buffer.append("FactorSub5(\n");

        buffer.append(" "+tab+B1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorSub5]");
        return buffer.toString();
    }
}
