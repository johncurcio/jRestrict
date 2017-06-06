package peg;

import java.util.function.BiFunction;

public class ChainL<T> implements Parser<T> {
	public final Parser<? extends T> termo;
	public final Parser<? extends BiFunction<? super T, ? super T, ? extends T>> op;
	
	public ChainL(Parser<? extends T> termo, 
			Parser<? extends BiFunction<? super T, ? super T, ? extends T>> op) {
		this.termo = termo;
		this.op = op;
	}

	@Override
	public Result<? extends T> parse(Error err, String ent) {
		Result<? extends T> resq = termo.parse(err, ent);
		T esq = resq.node;
		ent = resq.saida;
		while(true) {
			try {
				Result<? extends BiFunction<? super T, ? super T, ? extends T>> rop = 
						op.parse(err, ent);
				Result<? extends T> rdir = termo.parse(err, rop.saida);
				esq = rop.node.apply(esq, rdir.node);
				ent = rdir.saida;
			} catch(Falha f) {
				return new Result<>(esq, ent);
			}
		}
	}
}
