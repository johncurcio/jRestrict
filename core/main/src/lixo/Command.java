package lixo;

import mast.Node;
import mast.Visitor;

public class Command extends Node {
	public final String nome;
	public final String codigo;
	
	public Command(int pos, String nome, String codigo) {
		super(pos);
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public String toString() {
		return nome + " " + codigo;
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return null;
		//return visitor.visit(this, ctx);
	}
}
