package handcoded;

import java.util.ArrayList;
import java.util.List;

import bast.*;

public class BlocoParser extends Parser<Prog> {
	/*
	 prog   := struct* bloco
     struct := "struct" NAME ("<:" NAME)? campo+ "end"
     campo  := NAME ":" NAME
	 bloco := stat*
     stat  := "while" exp "do" bloco "end" | NAME "=" exp ";" |
           NAME ("." NAME)+ "=" exp ";" 
	 exp  := aexp "<" aexp | aexp
	 aexp := termo ("+" termo | "-" termo)*
	 termo := fator ("*" fator | "/" fator)*
	 fator := "(" exp ")" | NUM | NAME | "new" NAME "(" args? ")" | NAME ("." NAME)+ | "(" exp ")"
	 args := exp ("," exp)* 
	*/
	public BlocoParser(Lexer _lex) {
		super(_lex);
	}

	public BlocoParser(String _ent) {
		super(new BlocoLexer(_ent));
	}
	
	@Override
	public Prog parse() {
		Prog p = prog();
		matchOrReport(EOF);
		return p;
	}
	
	// 	 prog   -> struct* bloco
	public Prog prog() {
		List<Struct> sts = new ArrayList<>();
		while(la.tipo == BlocoLexer.STRUCT) {
			sts.add(struct());
		}
		return new Prog(sts, bloco());
	}
	
    // bloco  -> stat*
	public List<Stat> bloco() {
		List<Stat> sts = new ArrayList<>();
		while(la.tipo == BlocoLexer.WHILE ||
				la.tipo == BlocoLexer.NOME) {
			sts.add(stat());
		}
		return sts;
	}
	
	// struct -> "struct" NAME ("<:" NAME)? is campo+ "end"
	public Struct struct() {
		int p = la.local;
		match(BlocoLexer.STRUCT);
		Token nome = la;
		Token sup = null;
		if(!matchOrReport(BlocoLexer.NOME)) { nome = null; }
		if(match(BlocoLexer.EXTENDS)) {
			sup = la;
			if(!matchOrReport(BlocoLexer.NOME)) { sup = null; }
		}
		if(matchOrReport(BlocoLexer.IS)) {}
		List<Campo> campos = new ArrayList<>();
		do {
			campos.add(campo());
		} while(la.tipo == BlocoLexer.NOME);
		if(matchOrReport(BlocoLexer.END)) {}
		return new Struct(p, nome.texto, sup == null ? null : sup.texto, campos);
	}
	
	//  campo  -> NAME ":" NAME
	public Campo campo() {
		Token nome = la;
		if(matchOrReportAndSync(BlocoLexer.NOME, BlocoLexer.END)) {
			int p = la.local;
			if(matchOrReport(':')) {}
			Token tipo = la;
			if(!matchOrReport(BlocoLexer.NOME)) { tipo = null; }
			return new Campo(p, nome.texto, tipo == null ? "int" : tipo.texto);
		} else return new Campo(la.local, "erro", "int");
	}

	/*
      stat  := "while" exp "do" bloco "end" | NAME "=" exp ";" |
        NAME ("." NAME)+ "=" exp ";" 

	 */
	public Stat stat() {
		int p = la.local;
		if(match(BlocoLexer.WHILE)) {
			Exp cond = exp();
			if(matchOrReport(BlocoLexer.DO)) {}
			List<Stat> corpo = bloco();
			if(matchOrReport(BlocoLexer.END)) {}
			return new While(p, cond, corpo);
		} else {
			Token id = la;
			if(matchOrReportAndSync(BlocoLexer.NOME, ';')) {
				if(la.tipo ==  '.') {
					Exp idx = index(id);
					p = la.local;
					if(matchOrReport('=')) {}
					Exp rval = exp();
					if(matchOrReport(';')) {}
					return new AtribIndex(p, idx, rval);
				} else {
					p = la.local;
					if(matchOrReport('=')) {}
					Exp rval = exp();
					if(matchOrReport(';')) {}
					return new Atrib(p, id.texto, rval);
				}
			}
			return new Skip(la.local);
		}
	}

