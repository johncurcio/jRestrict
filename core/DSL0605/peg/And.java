package peg;

public class And implements Parser<Void> {
	public final Parser<?> p;
	
	public And(Parser<?> p) {
		this.p = p;
	}

	@Override
	public Result<Void> parse(Error err, String ent) {
		p.parse(new Error(), ent);
		return Result.empty(ent);
	}
	
}
