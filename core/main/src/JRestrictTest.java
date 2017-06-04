import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author joao curcio
 * 
 * Incremental Grammar:
 * 
 * script    := (file mainc | mainc file) 
 * mainc     := (encloses? requires?) | (prohibits? requires?)
 * 
 * file      := "file" ":" [A-Z][a-zA-Z]+ ".java" ";"
 * requires  := "requires"  ":"   ";"
 * encloses  := "encloses"  ":"   ";"
 * prohibits := "prohibits" ":"   ";"
 * 
 * comentario  := '--'[^\n]*
 * 

public class JRestrictTest {
	
	public static final int NAME        = 1;
	public static final int FILE        = 3;
	public static final int REQUIRES    = 4;
	public static final int ENCLOSES    = 5;
	public static final int PROHIBITS   = 6;
	public static final int FILENAME    = 7;
	
	public static Parser script;
	public static Parser mainc;
	public static Parser file;
	public static Parser requires;
	public static Parser encloses;
	public static Parser prohibits;
	
	public static List<String> test(){
		script    = lazy(() -> choice(seq(file, mainc), seq(mainc, file)));
		mainc     = lazy(() -> choice(seq(opt(encloses), opt(requires)), seq(opt(prohibits), opt(requires))));
		file      = lazy(() -> seq(token(FILE), token(':'), token(FILENAME), token(';')));
		requires  = lazy(() -> seq(token(REQUIRES), token(':'), token(';'))); //missing an argument for now
		prohibits = lazy(() -> seq(token(PROHIBITS), token(':'), token(';')));
		encloses  = lazy(() -> seq(token(ENCLOSES), token(':'), token(';')));
		
		String jscript = ""  
		     + " file: Main.java;\n"
		     + " encloses: ;\n"
			 + " requires: ;\n"
			 +  "prohibts: ;\n";
			 
		
		List<Token> ent = new ArrayList<>();
		ScriptLexer sl = new ScriptLexer(jscript);
		Token tok;
		do {
			tok = sl.proximoToken();
			ent.add(tok);
		} while(tok.tipo != 0);
		System.out.println(script);
		System.out.println(ent);
		return run(sl, script, ent);
	}
}
 */