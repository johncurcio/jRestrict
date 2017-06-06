package bast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeVisitor implements Visitor<Void, String> {
	final List<String> erros;
	
	Map<String, String> tvars = new HashMap<>();
	Map<String, Struct> structs = new HashMap<>();
	
	// supertipos *diretos*
	Map<String, String> supertypes = new HashMap<>();
	
	public TypeVisitor(List<String> erros) {
		this.erros = erros;
	}
	
	public boolean subtype(String t1, String t2) {
		if(t1.equals(t2)) return true;
		if(t1.equals("nil") && 
				!(t2.equals("bool") || t2.equals("int") || t2.equals("real")))
			return true;
		if(t2.equals(supertypes.get(t1))) return true;
		String sup = supertypes.get(t1);
		if(sup == null) return false;
		return subtype(sup, t2);
	}
	
	@Override
	public String visit(Atrib st, Void ctx) {
		String trval = st.rval.visit(this, ctx);
		if(tvars.containsKey(st.lval)) {
			String tlval = tvars.get(st.lval);
			if(trval.equals("int") && tlval.equals("real")) {
				st.rval = new Int2Real(st.rval);
				trval = "real";
			}
			if(!subtype(trval, tvars.get(st.lval)))
				erros.add("tipos da atribução na posição " + st.pos + " incompatíveis, lado esquerdo é " +
						tvars.get(st.lval) + " e lado direito é " + trval);
		} else tvars.put(st.lval, trval);
		return null;
	}

	@Override
	public String visit(Div exp, Void ctx) {
		String tesq = exp.esq.visit(this, ctx);
		String tdir = exp.dir.visit(this, ctx);
		if(tesq.equals("int") && tdir.equals("int")) {
			exp.tipo = "int";
			return "int";
		}
		if(tesq.equals("int")) {
			exp.esq = new Int2Real(exp.esq);
			tesq = "real"; 
		}
		if(!tesq.equals("real"))
			erros.add("lado esquerdo da divisão na posição " + exp.pos + " não é número mas " + tesq);
		if(tdir.equals("int")) {
			exp.dir = new Int2Real(exp.dir);
			tdir = "real"; 
		}
		if(!tdir.equals("real"))
			erros.add("lado direito da divisão na posição " + exp.pos + " não é número mas " + tdir);
		exp.tipo = "real";
		return "real";
	}

	@Override
	public String visit(Menor exp, Void ctx) {
		String tesq = exp.esq.visit(this, ctx);
		String tdir = exp.dir.visit(this, ctx);
		if(tesq.equals("int") && tdir.equals("int")) {
			exp.tipo = "int";
			return "bool";
		}
		if(tesq.equals("int")) {
			exp.esq = new Int2Real(exp.esq);
			tesq = "real"; 
		}
		if(!tesq.equals("real"))
			erros.add("lado esquerdo da comparação na posição " + exp.pos + " não é número mas " + tesq);
		if(tdir.equals("int")) {
			exp.dir = new Int2Real(exp.dir);
			tdir = "real"; 
		}
		if(!tdir.equals("real"))
			erros.add("lado direito da comparação na posição " + exp.pos + " não é número mas " + tdir);
		exp.tipo = "real";
		return "bool";
	}

	@Override
	public String visit(Mult exp, Void ctx) {
		String tesq = exp.esq.visit(this, ctx);
		String tdir = exp.dir.visit(this, ctx);
		if(tesq.equals("int") && tdir.equals("int")) {
			exp.tipo = "int";
			return "int";
		}
		if(tesq.equals("int")) {
			exp.esq = new Int2Real(exp.esq);
			tesq = "real"; 
		}
		if(!tesq.equals("real"))
			erros.add("lado esquerdo da multiplicação na posição " + exp.pos + " não é número mas " + tesq);
		if(tdir.equals("int")) {
			exp.dir = new Int2Real(exp.dir);
			tdir = "real"; 
		}
		if(!tdir.equals("real"))
			erros.add("lado direito da multiplicação na posição " + exp.pos + " não é número mas " + tdir);
		exp.tipo = "real";
		return "real";
	}

	@Override
	public String visit(Soma exp, Void ctx) {
		String tesq = exp.esq.visit(this, ctx);
		String tdir = exp.dir.visit(this, ctx);
		if(tesq.equals("int") && tdir.equals("int")) {
			exp.tipo = "int";
			return "int";
		}
		if(tesq.equals("int")) {
			exp.esq = new Int2Real(exp.esq);
			tesq = "real"; 
		}
		if(!tesq.equals("real"))
			erros.add("lado esquerdo da soma na posição " + exp.pos + " não é número mas " + tesq);
		if(tdir.equals("int")) {
			exp.dir = new Int2Real(exp.dir);
			tdir = "real"; 
		}
		if(!tdir.equals("real"))
			erros.add("lado direito da soma na posição " + exp.pos + " não é número mas " + tdir);
		exp.tipo = "real";
		return "real";
	}

	@Override
	public String visit(Sub exp, Void ctx) {
		String tesq = exp.esq.visit(this, ctx);
		String tdir = exp.dir.visit(this, ctx);
		if(tesq.equals("int") && tdir.equals("int")) {
			exp.tipo = "int";
			return "int";
		}
		if(tesq.equals("int")) {
			exp.esq = new Int2Real(exp.esq);
			tesq = "real"; 
		}
		if(!tesq.equals("real"))
			erros.add("lado esquerdo da subtração na posição " + exp.pos + " não é número mas " + tesq);
		if(tdir.equals("int")) {
			exp.dir = new Int2Real(exp.dir);
			tdir = "real"; 
		}
		if(!tdir.equals("real"))
			erros.add("lado direito da subtração na posição " + exp.pos + " não é número mas " + tdir);
		exp.tipo = "real";
		return "real";
	}

	@Override
	public String visit(Num exp, Void ctx) {
		exp.tipo = exp.num.contains(".") ? "real" : "int";
		return exp.tipo;
	}

	@Override
	public String visit(Var exp, Void ctx) {
		exp.tipo = tvars.getOrDefault(exp.nome, "int");
		return exp.tipo;
	}

	@Override
	public String visit(While st, Void ctx) {
		String tcond = st.cond.visit(this, ctx);
		if(!tcond.equals("bool"))
			erros.add("condição do while na posição " + st.pos + " não é booleano mas " + tcond);
		for(Stat s: st.corpo) {
			s.visit(this, ctx);
		}
		return null;
	}

	@Override
	public String visit(Int2Real exp, Void ctx) {
		String texp = exp.exp.visit(this, ctx);
		if(!texp.equals("int")) 
			erros.add("operando da conversão para real não é inteiro na posição " + exp.pos);
		exp.tipo = "real";
		return "real";
	}

	@Override
	public String visit(AtribIndex st, Void ctx) {
		String trval = st.rval.visit(this, ctx);
		String tlval = st.lval.visit(this, ctx);
		if(trval.equals("int") && tlval.equals("real")) {
			st.rval = new Int2Real(st.rval);
			trval = "real";
		}
		if(!subtype(trval, tlval))
			erros.add("tipos da atribução na posição " + st.pos + " incompatíveis, lado esquerdo é " +
					tlval + " e lado direito é " + trval);
		return null;
	}

	@Override
	public String visit(Index exp, Void ctx) {
		String tstru = exp.struct.visit(this, ctx);
		Struct stru = structs.get(tstru);
		if(stru == null) {
			erros.add("tentando indexar um " + tstru + " na posição " + exp.pos);
		} else if(!stru.tcampos.containsKey(exp.campo)) {
			erros.add("tentando indexar campo " + exp.campo + " que não existe na struct " + tstru +
			 	" na posição " + exp.pos);
		}
		exp.idx = stru.ncampos.indexOf(exp.campo);
		exp.tipo = stru.tcampos.getOrDefault(exp.campo, "int");
		return exp.tipo;
	}

	@Override
	public String visit(New exp, Void ctx) {
		if(!structs.containsKey(exp.nome))
			erros.add("tipo da struct " + exp.nome + " na instanciação da posição " + exp.pos + " não existe");
		else {
			Struct stru = structs.get(exp.nome);
			if(stru.ncampos.size() != exp.args.size())
				erros.add("número de argumentos do construtor não bate com o número " + stru.ncampos.size() + " da struct " + exp.nome + " na posição " + exp.pos);
			for(int i = 0; i < exp.args.size(); i++) {
				String targ = exp.args.get(i).visit(this, ctx);
				String tcampo = i < stru.ncampos.size() ? stru.tcampos.get(stru.ncampos.get(i)) : "int";
				if(targ.equals("int") && tcampo.equals("real")) {
					exp.args.set(i, new Int2Real(exp.args.get(i)));
					targ = "real";
				}
				if(!subtype(targ, tcampo))
					erros.add("tipos do parâmetro " + i + " do construtor na posição " + exp.pos +
							" incompatíveis, argument é " +
							targ + " e campo é " + tcampo);
			}
		}
		exp.tipo = exp.nome;
		return exp.nome;
	}

	@Override
	public String visit(Prog prog, Void ctx) {
		for(Struct st: prog.structs) {
			if(structs.containsKey(st.nome)) {
				erros.add("struct " + st.nome + " redeclarada na posição " + st.pos);
			} else structs.put(st.nome, st);
		}
		for(Struct st: structs.values()) {
			st.visit(this, ctx);
		}
		for(Stat st: prog.bloco) {
			st.visit(this, ctx);
		}
		return null;
	}

	@Override
	public String visit(Struct stru, Void ctx) {
		if(!stru.tcampos.isEmpty()) return null;
		if(stru.sup != null) {
			if(!structs.containsKey(stru.sup)) {
				erros.add("supertipo " + stru.sup + " da struct " + stru.nome + "na posição " + stru.pos + " não existe");
			} else if(subtype(stru.sup, stru.nome)) {
				erros.add("supertipo " + stru.sup + " da struct " + stru.nome + "na posição " + stru.pos + " já é subtipo dela");
			} else {
				supertypes.put(stru.nome, stru.sup);
				Struct sup = structs.get(stru.sup); 
				sup.visit(this, ctx);
				stru.tcampos.putAll(sup.tcampos);
				stru.ncampos.addAll(sup.ncampos);
			}
		}
		for(Campo c: stru.campos) {
			if(stru.tcampos.containsKey(c.nome)) {
				erros.add("campo " + c.nome + " redeclarado na struct " + stru.nome + " na posição " + c.pos);
			} else if(!structs.containsKey(c.tipo) && 
					!c.tipo.equals("int") && !c.tipo.equals("real") &&
					!c.tipo.equals("bool")) {
				erros.add("tipo " + c.tipo + " do campo " + c.nome + " da struct " + stru.nome + " na posição " + c.pos + " não existe");
			} else {
				stru.ncampos.add(c.nome);
				stru.tcampos.put(c.nome, c.tipo);
			}
		}
		return null;
	}

	@Override
	public String visit(Nil exp, Void ctx) {
		exp.tipo = "nil";
		return "nil";
	}

}
