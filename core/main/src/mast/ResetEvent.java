package mast;

public class ResetEvent extends Node {
	public final String nome;
	
	EventSymbol symbol;
	
	public ResetEvent(int pos, String nome) {
		super(pos);
		this.nome = nome;
	}

	public String toString() {
		return nome;
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
