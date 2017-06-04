package peg;

import java.util.function.Predicate;

public class CharClass implements Parser<Void> {
	public final Predicate<Character> cls;
	
	public CharClass(Predicate<Character> cls) {
		this.cls = cls;
	}

	@Override
	public Result<Void> parse(Error err, String ent) {
		if(!ent.isEmpty() && cls.test(ent.charAt(0))) {
			return Result.empty(ent.substring(1));
		} else {
			throw new Falha();
		}
	}
	
}
