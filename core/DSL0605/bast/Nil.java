package bast;

import java.math.BigInteger;
import java.util.Map;

public class Nil extends Node implements Exp {

	public Nil(int pos) {
		super(pos);
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
