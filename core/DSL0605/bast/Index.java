package bast;

import java.math.BigInteger;
import java.util.Map;

import peg.Symbol;

public class Index extends Node implements Exp {
	public final Exp struct;
	public final String campo;
	
	public int idx;
	
	public Index(int pos, Exp struct, Symbol campo) {
		super(pos);
		this.struct = struct;
		this.campo = campo.texto;
	}

	public Index(int pos, Exp struct, String campo) {
		super(pos);
		this.struct = struct;
		this.campo = campo;
	}

	@Override
	public BigInteger eval(Map<String, BigInteger> vars) {
		throw new RuntimeException("não implementado");
	}

	@Override
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
