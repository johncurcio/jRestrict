package peg;

public class Not implements Parser<Void> {
	public final Parser<?> p;
	
	public Not(Parser<?> p) {
		this.p = p;
	}
	
	@Override
	public Result<Void> parse(Error err, String ent) {
		try {
			p.parse(new Error(), ent);
		} catch(Falha f) {
			return Result.empty(ent);
		}
		throw new Falha();
	}

}
