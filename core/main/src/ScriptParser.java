
/**
 * 
 * @author joao curcio
 * 
 * Incremental Grammar:
 * 
 * script    := file requires? prohibits? encloses?
 * 
 * filename  := [a-zA-Z]+ ".java" ";"
 * file      := "files"     "{" filename+ "}"
 * requires  := "requires"  "{" clauses*  "}"
 * encloses  := "encloses"  "{" clauses*  "}"
 * prohibits := "prohibits" "{" clauses*  "}"
 * clauses   := (clause-type | clause-returntype | clause-vartype | clause-operator | clause-modifier | clause-import | clause-exception | clause-loop | clause-branch)
 * 
 * clause-type       := "type:" java-type ("," java-type)* ";" 
 * clause-returntype := "returntype:" java-type ("," java-type)* ";" 
 * clause-vartype    := "vartype:" java-type ("," java-type)* ";" 
 * java-type         := ("int" | "double" | "boolean" | "float" | "char" | "byte" | "short" | "long" | "void")
 * clause-modifier   := "modifier:" java-modifier ("," java-modifier)* ";"
 * java-modifier     := ("public" | "private" | "protected" | "static" | "final" | "abstract" | "volatile" | "synchronized" | "class")
 * clause-loop       := "loop:" java-loop ("," java-loop)* ";" 
 * java-loop         := ("while" | "do" | "for" | "foreach" | "break" | "continue")
 * clause-branch     := "branch:" java-branch ("," java-branch)* ";" 
 * java-branch       := ("switch" | "if")
 * clause-operator   := "operator:" java-operator ("," java-operator)* ";" 
 * java-operator     := (java-ternaryop | java-binaryop | java-unaryop)
 * java-unaryop      := ("+" | "-" | "/" | "*" | "%" | "|" | "&" | "=" | "!" | "^" | "~")
 * java-binaryop     := ("++" | "--" | ">>" | "<<" | "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "^=" | "|=" | "&&" | "||" | "==" | "<=" | ">=" | "!=")
 * java-ternaryop    := (">>>=" | ">>>" | "<<=" | ">>=")
 * clause-import     := "import:" java-import ("," java-import)* ";"
 * java-import       := [a-zA-Z] ("." [a-zA-Z]* | ".*")
 * 
 * comments  := "#" [^\n]*
 * spaces    := [ \n\r\t]+ | comments
 * 
 */

import static peg.peg.*;

import java.util.ArrayList;
import java.util.List;

import mast.*;
import peg.Parser;
import peg.Symbol;

public class ScriptParser {
	/*Defining void stuff*/
	public static Parser<Void> comments   = seq(lit("#"), star(cls((c) -> c != '\n')));
	public static Parser<Void> spaces     = choice(plus(cls(Character::isWhitespace)), comments);
	public static Parser<Void> sp         = starv(spaces);
	
	/*Defining the keywords for my language*/
	public static Parser<Symbol> FILES     = seqr(sp, kw("files", "fi"));
	public static Parser<Symbol> REQUIRES  = seqr(sp, kw("requires", "re"));
	public static Parser<Symbol> ENCLOSES  = seqr(sp, kw("encloses", "en"));
	public static Parser<Symbol> PROHIBITS = seqr(sp, kw("prohibits", "pb"));
	public static Parser<Symbol> TYPE      = seqr(sp, kw("type", "tp"));
	public static Parser<Symbol> RETTYPE   = seqr(sp, kw("returntype", "rt"));
	public static Parser<Symbol> VARTYPE   = seqr(sp, kw("vartype", "vt"));
	public static Parser<Symbol> MODIFIER  = seqr(sp, kw("modifier", "md"));
	public static Parser<Symbol> LOOP      = seqr(sp, kw("loop", "lp"));
	public static Parser<Symbol> BRANCH    = seqr(sp, kw("branch", "br"));
	public static Parser<Symbol> OPERATOR  = seqr(sp, kw("operator", "op"));
	public static Parser<Symbol> IMPORT    = seqr(sp, kw("import", "im"));
		
