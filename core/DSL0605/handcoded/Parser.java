package handcoded;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class Parser<T> {
	public static final int EOF = 0;
	public final Lexer lex;
	public final List<Token> ent;
	public int pos;
	public Token la;

	public List<String> erros = new ArrayList<>();

	public Parser(Lexer _lex) {
		lex = _lex;
		ent = new ArrayList<>();
		Token tok;
		do {
			tok = lex.proximoToken();
			ent.add(tok);
		} while(tok.tipo != EOF);
		pos = 0;
		la = ent.get(pos);
	}
	
	public Token skip() {
		if(la.tipo == EOF) return la;
		pos++;
		Token tok = la;
		la = ent.get(pos);
		return tok;
	}
	
	public boolean test(int... tipos) {
		for(int tipo: tipos) {
			if(la.tipo == tipo) {
				return true;
			}
		}
		return false;
	}
	
	public boolean match(int... tipos) {
		for(int tipo: tipos) {
			if(la.tipo == tipo) {
				skip();
				return true;
			}
		}
		return false;
	}
	
	public boolean match(Predicate<Token> cc) {
		if(cc.test(la)) {
			skip();
			return true;
		} else return false;
	}
	
	public boolean matchOrReport(int... tipos) {
		if(!match(tipos)) {
			String[] ntipos = new String[tipos.length];
			for(int i = 0; i < tipos.length; i++) ntipos[i] = lex.nomeToken(tipos[i]);
			erros.add("encontrado " + lex.nomeToken(la.tipo) + " na posição " + la.local + ", mas esperado " +
					String.join(", ", ntipos) + ": " + la);
			return false;
		} else return true;
	}
	
	public boolean matchOrReportAndSync(int tipo, int... stipos) {
		if(!match(tipo)) {
			erros.add("encontrado " + lex.nomeToken(la.tipo) + " na posição " + la.local + ", mas esperado " +
					lex.nomeToken(tipo) + ": " + la);
			while(la.tipo != EOF && !test(stipos)) {
				skip();
			}
			return false;
		} return true;
	}
	
	public Token peek(int la) {
		return (pos + la) >= ent.size() ? ent.get(ent.size()-1) : ent.get(pos + la);
	}
	
	public abstract T parse();
}
