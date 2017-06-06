package bast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvalVisitor implements Visitor<Void, Object> {
	public final Map<String, Object> ctx;
	
	public EvalVisitor() {
		ctx = new HashMap<>();
	}
	
	@Override
	public Object visit(Num exp, Void naousado) {
		if(exp.num.contains(".")) 
			return new BigDecimal(exp.num);
		else
			return new BigInteger(exp.num);
	}

	@Override
	public Object visit(Var exp, Void naousado) {
		return ctx.get(exp.nome);
	}

	@Override
	public Object visit(Soma exp, Void naousado) {
		if(exp.tipo.equals("int"))
			return ((BigInteger)(exp.esq.visit(this, null))).add((BigInteger)(exp.dir.visit(this, null)));
		else
			return ((BigDecimal)(exp.esq.visit(this, null))).add((BigDecimal)(exp.dir.visit(this, null)));
	}

	@Override
	public Object visit(Sub exp, Void naousado) {
		if(exp.tipo.equals("int"))
			return ((BigInteger)(exp.esq.visit(this, null))).subtract((BigInteger)(exp.dir.visit(this, null)));
		else
			return ((BigDecimal)(exp.esq.visit(this, null))).subtract((BigDecimal)(exp.dir.visit(this, null)));
	}

	@Override
	public Object visit(Mult exp, Void naousado) {
		if(exp.tipo.equals("int"))
			return ((BigInteger)(exp.esq.visit(this, null))).multiply((BigInteger)(exp.dir.visit(this, null)));
		else
			return ((BigDecimal)(exp.esq.visit(this, null))).multiply((BigDecimal)(exp.dir.visit(this, null)));
	}

	@Override
	public Object visit(Div exp, Void naousado) {
		if(exp.tipo.equals("int"))
			return ((BigInteger)(exp.esq.visit(this, null))).divide((BigInteger)(exp.dir.visit(this, null)));
		else
			return ((BigDecimal)(exp.esq.visit(this, null))).divide((BigDecimal)(exp.dir.visit(this, null)));
	}

	@Override
	public Object visit(Menor exp, Void naousado) {
		if(exp.tipo.equals("int"))
			return ((BigInteger)(exp.esq.visit(this, null))).compareTo((BigInteger)(exp.dir.visit(this, null))) == -1;
		else
			return ((BigDecimal)(exp.esq.visit(this, null))).compareTo((BigDecimal)(exp.dir.visit(this, null))) == -1;
	}

	@Override
	public Object visit(Atrib st, Void naousado) {
		ctx.put(st.lval, st.rval.visit(this, null));
		return null;
	}

	@Override
	public Object visit(While whl, Void naousado) {
		while((boolean)(whl.cond.visit(this, null))) {
			for(Stat st: whl.corpo) {
				st.visit(this, null);
			}
		}
		return null;
	}

	@Override
	public Object visit(Int2Real exp, Void ctx) {
		return new BigDecimal((BigInteger)(exp.exp.visit(this, ctx)));
	}

	@Override
	public Object visit(AtribIndex st, Void ctx) {
		Object rval = st.rval.visit(this, ctx);
		List<Object> stru = (List<Object>)(st.lval.struct.visit(this, ctx));
		if(stru == null) throw new RuntimeException("struct é nil na posição " + st.pos);
		stru.set(st.lval.idx, rval);
		return null;
	}

	@Override
	public Object visit(Index exp, Void ctx) {
		List<Object> stru = (List<Object>)(exp.struct.visit(this, ctx));
		if(stru == null) throw new RuntimeException("struct é nil na posição " + exp.pos);
		return stru.get(exp.idx);
	}

	@Override
	public Object visit(New exp, Void ctx) {
		List<Object> stru = new ArrayList<>(exp.args.size());
		for(Exp arg: exp.args) {
			stru.add(arg.visit(this, ctx));
		}
		return stru;
	}

	@Override
	public Object visit(Prog prog, Void ctx) {
		for(Stat st: prog.bloco)
			st.visit(this, ctx);
		return null;
	}

	@Override
	public Object visit(Struct stru, Void ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Nil exp, Void ctx) {
		return null;
	}

}
