package bast;

public abstract class ExpBin extends Node implements Exp{
	public Exp esq;
	public Exp dir;
	
	public ExpBin(int pos, Exp esq, Exp dir) {
		super(pos);
		this.esq = esq;
		this.dir = dir;
	}
}
