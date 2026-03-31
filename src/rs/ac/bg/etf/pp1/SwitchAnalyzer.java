package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.CaseNumber;
import rs.ac.bg.etf.pp1.ast.*;

public class SwitchAnalyzer extends VisitorAdaptor {
	private List<Integer> caseValues = new ArrayList<Integer>();
	private Stack<List<Integer>> stackOfLists = new Stack<List<Integer>>();
	
	@Override
	public void visit(Switch switchStart) {
		stackOfLists.push(new ArrayList<Integer>());
	}
	
	@Override
	public void visit(CaseNumber caseVal) {
		stackOfLists.peek().add(caseVal.getN1());
		//caseValues.add(caseVal.getN1());
	}
	
	@Override
	public void visit(Statement_switch switchEnd) {
		caseValues = stackOfLists.pop();
	}
	
	public List<Integer> getCases(){
		return caseValues;
	}
}
