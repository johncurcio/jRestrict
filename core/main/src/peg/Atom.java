package peg;

import java.util.function.BiFunction;

public class Atom<T> implements Parser<T> {
	public final Parser<?> p;
	public final BiFunction<Integer, String, T> f;
	
	public Atom(Parser<?> p, 
			BiFunction<Integer, String, T> f) {
		this.p = p;
		this.f = f;
	}

	@Override
	public Result<T> parse(Error err, String ent) {
		Result<?> res = p.parse(err, ent);
		int pos = ent.length();
		String texto = ent.substring(0, ent.length() - res.saida.length());
		return new Result<>(f.apply(pos, texto), res.saida);
	}
}