	// exp  := aexp "<" aexp | aexp
	public Exp exp() {
		Exp esq = aexp();
		if(la.tipo == '<' || la.tipo == '(' || la.tipo == BlocoLexer.NOME || la.tipo == BlocoLexer.NUM
				|| la.tipo == BlocoLexer.NEW || la.tipo == BlocoLexer.NIL) {
			Token op = la;
			matchOrReport('<');
			return new Menor(op.local, esq, aexp());
		}
		return esq;
	}

	// aexp := termo (‘+’ termo | ‘-’ termo)*
	public Exp aexp() {
		Exp res = termo();
		while(la.tipo == '+' || la.tipo == '-' || la.tipo == '(' || la.tipo == BlocoLexer.NOME || la.tipo == BlocoLexer.NUM
				|| la.tipo == BlocoLexer.NEW || la.tipo == BlocoLexer.NIL) {
			Token op = la;
			matchOrReport('+', '-');
			if(op.tipo == '-') {
				res = new Sub(op.local, res, termo());
			} else {
				res = new Soma(op.local, res, termo());
			}
		}
		return res;
	}
	
	// termo := fator (‘*’ fator | ‘/’ fator)*
	public Exp termo() {
		Exp res = fator();
		while(la.tipo == '*' || la.tipo == '/' || la.tipo == '(' || la.tipo == BlocoLexer.NOME || la.tipo == BlocoLexer.NUM
				|| la.tipo == BlocoLexer.NEW || la.tipo == BlocoLexer.NIL) {
			Token op = la;
			matchOrReport('*', '/');
			if(op.tipo == '/') {
				res = new Div(op.local, res, fator());
			} else {
				res = new Mult(op.local, res, fator());
			}
		}
		return res;
	}
	
	// fator := "(" exp ")" | NUM | NAME | "new" NAME "(" args? ")" | NAME ("." NAME)+ | NIL
	public Exp fator() {
		if(la.tipo == '(') {
			skip();
			Exp res = exp();
			matchOrReport(')');
			return res;
		} else if(la.tipo == BlocoLexer.NIL) {
			Token nil = skip();
			return new Nil(nil.local);
		} else if(la.tipo == BlocoLexer.NUM) {
			Token num = skip();
			return new Num(num.local, num.texto);
		} else if(la.tipo == BlocoLexer.NOME) {
			if(peek(1).tipo == '.') {
				Token id = skip();
				return index(id);
			} else {
				Token nome = skip();
				return new Var(nome.local, nome.texto);
			}
		} else if(la.tipo == BlocoLexer.NEW) {
			Token n = skip();
			Token nome = la;
			if(!matchOrReport(BlocoLexer.NOME)) { nome = null; }
			matchOrReport('(');
			List<Exp> args = new ArrayList<>();
			if(la.tipo != ')')
				args = args();
			matchOrReport(')');
			return new New(n.local, nome.texto, args);
		} else {
			matchOrReport('(', BlocoLexer.NUM, BlocoLexer.NOME, BlocoLexer.NEW);
			return new Num(la.local, "0");
		}
	}

	// 	 args := exp ("," exp)* 
	public List<Exp> args() {
		List<Exp> res = new ArrayList<>();
		res.add(exp());
		while(la.tipo == ',' || la.tipo == '(' || la.tipo == BlocoLexer.NOME || la.tipo == BlocoLexer.NUM
				|| la.tipo == BlocoLexer.NEW || la.tipo == BlocoLexer.NIL) {
			matchOrReport(',');
			res.add(exp());
		}
		return res;
	}
	
	// index = NAME ("." NAME)+
	public Exp index(Token id) {
		Exp res = new Var(id.local, id.texto);
		while(la.tipo == '.' || la.tipo == BlocoLexer.NOME) {
			Token op = la;
			matchOrReport('.');
			Token campo = la;
			if(matchOrReport(BlocoLexer.NOME)) {
				res = new Index(op.local, res, campo.texto);
			}
		}
		return res;
	}
}
