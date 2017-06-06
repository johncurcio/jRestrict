import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import handcoded.BlocoParser;
import peg.Pair;
import peg.Parser;
import peg.Symbol;

import static peg.peg.*;

public class BlocoTest {
	public static Parser<bast.Prog> prog;
	public static Parser<bast.Struct> struct;
	public static Parser<bast.Campo> campo;
	public static Parser<List<bast.Stat>> bloco;
	public static Parser<bast.Stat> stat;
	public static Parser<bast.Exp> index;
	public static Parser<bast.Exp> exp, aexp, termo, fator, var;
	public static Parser<List<bast.Exp>> args;

	public final static Parser<Void> comentario = seq(lit("--"), star(cls((c) -> c != '\n')));
	public final static Parser<Void> espaco = choice(plus(cls(Character::isWhitespace)), comentario);
	public final static Parser<Void> sp = starv(espaco);
	public final static Parser<Symbol> WHILE = seqr(sp, kw("while", "wh"));
	public final static Parser<Symbol> DO = seqr(sp, kw("do", "d"));
	public final static Parser<Symbol> END = seqr(sp, kw("end", "en"));
	public final static Parser<Symbol> STRUCT = seqr(sp, kw("struct", "stru"));
	public final static Parser<Symbol> IS = seqr(sp, kw("is", "is"));
	public final static Parser<Symbol> NEW = seqr(sp, kw("new", "ne"));
	public final static Parser<Symbol> NIL = seqr(sp, kw("nil", "ni"));
	public final static Parser<Symbol> keywords = choice(kw("while", "wh"), 
			kw("struct", "stru"), kw("is", "is"), kw("new", "ne"), kw("nil", "ni"), kw("do", "d"), kw("end", "en")); 
	public final static Parser<Symbol> NOME = seqr(sp, token(seq(not(keywords),
			seq(cls(Character::isJavaIdentifierStart), star(cls(Character::isJavaIdentifierPart)))), "nome"));
	public final static Parser<Symbol> NUM = seqr(sp, token(seq(plusv(cls(Character::isDigit)), opt(seq(lit("."), plusv(cls(Character::isDigit))))), "num"));

	public final static Parser<BiFunction<bast.Exp, bast.Exp, bast.Exp>> MAIS =
			fun(op("+"), (op) -> (esq, dir) -> new bast.Soma(op.pos, esq, dir));
	public final static Parser<BiFunction<bast.Exp, bast.Exp, bast.Exp>> MENOS =
			fun(op("-"), (op) -> (esq, dir) -> new bast.Sub(op.pos, esq, dir));
	public final static Parser<BiFunction<bast.Exp, bast.Exp, bast.Exp>> VEZES =
			fun(op("*"), (op) -> (esq, dir) -> new bast.Mult(op.pos, esq, dir));
	public final static Parser<BiFunction<bast.Exp, bast.Exp, bast.Exp>> SOBRE =
			fun(op("/"), (op) -> (esq, dir) -> new bast.Div(op.pos, esq, dir));
	public final static Parser<BiFunction<bast.Exp, Symbol, bast.Exp>> PONTO =
			fun(op("."), (op) -> (esq, dir) -> new bast.Index(op.pos, esq, dir));

	
	public static Parser<Symbol> op(String texto) {
		return seqr(sp, token(lit(texto), texto));
	}