	/*Defining symbols which will be used in my language*/
	public static Parser<Symbol> lbracket = seqr(sp, token(lit("{"), "{"));	
	public static Parser<Symbol> rbracket = seqr(sp, token(lit("}"), "}"));	
	public static Parser<Symbol> semicol  = seqr(sp, token(lit(";"), ";"));
	public static Parser<Symbol> comma    = seqr(sp, token(lit(","), ","));
	public static Parser<Symbol> dots     = seqr(sp, token(lit(":"), ":"));
	public static Parser<Symbol> dot      = seqr(sp, token(lit("."), "."));
	
	/*Defining instructions for my language*/
	public static Parser<Symbol> filename   = seq(sp, token(seq(plus(cls(Character::isAlphabetic)), lit(".java")), "filename"), semicol, (r1,r2,r3)->r2);
	
	/*Defining my java accepted dictionaries*/
	public static Parser<JavaArgs> javaType = choice(
		    	seq(sp, token(lit("int"), "int"), (Void r1, Symbol r2) -> new JavaType(r2.pos, r2.texto)), 
		    	seq(sp, token(lit("boolean"), "boolean"), (Void r1, Symbol r2) -> new JavaType(r2.pos, r2.texto)),
		    	seq(sp, token(lit("double"), "double"), (Void r1, Symbol r2) -> new JavaType(r2.pos, r2.texto)), 
		    	seq(sp, token(lit("float"), "float"), (Void r1, Symbol r2) -> new JavaType(r2.pos, r2.texto)),
		    	seq(sp, token(lit("char"), "char"), (Void r1, Symbol r2) -> new JavaType(r2.pos, r2.texto)), 
		    	seq(sp, token(lit("long"), "long"), (Void r1, Symbol r2) -> new JavaType(r2.pos, r2.texto)),
		    	seq(sp, token(lit("short"), "short"), (Void r1, Symbol r2) -> new JavaType(r2.pos, r2.texto)), 
		    	seq(sp, token(lit("byte"), "byte"), (Void r1, Symbol r2) -> new JavaType(r2.pos, r2.texto)),
		    	seq(sp, token(lit("void"), "void"), (Void r1, Symbol r2) -> new JavaType(r2.pos, r2.texto))
       		);
	
	public static Parser<JavaArgs> javaModifier = choice(
	    	seq(sp, token(lit("public"), "public"), (Void r1, Symbol r2) -> new JavaModifier(r2.pos, r2.texto)), 
	    	seq(sp, token(lit("private"), "private"), (Void r1, Symbol r2) -> new JavaModifier(r2.pos, r2.texto)),
	    	seq(sp, token(lit("protected"), "protected"), (Void r1, Symbol r2) -> new JavaModifier(r2.pos, r2.texto)), 
	    	seq(sp, token(lit("static"), "static"), (Void r1, Symbol r2) -> new JavaModifier(r2.pos, r2.texto)),
	    	seq(sp, token(lit("final"), "final"), (Void r1, Symbol r2) -> new JavaModifier(r2.pos, r2.texto)), 
	    	seq(sp, token(lit("abstract"), "abstract"), (Void r1, Symbol r2) -> new JavaModifier(r2.pos, r2.texto)),
	    	seq(sp, token(lit("volatile"), "volatile"), (Void r1, Symbol r2) -> new JavaModifier(r2.pos, r2.texto)), 
	    	seq(sp, token(lit("synchronized"), "synchronized"), (Void r1, Symbol r2) -> new JavaModifier(r2.pos, r2.texto)),
	    	seq(sp, token(lit("class"), "class"), (Void r1, Symbol r2) -> new JavaModifier(r2.pos, r2.texto))
   		);

	public static Parser<JavaArgs> javaBranch = choice(
	    	seq(sp, token(lit("switch"), "switch"), (Void r1, Symbol r2) -> new JavaLoop(r2.pos, r2.texto)), 
	    	seq(sp, token(lit("if"), "if"), (Void r1, Symbol r2) -> new JavaLoop(r2.pos, r2.texto))
	    	//seq(sp, token(lit("?:"), "?:"), (Void r1, Symbol r2) -> new JavaLoop(r2.pos, r2.texto)) 
   		);
	
