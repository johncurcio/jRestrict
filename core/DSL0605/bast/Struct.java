package bast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import peg.Symbol;

public class Struct extends Node {
	public final String nome;
	public final String sup;
	public final List<Campo> campos;
	
	public Map<String, String> tcampos = new HashMap<>();
	public List<String> ncampos = new ArrayList<>();
	
	public Struct(int pos, String nome, Symbol sup, List<Campo> campos) {
		super(pos);
		this.nome = nome;
		this.sup = sup != null ? sup.texto : null;
		this.campos = campos;
	}

	public Struct(int pos, String nome, String sup, List<Campo> campos) {
		super(pos);
		this.nome = nome;
		this.sup = sup;
		this.campos = campos;
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
