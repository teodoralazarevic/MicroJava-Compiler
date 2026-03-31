package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemAnalyzer extends VisitorAdaptor {
	
	private boolean errorDetected = false;
	private boolean returnHappened = false;
	private int switchCnt = 0;
	Logger log = Logger.getLogger(getClass());
	private Obj currentProgram;
	private Struct currentType;
	private int constant;
	private Struct constantType;
	private Struct boolType = Tab.find("bool").getType();
	private Struct currentMethReturn;
	private Obj currentMethod;
	private Obj mainMethod=null;
	
	private Obj currentEnum;
	private int lastEnumVal=0; //poslednja vrednost dodeljena clanu enuma 
	private Set<Integer> enumVals; //vrednosti konstanti koje su do sada ubacene u enum
	
	// stack skupova, po jedan skup za svaki ugnjezdeni switch case
	private Stack<Set<Integer>> caseVals=new Stack<Set<Integer>>(); 
	
	//private Set<Integer> caseVals; //vrednosti case grana u switch naredbi
	
	private int forCnt = 0; //broji ugnezdene for petlje
	private int nVars;
	

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder();
		int line = (info==null) ? 0 : info.getLine();
		if(line!=0)
			msg.append("Pretraga na ").append(line).append(" ").append(message);
		log.info(msg.toString());
			
		/*StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());*/
	}
	
	public boolean passed() {
		return !errorDetected;
	}
	
	private String getKindName(int i) {
		switch(i) {
			case 0:
				return "Con";
			case 1:
				return "Var";
			case 2:
				return "Type";
			case 3:
				return "Meth";
			case 4:
				return "Fld";
			case 5:
				return "Elem";
			case 6:
				return "Prog";
		}
		return "";
	}
	
	private String getTypeName(int i) {
		switch(i) {
			case 0:
				return "None";
			case 1:
				return "Int";
			case 2:
				return "Char";
			case 3:
				return "Array";
			case 4:
				return "Class";
			case 5:
				return "Bool";
			case 6:
				return "Enum";
		}
	return "";
	}
	
	/*SEMANTIC PASS CODE*/
	
	//ovo se prvo poziva (najlevlja smena)
	@Override
	public void visit(ProgramName programName) {
		currentProgram = Tab.insert(Obj.Prog, programName.getI1(), Tab.noType);
		// postavljanje fpPos na 1 za lokalne simbole za metode chr, ord i len
		
		Obj methodChr = Tab.find("chr");
		Collection<Obj> localsChr = methodChr.getLocalSymbols();
		for(Obj obj:localsChr) {
			obj.setFpPos(1);
		}
		
		Obj methodOrd = Tab.find("ord");
		Collection<Obj> localsOrd = methodOrd.getLocalSymbols();
		for(Obj obj:localsOrd) {
			obj.setFpPos(1);
		}
		
		Obj methodLen = Tab.find("len");
		Collection<Obj> localsLen = methodLen.getLocalSymbols();
		for(Obj obj:localsLen) {
			obj.setFpPos(1);
		}
		
		Tab.openScope();
	}
	
	//ovo se poslednje poziva (zatvaramo program scope)
	@Override
	public void visit(Program program) {
		// brojimo globalne podatke (da bismo znali codeSize)
		nVars = Tab.currentScope().getnVars(); //broj VARIJABLI u scope-u (globalnom)
		
		Tab.chainLocalSymbols(currentProgram);
		Tab.closeScope();
		currentProgram = null;
		
		//ako je getLevel > 0 onda znaci da metoda ima parametre!
		if(mainMethod==null || mainMethod.getLevel()>0) {
			report_error("Program nema ispravnu main metodu!", program);
		}
	}
	
	public int getNVars() {
		return nVars;
	}
	
	/*CONST DECLARATIONS*/
	
	@Override
	public void visit(ConstDecl constDecl) {
		// provera da li ta konstanta vec postoji
		Obj conObj = Tab.find(constDecl.getI1());
		if(conObj != Tab.noObj) {
			report_error("Dvostruka definicija konstante: "+constDecl.getI1(), constDecl);
		}
		else {
			//provera da li je tip vrednosti i tip konstante isti
			if(constantType.assignableTo(currentType)) {
				conObj = Tab.insert(Obj.Con, constDecl.getI1(), currentType);
				// u addr polje konstante se upisuje vrednost te konstante (to je prethodno obidjeno i sacuvano u constant)
				conObj.setAdr(constant); 
			}
			else {
				report_error("Neadekvatna dodela konstanti: "+ constDecl.getI1(), constDecl);
			}
		}
	}
	
	
	/* vrednosti konstanti i tip, moramo da znamo kada se konstanti posle dodeljuje vrednost da li su vrednost i konstanta
	 * istog tipa*/
	@Override
	public void visit(Constant1 const_number) {
		constant = const_number.getN1();
		constantType = Tab.intType;
	}
	
	@Override
	public void visit(Constant2 const_char) {
		constant = const_char.getC1();
		constantType = Tab.charType;
	}
	
	@Override
	public void visit(Constant3 const_bool) {
		constant = const_bool.getB1();
		constantType = boolType;
	}
	
	
	/*VAR DECLARATIONS*/
	
	//deklaracija globalne promenljive
	@Override
	public void visit(VarDecl1 varDecl_var) {
		//pretrazujemo samo u trenutnom scope-u to ime!
		//lokalna i globalna mogu da imaju isto ime
		Obj varObj = null;
		if(currentMethod==null) //onda sam u globalnom scope-u
			varObj = Tab.find(varDecl_var.getI1());
		else //onda sam u lokalnom scope-u, gledam samo trenutni scope
			varObj = Tab.currentScope().findSymbol(varDecl_var.getI1()); //vraca null ako ne pronadje nista

		
		if(varObj==null || varObj==Tab.noObj) {
			varObj = Tab.insert(Obj.Var, varDecl_var.getI1(), currentType);
		}
		else if(varObj!=Tab.noObj){
			report_error("Dvostruka definicija promenljive: "+varDecl_var.getI1(), varDecl_var);
		}
	}
	
	//deklaracija niza
	@Override
	public void visit(VarDecl2 varDecl_array) {
		Obj varObj = null;
		if(currentMethod==null) //onda sam u globalnom scope-u
			varObj = Tab.find(varDecl_array.getI1());
		else //onda sam u lokalnom scope-u, gledam samo trenutni scope
			varObj = Tab.currentScope().findSymbol(varDecl_array.getI1()); //vraca null ako ne pronadje nista
		
		if(varObj==null || varObj==Tab.noObj) {
			varObj = Tab.insert(Obj.Var, varDecl_array.getI1(), new Struct(Struct.Array, currentType));
		}
		else if(varObj==Tab.noObj){
			report_error("Dvostruka definicija promenljive: "+varDecl_array.getI1(), varDecl_array);
		}
	}
	
	
	/* ENUM DECLARATIONS */
	
	@Override
	public void visit(EnumName enumName) {
		Obj enumObj = Tab.find(enumName.getI1());
		if(enumObj!=Tab.noObj) {
			report_error("Dvostruka definicija enuma "+enumName.getI1(), enumName);
		}
		else {
			lastEnumVal = 0;
			enumVals=new HashSet<Integer>(); //kreira se set za ovaj enum
			Struct enumType = new Struct(Struct.Enum);
			currentEnum = Tab.insert(Obj.Type, enumName.getI1(), enumType);
			Tab.openScope();
		}
	}
	
	//enum element bez dodeljene vrednosti
	@Override
	public void visit(EnumDecl1 enumWithoutVal) {
		//ime clana enum-a mora da bude jedinstveno na nivou jednog enum-a (tog scope-a)
		Obj enumElemObj = Tab.currentScope().findSymbol(enumWithoutVal.getI1());
		if(enumElemObj!=null) {
			report_error("Konstanta "+enumWithoutVal.getI1()+" unutar enum-a mora biti jedinstvena!", enumWithoutVal);		}
		else {
			//clan enum-a se tretira kao konstanta tipa int, posto je ovo enum clan bez eksplicitno
			//dodeljene vrednosti, dodeljuje mu se vrednost lastEnumVal za taj enum
			
			int predictedValue = lastEnumVal++;
			if(enumVals.contains(predictedValue)) {
				report_error("Vrednosti konstante unutar enum-a moraju biti jedinstvene! Duplikat vrednost "+predictedValue, enumWithoutVal);
			}
			else {
				enumElemObj = Tab.insert(Obj.Con, enumWithoutVal.getI1(), Tab.intType);
				enumElemObj.setLevel(2);		
				enumElemObj.setAdr(predictedValue); // u fp pos se upisuje vrednost enuma
				enumVals.add(predictedValue);
			}
		}
	}
	
	//enum element sa dodeljenom vrednoscu
	@Override
	public void visit(EnumDecl2 enumWithVal) {
		//ime clana enum-a mora da bude jedinstveno na nivou jednog enum-a (tog scope-a)
		Obj enumElemObj = Tab.currentScope().findSymbol(enumWithVal.getI1());
		if(enumElemObj!=null) {
			report_error("Konstanta "+enumWithVal.getI1()+" unutar enum-a mora biti jedinstvena!", enumWithVal);
		}
		else {
			//clan enum-a se tretira kao konstanta tipa int, posto je ovo enum clan sa eksplicitno dodeljenom vrednoscu
			//dodeljuje mu se ta vrednost, i to postaje nova poslednja u enumu
			
			int predictedVal = enumWithVal.getN2();
			if(enumVals.contains(predictedVal)) {
				report_error("Vrednosti konstante unutar enum-a moraju biti jedinstvene! Duplikat vrednost "+predictedVal, enumWithVal);
			}
			else {			
				enumElemObj = Tab.insert(Obj.Con, enumWithVal.getI1(), Tab.intType);
				enumElemObj.setLevel(2);
				enumElemObj.setAdr(enumWithVal.getN2());
				lastEnumVal = enumWithVal.getN2()+1;
				enumVals.add(predictedVal);
			}
		}
	}
	
	@Override
	public void visit(EnumDeclList enumEnd) {
		//ovde se koristi Tab.chainLocalSymbols(Struct)
		//Tab.chainLocalSymbols(currentEnum);
		Tab.chainLocalSymbols(currentEnum);
		Tab.closeScope();
		currentEnum=null;
		lastEnumVal = 0;
		enumVals=null;
	}
	
	/* METHOD DECLARATIONS */
	
	//povratna vrednost metode je tip
	@Override
	public void visit(MethodOpt1 methodReturnType) {
		currentMethReturn = currentType;
		Obj methObj = Tab.find(methodReturnType.getI2());
		if(methObj != Tab.noObj) {
			report_error("Dvostruka definicija metode: "+methodReturnType.getI2(), methodReturnType);
		}
		else {
			methodReturnType.obj = currentMethod = Tab.insert(Obj.Meth, methodReturnType.getI2(), currentMethReturn);
			Tab.openScope();
			
			//kreirali smo metodu, sada proveravamo da li je main
			if(methodReturnType.getI2().equals("main")) {
				mainMethod = currentMethod;
				
				if(currentMethReturn!=Tab.noType) {
					report_error("Main metoda ne sme da ima povratnu vrednost!", methodReturnType);
				}
			}
		}
	}
	
	//povratna vrednost metode je void
	@Override
	public void visit(MethodOpt2 methodReturnVoid) {
		currentMethReturn = Tab.noType;
		Obj methObj = Tab.find(methodReturnVoid.getI1());
		if(methObj != Tab.noObj) {
			report_error("Dvostruka definicija metode: "+methodReturnVoid.getI1(), methodReturnVoid);
		}
		else {
			methodReturnVoid.obj = currentMethod = Tab.insert(Obj.Meth, methodReturnVoid.getI1(), currentMethReturn);
			Tab.openScope();
			
			//kreirali smo metodu, sada proveravamo da li je main
			if(methodReturnVoid.getI1().equals("main")) {
				mainMethod = currentMethod;
				
				if(currentMethReturn!=Tab.noType) {
					report_error("Main metoda ne sme da ima povratnu vrednost!", methodReturnVoid);
				}
			}
		}
	}
	
	/*@Override
	public void visit(MethodName methodName) {
		Obj methObj = Tab.find(methodName.getI1());
		if(methObj != Tab.noObj) {
			report_error("Dvostruka definicija metode: "+methodName.getI1(), methodName);
		}
		else {
			methodName.obj = currentMethod = Tab.insert(Obj.Meth, methodName.getI1(), currentMethReturn);
			Tab.openScope();
			
			//kreirali smo metodu, sada proveravamo da li je main
			if(methodName.getI1().equals("main")) {
				mainMethod = currentMethod;
				
				if(currentMethReturn!=Tab.noType) {
					report_error("Main metoda ne sme da ima povratnu vrednost!", methodName);
				}
			}
		}
	}*/
	
	/*FORM PAR DECLARATIONS*/
	
	//parametar koji je promenljiva
	@Override
	public void visit(FormPar1 formParVar) {
		Obj par_var = null;
		if(currentMethod==null)
			report_error("Greska! [FormPar1]", formParVar);
		else
			par_var = Tab.currentScope().findSymbol(formParVar.getI2()); //pretrazujemo trenutni scope (unutar metode smo)
		if(par_var==null || par_var==Tab.noObj) {
			par_var = Tab.insert(Obj.Var, formParVar.getI2(), currentType);
			par_var.setFpPos(1);
			// jer u metodi level predstavlja broj formalnih parametara, sada smo prepoznali jos jedan parametar
			currentMethod.setLevel(currentMethod.getLevel()+1);
		}
		else {
			report_error("Dvostruka definicija formalnog parametra "+formParVar.getI2(), formParVar);
		}
		
	}
	
	//parametar koji je niz
	@Override
	public void visit(FormPar2 formParArray) {
		Obj par_var = null;
		if(currentMethod==null)
			report_error("Greska! [FormPar2]", formParArray);
		else
			par_var = Tab.currentScope().findSymbol(formParArray.getI2()); //pretrazujemo trenutni scope (unutar metode smo)
		if(par_var==null || par_var==Tab.noObj) {
			par_var = Tab.insert(Obj.Var, formParArray.getI2(), new Struct(Struct.Array, currentType));
			par_var.setFpPos(1);
			// jer u metodi level predstavlja broj formalnih parametara, sada smo prepoznali jos jedan parametar
			currentMethod.setLevel(currentMethod.getLevel()+1);
		}
		else {
			report_error("Dvostruka definicija formalnog parametra "+formParArray.getI2(), formParArray);
		}
		
	}
	
	@Override
	public void visit(MethodDecl methodEnd) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		// ako nije void i nije se desio return
		if(currentMethod.getType()!=Tab.noType && !returnHappened) {
			report_error("Ne postoji return u metodi "+currentMethod.getName(), methodEnd);
		}
		returnHappened = false;
		currentMethod = null;
	}
	
	@Override
	public void visit(Type type) {
		//provera jel tip postojeci (da li se vec nalazi objekat sa tim imenom u tabeli simbola)
		Obj typeObj = Tab.find(type.getI1());
		if(typeObj == Tab.noObj) {
			report_error("Nepostojeci tip podatka: "+type.getI1(), type);
			type.struct = currentType = Tab.noType;
		}
		else if(typeObj.getKind() != Obj.Type) { //mora da bude tip tog objektnog cvora Type!!
			report_error("Neadekvatan tip podatka: " +type.getI1(), type);
			type.struct = currentType = Tab.noType;
		}
		else {
			type.struct = currentType = typeObj.getType();
		}
	}
	
	
	/*CONTEXT REQUESTS*/
	
	/*Factor sub*/
	
	// factor character	
	@Override
	public void visit(FactorSub4 factorCharacter) {
		factorCharacter.struct = Tab.charType;
	}
	
	// factor number
	@Override
	public void visit(FactorSub3 factorNumber) {
		factorNumber.struct = Tab.intType;
	}
	
	// factor bool
	@Override
	public void visit(FactorSub5 factorBool) {
		factorBool.struct = boolType;
	}
	
	// factor sub designator
	@Override
	public void visit(FactorSub1 factorDesignator) {
		// tip designatora postaje tipa factora 
		// designator je obidjen pre ove smene
		if(factorDesignator.getDesignator().obj.getType().getKind()==Struct.Enum)
			factorDesignator.struct = Tab.find("int").getType();
		else
			factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
	}
	
	// factor sub method
	@Override
	public void visit(FactorSub2 factorMethod) {
		Obj designatorObj = factorMethod.getDesignator().obj;
		if(designatorObj.getKind()!=Obj.Meth) {
			report_error("Poziv neadekvatne metode "+designatorObj.getName(), factorMethod);
			factorMethod.struct = Tab.noType;
		}
		else {
			// tip ce biti povratna vrednost metode
			factorMethod.struct = factorMethod.getDesignator().obj.getType();
			// proveravamo da li poziv metode zadovoljava njene parametre
			List<Struct> formalParams = new ArrayList<Struct>();
			// prolazimo kroz sve lokalne simbole metode koju pozivamo i izdavajamo parametre
			// parametri imaju polje fp=1
			for(Obj local:factorMethod.getDesignator().obj.getLocalSymbols()) {
				if(local.getFpPos()==1 && local.getKind()==Obj.Var && local.getLevel()==1) {
					formalParams.add(local.getType());
				}
			}
			
			ActParamsAnalyzer acAnalyzer = new ActParamsAnalyzer();
			factorMethod.getActParList().traverseBottomUp(acAnalyzer);
			List<Struct> actualParams = acAnalyzer.getListOfActualParams();
			
			Obj methodObj = factorMethod.getDesignator().obj;
			
			if(formalParams.size()!=actualParams.size()) {
				report_error("Neispravan poziv metode "+methodObj.getName(), factorMethod);
			}
			else {
				for(int i=0;i<formalParams.size();i++) {
					// ako nije dodeljiv stvarni parametar formarnom
					if(!actualParams.get(i).assignableTo(formalParams.get(i))) {
						report_error("Neispravan poziv metode "+methodObj.getName(), factorMethod);
					}
				}
				report_info("("+methodObj.getName()+"), nadjeno "+getKindName(methodObj.getKind())+" "+methodObj.getName()+
						": "+getTypeName(methodObj.getType().getKind())+", "+methodObj.getFpPos()+", "+methodObj.getLevel(), factorMethod);
				/*report_info("Poziv globalne metode "+methodObj.getName()+". [Kind: "+methodObj.getKind() +
						" | Level: "+methodObj.getLevel()+" | FpPos: "+methodObj.getFpPos()+
						" | Local symbols:"+methodObj.getLocalSymbols()+"]", factorMethod);*/
			
			}
			
		}
	}
	
	// factor sub new
	@Override
	public void visit(FactorSub6 factorNewArray) {
		// provera da li je expr int
		if(!factorNewArray.getExpr().struct.equals(Tab.intType)) {
			report_error("Kreiranje niza sa neispravnom velicinom", factorNewArray);
			factorNewArray.struct = Tab.noType;
		}
		else {
			factorNewArray.struct = new Struct(Struct.Array, currentType);
		}
	}
	
	// factor (expr)
	@Override
	public void visit(FactorSub7 factorExpr) {
		// tip ce biti tip zagrade
		factorExpr.struct = factorExpr.getExpr().struct;
	}
	
	
	/*Designator*/
	
	
	@Override 
	public void visit(ArrayNameBracket designArrayName) {
		// provera jel postoji u tabeli simbola
		Obj arrayObj = Tab.find(designArrayName.getI1());
		if(arrayObj==Tab.noObj) {
			report_error("Pristup nedefinisanom nizu "+designArrayName.getI1(), designArrayName);
			designArrayName.obj = Tab.noObj;
		}
		else {
			// niz je kind var a njegov tip je kind Array
			if(arrayObj.getKind()!=Obj.Var || arrayObj.getType().getKind()!=Struct.Array) {
				report_error("Pristup neispravnoj promenljivi niza "+designArrayName.getI1(), designArrayName);
				designArrayName.obj = Tab.noObj;
			}
			else {
				designArrayName.obj = arrayObj;
			}
		}
	}
	
	@Override 
	public void visit(ArrayNameLenght designArrayName) {
		// provera jel postoji u tabeli simbola
		Obj arrayObj = Tab.find(designArrayName.getI1());
		if(arrayObj==Tab.noObj) {
			report_error("Trazenje duzine nedefinisanom nizu "+designArrayName.getI1(), designArrayName);
			designArrayName.obj = Tab.noObj;
		}
		else {
			// niz je kind var a njegov tip je kind Array
			if(arrayObj.getKind()!=Obj.Var || arrayObj.getType().getKind()!=Struct.Array) {
				report_error("Pristup neispravnoj promenljivi niza "+designArrayName.getI1(), designArrayName);
				designArrayName.obj = Tab.noObj;
			}
			else {
				designArrayName.obj = arrayObj;
				if(arrayObj.getLevel()==0) {
					report_info("("+arrayObj.getName()+"), nadjeno "+getKindName(arrayObj.getKind())+" "+arrayObj.getName()+
							": "+getTypeName(arrayObj.getType().getKind())+", "+arrayObj.getFpPos()+", "+arrayObj.getLevel(), designArrayName);
					/*report_info("Pristup globalnom nizu "+arrayObj.getName()+". [Kind: "+arrayObj.getKind() +
						" | Level: "+arrayObj.getLevel()+" | FpPos: "+arrayObj.getFpPos()+
						" | Local symbols:"+arrayObj.getLocalSymbols()+"]", designArrayName);*/
			
				}
				else if(arrayObj.getLevel()==1) {
					if(arrayObj.getFpPos()!=1) {
						report_info("("+arrayObj.getName()+"), nadjeno "+getKindName(arrayObj.getKind())+" "+arrayObj.getName()+
								": "+getTypeName(arrayObj.getType().getKind())+", "+arrayObj.getFpPos()+", "+arrayObj.getLevel(), designArrayName);
						/*report_info("Pristup lokalnom nizu "+arrayObj.getName()+". [Kind: "+arrayObj.getKind() +
							" | Level: "+arrayObj.getLevel()+" | FpPos: "+arrayObj.getFpPos()+
							" | Local symbols:"+arrayObj.getLocalSymbols()+"]", designArrayName);*/
					}
					else {
						report_info("("+arrayObj.getName()+"), nadjeno "+getKindName(arrayObj.getKind())+" "+arrayObj.getName()+
								": "+getTypeName(arrayObj.getType().getKind())+", "+arrayObj.getFpPos()+", "+arrayObj.getLevel(), designArrayName);
						/*report_info("Pristup formalnom parametru "+arrayObj.getName()+" funkcije. [Kind: "+arrayObj.getKind() +
								" | Level: "+arrayObj.getLevel()+" | FpPos: "+arrayObj.getFpPos()+
								" | Local symbols:"+arrayObj.getLocalSymbols()+"]", designArrayName);*/
					}
				}
			}
		}
	}
	
	
	
	@Override
	public void visit(Designator1 designIdent) {
		// provera da li promenljiva postoji
		Obj designObj = Tab.find(designIdent.getI1());
		if(designObj==Tab.noObj) {
			report_error("Promenljiva kojoj se pristupa nije definisana: "+designIdent.getI1(), designIdent);
			designIdent.obj = Tab.noObj;
		}
		else {
			// provera da li je odgovarajuceg tipa
			// Designator moze da se nadje kao Factor sa desne strange -> Var, Meth, Con, Elem
		    // Designator moze da se nadje u DesignatorStatement
			//		- Designator = Expr (Var, Elem)
			//		- Designator(params) (Meth)
			//		- Designator++/-- (Var, Elem)
			if(designObj.getKind()!=Obj.Var && designObj.getKind()!=Obj.Meth && 
					designObj.getKind()!=Obj.Con && designObj.getKind()!=Obj.Elem) {
				report_error("Promenljiva "+designIdent.getI1()+" nije odgovarajuceg tipa", designIdent);
				designIdent.obj = Tab.noObj;
			}
			else {
				designIdent.obj = designObj;
				if(designObj.getLevel()==0 && designObj.getKind()==Obj.Var) {
					report_info("("+designObj.getName()+"), nadjeno "+getKindName(designObj.getKind())+" "+designObj.getName()+
							": "+getTypeName(designObj.getType().getKind())+", "+designObj.getFpPos()+", "+designObj.getLevel(), designIdent);
					/*report_info("Pristup globalnoj promenljivoj "+designIdent.getI1()+". [Kind: "+designObj.getKind()+" |"
							+ " Level: "+designObj.getLevel()+" | FpPos: "+designObj.getFpPos()+ " | "+
							"Local symbols: "+designObj.getLocalSymbols()+"]", designIdent);*/
				}
				else if(designObj.getLevel()==1 && designObj.getKind()==Obj.Var) {
					if(designObj.getFpPos()!=1) {
						report_info("("+designObj.getName()+"), nadjeno "+getKindName(designObj.getKind())+" "+designObj.getName()+
								": "+getTypeName(designObj.getType().getKind())+", "+designObj.getFpPos()+", "+designObj.getLevel(), designIdent);
					/*report_info("Pristup lokalnoj promenljivoj "+designIdent.getI1()+". [Kind: "+designObj.getKind()+" |"
							+ " Level: "+designObj.getLevel()+" | FpPos: "+designObj.getFpPos()+ " | "+
							"Local symbols: "+designObj.getLocalSymbols()+"]", designIdent);*/
					}
					else {
						report_info("("+designObj.getName()+"), nadjeno "+getKindName(designObj.getKind())+" "+designObj.getName()+
								": "+getTypeName(designObj.getType().getKind())+", "+designObj.getFpPos()+", "+designObj.getLevel(), designIdent);
						/*report_info("Pristup formalnom parametru "+designIdent.getI1()+" funkcije. [Kind: "+designObj.getKind()+" |"
								+ " Level: "+designObj.getLevel()+" | FpPos: "+designObj.getFpPos()+ " | "+
								"Local symbols: "+designObj.getLocalSymbols()+"]", designIdent);*/
					}
				}
			}
		}	
	}
	
	@Override
	public void visit(Designator2 designatorIdent) {
		// provera da li promenljiva kojoj se pristupa . postoji i da li je tipa enum
		Obj enumObj = Tab.find(designatorIdent.getI1());
		if(enumObj==Tab.noObj) {
			report_error("Promenljiva "+designatorIdent.getI1()+" kojoj se pristupa nije definisana", designatorIdent);
			designatorIdent.obj = Tab.noObj;
		}
		else if(enumObj.getType().getKind()!=Struct.Enum) {
			report_error("Promenljiva "+designatorIdent.getI1()+" kojoj se pristupa nije tipa enum", designatorIdent);
			designatorIdent.obj = Tab.noObj;
		}
		else {
			Collection<Obj> constObjects = enumObj.getLocalSymbols();
			boolean fieldFound = false; //da li je pronadjeno polje kome se pristupa u tom enumu
			Obj field=null;
			for(Obj o:constObjects) {
				if(o.getName().equals(designatorIdent.getI2()) && o.getKind()==Obj.Con && o.getLevel()==2) {
					fieldFound=true;
					field=o;
				}
			}
			if(!fieldFound) {
				report_error("Polje enuma "+designatorIdent.getI2()+" kome se pristupa nije definisano", designatorIdent);
				designatorIdent.obj = Tab.noObj;
			}
			else {
				designatorIdent.obj = field;
				//designatorIdent.obj = Tab.find("int");
				report_info("("+enumObj.getName()+"), nadjeno "+getKindName(enumObj.getKind())+" "+enumObj.getName()+
						": "+getTypeName(enumObj.getType().getKind())+", "+enumObj.getFpPos()+", "+enumObj.getLevel(), designatorIdent);
				/*report_info("Pristup simolickoj konstanti "+enumObj.getName()+". [Kind: "+enumObj.getKind() +
						" | Level: "+enumObj.getLevel()+" | FpPos: "+enumObj.getFpPos()+
						" | Local symbols:"+enumObj.getLocalSymbols()+"]", designatorIdent);*/
			}
		}
		
	}
	
	@Override
	public void visit(Designator3 designLength) {
		// provera je l ident tipa niz
		String arrayName = designLength.getArrayNameLenght().getI1();
		Obj arrayObj = Tab.find(arrayName);
		if(arrayObj==Tab.noObj) {
			designLength.obj = Tab.noObj;
		}
		else if(arrayObj.getKind()!=Obj.Var || arrayObj.getType().getKind()!=Struct.Array) {
			//report_error("Pristup neispravnoj promenljivi niza "+arrayName, designLength);
			designLength.obj = Tab.noObj;
		}
		else {
			// to je na dalje vrednost tipa int
			//MENJALA SAM
			//designLength.obj = new Obj(Obj.Con, arrayObj.getName() + ".length", Tab.intType);
			designLength.obj = Tab.find("int");
		}
	}
	
	
	@Override
	public void visit(Designator4 designElemArray) {
		// dohvata objekat svog sina
		Obj objArray = designElemArray.getArrayNameBracket().obj;
		if(objArray==Tab.noObj) {
			designElemArray.obj = Tab.noObj;
		}
		else {
			// niz je validan, iznad sebe prosledjujem element
			// provera da li je ono cime se indeksira tipa int
			if(!designElemArray.getExpr().struct.equals(Tab.intType)) {
				report_error("Indeksiranje niza "+designElemArray.getArrayNameBracket().getI1() 
						+" vrednoscu koja nije celobrojna", designElemArray);
				designElemArray.obj = Tab.noObj;
			}
			else {
				designElemArray.obj = new Obj(Obj.Elem, objArray.getName()+"[$]", objArray.getType().getElemType());
				report_info("("+objArray.getName()+"), nadjeno "+getKindName(objArray.getKind())+" "+objArray.getName()+
						": "+getTypeName(objArray.getType().getKind())+", "+objArray.getFpPos()+", "+objArray.getLevel(), designElemArray);
				/*report_info("Pristup elementu niza "+objArray.getName()+". [Kind: "+objArray.getKind() +
						" | Level: "+objArray.getLevel()+" | FpPos: "+objArray.getFpPos()+
						" | Local symbols:"+objArray.getLocalSymbols()+"]", designElemArray);*/
			}
		}
	}
	
	/*Factor*/
	
	@Override
	public void visit(Factor factor) {
		
		if(factor.getUnary() instanceof Unary1) { //minus before Factor is used
			// ako je ono sto je negirano tipa inta, sve je u redu, ceo rezultat je tipa int
			if(factor.getFactorSub().struct.equals(Tab.intType)) {
				factor.struct = Tab.intType; 
			}
			else { // javio se minus ali negirana vrednost nije int
				report_error("Pokusaj negiranja vrednosti koja nije tipa int!", factor);
				factor.struct = Tab.noType;
			}
		}
		else {
			// minus se nije desio, tip ostaje isti koji god da je
			factor.struct = factor.getFactorSub().struct;
		}
	}
	
	
	/*EXPR*/
	
	@Override
	public void visit(MoreTerm2 termFactor) {
		termFactor.struct = termFactor.getFactor().struct;
	}
	
	@Override
	public void visit(MoreTerm1 mulList) {
		// mnozenje, leva i desna strana treba da budu int
		/*report_info("AAA:"+mulList.getMoreTerm().struct.getKind(), mulList);
		report_info("BBB:"+mulList.getFactor().struct.getKind(), mulList);*/
		if(mulList.getMoreTerm().struct.equals(Tab.intType) && 
				mulList.getFactor().struct.equals(Tab.intType)) {
			mulList.struct = Tab.intType;
		}
		else {
			report_error("Operandi za mnozenje moraju biti int", mulList);
			mulList.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(Term term) {
		term.struct = term.getMoreTerm().struct;
	}
	
	@Override
	public void visit(AddopTermList2 term) {
		term.struct = term.getTerm().struct;
	}
	
	@Override
	public void visit(AddopTermList1 addopList) {
		// sabiranje, leva i desna strana treba da budu int ili enum
		if(addopList.getAddopTermList().struct.equals(Tab.intType) && 
				addopList.getTerm().struct.equals(Tab.intType) && 
				addopList.getAddopTermList().struct.compatibleWith(addopList.getTerm().struct)) {
			addopList.struct = Tab.intType;
		}
		else {
			report_error("Operandi za sabiranje moraju biti int", addopList);
			addopList.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(Expr1 addopTermList) {
		addopTermList.struct = addopTermList.getAddopTermList().struct;
	}
	
	@Override
	public void visit(Expr2 ternaryExpr) {
		// drugi i treci izraz moraju biti istog tipa
		if(!ternaryExpr.getExpr().struct.equals(ternaryExpr.getExpr1().struct)) {
			report_error("Drugi i treci izraz u ternarnom operatoru moraju biti istog tipa", ternaryExpr);
			ternaryExpr.struct = Tab.noType;
		}
		else {
			ternaryExpr.struct = ternaryExpr.getExpr().struct;
		}
	}
	
	/*DESIGNATOR STATEMENT*/
	
	// naredba dodela
	@Override
	public void visit(DesignatorStatement1 designStm) {
		Obj designatorObj = designStm.getDesignator().obj;
		Struct exprStruct = designStm.getExpr().struct;
		// designator mora oznacavati promenljivu ili element niza
		if(!(designatorObj.getKind()==Obj.Var || designatorObj.getKind()==Obj.Elem)) {
			report_error("Sa leve strane naredbe dodele nije ni promenljiva ni element niza "
					+designatorObj.getName(), designStm);
		}
		else if(!exprStruct.assignableTo(designatorObj.getType()) && 
				!(exprStruct.equals(Tab.find("int").getType()) && designatorObj.getType().getKind()==Struct.Enum)){
			// tip neterminala Expr mora biti kompatibilan sa tip Designator
			report_error("Leva i desna strana naredbe dodele nisu kompatibilne. Neadekvatna dodela u promenljivu "
						+designatorObj.getName(), designStm);
		}
	} 
	
	// inkrementiranje
	@Override
	public void visit(DesignatorStatement3 designInc) {
		// designator mora biti promenljiva ili element niza
		Obj designatorObj = designInc.getDesignator().obj;
		if(designatorObj.getKind()!=Obj.Var && designatorObj.getKind()!=Obj.Elem) {
			report_error("Pokusaj inkrementiranja pogresne promenljive "+designatorObj.getName(), designInc);
		}
		else if(!designatorObj.getType().equals(Tab.intType)) {
			// designator mora biti tipa int
			report_error("Pokusaj inkrementiranja promenljive "+designatorObj.getName()+" koja nije celobrojna", designInc);
		}
	}
	
	// inkrementiranje
	@Override
	public void visit(DesignatorStatement4 designDec) {
		// designator mora biti promenljiva ili element niza
		Obj designatorObj = designDec.getDesignator().obj;
		if(designatorObj.getKind()!=Obj.Var && designatorObj.getKind()!=Obj.Elem) {
			report_error("Pokusaj inkrementiranja pogresne promenljive "+designatorObj.getName(), designDec);
		}
		else if(!designatorObj.getType().equals(Tab.intType)) {
			// designator mora biti tipa int
			report_error("Pokusaj inkrementiranja promenljive "+designatorObj.getName()+" koja nije celobrojna", designDec);
		}
	}
	
	// poziv metode
	@Override
	public void visit(DesignatorStatement2 designMeth) {
		// designator mora biti globalna funkcija
		Obj objDesignator = designMeth.getDesignator().obj;
		if(objDesignator.getKind()!=Obj.Meth) {
			report_error("Poziv neadekvatne metode "+ objDesignator.getName(), designMeth);
		}
		else {
			// proveravamo da li poziv metode zadovoljava njene parametre
			List<Struct> formalParams = new ArrayList<Struct>();
			// prolazimo kroz sve lokalne simbole metode koju pozivamo i izdavajamo parametre
			// parametri imaju polje fp=1
			for(Obj local:designMeth.getDesignator().obj.getLocalSymbols()) {
				if(local.getFpPos()==1 && local.getKind()==Obj.Var && local.getLevel()==1) {
					formalParams.add(local.getType());
				}
			}
			
			ActParamsAnalyzer acAnalyzer = new ActParamsAnalyzer();
			designMeth.getActParList().traverseBottomUp(acAnalyzer);
			List<Struct> actualParams = acAnalyzer.getListOfActualParams();
			
			Obj methodObj = designMeth.getDesignator().obj;
			
			if(formalParams.size()!=actualParams.size()) {
				report_error("Neispravan poziv metode "+methodObj.getName(), designMeth);
			}
			else {
				for(int i=0;i<formalParams.size();i++) {
					// ako nije dodeljiv stvarni parametar formarnom
					if(!actualParams.get(i).assignableTo(formalParams.get(i))) {
						report_error("Neispravan poziv metode "+methodObj.getName(), designMeth);
					}
				}
				report_info("("+objDesignator.getName()+"), nadjeno "+getKindName(objDesignator.getKind())+" "+objDesignator.getName()+
						": "+getTypeName(objDesignator.getType().getKind())+", "+objDesignator.getFpPos()+", "+objDesignator.getLevel(), designMeth);
				/*report_info("Poziv globalne funkcije "+objDesignator.getName()+". [Kind: "+objDesignator.getKind() +
						" | Level: "+objDesignator.getLevel()+" | FpPos: "+objDesignator.getFpPos()+
						" | Local symbols:"+objDesignator.getLocalSymbols()+"]", designMeth);*/
			}
		}
	}

	
	
	/*STATEMENT*/
	
	// read
	@Override
	public void visit(Statement_read readStm) {
		// designator mora biti promenljiva ili element niza
		Obj designObj = readStm.getDesignator().obj;
		Struct designType = designObj.getType();
		if(designObj.getKind()!=Obj.Var && designObj.getKind()!=Obj.Elem) 
		{
			report_error("Pokusaj read operacije nad pogresnim identifikatorom "+designObj.getName(), readStm);
		}
		else if(!(designType.equals(Tab.intType) || designType.equals(Tab.charType) || 
				designType.equals(boolType))) {
			report_error("Pokusaj read operacije nad pogresnim tipom ", readStm);
		}
	}
	
	
	// print
	@Override
	public void visit(Statement_print1 printStm) {
		// Expr mora biti int, char ili bool
		Struct exprStruct = printStm.getExpr().struct;
		if(!(exprStruct.equals(Tab.intType) || exprStruct.equals(Tab.charType) || 
				exprStruct.equals(boolType))) {
			report_error("Pokusaj print operacije nad izrazom pogresnog tipa", printStm);
		}
	}
	
	@Override
	public void visit(Statement_print2 printStm) {
		// Expr mora biti int, char ili bool
		Struct exprStruct = printStm.getExpr().struct;
		if(!(exprStruct.equals(Tab.intType) || exprStruct.equals(Tab.charType) || 
				exprStruct.equals(boolType))) {
			report_error("Pokusaj print operacije nad izrazom pogresnog tipa", printStm);
		}
	}


	// PROVERA DA LI SE RETURN DESIO
	
	@Override
	public void visit(Statement_ret1 returnStm) {
		returnHappened = true;	
		// metoda ima tip a iskoriscen je samo return;
		if(!currentMethod.getType().equals(Tab.noType)) {
			report_error("Nema povratnog tipa u metodi "+currentMethod.getName(), returnStm);
		}
	}
	
	@Override
	public void visit(Statement_ret2 returnStm) {
		returnHappened = true;
		if(!returnStm.getExpr().struct.equals(currentMethod.getType())){
			report_error("Neodgovorajuci tip povratne vrednosti u metodi "+currentMethod.getName(), returnStm);
		}
	}

	/*FOR*/
	
	// for - pocetak petlje
	@Override
	public void visit(For forStm) {
		forCnt++;
	}
	
	
	// for - kraj petlje
	@Override
	public void visit(Statement_for forStm) {
		forCnt--;
		if(!forStm.getConditionOpt().struct.equals(boolType)) {
			report_error("Uslovni izraz u for-u mora biti bool tipa", forStm);
		}
	}
	
	@Override
	public void visit(ConditionOpt1 condition) {
		condition.struct = condition.getCondition().struct;
	}
	
	@Override
	public void visit(ConditionOpt2 condition) {
		condition.struct = boolType;
	}
	
	// break
	@Override
	public void visit(Statement_break breakStm) {
		if(forCnt==0 && switchCnt==0) {
			report_error("Break na pogresnom mestu", breakStm);
		}
	}
	
	// continue
	@Override
	public void visit(Statement_cont contStm) {
		if(forCnt==0) {
			report_error("Continue na pogresnom mestu", contStm);
		}
	}


	/*SWITCH*/
	
	// switch - pocetak
	@Override
	public void visit(Switch switchStm) {
		switchCnt++;
		caseVals.push(new HashSet<Integer>()); // cuvanje jedinstvenih vrednosti samo za ovaj switch
		//caseVals=new HashSet<Integer>();
	}
	
	// switch - kraj
	@Override
	public void visit(Statement_switch switchStm) {
		switchCnt--;;
		//caseVals = null;
		// expr mora biti celobrojnog tipa
		if(!switchStm.getExpr().struct.equals(Tab.intType)) {
			report_error("Izraz u switch naredbi mora biti int tipa ", switchStm);
		}
		caseVals.pop();
	}
		
	// case grana
	@Override
	public void visit(Case caseEdge) {
		int caseVal = caseEdge.getCaseNumber().getN1();
		Set<Integer> setOfaVals = caseVals.peek();
		if(setOfaVals.contains(caseVal)) {
			report_error("Ne sme postojati vise case grana sa istom celobrojnom konstantom "+caseVal, caseEdge);
		}
		else {
			caseVals.peek().add(caseVal);
			//caseVals.add(caseVal);
		}
	}

		
	/*CONDITION*/
	
	// CondRelExpr
	@Override
	public void visit(CondRelExpr2 addopTermList) {
		if(!addopTermList.getAddopTermList().struct.equals(boolType)) {
			report_error("Logicki operand nije odgovarajuceg tipa", addopTermList);
			addopTermList.struct = Tab.noType;
		}
		else {
			addopTermList.struct = boolType;
		}
	}
	
	// CondRelExpr
	@Override
	public void visit(CondRelExpr1 addopTermList) {
		Struct left = addopTermList.getAddopTermList().struct;
		Struct right = addopTermList.getAddopTermList1().struct;
		// da li su compatible ova dva izraza!
		if(!left.compatibleWith(right)) {
			report_error("Logicki operandi nisu odgovarajuceg tipa", addopTermList);
			addopTermList.struct = Tab.noType;
		}
		else {
			// uz promenljive tipa niza moze se od relacionih operatora koristiti samo == i !=
			if(left.isRefType() || right.isRefType()) {
				if(addopTermList.getRelop() instanceof Relop_eq ||
					addopTermList.getRelop() instanceof Relop_neq)
					addopTermList.struct = boolType;
				else {
					report_error("Poredjenje nizova losim operatotorm", addopTermList);
					addopTermList.struct = Tab.noType;
				}
					
			}
			else {
				addopTermList.struct = boolType;
			}
		}
	}
	
	// CondAndExpr
	@Override
	public void visit(CondAndExpr2 condAndExpr) {
		condAndExpr.struct = condAndExpr.getCondRelExpr().struct;
	}
	
	// CondAndExpr
	@Override
	public void visit(CondAndExpr1 condAndExpr) {
		Struct left = condAndExpr.getCondAndExpr().struct;
		Struct right = condAndExpr.getCondRelExpr().struct;
		if(!left.equals(boolType) || !right.equals(boolType)) {
			report_error("Logicki operandi AND operacije nisu odgovarajuceg tipa", condAndExpr);
			condAndExpr.struct = Tab.noType;
		}
		else {
			condAndExpr.struct = boolType;
		}
	}
		
	// CondOrExpr
	@Override
	public void visit(CondOrExpr2 condOrExpr) {
		condOrExpr.struct = condOrExpr.getCondAndExpr().struct;
	}
	
	// CondOrExpr
	@Override
	public void visit(CondOrExpr1 condOrExpr) {
		Struct left = condOrExpr.getCondOrExpr().struct;
		Struct right = condOrExpr.getCondAndExpr().struct;
		if(!left.equals(boolType) || !right.equals(boolType)) {
			report_error("Logicki operandi OR operacije nisu odgovarajuceg tipa", condOrExpr);
			condOrExpr.struct = Tab.noType;
		}
		else {
			condOrExpr.struct = boolType;
		}
	}
	
	// Condition
	@Override
	public void visit(Condition1 condition) {
		condition.struct = condition.getCondOrExpr().struct;
		if(!condition.struct.equals(boolType)) {
			report_error("Uslov nije logickog tipa", condition);
		}
	}
		
}








