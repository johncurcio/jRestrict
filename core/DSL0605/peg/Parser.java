package peg;

import java.util.ArrayList;
import java.util.List;

public interface Parser<T> {
	Result<? extends T> parse(Error err, String ent);

	public final static Parser<Void> eps = (err, ent) -> Result.empty(ent); 
	public static <T> Parser<List<T>> epslist() {
		return (err, ent) -> new Result<>(new ArrayList<>(), ent); 
	}
}
