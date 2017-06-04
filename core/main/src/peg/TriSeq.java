package peg;

public class TriSeq<T1, T2, T3, R> implements Parser<R> {
	public final Parser<? extends T1> p1;
	public final Parser<? extends T2> p2;
	public final Parser<? extends T3> p3;
	public final TriFunction<? super T1, ? super T2, ? super T3, ? extends R> f;
	
	public TriSeq(Parser<? extends T1> p1, Parser<? extends T2> p2, Parser<? extends T3> p3, 
			TriFunction<? super T1, ? super T2, ? super T3, ? extends R> f) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.f = f;
	}
	
	@Override
	public Result<? extends R> parse(Error err, String ent) {
		Result<? extends T1> res1 = p1.parse(err, ent);
		Result<? extends T2> res2 = p2.parse(err, res1.saida);
		Result<? extends T3> res3 = p3.parse(err, res2.saida);
		return new Result<>(f.apply(res1.node, res2.node, res3.node), res3.saida);
	}
}
