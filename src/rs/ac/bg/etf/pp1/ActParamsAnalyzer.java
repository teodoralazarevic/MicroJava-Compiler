package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.ac.bg.etf.pp1.ast.*;

public class ActParamsAnalyzer extends VisitorAdaptor {
	private List<Struct> actParList = new ArrayList<Struct>();
	private Stack<List<Struct>> stackOfLists = new Stack<List<Struct>>();
	
	@Override
	public void visit(ActParStart actParBegin) {
		stackOfLists.push(new ArrayList<Struct>());
	}
	
	@Override
	public void visit(ActPar actPar) {
		stackOfLists.peek().add(actPar.getExpr().struct);
	}
	
	@Override
	public void visit(ActParList1 actPList) {
		actParList = stackOfLists.pop();
	}
	
	@Override
	public void visit(ActParList2 actPList) {
		actParList = stackOfLists.pop();
	}
	
	public List<Struct> getListOfActualParams(){
		return actParList;
	}
}
