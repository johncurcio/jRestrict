package peg;

public class StarVoid implements Parser<Void> {
	public final Parser<Void> sp;
	
	public StarVoid(Parser<?> p) {
		sp = new OrdChoice<>(new Seq<>(p, this, (x, xs) -> null), Parser.eps);
	}
	
	@Override
	public Result<? extends Void> parse(Error err, String ent) {
		return sp.parse(err, ent);
	}
}
