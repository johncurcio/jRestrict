package peg;

public class StarString implements Parser<String> {
	public final Parser<String> sp;
	
	public StarString(Parser<?> p) {
		sp = new OrdChoice<String>(new Seq<>(p, this, (x, xs) -> {
			xs = x + xs;
			return xs;
		}), Parser.epsstring());
	}
	
	@Override
	public Result<? extends String> parse(Error err, String ent) {
		return sp.parse(err, ent);
	}
}