	public static Parser<JavaArgs> javaLoop = choice(
	    	seq(sp, token(lit("while"), "while"), (Void r1, Symbol r2) -> new JavaBranch(r2.pos, r2.texto)), 
	    	seq(sp, token(lit("do"), "do"), (Void r1, Symbol r2) -> new JavaBranch(r2.pos, r2.texto)),
	    	seq(sp, token(lit("for"), "for"), (Void r1, Symbol r2) -> new JavaBranch(r2.pos, r2.texto)),
	    	seq(sp, token(lit("foreach"), "foreach"), (Void r1, Symbol r2) -> new JavaBranch(r2.pos, r2.texto)),
	    	seq(sp, token(lit("break"), "break"), (Void r1, Symbol r2) -> new JavaBranch(r2.pos, r2.texto)),
	    	seq(sp, token(lit("continue"), "continue"), (Void r1, Symbol r2) -> new JavaBranch(r2.pos, r2.texto)) 
   		);
	
	
	//operators need to be ordered from bigger to smaller in length 
	public static Parser<JavaArgs> javaUnaryArith = choice(
	    	seq(sp, token(lit("+"), "+"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("-"), "-"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("/"), "/"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("*"), "*"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("="), "="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("%"), "%"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto))
   		);
	
	public static Parser<JavaArgs> javaUnaryBool = choice(
	    	seq(sp, token(lit("|"), "|"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("&"), "&"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("~"), "~"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("^"), "^"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("!"), "!"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("<"), "<"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit(">"), ">"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto))
   		);
	
	public static Parser<JavaArgs> javaUnaryOperator = choice(javaUnaryArith, javaUnaryBool);
	
	public static Parser<JavaArgs> javaBinaryBool = choice(
			seq(sp, token(lit("&&"), "&&"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("||"), "||"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("=="), "=="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("!="), "!="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("<="), "<="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit(">="), ">="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("|="), "|="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("&="), "&="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("<<"), "<<"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit(">>"), ">>"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto))
   		);
	
