package peg;

import java.util.List;

public class Star<T> implements Parser<List<T>> {
	public final Parser<List<T>> sp;
	
	public Star(Parser<? extends T> p) {
		sp = new OrdChoice<List<T>>(new Seq<>(p, this, (x, xs) -> {
			xs.add(0, x);
			return xs;
		}), Parser.epslist());
	}
	
	@Override
	public Result<? extends List<T>> parse(Error err, String ent) {
		return sp.parse(err, ent);
	}
}
