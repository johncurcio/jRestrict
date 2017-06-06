package bast;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class New extends Node implements Exp {
	public final String nome;
	public final List<Exp> args;
	
	public New(int pos, String nome, List<Exp> args) {
		super(pos);
		this.nome = nome;
		this.args = args;
	}

	@Override
	public BigInteger eval(Map<String, BigInteger> vars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}

}
