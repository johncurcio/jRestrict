package peg;

public class Token implements Parser<Void> {
	public final Parser<Void> p;
	public final String tipo;
	public final Parser<Void> rec;
	
	public Token(Parser<Void> p, Parser<Void> rec, String tipo) {
		this.p = p;
		this.rec = rec;
		this.tipo = tipo;
	}
	
	@Override
	public Result<? extends Void> parse(Error err, String ent) {
		if(err.log) System.out.println("token " + tipo + "|" + ent.substring(0, ent.length() < 10 ? ent.length() : 10));
		try {
			return p.parse(new Error(), ent);
		} catch(Falha f) {
			if(err.update(ent.length(), tipo)) {
				return rec.parse(err, ent);
			} else {
				throw f;
			}
		}
	}

}