	/*
	 prog   -> struct* bloco
     bloco  -> stat*
     struct -> "struct" NAME ("<:" NAME)? campo+ "end"
     campo  -> NAME ":" NAME
	 bloco := stat*
     stat  := "while" exp "do" bloco "end" | index "=" exp ";" | NAME "=" exp ";"
	 exp  := aexp "<" aexp | aexp
	 aexp := termo ("+" termo | "-" termo)*
	 termo := fator ("*" fator | "/" fator)*
	 fator := "(" exp ")" | NUM | index | "new" NAME "(" args? ")" | var | NIL
	 var := NAME
	 index := (var "." NAME) ("." NAME)*
	 args := exp ("," exp)* 
	*/
	static {
		prog = lazy(() -> seq(star(struct), bloco, (List<bast.Struct> r1, List<bast.Stat> r2) -> new bast.Prog(r1, r2)));
		struct = lazy(() -> seq(STRUCT, NOME, opt(seqr(op("<:"), NOME)), plus(campo), END,
				(r1, r2, r3, r4, r5) -> new bast.Struct(r1.pos, r2.texto, r3, r4)));
		campo = lazy(() -> seq(NOME, op(":"), NOME, (r1, r2, r3) -> new bast.Campo(r2.pos, r1.texto, r3.texto)));
		bloco = lazy(() -> star(stat));
		stat = lazy(() -> choice(
				seq(WHILE, exp, DO, bloco, END, (r1, r2, r3, r4, r5) -> new bast.While(r1.pos, r2, r4)), 
				seq(NOME, op("="), exp, op(";"), (r1, r2, r3, r4) -> new bast.Atrib(r2.pos, r1.texto, r3)),
				seq(index, op("="), exp, op(";"), 
						(r1, r2, r3, r4) -> new bast.AtribIndex(r2.pos, r1, r3))));
		index = lazy(() -> chainl(
					seq(var, op("."), NOME, (r1, r2, r3) -> new bast.Index(r2.pos, r1, r3)), 
					PONTO,
					NOME
					));
		exp = lazy(() -> choice(
				seq(aexp, op("<"), aexp, (r1, r2, r3) -> new bast.Menor(r2.pos, r1, r3)), 
				aexp));
		aexp = lazy(() -> chainl(termo, choice(MAIS, MENOS)));
		termo = lazy(() -> chainl(fator, choice(VEZES, SOBRE))); 
		fator = lazy(() -> choice(
				seq(op("("), exp, op(")"), (r1, r2, r3) -> r2), 
				index,
				var,
				fun(NIL, (n) -> new bast.Nil(n.pos)),
				seq(NEW, NOME, op("("), opt(args), op(")"), (r1, r2, r3, r4, r5) -> new bast.New(r1.pos, r2.texto, r4)),
				fun(NUM, (n) -> new bast.Num(n.pos, n.texto))));
		var = lazy(() -> fun(NOME, (n) -> new bast.Var(n.pos, n.texto)));
		args = lazy(() -> listof(exp, op(",")));
	}
	
	public static Map<String, Object> exec(boolean hc) {
		Pair<bast.Prog, List<String>> res = hc ? testhc() : test();
		bast.TypeVisitor tvisitor = new bast.TypeVisitor(res.y);
		bast.EvalVisitor visitor = new bast.EvalVisitor();
		if(res.y.isEmpty()) {
			res.x.visit(tvisitor, null);
		}
		if(res.y.isEmpty()) {
			res.x.visit(visitor, null);
			return visitor.ctx;
		}
		for(String err: res.y) System.err.println(err);
		return null;
	}

	public static Pair<bast.Prog, List<String>> test() {
		String entrada =
				"struct IntNode <: Node\n" +
				"  val: int\n" +
				"end\n"	+	
				"struct Node <: Obj\n" +
				"  esq: Node\n" +
				"  dir: Node\n" +
		        "end\n" +
				"struct Obj\n" +
		        " hash: int\n" +
		        "end\n" +
		        "obj = new IntNode(5, nil, nil, 50);\n" +
		        "no = new IntNode(10, obj, nil, 20);\n" +
		        "x = no.hash;\n" +
				"n = 50;\n" + 
				"f = 1.0;\n" + 
			    "cond = 0 < n;" +
				"while cond do\n" + 
				"    f = f * n;\n" +
				"    n = n - 1;\n" +
				"    while 0 < n do\n" + 
				"       f = f * n;\n" + 
				"       n = n - 1;       -- falta um =  \n" + 
				"    end\n"+
				"    cond = 0 < n;" +
				"end";
		return run(prog, entrada);
	}

	public static Pair<bast.Prog, List<String>> testhc() {
		String entrada =
				"struct IntNode <: Node is\n" +
				"  val: int\n" +
				"end\n"	+	
				"struct Node <: Obj is\n" +
				"  esq: Node\n" +
				"  dir: Node\n" +
		        "end\n" +
				"struct Obj is\n" +
		        " hash: int\n" +
		        "end\n" +
		        "obj = new IntNode(5, nil, nil, 50);\n" +
		        "no = new IntNode(10, obj, nil, 20);\n" +
		        "x = no.hash;\n" +
				"n = 50;\n" + 
				"f = 1.0;\n" + 
			    "cond = 0 < n;" +
				"while cond do\n" + 
				"    f = f * n;\n" +
				"    n = n - 1;\n" +
				"    while 0 < n do\n" + 
				"       f = f * n;\n" + 
				"       n = n - 1;       -- falta um =  \n" + 
				"    end\n"+
				"    cond = 0 < n;" +
				"end";
		BlocoParser bp = new BlocoParser(entrada);
		bast.Prog saida = bp.parse();
		return new Pair<>(saida, bp.erros);
	}

}


/*
[erro de sintaxe na posição 47("while n > "), era esperado *, operador binário, +, ;, -, >, /, 
 erro de sintaxe na posição 69("= f n;
   "), era esperado *, operador binário, +, do, -, /, 
 erro de sintaxe na posição 69("= f n;
   "), era esperado *, nome, end, operador binário, +, do, while, -, /]

*/