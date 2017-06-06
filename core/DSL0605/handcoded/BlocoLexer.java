package handcoded;
import java.util.HashMap;

public class BlocoLexer extends Lexer {
	public static final int EOF = 0;
	public static final int NOME = 1;
	public static final int NUM = 2;
	public static final int WHILE = 3;
	public static final int END = 4;
	public static final int DO = 5;
	public static final int STRUCT = 6;
	public static final int IS = 7;
	public static final int NEW = 8;
	public static final int NIL = 9;
	public static final int EXTENDS = 10;
	
	public static final HashMap<String, Integer> reservadas;
	
	static {
		reservadas = new HashMap<>();
		reservadas.put("while", WHILE);
		reservadas.put("do", DO);
		reservadas.put("end", END);
		reservadas.put("struct", STRUCT);
		reservadas.put("new", NEW);
		reservadas.put("nil", NIL);
		reservadas.put("is", IS);
	}

	public static final String[] nomesToken = new String[] {
			"EOF", "nome", "num", "while", "end", "do", "struct", "is", "new", "nil", "<:"
	};
	
	public BlocoLexer(String _ent) {
		super(_ent);
	}

	public void comentario() {
		match('-');
		match('-');
		while(match(c -> c != Lexer.EOF && c != '\n')) {}
	}
	
	public Token nomeOuReservada() {
		int p = pos;
		while(match(Character::isJavaIdentifierPart)) {}
		String t = texto(p);
		if(reservadas.containsKey(t)) return new Token(reservadas.get(t), t, p);
		else return new Token(NOME, t, p);
	}

	public Token num() {
		int p = pos;
		while(match(Character::isDigit)) {}
		if(match('.')) {
			while(match(Character::isDigit)) {}
		}
		String t = texto(p);
		return new Token(NUM, t, p);
	}

	@Override
	public Token proximoToken() {
		while(c != Lexer.EOF) {
			if(c == '-' && peek(1) == '-') {
				comentario();
				continue;
			}
			if(c == ' ' || c == '\n' || c == '\r' || c == '\t') {
				skip();
				continue;
			}
			if(Character.isJavaIdentifierStart(c)) return nomeOuReservada();
			if(Character.isDigit(c)) return num();
			if(c == '<' && peek(1) == ':') {
				int p = pos;
				skip(); skip();
				return new Token(EXTENDS, "<:", p);
			}
			Token t = new Token(c, "" + c, pos);
			skip();
			return t;
		}
		return new Token(EOF, "<<EOF>>", pos);
	}

	@Override
	public String nomeToken(int tipo) {
		if(tipo < nomesToken.length) return nomesToken[tipo];
		else return "" + (char)tipo;
	}

}
