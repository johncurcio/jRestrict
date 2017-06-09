import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ScriptDriver {

	//@TODO: do i need to use modelvisitor here?
	public static peg.Pair<mast.Script, List<String>> read(String fname) throws IOException {
		return openScript(fname);
	}
	
	public static peg.Pair<mast.Script, List<String>> openScript(String fname) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fname));
		StringBuffer buf = new StringBuffer();
		String line = br.readLine();
		while(line != null) {
			buf.append(line + "\n");
			line = br.readLine();
		}
		br.close();
		peg.Pair<mast.Script, java.util.List<String>> res = peg.peg.run(ScriptParser.script, buf.toString()); 
		mast.CollectVisitor cv = new mast.CollectVisitor(res.y);
		mast.BindingVisitor bv = new mast.BindingVisitor(res.y);
		res.x.visit(cv, null);
		res.x.visit(bv, null);
		return res;
	}
	
	public static void main(String[] args) throws IOException {
		String fname = args[0];
		peg.Pair<mast.Script, List<String>> m = read(fname);
		System.out.println(">>>>>>>>>> DEBUG <<<<<<<<<<<");
		System.out.println("> Files: " + m.x.files.toString());
		System.out.println("> Requires: " + m.x.requirements.toString());
		System.out.println("> Encloses: " + m.x.enclosement.toString());
		System.out.println("> Prohibits: " + m.x.prohibitions.toString());
	}
}