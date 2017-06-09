package lixo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mast.Symbol;

public class StateSymbol extends Symbol {
	public final Set<CommandSymbol> actions = new HashSet<>();
	public final Map<EventSymbol, StateSymbol> transitions = new HashMap<>();
	
	public StateSymbol(String name) {
		super(name);
	}
}
