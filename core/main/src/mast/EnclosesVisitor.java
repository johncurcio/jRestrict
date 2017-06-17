package mast;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class EnclosesVisitor implements Visitor<Void, Void> {

	final List<String> errors;
	
	String filename = "";
	CompilationUnit compilationUnit;

	public EnclosesVisitor(List<String> errors) {
		this.errors = errors;
	}
	
	@Override
	public Void visit(CommandFiles fi, Void ctx) {
		try {
			this.filename = fi.filename;
			compilationUnit = JavaParser.parse(new String(Files.readAllBytes(Paths.get(fi.filename))));
		} catch (IOException e) {
			throw new RuntimeException(fi.filename + " file not found or not compatible.");
		}
		return null;
	}

	@Override
	public Void visit(CommandRequires cmd, Void ctx) {
		return null;
	}

	@Override
	public Void visit(CommandProhibits cmd, Void ctx) {
		return null;
	}

	@Override
	public Void visit(CommandEncloses cmd, Void ctx) {
		cmd.clause.visit(this, ctx);
		return null;
	}

	@Override
	public Void visit(JavaArgs arg, Void ctx) {
		return null;
	}

	@Override
	public Void visit(Clause clause, Void ctx) {
		return null;
	}

	@Override
	public Void visit(ClauseRetType clause, Void ctx) {
		List<peg.Pair<String, String>> decltypes = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(MethodDeclaration n, Object xxx) {
            	String pos = n.getBegin().get().toString();
            	decltypes.add(new peg.Pair<String, String>(pos  + ": " + n, n.getType().toString()));
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (peg.Pair<String, String> decl: decltypes){
            if (!clause.args.toString().contains(decl.y)){
            	errors.add("[jRestrict] " + this.filename + " contains a non specified " + clause.type + " (" + decl.y + ") at " + decl.x);
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseType clause, Void ctx) {
		List<peg.Pair<String, String>> decltypes = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(MethodDeclaration n, Object xxx) {
            	String pos = n.getBegin().get().toString();
            	decltypes.add(new peg.Pair<String, String>(pos  + ": " + n, n.getType().toString()));
            	for (Parameter parameters: n.getParameters()){
                	String param = parameters.getType().toString();
                	pos = n.getBegin().get().toString();
                	decltypes.add(new peg.Pair<String, String>(pos  + ": " + n, param));
                }
                super.visit(n, null);
            }
            @Override
            public void visit(VariableDeclarator n, Object xxx) {
            	String location = n.getParentNode() ==  null ? n.toString() : n.getParentNode().get().toString();
            	String pos = n.getBegin().get().toString();
            	decltypes.add(new peg.Pair<String, String>(pos  + ": " + location, n.getType().toString()));
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (peg.Pair<String, String> decl: decltypes){
            if (!clause.args.toString().contains(decl.y)){
            	errors.add("[jRestrict] " + this.filename + " contains a non specified " + clause.type + " (" + decl.y + ") at " + decl.x);
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseVarType clause, Void ctx) {
		List<peg.Pair<String, String>> decltypes = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(VariableDeclarator n, Object xxx) {
            	String location = n.getParentNode() ==  null ? n.toString() : n.getParentNode().get().toString();
            	String pos = n.getBegin().get().toString();
            	decltypes.add(new peg.Pair<String, String>(pos  + ": " + location, n.getType().toString()));
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (peg.Pair<String, String> decl: decltypes){
            if (!clause.args.toString().contains(decl.y)){
            	errors.add("[jRestrict] " + this.filename + " contains a non specified " + clause.type + " (" + decl.y + ") at " + decl.x);
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseLoop clause, Void ctx) {
		List<peg.Pair<String, String>> declloops = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(ContinueStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	String pos = n.getBegin().get().toString();
                	declloops.add(new peg.Pair<String, String>(pos  + ": " + n, "continue"));
                }
                super.visit(n, null);
            }
            @Override
            public void visit(BreakStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	String pos = n.getBegin().get().toString();
                	declloops.add(new peg.Pair<String, String>(pos  + ": " + n, "break"));
                }
                super.visit(n, null);
            }
            @Override
            public void visit(WhileStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	String pos = n.getBegin().get().toString();
                	declloops.add(new peg.Pair<String, String>(pos  + ": " + n, "while"));
                }
                super.visit(n, null);
            }
            @Override
            public void visit(ForStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	String pos = n.getBegin().get().toString();
                	declloops.add(new peg.Pair<String, String>(pos  + ": " + n, "for"));
                }
                super.visit(n, null);
            }
            @Override
            public void visit(DoStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	String pos = n.getBegin().get().toString();
                	declloops.add(new peg.Pair<String, String>(pos  + ": " + n, "do"));
                }
                super.visit(n, null);
            }
            @Override
            public void visit(ForeachStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	String pos = n.getBegin().get().toString();
                	declloops.add(new peg.Pair<String, String>(pos  + ": " + n, "foreach"));
                }
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (peg.Pair<String, String> decl: declloops){
            if (!clause.args.toString().contains(decl.y)){
            	errors.add("[jRestrict] " + this.filename + " contains a non specified " + clause.type + " (" + decl.y + ") at " + decl.x);
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseBranch clause, Void ctx) {
		List<peg.Pair<String, String>> declbranches = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(IfStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	String pos = n.getBegin().get().toString();
                	declbranches.add(new peg.Pair<String, String>(pos  + ": " + n, "if"));
                }
                super.visit(n, null);
            }
            @Override
            public void visit(SwitchStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	String pos = n.getBegin().get().toString();
                	declbranches.add(new peg.Pair<String, String>(pos  + ": " + n, "switch"));
                }
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (peg.Pair<String, String> decl: declbranches){
            if (!clause.args.toString().contains(decl.y)){
            	errors.add("[jRestrict] " + this.filename + " contains a non specified " + clause.type + " (" + decl.y + ") at " + decl.x);
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseImport clause, Void ctx) {
		List<peg.Pair<String, String>> declimports = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(ImportDeclaration n, Object xxx) {
                String pos = n.getBegin().get().toString();
            	declimports.add(new peg.Pair<String, String>(pos  + ": " + n, n.getNameAsString()));
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		
		boolean accept;
		for (peg.Pair<String, String> decl: declimports){
			accept = false;
			for (JavaArgs ar: clause.args){
				if (decl.y.contains(ar.arg)){
					accept = true;
				}
				if (ar.arg.equals(decl)){
					accept = true;
				}
			}
			if (!accept){
				errors.add("[jRestrict] " + this.filename + " contains a non specified " + clause.type + " (" + decl.y + ") at " + decl.x);
			}
		}
		return null;
	}

	@Override
	public Void visit(ClauseModifier clause, Void ctx) {
		List<peg.Pair<String, String>> declmodifiers = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(ClassOrInterfaceDeclaration n, Object xxx) {
            	for(Object m: n.getModifiers().toArray()){
            		String modifier = m.toString().toLowerCase();
            		String pos = n.getBegin().get().toString();
            		declmodifiers.add(new peg.Pair<String, String>(pos  + ": " + n, modifier));
            	}
                super.visit(n, null);
            }
            @Override
            public void visit(MethodDeclaration n, Object xxx) {
            	for(Object m: n.getModifiers().toArray()){
            		String modifier = m.toString().toLowerCase();
            		String pos = n.getBegin().get().toString();
                	declmodifiers.add(new peg.Pair<String, String>(pos  + ": " + n, modifier));
            	}
                super.visit(n, null);
            }
            @Override
            public void visit(FieldDeclaration n, Object xxx) {
            	for(Object m: n.getModifiers().toArray()){
            		String modifier = m.toString().toLowerCase();
            		String pos = n.getBegin().get().toString();
                	declmodifiers.add(new peg.Pair<String, String>(pos  + ": " + n, modifier));
            	}
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (peg.Pair<String, String> decl: declmodifiers){
            if (!clause.args.toString().contains(decl.y)){
            	errors.add("[jRestrict] " + this.filename + " contains a non specified " + clause.type + " (" + decl.y + ") at " + decl.x);
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseOperator clause, Void ctx) {
		List<peg.Pair<String, String>> decloperators = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(AssignExpr n, Object xxx) {
            	String pos = n.getBegin().get().toString();
            	decloperators.add(new peg.Pair<String, String>(pos  + ": " + n, n.getOperator().asString()));
                super.visit(n, null);
            }
            @Override
            public void visit(UnaryExpr n, Object xxx) {
            	String pos = n.getBegin().get().toString();
            	decloperators.add(new peg.Pair<String, String>(pos  + ": " + n, n.getOperator().asString()));;
                super.visit(n, null);
            }
            @Override
            public void visit(BinaryExpr n, Object xxx) {
            	String pos = n.getBegin().get().toString();
            	decloperators.add(new peg.Pair<String, String>(pos  + ": " + n, n.getOperator().asString()));;
                super.visit(n, null);
            }
            @Override
            public void visit(FieldDeclaration n, Object xxx) { 
            	if (n.getVariables().get(0).getInitializer().isPresent()){
            		Expression value = n.getVariables().get(0).getInitializer().get();
            		Expression target = new NameExpr(n.getVariables().get(0).getName());
            		AssignExpr a = new AssignExpr(target, value, Operator.ASSIGN);
                	super.visit(a, null);
            	}
            }
		}.visit(compilationUnit, null);
		for (peg.Pair<String, String> decl: decloperators){
            if (!clause.args.toString().contains(decl.y)){
            	errors.add("[jRestrict] " + this.filename + " contains a non specified " + clause.type + " (" + decl.y + ") at " + decl.x);
            }
		}
		return null;
	}

	@Override
	public Void visit(Script script, Void ctx) {
		for(CommandFiles fi: script.files) {
			fi.visit(this, ctx);
			for(CommandEncloses en: script.enclosement) {
				en.visit(this, ctx);
			}
		}		
		return null;
	}

}
