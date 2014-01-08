package utility;

public class Pair<S,T> implements java.io.Serializable{
	
	private final S s;
	private final T t;
	
	public Pair(S s, T t) {
		this.s = s;
		this.t = t;
	}
	
	public S get1() {
		return this.s;
	}
	
	public T get2() {
		return this.t;
	}

}