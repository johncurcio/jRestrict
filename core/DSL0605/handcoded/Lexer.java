package handcoded;
import java.util.function.Predicate;

public abstract class Lexer {
	public static final char EOF = (char)-1;
	String ent;
	int pos;
	char c;
	
	public Lexer(String _ent) {
		ent = _ent;
		pos = 0;
		c = ent.charAt(pos);
	}
	
	public void skip() {
		if(c == EOF) return;
		pos++;
		if(pos >= ent.length()) c = EOF;
		else c = ent.charAt(pos);
	}
	
	public boolean match(char x) {
		if(c == x) {
			skip();
			return true;
		} else return false;
	}
	
	public boolean match(Predicate<Character> cc) {
		if(cc.test(c)) {
			skip();
			return true;
		} else return false;
	}
	
	public char peek(int la) {
		return (pos + la) >= ent.length() ? EOF : ent.charAt(pos + la);
	}
	
	public String texto(int inicio) {
		return ent.substring(inicio, pos);
	}
	
	public abstract Token proximoToken();
	public abstract String nomeToken(int tipo);
}
