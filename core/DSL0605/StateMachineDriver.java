import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class StateMachineDriver {
	public static machinemodel.StateMachine read(String fname) throws IOException {
		peg.Pair<mast.Machine, List<String>> res = machine(fname);
		if(res.y.isEmpty()) {
			mast.ModelVisitor mv = new mast.ModelVisitor();
			res.x.visit(mv, null);
			return mv.machine;
		} else {
				for(String err: res.y) System.err.println(err);
			return null;
		}
	}
	
	public static peg.Pair<mast.Machine, List<String>> machine(String fname) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fname));
		StringBuffer buf = new StringBuffer();
		String line = br.readLine();
		while(line != null) {
			buf.append(line + "\n");
			line = br.readLine();
		}
		br.close();
		peg.Pair<mast.Machine, java.util.List<String>> res = peg.peg.run(StateMachineParser.machine, buf.toString()); 
		mast.CollectVisitor cv = new mast.CollectVisitor(res.y);
		mast.BindingVisitor bv = new mast.BindingVisitor(res.y);
		res.x.visit(cv, null);
		res.x.visit(bv, null);
		return res;
	}
	
	public static void main(String[] args) throws IOException {
		String fname = args[0];
		machinemodel.StateMachine m = read(fname);
		if(m != null) {
			machinemodel.Controller controller = new machinemodel.Controller(m);
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String cmd = in.readLine();
			while(cmd != null) {
				controller.handle(cmd);
				cmd = in.readLine();
			}
		}
	}
}
