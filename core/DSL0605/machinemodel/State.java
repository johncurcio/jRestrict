package machinemodel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
	public final String nome;
	public List<Command> comandos = new ArrayList<>();
	public Map<String, Transition> transicoes = new HashMap<>();

	public State(String _nome) {
		nome = _nome;
	}
	
	public void addAction(Command comando) {
		comandos.add(comando);
	}
	
	public void addTransition(Event evt, State alvo) {
		transicoes.put(evt.codigo, new Transition(this, evt, alvo));
	}

	public void executeActions() {
		for(Command c : comandos) {
			System.out.println(c.codigo);
		}
	}
}
