package bast;

import java.math.BigInteger;
import java.util.Map;

public interface Stat {
	void run(Map<String, BigInteger> vars);
	<C, R> R visit(Visitor<C, R> visitor, C ctx);
}
