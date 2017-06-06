package bast;

import java.math.BigInteger;
import java.util.Map;

public class Num extends Node implements Exp{
	public final String num;
	
	public Num(int pos, String num) {
		super(pos);
		this.num = num;
	}

	@Override
	public BigInteger eval(Map<String, BigInteger> vars) {
		return new BigInteger(num);
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
