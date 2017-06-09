
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
 * java-loop         := ("while" | "do" | "for" | "break" | "continue")
 * clause-branch     := "branch:" java-branch ("," java-branch)* ";" 
 * java-branch       := ("switch" | "if")
 * 
 * comments  := '#'[^\n]*
 * spaces    := [ \n\r\t]+ | comments
 * 
 */

import static peg.peg.*;

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
	
	//public static Parser<Symbol> keywords  = choice(FILES, REQUIRES, ENCLOSES, PROHIBITS, TYPE, RETTYPE, VARTYPE, MODIFIER, LOOP);

	/*Defining symbols which will be used in my language*/
	public static Parser<Symbol> lbracket = seqr(sp, token(lit("{"), "{"));	
	public static Parser<Symbol> rbracket = seqr(sp, token(lit("}"), "}"));	
	public static Parser<Symbol> semicol  = seqr(sp, token(lit(";"), ";"));
	public static Parser<Symbol> comma    = seqr(sp, token(lit(","), ","));
	public static Parser<Symbol> dots     = seqr(sp, token(lit(":"), ":"));
	
	/*Defining instructions for my language*/
	public static Parser<Symbol> filename    = seq(sp, token(seq(plus(cls(Character::isAlphabetic)), lit(".java")), "filename"), semicol, (r1,r2,r3)->r2);
	public static Parser<List<CommandFiles>> files   = seq(FILES, lbracket, 
			star(fun(filename, (n) -> new CommandFiles(n.pos, n.texto))), rbracket, (r1, r2, r3, r4) -> r3);
	
	/*Defining my java accepted dictionaries*/
	public static Parser<JavaArgs> javatype = choice(
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
	
	public static Parser<JavaArgs> javamodifier = choice(
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

	public static Parser<JavaArgs> javaloop = choice(
	    	seq(sp, token(lit("while"), "while"), (Void r1, Symbol r2) -> new JavaLoop(r2.pos, r2.texto)), 
	    	seq(sp, token(lit("do"), "do"), (Void r1, Symbol r2) -> new JavaLoop(r2.pos, r2.texto)),
	    	seq(sp, token(lit("for"), "for"), (Void r1, Symbol r2) -> new JavaLoop(r2.pos, r2.texto)), 
	    	seq(sp, token(lit("break"), "break"), (Void r1, Symbol r2) -> new JavaLoop(r2.pos, r2.texto)),
	    	seq(sp, token(lit("continue"), "continue"), (Void r1, Symbol r2) -> new JavaLoop(r2.pos, r2.texto)) 
   		);

	
	/*Defining my clauses*/
	public static Parser<Clause> clausetype     = seq(TYPE, dots, listof(javatype, comma), semicol, (r1, r2, r3, r4) -> new ClauseType(r4.pos, r1.texto, r3));
	public static Parser<Clause> clauserettype  = seq(RETTYPE, dots, listof(javatype, comma), semicol, (r1, r2, r3, r4) -> new ClauseRetType(r4.pos, r1.texto, r3));
	public static Parser<Clause> clausevartype  = seq(VARTYPE, dots, listof(javatype, comma), semicol, (r1, r2, r3, r4) -> new ClauseVarType(r4.pos, r1.texto, r3));
	public static Parser<Clause> clausemodifier = seq(MODIFIER, dots, listof(javamodifier, comma), semicol, (r1, r2, r3, r4) -> new ClauseModifier(r4.pos, r1.texto, r3));
	public static Parser<Clause> clauseloop     = seq(LOOP, dots, listof(javaloop, comma), semicol, (r1, r2, r3, r4) -> new ClauseLoop(r4.pos, r1.texto, r3));

	public static Parser<Clause> clauses        = choice(clausetype, clauserettype, 
														clausevartype, clausemodifier, clauseloop);
	
	/*Defining the three main commands*/
	public static Parser<List<CommandRequires>> requires   = seq(REQUIRES, lbracket, 
			star(fun(clauses, (n) -> new CommandRequires(n.pos, n))), rbracket, (r1, r2, r3, r4) -> r3);
	public static Parser<List<CommandProhibits>> prohibits  = seq(PROHIBITS, lbracket, 
			star(fun(clauses, (n) -> new CommandProhibits(n.pos, n))), rbracket, (r1, r2, r3, r4) -> r3);
	public static Parser<List<CommandEncloses>> encloses   = seq(ENCLOSES, lbracket, 
			star(fun(clauses, (n) -> new CommandEncloses(n.pos, n))), rbracket, (r1, r2, r3, r4) -> r3);
	
	/*Defining the main clause SCRIPT*/
	public static Parser<Script> script = seq(files, opt(requires), opt(prohibits), opt(encloses), 
					(fi, re, pr, en) -> new Script(fi, re, pr, en)
				);
	
	/*public static Parser<Void> comentario = seq(lit("--"), star(cls((c) -> c != '\n')));
	public static Parser<Void> espaco = choice(plus(cls(Character::isWhitespace)), comentario);
	public static Parser<Void> sp = starv(espaco);
	public static Parser<Symbol> EVENTS = seqr(sp, kw("events", "ev"));
	public static Parser<Symbol> COMMANDS = seqr(sp, kw("commands", "com"));
	public static Parser<Symbol> REVENTS = seqr(sp, kw("resetEvents", "reset"));
	public static Parser<Symbol> ACTIONS = seqr(sp, kw("actions", "ac"));
	public static Parser<Symbol> STATE = seqr(sp, kw("state", "sta"));
	public static Parser<Symbol> END = seqr(sp, kw("end", "en"));
	public static Parser<Symbol> keywords = choice(kw("events", "ev"), kw("commands", "com"), 
			kw("resetEvents", "reset"), kw("actions", "ac"), kw("state", "sta"), kw("end", "en"));
	public static Parser<Symbol> NOME = seqr(sp, token(seq(not(keywords), plus(cls(Character::isAlphabetic))), "nome"));
	public static Parser<Void> AZ09 = cls((c) -> (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
	public static Parser<Symbol> CODIGO = seqr(sp, token(seq(AZ09, AZ09, AZ09, AZ09, not(cls(Character::isJavaIdentifierPart))), 
			"codigo", seq(plus(AZ09), not(cls(Character::isJavaIdentifierPart)))));
	public static Parser<Symbol> SETA = seqr(sp, token(lit("=>"), "=>"));	
	public static Parser<Symbol> ACH = seqr(sp, token(lit("{"), "{"));	
	public static Parser<Symbol> FCH = seqr(sp, token(lit("}"), "}"));	
	
	public static Parser<mast.Event> event = seq(NOME, CODIGO, 
			(Symbol n, Symbol c) -> new mast.Event(n.pos, n.texto, c.texto));
	public static Parser<mast.Command> command = seq(NOME, CODIGO, 
			(Symbol n, Symbol c) -> new mast.Command(n.pos, n.texto, c.texto));
	public static Parser<List<mast.Event>> events = seqr(EVENTS, seql(plus(event), END));
	public static Parser<List<mast.Command>> commands = seq(COMMANDS, plus(command), END, 
			(r1, r2, r3) -> r2);
	public static Parser<List<mast.ResetEvent>> revents = seqr(REVENTS, 
			seql(plus(fun(NOME, (n) -> new mast.ResetEvent(n.pos, n.texto))), END));
	public static Parser<mast.Transition> transition = seq(NOME, SETA, NOME, 
			(ev, seta, st) -> new mast.Transition(seta.pos, ev.texto, st.texto));
	public static Parser<List<mast.Action>> actions = seq(ACTIONS, ACH, 
			plus(fun(NOME, (n) -> new mast.Action(n.pos, n.texto))), FCH, (r1, r2, r3, r4) -> r3);
	public static Parser<mast.State> state = seq(STATE, NOME, opt(actions), star(transition), END,
			(st, nome, acs, ts, end) -> new mast.State(st.pos, nome.texto,
					acs == null ? new ArrayList<>(): acs, ts));
	public static Parser<mast.Machine> machine = seq(events, opt(revents), commands, plus(state),
			(evs, revs, cms, sts) -> new mast.Machine(evs, cms, 
					revs == null ? new ArrayList<>() : revs, sts));*/
}
