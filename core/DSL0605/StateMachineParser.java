import static peg.peg.*;

import java.util.ArrayList;
import java.util.List;

import peg.Parser;
import peg.Symbol;

/*
reservada   := ('events' | 'state' | 'end' | 'action') ![a-zA-Z]
nome        := !reservada [a-zA-Z]+
codigo      := !nome [A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9]
comentario  := '--'[^\n]*
espaco      := [ \n\r\t]+ | comentario
*/

public class StateMachineParser {
	public static Parser<Void> comentario = seq(lit("--"), star(cls((c) -> c != '\n')));
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
					revs == null ? new ArrayList<>() : revs, sts));

}
