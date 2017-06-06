package bast;

import java.math.BigInteger;
import java.util.Map;

public interface Exp {
	BigInteger eval(Map<String, BigInteger> vars);
	<C, R> R visit(Visitor<C, R> visitor, C ctx);
}
