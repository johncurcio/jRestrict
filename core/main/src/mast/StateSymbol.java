package mast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateSymbol extends Symbol {
	public final Set<CommandSymbol> actions = new HashSet<>();
	public final Map<EventSymbol, StateSymbol> transitions = new HashMap<>();
	
	public StateSymbol(String name) {
		super(name);
	}
}
