package machinemodel;

public class Transition {
	public final Event evt;
	public final State fonte;
	public final State alvo;
	
	public Transition(State _fonte, Event _evt, State _alvo) {
		evt = _evt;
		fonte = _fonte;
		alvo = _alvo;
	}
}
