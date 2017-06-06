package peg;

public class TetraSeq<T1, T2, T3, T4, R> implements Parser<R> {
	public final Parser<? extends T1> p1;
	public final Parser<? extends T2> p2;
	public final Parser<? extends T3> p3;
	public final Parser<? extends T4> p4;
	public final TetraFunction<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f;
	
	public TetraSeq(Parser<? extends T1> p1, Parser<? extends T2> p2, Parser<? extends T3> p3, 
			Parser<? extends T4> p4,
			TetraFunction<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.f = f;
	}
	
	@Override
	public Result<? extends R> parse(Error err, String ent) {
		Result<? extends T1> res1 = p1.parse(err, ent);
		Result<? extends T2> res2 = p2.parse(err, res1.saida);
		Result<? extends T3> res3 = p3.parse(err, res2.saida);
		Result<? extends T4> res4 = p4.parse(err, res3.saida);
		return new Result<>(f.apply(res1.node, res2.node, res3.node, res4.node), res4.saida);
	}
}
