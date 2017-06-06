package peg;

import java.util.function.BiFunction;

public class Seq<T1, T2, T3> implements Parser<T3> {
	public final Parser<? extends T1> p1;
	public final Parser<? extends T2> p2;
	public final BiFunction<? super T1, ? super T2, ? extends T3> f;
	
	public Seq(Parser<? extends T1> p1, Parser<? extends T2> p2, 
			BiFunction<? super T1, ? super T2, ? extends T3> f) {
		this.p1 = p1;
		this.p2 = p2;
		this.f = f;
	}
	
	@Override
	public Result<? extends T3> parse(Error err, String ent) {
		Result<? extends T1> res1 = p1.parse(err, ent);
		Result<? extends T2> res2 = p2.parse(err, res1.saida);
		return new Result<>(f.apply(res1.node, res2.node), res2.saida);
	}
}
