package peg;

public class Result<T> {
	public final T node;
	public final String saida;
	
	public Result(T node, String saida) {
		this.node = node;
		this.saida = saida;
	}
	
	public static Result<Void> empty(String saida) {
		return new Result<Void>(null, saida);
	}
}
