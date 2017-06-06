package bast;

import java.math.BigInteger;
import java.util.Map;

public class Menor extends ExpBin {

	public Menor(int pos, Exp esq, Exp dir) {
		super(pos, esq, dir);
	}

	@Override
	public BigInteger eval(Map<String, BigInteger> vars) {
		return esq.eval(vars).compareTo(dir.eval(vars)) == -1 ? BigInteger.ONE : BigInteger.ZERO; 
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