	public static Parser<JavaArgs> javaBinaryArith = choice(
	    	seq(sp, token(lit("++"), "++"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("--"), "--"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
			seq(sp, token(lit("+="), "+="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("-="), "-="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("/="), "/="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("*="), "*="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("%="), "%="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit("^="), "^="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto))
   		);
	
	public static Parser<JavaArgs> javaBinaryOperator  = choice(javaBinaryBool, javaBinaryArith);
	
	public static Parser<JavaArgs> javaTernaryOperator = choice(
			seq(sp, token(lit(">>>="), ">>>="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
			seq(sp, token(lit(">>>"), ">>>"), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
			seq(sp, token(lit("<<="), "<<="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto)),
	    	seq(sp, token(lit(">>="), ">>="), (Void r1, Symbol r2) -> new JavaOperator(r2.pos, r2.texto))
   		);
	    	
	public static Parser<JavaArgs> javaOperator = choice(javaTernaryOperator, javaBinaryOperator, javaUnaryOperator);
	
	public static Parser<Symbol> piece  = seqr(sp, token(seq(sp, plus(cls(Character::isAlphabetic))), "importpiece"));
	public static Parser<JavaArgs> javaImport = seq(sp, 
				piece, 
				stars(seq(sp, dot, piece, (r1, r2, r3) -> new String(r2.texto+r3.texto))),
				opt(token(lit(".*"), ".*")),
				(r1, r2, r3, r4) -> new JavaImport(r2.pos, r2.texto+r3, r4 == null ? "" : r4.texto)
			);
	
	/*Defining my clauses*/
	public static Parser<Clause> clauseType     = seq(TYPE, dots, listof(javaType, comma), semicol, (r1, r2, r3, r4) -> new ClauseType(r4.pos, r1.texto, r3));
	public static Parser<Clause> clauseRetType  = seq(RETTYPE, dots, listof(javaType, comma), semicol, (r1, r2, r3, r4) -> new ClauseRetType(r4.pos, r1.texto, r3));
	public static Parser<Clause> clauseVarType  = seq(VARTYPE, dots, listof(javaType, comma), semicol, (r1, r2, r3, r4) -> new ClauseVarType(r4.pos, r1.texto, r3));
	public static Parser<Clause> clauseModifier = seq(MODIFIER, dots, listof(javaModifier, comma), semicol, (r1, r2, r3, r4) -> new ClauseModifier(r4.pos, r1.texto, r3));
	public static Parser<Clause> clauseLoop     = seq(LOOP, dots, listof(javaLoop, comma), semicol, (r1, r2, r3, r4) -> new ClauseLoop(r4.pos, r1.texto, r3));
	public static Parser<Clause> clauseBranch   = seq(BRANCH, dots, listof(javaBranch, comma), semicol, (r1, r2, r3, r4) -> new ClauseBranch(r4.pos, r1.texto, r3));
	public static Parser<Clause> clauseOperator = seq(OPERATOR, dots, listof(javaOperator, comma), semicol, (r1, r2, r3, r4) -> new ClauseOperator(r4.pos, r1.texto, r3));
	public static Parser<Clause> clauseImport   = seq(IMPORT, dots, listof(javaImport, comma), semicol, (r1, r2, r3, r4) -> new ClauseImport(r4.pos, r1.texto, r3));

	
	public static Parser<Clause> clauses        = choice(clauseType, clauseRetType, 
														clauseVarType, clauseModifier, 
														clauseLoop, clauseBranch, clauseOperator,
														clauseImport);
	
	/*Defining the main commands*/
	public static Parser<List<CommandFiles>> files          = seq(FILES, lbracket, 
			star(fun(filename, (n) -> new CommandFiles(n.pos, n.texto))), rbracket, (r1, r2, r3, r4) -> r3);
	public static Parser<List<CommandRequires>> requires    = seq(REQUIRES, lbracket, 
			star(fun(clauses, (n) -> new CommandRequires(n.pos, n))), rbracket, (r1, r2, r3, r4) -> r3);
	public static Parser<List<CommandProhibits>> prohibits  = seq(PROHIBITS, lbracket, 
			star(fun(clauses, (n) -> new CommandProhibits(n.pos, n))), rbracket, (r1, r2, r3, r4) -> r3);
	public static Parser<List<CommandEncloses>> encloses    = seq(ENCLOSES, lbracket, 
			star(fun(clauses, (n) -> new CommandEncloses(n.pos, n))), rbracket, (r1, r2, r3, r4) -> r3);
	
	/*Defining the main clause SCRIPT*/
	public static Parser<Script> script = choice(
				seq(files, requires, prohibits, encloses, (fi, re, pr, en) -> new Script(fi, re == null ? new ArrayList<>() : re, pr == null ? new ArrayList<>() : pr, en == null ? new ArrayList<>() : en)),
				seq(files, prohibits, requires, encloses, (fi, pr, re, en) -> new Script(fi, re == null ? new ArrayList<>() : re, pr == null ? new ArrayList<>() : pr, en == null ? new ArrayList<>() : en)),
				seq(files, encloses, requires, prohibits, (fi, en, re, pr) -> new Script(fi, re == null ? new ArrayList<>() : re, pr == null ? new ArrayList<>() : pr, en == null ? new ArrayList<>() : en)),
				seq(files, requires, encloses, prohibits, (fi, re, en, pr) -> new Script(fi, re == null ? new ArrayList<>() : re, pr == null ? new ArrayList<>() : pr, en == null ? new ArrayList<>() : en)),
				seq(files, prohibits, encloses, requires, (fi, pr, en, re) -> new Script(fi, re == null ? new ArrayList<>() : re, pr == null ? new ArrayList<>() : pr, en == null ? new ArrayList<>() : en)),
				seq(files, encloses, prohibits, requires, (fi, en, pr, re) -> new Script(fi, re == null ? new ArrayList<>() : re, pr == null ? new ArrayList<>() : pr, en == null ? new ArrayList<>() : en))
			);
}
