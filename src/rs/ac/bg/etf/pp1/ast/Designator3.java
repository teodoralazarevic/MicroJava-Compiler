// generated with ast extension for cup
// version 0.8
// 15/1/2026 16:59:59


package rs.ac.bg.etf.pp1.ast;

public class Designator3 extends Designator {

    private ArrayNameLenght ArrayNameLenght;

    public Designator3 (ArrayNameLenght ArrayNameLenght) {
        this.ArrayNameLenght=ArrayNameLenght;
        if(ArrayNameLenght!=null) ArrayNameLenght.setParent(this);
    }

    public ArrayNameLenght getArrayNameLenght() {
        return ArrayNameLenght;
    }

    public void setArrayNameLenght(ArrayNameLenght ArrayNameLenght) {
        this.ArrayNameLenght=ArrayNameLenght;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ArrayNameLenght!=null) ArrayNameLenght.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArrayNameLenght!=null) ArrayNameLenght.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArrayNameLenght!=null) ArrayNameLenght.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designator3(\n");

        if(ArrayNameLenght!=null)
            buffer.append(ArrayNameLenght.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designator3]");
        return buffer.toString();
    }
}
