package bast;

public class Campo extends Node {
	public final String nome;
	public final String tipo;
	
	public Campo(int pos, String nome, String tipo) {
		super(pos);
		this.nome = nome;
		this.tipo = tipo;
	}
}
