package peg;

import java.util.function.BiFunction;

public class ChainLR<TB, TR> implements Parser<TB> {
	public final Parser<? extends TB> base;
	public final Parser<? extends TR> rep;
	public final Parser<? extends BiFunction<? super TB, ? super TR, ? extends TB>> op;
	
	public ChainLR(Parser<? extends TB> base, 
			Parser<? extends BiFunction<? super TB, ? super TR, ? extends TB>> op,
			Parser<? extends TR> rep) {
		this.base = base;
		this.op = op;
		this.rep = rep;
	}

	@Override
	public Result<? extends TB> parse(Error err, String ent) {
		Result<? extends TB> resq = base.parse(err, ent);
		TB esq = resq.node;
		ent = resq.saida;
		while(true) {
			try {
				Result<? extends BiFunction<? super TB, ? super TR, ? extends TB>> rop = 
						op.parse(err, ent);
				Result<? extends TR> rdir = rep.parse(err, rop.saida);
				esq = rop.node.apply(esq, rdir.node);
				ent = rdir.saida;
			} catch(Falha f) {
				return new Result<>(esq, ent);
			}
		}
	}
}
