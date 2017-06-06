package peg;

import java.util.function.Supplier;

public class LazyParser<T> implements Parser<T> {
	Supplier<Parser<T>> sp;
	
	public LazyParser(Supplier<Parser<T>> sp) {
		this.sp = sp;
	}
	
	@Override
	public Result<? extends T> parse(Error err, String ent) {
		return sp.get().parse(err, ent);
	}

}
