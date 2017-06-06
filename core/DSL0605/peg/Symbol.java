package peg;

public class Symbol {
	public final String texto;
	public final int pos;
		
	public Symbol(String _texto, int _local) {
		texto = _texto;
		pos = _local;
	}
	
	public String toString() {
		return "<" + texto + "," + pos + ">";
	}
	
	public boolean equals(Object o) {
		if(o instanceof Symbol) {
			Symbol ot = (Symbol)o;
			return texto.equals(ot.texto) && pos == ot.pos;
		} else return false;
	}
	
	public int hashCode() {
		return texto.hashCode();
	}

}
