package peg;

public class Literal implements Parser<Void> {
	public final String lit;
	
	public Literal(String lit) {
		this.lit = lit;
	}

	@Override
	public Result<Void> parse(Error err, String ent) {
		if(ent.startsWith(lit)) {
			return Result.empty(ent.substring(lit.length()));
		} else {
			throw new Falha();
		}
	}
}
