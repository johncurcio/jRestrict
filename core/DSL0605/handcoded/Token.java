package handcoded;

public class Token {
	public final int tipo;
	public final String texto;
	public final int local;
		
	public Token(int _tipo, String _texto, int _local) {
		tipo = _tipo;
		texto = _texto;
		local = _local;
	}
	
	public String toString() {
		return "<" + tipo + "," + texto + "," + local + ">";
	}
	
	public boolean equals(Object o) {
		if(o instanceof Token) {
			Token ot = (Token)o;
			return tipo == ot.tipo && texto == ot.texto && local == ot.local;
		} else return false;
	}
	
	public int hashCode() {
		return texto.hashCode();
	}
}
