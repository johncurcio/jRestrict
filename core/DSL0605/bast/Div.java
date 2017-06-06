package bast;

import java.math.BigInteger;
import java.util.Map;

public class Div extends ExpBin {

	public Div(int pos, Exp esq, Exp dir) {
		super(pos, esq, dir);
	}

	@Override
	public BigInteger eval(Map<String, BigInteger> vars) {
		return esq.eval(vars).divide(dir.eval(vars));
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
