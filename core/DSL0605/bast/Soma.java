package bast;

import java.math.BigInteger;
import java.util.Map;

public class Soma extends ExpBin {

	public Soma(int pos, Exp esq, Exp dir) {
		super(pos, esq, dir);
	}

	@Override
	public BigInteger eval(Map<String, BigInteger> vars) {
		return esq.eval(vars).add(dir.eval(vars));
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
