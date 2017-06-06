package peg;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Error {
	public int pos = Integer.MAX_VALUE;
	Set<String> tipos = new HashSet<>();
	Map<Integer, Integer> recover = new HashMap<>();
	
	public final boolean log;
	
	public Error() { log = false; }
	public Error(boolean log) { this.log = log; }
	
	public boolean update(int pos, String tipo) {
		if(log) System.out.println("update " + pos + "|" + this.pos + "|" + tipo);
		if(pos < this.pos) {
			this.pos = pos;
			this.tipos.clear();
			this.tipos.add(tipo);
			if(log) System.out.println("check recover " + pos + "|" + 
										this.tipos.size() + "|" + 
										(recover.containsKey(pos) && 
										 (recover.get(pos) == this.tipos.size())));
			return recover.containsKey(pos) && (recover.get(pos) == this.tipos.size());
		} else if(pos == this.pos) {
			this.tipos.add(tipo);
			if(log) System.out.println("check recover " + pos + "|" + 
					this.tipos.size() + "|" + 
					(recover.containsKey(pos) && 
					 (recover.get(pos) == this.tipos.size())));
			return recover.containsKey(pos) && (recover.get(pos) == this.tipos.size());
		}
		return false;
	}
	
	public void recover() {
		if(log) System.out.println("recover " + pos + "|" + tipos.size());
		recover.put(pos, tipos.size());
		pos = Integer.MAX_VALUE;
		tipos.clear();
	}

}
