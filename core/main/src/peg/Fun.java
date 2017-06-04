package peg;

import java.util.function.Function;

public class Fun<T1, T2> implements Parser<T2> {
	public final Parser<T1> p;
	public final Function<? super T1, ? extends T2> f;
	
	public Fun(Parser<T1> p, Function<? super T1, ? extends T2> f) {
		this.p = p;
		this.f = f;
	}

	@Override
	public Result<? extends T2> parse(Error err, String ent) {
		Result<? extends T1> res = p.parse(err, ent);
		return new Result<>(f.apply(res.node), res.saida);
	}
}
