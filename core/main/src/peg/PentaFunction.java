package peg;

public interface PentaFunction<T1, T2, T3, T4, T5, R> {
	R apply(T1 x1, T2 x2, T3 x3, T4 x4, T5 x5);
}
