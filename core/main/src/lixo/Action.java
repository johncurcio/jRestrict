package lixo;

import mast.Node;
import mast.Visitor;

public class Action extends Node {
	public final String nome;
	
	CommandSymbol symbol;
	
	public Action(int pos, String nome) {
		super(pos);
		this.nome = nome;
	}
	
	public String toString() {
		return nome;
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return null;
		//return visitor.visit(this, ctx);
	}
}
