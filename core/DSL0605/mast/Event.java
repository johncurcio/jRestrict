package mast;

public class Event extends Node {
	public final String nome;
	public final String codigo;
	
	public Event(int pos, String nome, String codigo) {
		super(pos);
		this.nome = nome;
		this.codigo = codigo;
	}

	public String toString() {
		return nome + " " + codigo;
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
