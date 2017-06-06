package bast;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class While extends Node implements Stat {
	public final Exp cond;
	public final List<Stat> corpo;
	
	public While(int pos, Exp cond, List<Stat> corpo) {
		super(pos);
		this.cond = cond;
		this.corpo = corpo;
	}

	@Override
	public void run(Map<String, BigInteger> vars) {
		while(cond.eval(vars).equals(BigInteger.ONE)) {
			for(Stat st: corpo) st.run(vars);
		}
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
