package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPC;
	
	/* IF ELSE */
	// adrese na kojima se desio neispunjen uslov, sa ovih adresa skacu na sledeci OR
	private Stack<Integer> conditionNotMet=new Stack<Integer>();
	// adrese na kojima se desio ispunjen uslov, sa ovim adresa skacu na then granu
	private Stack<Integer> conditionMet = new Stack<Integer>();
	private Stack<Integer> jumpToElse = new Stack<Integer>();
	private Stack<Integer> jumpBehindElse = new Stack<Integer>();
	
	
	// potrebno zbog break-a, vodi se evidencija kojim redom se ugnjezdavaju switch i for
	private enum Scope{SWITCH, FOR, IF, TERNARY};
	private Stack<Scope> scopes=new Stack<Scope>();

	
	private Scope lastScope; // bez if-a i ternary da znam iz cega se izlazi break-om
	
	public int getMainPc() {
		return mainPC;
	}
	
	private void initPredeclaredMethods() {
		Obj ordMethod = Tab.find("ord");
        Obj chrMethod = Tab.find("chr");
        ordMethod.setAdr(Code.pc);
        chrMethod.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        Code.put(Code.load_n);
        Code.put(Code.exit);
        Code.put(Code.return_);
 
        Obj lenMethod = Tab.find("len");
        lenMethod.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        Code.put(Code.load_n);
        Code.put(Code.arraylength);
        Code.put(Code.exit);
        Code.put(Code.return_);

	}
	
	public CodeGenerator() {
		initPredeclaredMethods();
	}
	
	@Override
	public void visit(MethodOpt1 methodStart) {
		Obj methObj = methodStart.obj; // sacuvali smo obj cvor u semAnalyzer
		methObj.setAdr(Code.pc); // pamcenje adrese metode
		if(methodStart.getI2().equals("main")) {
			this.mainPC = Code.pc;
		}
		Code.put(Code.enter); //1B
		Code.put(methObj.getLevel()); // b1
		Code.put(methObj.getLocalSymbols().size()); // b2

	}
	
	@Override
	public void visit(MethodOpt2 methodStart) {
		Obj methObj = methodStart.obj; // sacuvali smo obj cvor u semAnalyzer
		methObj.setAdr(Code.pc); // pamcenje adrese metode
		if(methodStart.getI1().equals("main")) {
			this.mainPC = Code.pc;
		}
		Code.put(Code.enter); //1B
		Code.put(methObj.getLevel()); // b1
		Code.put(methObj.getLocalSymbols().size()); // b2

	}
	
	@Override
	public void visit(MethodDecl methodEnd) {
		
		Obj currentMethod = methodEnd.getMethodName().obj;

	    if(!currentMethod.getType().equals(Tab.noType)) {
	        Code.put(Code.trap);
	        Code.put(1);
	    }
	    
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	
	/* STATEMENT */

	
	@Override
	public void visit(Statement_ret1 return_stm1) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(Statement_ret2 return_stm2) {
		
		Code.put(Code.exit);
		Code.put(Code.return_);

	}
	
	@Override
	public void visit(Statement_read read_stm) {
		if(read_stm.getDesignator().obj.getType().equals(Tab.charType)) {
			Code.put(Code.bread);
		}
		else {
			Code.put(Code.read);
		}
		Code.store(read_stm.getDesignator().obj);
	}
	
	
	@Override
	public void visit(Statement_print1 print_stm1) {
		Code.loadConst(0);
		if(print_stm1.getExpr().struct.equals(Tab.charType)) {
			Code.put(Code.bprint);
		}
		else {
			Code.put(Code.print);
		}
	}
	
	@Override
	public void visit(Statement_print2 print_stm2) {
		Code.loadConst(print_stm2.getN3());
		if(print_stm2.getExpr().struct.equals(Tab.charType)) {
			Code.put(Code.bprint);
		}
		else {
			Code.put(Code.print);
		}
	}
	
	
	
	
	/* FACTOR */
	/* factori uvek cine delove izraza koji moraju da se nadju na steku */
	
	
	@Override
	public void visit(FactorSub1 factorDesign) {
		// MENJALA SAM DODAT IF
		if(factorDesign.getDesignator().obj!=Tab.noObj)
			Code.load(factorDesign.getDesignator().obj);
	}
	
	@Override
	public void visit(FactorSub2 factorMethodCall) {
		// racunamo relativni pomeraj
		int offset = factorMethodCall.getDesignator().obj.getAdr() - Code.pc; // adresa metode
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	@Override
	public void visit(FactorSub3 factorNumber) {
		Code.loadConst(factorNumber.getN1());
	}
	
	@Override
	public void visit(FactorSub4 factorChar) {
		Code.loadConst(factorChar.getC1());
	}
	
	@Override
	public void visit(FactorSub5 factorBool) {
		Code.loadConst(factorBool.getB1());
	}
	
	@Override
	public void visit(FactorSub6 factorNewArray) {
		Code.put(Code.newarray);
		if(factorNewArray.getType().struct.equals(Tab.charType)) {
			Code.put(0);
		}
		else {
			Code.put(1);
		}
	}
	
	@Override
	public void visit(Factor factor) {
		if(factor.getUnary() instanceof Unary1) {
			Code.put(Code.neg);
		}
	}
	
	
	
	/* EXPR */
	
	@Override
	public void visit(AddopTermList1 addop) {
		if(addop.getAddop() instanceof Addop_plus) {
			Code.put(Code.add);
		}
		else {
			Code.put(Code.sub);
		}
	}
	
	@Override
	public void visit(MoreTerm1 mulop) {
		if(mulop.getMulop() instanceof Mulop_mul) {
			Code.put(Code.mul);
		}
		else if(mulop.getMulop() instanceof Mulop_div) {
			Code.put(Code.div);
		}
		else {
			Code.put(Code.rem);
		}
	}

	/*DESIGNATOR*/
	
	@Override
	public void visit(ArrayNameBracket arrayName) {
		Code.load(arrayName.obj);
	}
	
	@Override
	public void visit(ArrayNameLenght arrayName) {
		Code.load(arrayName.obj);
	}
	
	@Override
	public void visit(Designator3 arrayLength) {
		//Code.load(arrayLength.obj);
		arrayLength.obj = Tab.noObj;
		Code.put(Code.arraylength);
	}
	
	
	/* DESIGNATOR STATEMENT */
	
	@Override
	public void visit(DesignatorStatement1 designAssign) {
		Code.store(designAssign.getDesignator().obj);
	}
	
	@Override
	public void visit(DesignatorStatement2 designMethodCall) {
		// racunamo relativni pomeraj
		int offset = designMethodCall.getDesignator().obj.getAdr() - Code.pc; // adresa metode
		Code.put(Code.call);
		Code.put2(offset);
		
		if(designMethodCall.getDesignator().obj.getType()!=Tab.noType) {
			// skida se rezultat sa steka ako ovo proizvodi rezultat
			// jer se ovo poziva kao funkcija() a ne a=funkcija(), niko nece da skupi vrednost sa steka
			Code.put(Code.pop); 
		}
	}
	
	@Override
	public void visit(DesignatorStatement3 designIncrement) {
		Obj designIncObj = designIncrement.getDesignator().obj;
		// jer ce aload da skine dve vrednosti i posle ce astore da skine dve vrednosti
		if(designIncObj.getKind()==Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(designIncObj); // jer nije na steku
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(designIncObj);
	}
	
	@Override
	public void visit(DesignatorStatement4 designDecrement) {
		Obj designDecObj = designDecrement.getDesignator().obj;
		if(designDecObj.getKind()==Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(designDecObj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(designDecObj);
	}
	
	
	
	
	
	/*CONDITION*/
	
	private int relop(Relop relop) {
		if(relop instanceof Relop_eq)
			return Code.eq;
		if(relop instanceof Relop_ge)
			return Code.ge;
		if(relop instanceof Relop_gt)
			return Code.gt;
		if(relop instanceof Relop_leq)
			return Code.le;
		if(relop instanceof Relop_lt)
			return Code.lt;
		return Code.ne;
	}
	
	@Override
	public void visit(If ifStart) {
		scopes.push(Scope.IF); 
	}
	
	@Override
	public void visit(Statement_if ifEnd) {
		if(!scopes.empty())
			scopes.pop();
	}
	
	@Override
	public void visit(CondRelExpr1 condExpr) {
		Code.putFalseJump(relop(condExpr.getRelop()), 0); 
		conditionNotMet.push(Code.pc-2); 
	}
	
	@Override
	public void visit(CondRelExpr2 condOneExpr) {		
		// ovde imamo samo jedan izraz (vec je na steku), moramo da dodamo jos jedan operand da bi se poredili
		// ako je expr 0 onda je on netacan, znaci skace se na sledeci OR ako je expr==0, mi cemo da
		// prosledimo suprotan uslov, skace se ako prosledjeni uslov NIJE ispunjen
		Code.loadConst(0);
		// treba da se skoci (izlazak iz if-a npr) ako su 0 i izraz isti, stavljamo KONTRA USLOV
		Code.putFalseJump(Code.ne, 0); // nije ispunjen uslov, skace se na sledeci OR uslov
		conditionNotMet.push(Code.pc-2); 
		// ispunjen uslov, nastavljamo dalje, do narednog AND-a
	}
	
	// DODATO
	@Override
	public void visit(CondOrExpr1 orExpr) {
		// zavrsen levi operand or-a
	    // ako je bio true, treba odmah u then
	    Code.putJump(0);
	    
	    conditionMet.add(Code.pc - 2);	
	    // svi FALSE skokovi iz levog operanda
	    // sada treba da nastave na DESNI operand OR-a
	    while (!conditionNotMet.isEmpty()) {
	        Code.fixup(conditionNotMet.pop());
	    }
	    
	}
	
	@Override
	public void visit(CondOrExpr2 lastOr) {
		// ako se doslo do ovde, sigurno je uslov ispunjen!
		Code.putJump(0); // bezuslovan skokna then deo
		
		conditionMet.push(Code.pc-2);  
		// sada fix-ujem one netacne
		while(!conditionNotMet.empty()) {
			Code.fixup(conditionNotMet.pop());
		}
		
	}
	
	@Override
	public void visit(Condition1 conditionEnd) {

	    Code.putJump(0);
	    jumpToElse.push(Code.pc-2);

	    //if(scopes.isEmpty() || scopes.peek() != Scope.FOR) {
	        while(!conditionMet.empty()) {
	            Code.fixup(conditionMet.pop());
	        }
	    //}
	}

	
	// else statement ne postoji
	@Override
	public void visit(ElseStmt2 noElseStm) {
		Code.fixup(jumpToElse.pop()); //vracamo one koje su preskocile then granu
	}
		
	// pocetak else grane
	@Override
	public void visit(Else else_branch) {
		// one koje su ispunile uslov treba da skoce iza else
		Code.putJump(0);
		jumpBehindElse.push(Code.pc-2); // NEMA
		// fixup-ujemo one koje treba da doskoce ovde
		Code.fixup(jumpToElse.pop());
	}

	// kraj else grane
	@Override
	public void visit(ElseStmt1 else_end) {
		// treba da fixup-ujemo one koje su preskakale else deo
		Code.fixup(jumpBehindElse.pop());
		// one koje su ispunile uslov + one koje nisu
	}
	
	
	/*TERNARNI OPERATOR*/
	

	/*
	 * Condition ? Expr1 : Expr2;
	 * Condition - ako uslov nije ispunjen, kada se obidje Condition radi se
	 * Code.putJump(0);
	 * jumpToElse.add(Code.pc-2);
	 * 
	 * Condition - ako je uslov ispunjen, dosli smo do kraja Condition
	 * 
	 * Ako se doslo do ?, sigurno je uslov ispunjen
	 */
	
	
	@Override
	public void visit(TernaryMid ternaryMid) {
		// zavrsio se expr true deo
		// skok iza celog ternarnog
		Code.putJump(0);
		jumpBehindElse.push(Code.pc-2);
		
		if(!jumpToElse.isEmpty())
			Code.fixup(jumpToElse.pop());
	}
	
	@Override
	public void visit(Expr2 ternEnd) {
		if(!jumpBehindElse.isEmpty())
			Code.fixup(jumpBehindElse.pop());
	}
	
	
	/*SWITCH CASE*/
	
	// stack jer mogu da budu ugnjezdene petlje i switch-evi
	// jedna lista za sve break-ove unutar jednog switch/for-a
	private Stack<List<Integer>> breakInFor = new Stack<List<Integer>>();
	private Stack<List<Integer>> breakInSwitch = new Stack<List<Integer>>();
    //HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    private Stack<HashMap<Integer, Integer>> stackMap = new Stack<HashMap<Integer,Integer>>();
    private Stack<Integer> noMatchedJumps = new Stack<Integer>();
    
	
	@Override
	public void visit(Switch switchStart) {
	    scopes.push(Scope.SWITCH);
	    lastScope = Scope.SWITCH;
	    breakInSwitch.push(new ArrayList<>());
	    
	    stackMap.push(new HashMap<Integer, Integer>());
	}
	
	@Override
	public void visit(SwitchBodyStart switchStart) {
		SwitchAnalyzer sa = new SwitchAnalyzer();
	    switchStart.getParent().traverseBottomUp(sa);
	    List<Integer> cases = sa.getCases(); // sada imamo vrednosti case-ova
	    

	    // za sve njih false jump
	    
	    for(int caseVal:cases) {
	    	Code.put(Code.dup);
	    	Code.loadConst(caseVal);
	    	// ako je jedanko case-u skace se
	    	Code.putFalseJump(Code.ne, 0);
	    	stackMap.peek().put(caseVal, Code.pc-2);
	    	//map.put(caseVal, Code.pc-2); // konstanta case-a i adesa koja se fix-uje
	    }
	    Code.putJump(0);
	    noMatchedJumps.push(Code.pc-2);
	}
	
	int lastCase = -1;
	
	@Override
	public void visit(CaseNumber caseVal) {
		lastCase = caseVal.getN1();
	}
	
	@Override
	public void visit(CaseBodyStart caseStart) {
		if(stackMap.peek().get(lastCase)!=null)
			Code.fixup(stackMap.peek().get(lastCase));
		//Code.fixup(map.get(lastCase));
	}
	
	@Override
	public void visit(Statement_switch switchEnd) {
		for(int addr:breakInSwitch.pop()) {
			Code.fixup(addr);
		}
		Code.fixup(noMatchedJumps.pop());
		stackMap.pop();
		
		scopes.pop();
		Code.put(Code.pop);
	}

	
	/*BREAK*/
	
	@Override
	public void visit(Statement_break breakStm) {
		// ovaj break napusta okruzujuci switch
		if((!scopes.isEmpty() && scopes.peek().equals(Scope.SWITCH)) || lastScope.equals(Scope.SWITCH)) {
			Code.putJump(0);
			if(!breakInSwitch.isEmpty())
				breakInSwitch.peek().add(Code.pc-2);
		}
		else if((!scopes.isEmpty() && scopes.peek().equals(Scope.FOR)) || lastScope.equals(Scope.FOR)) {
			Code.putJump(0);
			if(!breakInFor.isEmpty())
				breakInFor.peek().add(Code.pc-2);
		}
	}
	
	
	
	/*CONTINUE*/
	
	private Stack<List<Integer>> contInFor = new Stack<List<Integer>>();
	
	@Override
	public void visit(Statement_cont contStm) {
		if(scopes.peek()==Scope.SWITCH)
			Code.put(Code.pop);
		Code.putJump(0);
		contInFor.peek().add(Code.pc-2);
	}
	
	/*FOR*/
	
	private Stack<Integer> forConditionAddr = new Stack<Integer>();
	private Stack<Integer> forPostfixAddr = new Stack<Integer>();
	
	@Override
	public void visit(For forStart) {
		scopes.push(Scope.FOR);
		lastScope = Scope.FOR;
		breakInFor.push(new ArrayList<Integer>());
		
		contInFor.push(new ArrayList<Integer>());
		
		/*conditionNotMetFor.push(new Stack<Integer>());
		conditionMetFor.push(new Stack<Integer>());
		jumpBehindFor.push(new Stack<Integer>());*/
	}
	
	@Override
	public void visit(ForConditionStart conditionAddr) {
		forConditionAddr.push(Code.pc);
	}
	
	@Override
	public void visit(ConditionOpt2 emptyCondition) {
		Code.putJump(0);
		conditionMet.push(Code.pc-2);
	}
	
	@Override
	public void visit(ForPostExprStart postfixAddr) {
		
		Code.putJump(0);
		conditionMet.push(Code.pc-2);
		
		forPostfixAddr.push(Code.pc);
	}
 
	
	@Override
	public void visit(PostfixExpr endOfPostfix) {
		// kada se izvrsio postfiksni izraz, bezuslovno se skace na proveru uslova
		Code.putJump(forConditionAddr.pop());
	}
	
	@Override
	public void visit(ForBodyStart bodyStart) {
		// ovde doskacu niti ciji je uslov ispunjen, pa izvrsavaju telo
		while(!conditionMet.isEmpty())
			Code.fixup(conditionMet.pop());
	}
	
	@Override
	public void visit(PostFor forBodyEnd) {
		// kada se zavrsi telo, skace se na postizraz
		while(!contInFor.peek().isEmpty())
			Code.fixup(contInFor.peek().remove(0));
		Code.putJump(forPostfixAddr.pop());
	}
	
	@Override
	public void visit(Statement_for forEnd) {
		// ako condition nije ispunjen, skace se ovde (iza for-a)
		if(!jumpToElse.empty())
			Code.fixup(jumpToElse.pop());
		// ako se nasao break unutar for-a
		for(int addr : breakInFor.pop()) {
	        Code.fixup(addr);
	    }
		
		contInFor.pop();
		scopes.pop();
	}

}
