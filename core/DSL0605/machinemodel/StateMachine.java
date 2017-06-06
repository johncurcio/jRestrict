package machinemodel;
import java.util.HashSet;
import java.util.Set;

public class StateMachine {
	public final State start;
	public Set<String> resetEvts = new HashSet<>();
	
	public StateMachine(State _start) {
		start = _start;
	}
	
	public void addResetEvents(Event evt) {
		resetEvts.add(evt.codigo);
	}
}
