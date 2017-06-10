package peg;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class peg {


	public static <T> Parser<List<T>> star(Parser<? extends T> p) {
		return new Star<>(p);
	}

	public static Parser<Void> starv(Parser<?> p) {
		return new StarVoid(p);
	}

	public static <T> Parser<List<T>> plus(Parser<? extends T> p) {
		return seq(p, star(p), (T x, List<T> xs) -> {
			xs.add(0, x);
			return xs;
		});
	}

	public static <T> Parser<List<T>> listof(Parser<? extends T> p, Parser<?> sep) {
	    return seq(p, star(seqr(sep, p)), (T x, List<T> xs) -> {
	         xs.add(0, x);
	         return xs;
		});
	}
	
	public static <T> Parser<T> chainl(Parser<? extends T> termo, 
			Parser<? extends BiFunction<? super T, ? super T, ? extends T>> op) {
		return new ChainL<>(termo, op);
	}

	public static <TB, TR> Parser<TB> chainl(Parser<? extends TB> termo, 
			Parser<? extends BiFunction<? super TB, ? super TR, ? extends TB>> op,
			Parser<? extends TR> rep) {
		return new ChainLR<>(termo, op, rep);
	}

	public static Parser<Void> plusv(Parser<?> p) {
		return seq(p, starv(p));
	}

	public static <T> Parser<T> opt(Parser<? extends T> p) {
		return peg.choice(p, epsnull());
	}
	
	public final static Parser<Void> eps = Parser.eps;
	
	public static <T> Parser<? extends T> epsnull() {
		return (err, ent) -> new Result<>(null, ent);
	}
	
	public static Parser<Void> lit(String lit) {
		return new Literal(lit);
	}
	
	public static Parser<Void> cls(Predicate<Character> cls) {
		return new CharClass(cls);
	}
	
	public static Parser<Symbol> token(Parser<Void> p, String tipo) {
		return new Atom<Symbol>(new Token(p, eps, tipo), (pos, texto) -> new Symbol(texto, pos));
	}
	
	public static Parser<Symbol> token(Parser<Void> p, String tipo, String pre) {
		return new Atom<Symbol>(
				new Token(p, choice(seq(and(lit(pre)),plus(cls(Character::isJavaIdentifierPart))), eps), tipo),
				(pos, texto) -> new Symbol(texto, pos));
	}

	public static Parser<Symbol> token(Parser<Void> p, String tipo, Parser<Void> tipoErr) {
		return new Atom<Symbol>(new Token(p, choice(tipoErr, eps), tipo),
				(pos, texto) -> new Symbol(texto, pos));
	}

	public static Parser<Symbol> kw(String lit, String pre) {
		return token(seq(lit(lit), not(cls(Character::isJavaIdentifierPart))), lit, pre);		
	}
	
	public static <T1, T2> Parser<T2> fun(Parser<T1> p, Function<? super T1, ? extends T2> f) {
		return new Fun<>(p, f);
	}
	
	public static Parser<Void> seq(Parser<?> p1, Parser<?> p2) {
		return new Seq<>(p1, p2, (r1, r2) -> null);
	}

	public static <T1, T2, R> Parser<R> seq(Parser<? extends T1> p1, Parser<? extends T2> p2,
			BiFunction<? super T1, ? super T2, ? extends R> f) {
		return new Seq<>(p1, p2, f);
	}

	public static Parser<Void> seq(Parser<?> p1, Parser<?> p2, Parser<?> p3) {
		return new TriSeq<>(p1, p2, p3, (r1, r2, r3) -> null);
	}
	
	public static <T1, T2, T3, R> Parser<R> seq(Parser<? extends T1> p1, 
			Parser<? extends T2> p2, Parser<? extends T3> p3, TriFunction<T1, T2, T3, R> f) {
		return new TriSeq<>(p1, p2, p3, f);
	}

	public static Parser<Void> seq(Parser<?> p1, Parser<?> p2, Parser<?> p3, Parser<?> p4) {
		return new TetraSeq<>(p1, p2, p3, p4, (r1, r2, r3, r4) -> null);
	}

	public static <T1, T2, T3, T4, R> Parser<R> seq(Parser<? extends T1> p1, 
			Parser<? extends T2> p2, Parser<? extends T3> p3, 
			Parser<? extends T4> p4, TetraFunction<T1, T2, T3, T4, R> f) {
		return new TetraSeq<>(p1, p2, p3, p4, f);
	}

	public static Parser<Void> seq(Parser<?> p1, Parser<?> p2, Parser<?> p3, Parser<?> p4, Parser<?> p5) {
		return new PentaSeq<>(p1, p2, p3, p4, p5, (r1, r2, r3, r4, r5) -> null);
	}

	public static <T1, T2, T3, T4, T5, R> Parser<R> seq(Parser<? extends T1> p1, 
			Parser<? extends T2> p2, Parser<? extends T3> p3, 
			Parser<? extends T4> p4, Parser<? extends T5> p5, 
			PentaFunction<T1, T2, T3, T4, T5, R> f) {
		return new PentaSeq<>(p1, p2, p3, p4, p5, f);
	}

	public static <T> Parser<T> seql(Parser<T> p1, Parser<?> p2) {
		return new Seq<>(p1, p2, (r1, r2) -> r1);
	}

	public static <T> Parser<T> seqr(Parser<?> p1, Parser<T> p2) {
		return new Seq<>(p1, p2, (r1, r2) -> r2);
	}

	public static <T> Parser<T> choice(Parser<? extends T> p1, Parser<? extends T> p2) {
		return new OrdChoice<>(p1, p2);
	}

	public static <T> Parser<T> choice(Parser<? extends T> p1, Parser<? extends T> p2, 
			Parser<? extends T> p3) {
		return new OrdChoice<>(p1, new OrdChoice<>(p2, p3));
	}

	public static <T> Parser<T> choice(Parser<? extends T> p1, Parser<? extends T> p2, 
			Parser<? extends T> p3, Parser<? extends T> p4) {
		return new OrdChoice<>(p1, new OrdChoice<>(p2, new OrdChoice<>(p3, p4)));
	}

	public static <T> Parser<T> choice(Parser<? extends T> p1, Parser<? extends T> p2, 
			Parser<? extends T> p3, Parser<? extends T> p4, Parser<? extends T> p5) {
		return new OrdChoice<>(p1, new OrdChoice<>(p2, new OrdChoice<>(p3, new OrdChoice<>(p4, p5))));
	}

	public static <T> Parser<T> choice(Parser<? extends T> p1, Parser<? extends T> p2, 
			Parser<? extends T> p3, Parser<? extends T> p4, Parser<? extends T> p5, 
			Parser<? extends T> p6) {
		return new OrdChoice<>(p1, new OrdChoice<>(p2, new OrdChoice<>(p3, new OrdChoice<>(p4, 
				new OrdChoice<>(p5, p6)))));
	}

	public static <T> Parser<T> choice(Parser<? extends T> p1, Parser<? extends T> p2, 
			Parser<? extends T> p3, Parser<? extends T> p4, Parser<? extends T> p5, 
			Parser<? extends T> p6, Parser<? extends T> p7) {
		return new OrdChoice<>(p1, new OrdChoice<>(p2, new OrdChoice<>(p3, new OrdChoice<>(p4, 
				new OrdChoice<>(p5, new OrdChoice<>(p6, p7))))));
	}
	
	public static <T> Parser<T> choice(Parser<? extends T> p1, Parser<? extends T> p2, 
			Parser<? extends T> p3, Parser<? extends T> p4, Parser<? extends T> p5, 
			Parser<? extends T> p6, Parser<? extends T> p7, Parser<? extends T> p8) {
		return new OrdChoice<>(p1, new OrdChoice<>(p2, new OrdChoice<>(p3, new OrdChoice<>(p4, 
				new OrdChoice<>(p5, new OrdChoice<>(p6, new OrdChoice<>(p7,p8)))))));
	}
	
	public static <T> Parser<T> choice(Parser<? extends T> p1, Parser<? extends T> p2, 
			Parser<? extends T> p3, Parser<? extends T> p4, Parser<? extends T> p5, 
			Parser<? extends T> p6, Parser<? extends T> p7, Parser<? extends T> p8,
			Parser<? extends T> p9) {
		return new OrdChoice<>(p1, new OrdChoice<>(p2, new OrdChoice<>(p3, new OrdChoice<>(p4, 
				new OrdChoice<>(p5, new OrdChoice<>(p6, new OrdChoice<>(p7,new OrdChoice<>(p8, p9))))))));
	}

	public static <T> Parser<T> choice(Parser<? extends T> p1, Parser<? extends T> p2, 
			Parser<? extends T> p3, Parser<? extends T> p4, Parser<? extends T> p5, 
			Parser<? extends T> p6, Parser<? extends T> p7, Parser<? extends T> p8,
			Parser<? extends T> p9, Parser<? extends T> p10) {
		return new OrdChoice<>(p1, new OrdChoice<>(p2, new OrdChoice<>(p3, new OrdChoice<>(p4, 
				new OrdChoice<>(p5, new OrdChoice<>(p6, new OrdChoice<>(p7,new OrdChoice<>(p8, 
						new OrdChoice<>(p9,p10)))))))));
	}
	
	public static <T> Parser<T> choice(Parser<? extends T> p1, Parser<? extends T> p2, 
			Parser<? extends T> p3, Parser<? extends T> p4, Parser<? extends T> p5, 
			Parser<? extends T> p6, Parser<? extends T> p7, Parser<? extends T> p8,
			Parser<? extends T> p9, Parser<? extends T> p10, Parser<? extends T> p11) {
		return new OrdChoice<>(p1, new OrdChoice<>(p2, new OrdChoice<>(p3, new OrdChoice<>(p4, 
				new OrdChoice<>(p5, new OrdChoice<>(p6, new OrdChoice<>(p7,new OrdChoice<>(p8, 
						new OrdChoice<>(p9,new OrdChoice<>(p10,p11))))))))));
	}
	public static <T> Parser<T> lazy(Supplier<Parser<T>> sp) {
		return new LazyParser<>(sp);
	}
	
	public static Parser<Void> not(Parser<?> p) {
		return new Not(p);
	}
	
	public static Parser<Void> and(Parser<?> p) {
		return new And(p);
	}
	
	public static <T> Pair<T, List<String>> run(Parser<T> p, String ent) {
		List<String> erros = new ArrayList<>();
		T res = null;
		Error err = new Error();
		while(true) {
			try {
				Result<? extends T> suf = p.parse(err, ent);
				res = suf.node;
				if(!suf.saida.trim().isEmpty()) {
					if(!error(ent, erros, err)) break;
					err.recover();
				} else break;
			} catch(Falha f) {
				if(!error(ent, erros, err)) break;
				err.recover();
			}
		}
		return new Pair<>(res, erros);
	}

	public static boolean error(String ent, List<String> erros, Error err) {
		int pos = ent.length() - err.pos;
		String[] ntipos = new String[err.tipos.size()];
		int i = 0;
		for(String tipo: err.tipos) {
			ntipos[i++] = tipo;
		}
		int endOfToken = (ent.indexOf(" ", pos) == -1 ? ent.indexOf("\n", pos): ent.indexOf(" ", pos));
		int beginOfToken = ent.lastIndexOf(" ", pos);
		endOfToken = (endOfToken == -1 ? ent.length()-1 : endOfToken);
		beginOfToken = (beginOfToken == -1 ? 0 : beginOfToken);
		String msg = "erro de sintaxe na posicao " + pos + " (\"" + ent.substring(beginOfToken, endOfToken).replace("\n", " ") + "\")" + 
				", era esperado " + (ntipos.length > 0 ? String.join(", ", ntipos) : " final da entrada");
		System.err.println(msg);
		if(erros.contains(msg)) return false;
		else return erros.add(msg);
	}
}
