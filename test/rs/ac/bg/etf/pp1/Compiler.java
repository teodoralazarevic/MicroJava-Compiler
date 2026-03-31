package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.mj.runtime.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class Compiler {
	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	private static Logger log = Logger.getLogger(Compiler.class);
	
	public static void tsdump() {
		log.info("===================================");
		Tab.dump();
	}
	
	public static void main(String[] args) throws Exception {
		
		
		if(args.length<2) {
			log.error("Unesite parametre komandne linije!");
			return;
		}
		
		Reader br = null;
		try {
			
			String sourceCodePath = args[0];
			String objFilePath = args[1];
			
			File sourceCode = new File(sourceCodePath);
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			//forming AST
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  
	        
	        Program prog = (Program)(s.value); 
	        
			// printing AST
			log.info(prog.toString(""));
			log.info("===================================");
			
			//symbol table initialization
			Tab.init();
			Struct boolType = new Struct(Struct.Bool); //adding bool type to symbol table	
			Obj boolObj = Tab.insert(Obj.Type, "bool", boolType); 
			boolObj.setAdr(-1);
			boolObj.setLevel(-1);
			
			//semantic analize
			SemAnalyzer semAnalyzer = new SemAnalyzer();
			prog.traverseBottomUp(semAnalyzer); 
			
			//printing symbol table
			tsdump();
			
			
			if(!p.errorDetected && semAnalyzer.passed()) {
				log.info("Parsiranje uspesno zavrseno!");
				
				// pravljenje objektnog fajla, u njega ce se upisati bytecode
				File objFile = new File(objFilePath);
				if(objFile.exists())
					objFile.delete();
				
				CodeGenerator codeGenerator = new CodeGenerator();
				prog.traverseBottomUp(codeGenerator);
				Code.dataSize = semAnalyzer.getNVars();
				Code.mainPc = codeGenerator.getMainPc();
				Code.write(new FileOutputStream(objFile));
			}
			else
				log.info("Parsiranje NIJE uspesno zavrseno!");

//			// ispis prepoznatih programskih konstrukcija
//			RuleVisitor v = new RuleVisitor();
//			prog.traverseBottomUp(v); 
//	      
//			log.info(" Print count calls = " + v.printCallCount);
//
//			log.info(" Deklarisanih promenljivih ima = " + v.varDeclCount);
			
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}

}
