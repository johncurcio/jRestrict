package bast;

import java.util.List;

public class Prog {
	public final List<Struct> structs;
	public final List<Stat> bloco;
	
	public Prog(List<Struct> structs, List<Stat> bloco) {
		this.structs = structs;
		this.bloco = bloco;
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}

}
