package peg;

public class Pair<T1, T2> {
	public final T1 x;
	public final T2 y;
	
	public Pair(T1 x, T2 y) {
		this.x = x;
		this.y = y;
	}
	
	public static <T1, T2> Pair<T1, T2> pair(T1 x, T2 y) {
		return new Pair<>(x, y);
	}
}
