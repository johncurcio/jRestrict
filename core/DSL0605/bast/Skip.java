package bast;

import java.math.BigInteger;
import java.util.Map;

public class Skip extends Node implements Stat {

	public Skip(int pos) {
		super(pos);
	}

	@Override
	public void run(Map<String, BigInteger> vars) {
		// TODO Auto-generated method stub
	}

	@Override
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return null;
	}

}
