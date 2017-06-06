package machinemodel;

public class Controller {
	public State atual;
	public final StateMachine maquina;
	
	public Controller(StateMachine _maquina) {
		maquina = _maquina;
		atual = maquina.start;
		System.out.println("state: " + atual.nome);
	}
	
	public void handle(String codigo) {
		if(atual.transicoes.containsKey(codigo)) {
			Transition t = atual.transicoes.get(codigo);
			atual = t.alvo;
			System.out.println("state: " + atual.nome);
			atual.executeActions();
		}
		if(maquina.resetEvts.contains(codigo)) {
			atual = maquina.start;
			System.out.println("state: " + atual.nome);
			atual.executeActions();
		}
	}
}
