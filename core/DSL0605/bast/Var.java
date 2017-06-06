package bast;

import java.math.BigInteger;
import java.util.Map;

public class Var extends Node implements Exp {
	public final String nome;
	
	public Var(int pos, String nome) {
		super(pos);
		this.nome = nome;
	}

	@Override
	public BigInteger eval(Map<String, BigInteger> vars) {
		return vars.get(nome);
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
