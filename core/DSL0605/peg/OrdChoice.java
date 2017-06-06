package peg;

public class OrdChoice<T> implements Parser<T> {
	public final Parser<? extends T> p1;
	public final Parser<? extends T> p2;
	
	public OrdChoice(Parser<? extends T> p1, Parser<? extends T> p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public Result<? extends T> parse(Error err, String ent) {
		try {
			return p1.parse(err, ent);
		} catch(Falha f) {
			return p2.parse(err, ent);
		}
	}

}
