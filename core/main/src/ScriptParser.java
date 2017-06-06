
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
 * clauses   := (clause-type | clause-returntype | clause-argtype | clause-vartype | clause-operator | clause-modifier | clause-import | clause-exception | clause-loop | clause-branch)
 * 
 * clause-type       := "type:" java-type ("," java-type)* ";" 
 * clause-returntype := "returntype:" java-type ("," java-type)* ";" 
 * java-type         := ("int" | "double" | "boolean" | "float" | "char")
 * 
 * comments  := '#'[^\n]*
 * spaces    := [ \n\r\t]+ | comments
 * 
 */

import static peg.peg.*;

import java.util.ArrayList;
import java.util.List;

import mast.Requires;
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
	
	public static Parser<Symbol> keywords  = choice(kw("files", "fi"), kw("requires", "req"), 
			                                        kw("encloses", "en"), kw("prohibits", "pb"),
			                                        kw("type", "tp"), kw("returntype", "rt"));

	/*Defining symbols which will be used in my language*/
	public static Parser<Symbol> lbracket = seqr(sp, token(lit("{"), "{"));	
	public static Parser<Symbol> rbracket = seqr(sp, token(lit("}"), "}"));	
	public static Parser<Symbol> semicol  = seqr(sp, token(lit(";"), ";"));
	public static Parser<Symbol> colon    = seqr(sp, token(lit(","), ","));
	public static Parser<Symbol> dots     = seqr(sp, token(lit(":"), ":"));
	
	/*Defining instructions for my language*/
	public static Parser<Symbol> filename    = seqr(sp, token(seq(plus(cls(Character::isAlphabetic)), lit(".java"), semicol), "filename"));
	public static Parser<mast.Files> files   = seq(FILES, lbracket, 
			fun(filename, (n) -> new mast.Files(n.pos, n.texto)), rbracket, (r1, r2, r3, r4) -> r3);//this should be a list like Actions if we want more than one filename
	
	/*Defining my java accepted dictionaries*/
	public static Parser<Symbol> javatype    = choice(
													  seqr(sp, token(lit("int"), "int")), 
			                                          seqr(sp, token(lit("boolean"), "boolean")), 
			                                          seqr(sp, token(lit("double"), "double")), 
			                                          seqr(sp, token(lit("float"), "float")),
			                                          seqr(sp, token(lit("char"), "char"))
			                                          );
	
	/*Defining my clauses*/
	//List<Symbol> because javatype is Symbol
	public static Parser<List<Symbol>> clausetype    = seq(TYPE, dots, listof(javatype, colon), semicol, (r1, r2, r3, r4) -> r3);
	public static Parser<List<Symbol>> clauserettype = seq(RETTYPE, dots, listof(javatype, colon), semicol, (r1, r2, r3, r4) -> r3);
	public static Parser<List<Symbol>> clauses       = choice(clausetype, clauserettype);
	
	/*Defining the three main commands*/
	//@TODO: generalize mast.Requires to work with the tree main clauses because they're identical!
	public static Parser<List<mast.Requires>> requires   = seq(REQUIRES, lbracket, 
			star(fun(clauses, (n) -> new mast.Requires(1, n))), rbracket, (r1, r2, r3, r4) -> r3);
	public static Parser<List<mast.Requires>> prohibits  = seq(PROHIBITS, lbracket, 
			star(fun(clauses, (n) -> new mast.Requires(1, n))), rbracket, (r1, r2, r3, r4) -> r3);
	public static Parser<List<mast.Requires>> encloses   = seq(ENCLOSES, lbracket, 
			star(fun(clauses, (n) -> new mast.Requires(1, n))), rbracket, (r1, r2, r3, r4) -> r3);
	
	/*Defining the main clause SCRIPT*/
	public static Parser<Void> script = seq(files, opt(requires), opt(prohibits), opt(encloses));
	
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
