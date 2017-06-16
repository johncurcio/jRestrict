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

public class RequiresVisitor implements Visitor<Void, Void> {
	
	final List<String> errors;
	
	String filename = "";
	CompilationUnit compilationUnit;

	public RequiresVisitor(List<String> errors) {
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
		cmd.clause.visit(this, ctx);
		return null;
	}

	@Override
	public Void visit(CommandProhibits cmd, Void ctx) {
		return null;
	}

	@Override
	public Void visit(CommandEncloses cmd, Void ctx) {
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
		List<String> decltypes = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(MethodDeclaration n, Object xxx) {
            	decltypes.add(n.getType().toString());
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (JavaArgs arg: clause.args){
            if (!decltypes.contains(arg.arg)){
            	errors.add("[jRestrict] " + filename + " does not contain required " + clause.type + " (" + arg.arg + ")");
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseType clause, Void ctx) {
		List<String> decltypes = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(MethodDeclaration n, Object xxx) {
            	decltypes.add(n.getType().toString());
            	for (Parameter parameters: n.getParameters()){
                	String param = parameters.getType().toString();
                	decltypes.add(param);
                }
                super.visit(n, null);
            }
            @Override
            public void visit(VariableDeclarator n, Object xxx) {
            	decltypes.add(n.getType().toString());
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (JavaArgs arg: clause.args){
            if (!decltypes.contains(arg.arg)){
            	errors.add("[jRestrict] " + filename + " does not contain required " + clause.type + " (" + arg.arg + ")");
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseVarType clause, Void ctx) {
		List<String> decltypes = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(VariableDeclarator n, Object xxx) {
            	decltypes.add(n.getType().toString());
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (JavaArgs arg: clause.args){
            if (!decltypes.contains(arg.arg)){
            	errors.add("[jRestrict] " + filename + " does not contain required " + clause.type + " (" + arg.arg + ")");
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseLoop clause, Void ctx) {
		List<String> declloops = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(ContinueStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	declloops.add("continue");
                }
                super.visit(n, null);
            }
            @Override
            public void visit(BreakStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	declloops.add("break");
                }
                super.visit(n, null);
            }
            @Override
            public void visit(WhileStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	declloops.add("while");
                }
                super.visit(n, null);
            }
            @Override
            public void visit(ForStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	declloops.add("for");
                }
                super.visit(n, null);
            }
            @Override
            public void visit(DoStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	declloops.add("do");
                }
                super.visit(n, null);
            }
            @Override
            public void visit(ForeachStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	declloops.add("foreach");
                }
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (JavaArgs arg: clause.args){
            if (!declloops.contains(arg.arg)){
            	errors.add("[jRestrict] " + filename + " does not contain required " + clause.type + " (" + arg.arg + ")");
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseBranch clause, Void ctx) {
		List<String> declbranches = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(IfStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	declbranches.add("if");
                }
                super.visit(n, null);
            }
            @Override
            public void visit(SwitchStmt n, Object xxx) {
                if (!n.toString().equals("")){
                	declbranches.add("switch");
                }
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (JavaArgs arg: clause.args){
            if (!declbranches.contains(arg.arg)){
            	errors.add("[jRestrict] " + filename + " does not contain required " + clause.type + " (" + arg.arg + ")");
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseImport clause, Void ctx) {
		List<String> declimports = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(ImportDeclaration n, Object xxx) {
                declimports.add(n.getNameAsString());                
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);

		boolean accept;
		for (String decl: declimports){
			accept = false;
			for (JavaArgs ar: clause.args){
				if (decl.contains(ar.arg)){
					accept = true;
				}
				if (ar.arg.equals(decl)){
					accept = true;
				}
			}
			if (!accept){
				errors.add("[jRestrict] " + this.filename + " contains a non specified " + clause.type + " (" + decl + ")");
			}
		}
		return null;
	}

	@Override
	public Void visit(ClauseModifier clause, Void ctx) {
		List<String> declmodifiers = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(ClassOrInterfaceDeclaration n, Object xxx) {
            	for(Object m: n.getModifiers().toArray()){
            		String modifier = m.toString().toLowerCase();
            		declmodifiers.add(modifier);
            	}
                super.visit(n, null);
            }
            @Override
            public void visit(MethodDeclaration n, Object xxx) {
            	for(Object m: n.getModifiers().toArray()){
            		String modifier = m.toString().toLowerCase();
            		declmodifiers.add(modifier);
            	}
                super.visit(n, null);
            }
            @Override
            public void visit(FieldDeclaration n, Object xxx) {
            	for(Object m: n.getModifiers().toArray()){
            		String modifier = m.toString().toLowerCase();
            		declmodifiers.add(modifier);
            	}
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		for (JavaArgs arg: clause.args){
            if (!declmodifiers.contains(arg.arg)){
            	errors.add("[jRestrict] " + filename + " does not contain required " + clause.type + " (" + arg.arg + ")");
            }
		}
		return null;
	}

	@Override
	public Void visit(ClauseOperator clause, Void ctx) {
		List<String> decloperators = new ArrayList<>();
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(AssignExpr n, Object xxx) {
            	decloperators.add(n.getOperator().asString());
                super.visit(n, null);
            }
            @Override
            public void visit(UnaryExpr n, Object xxx) {
            	decloperators.add(n.getOperator().asString());
                super.visit(n, null);
            }
            @Override
            public void visit(BinaryExpr n, Object xxx) {
            	decloperators.add(n.getOperator().asString());
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
		for (JavaArgs arg: clause.args){
            if (!decloperators.contains(arg.arg)){
            	errors.add("[jRestrict] " + filename + " does not contain required " + clause.type + " (" + arg.arg + ")");
            }
		}
		return null;
	}

	@Override
	public Void visit(Script script, Void ctx) {
		for(CommandFiles fi: script.files) {
			fi.visit(this, ctx);
			for(CommandRequires en: script.requirements) {
				en.visit(this, ctx);
			}
		}
		return null;
	}

}
