package mast;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ProhibitsVisitor implements Visitor<Void, Void> {
	final List<String> errors;
	
	String argument = "";
	String filename = "";
	CompilationUnit compilationUnit;

	public ProhibitsVisitor(List<String> errors) {
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
	public Void visit(JavaArgs arg, Void ctx) {
		return null;
	}

	@Override
	public Void visit(Clause cl, Void ctx) {
		return null;
	}
	
	@Override
	public Void visit(Script script, Void ctx) {
		for(CommandFiles fi: script.files) {
			fi.visit(this, ctx);
			for(CommandProhibits pb: script.prohibitions) {
				pb.visit(this, ctx);
			}
		}
		return null;
	}	

	@Override
	public Void visit(CommandRequires cmd, Void ctx) {
		return null;
	}
	
	@Override
	public Void visit(CommandProhibits cmd, Void ctx) {
		for (JavaArgs arg: cmd.clause.args){
			final String currArg = arg.visit(new StringVisitor(), null);
			this.argument = currArg;
			cmd.clause.visit(this, ctx);
		}
		return null;
	}

	@Override
	public Void visit(CommandEncloses cmd, Void ctx) {
		return null;
	}

	@Override
	public Void visit(ClauseRetType clause, Void ctx) {
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(MethodDeclaration n, Object xxx) {
                if (n.getType().toString().equals(argument)){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		return null;
	}

	@Override
	public Void visit(ClauseType clause, Void ctx) {
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(VariableDeclarator n, Object xxx) {
                if (n.getType().toString().equals(argument)){
                	String pos = n.getBegin().get().toString();
                	String location = n.getParentNode() ==  null ? n.toString() : n.getParentNode().get().toString(); 
                	errors.add("[jRestrict] " + filename + " contains prohibited variable " + clause.type + " (" + argument + ") at " + pos  + ": " + location);
                }
                super.visit(n, null);
            }
            @Override
            public void visit(MethodDeclaration n, Object xxx) {
                if (n.getType().toString().equals(argument)){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited method " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                for (Parameter parameters: n.getParameters()){
                	String param = parameters.getType().toString();
                	if (param.equals(argument)){
                		String pos = n.getBegin().get().toString();
                		errors.add("[jRestrict] " + filename + " contains prohibited parameters " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                	}
                }
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		return null;
	}

	/**
	 * @TODO: currently types such as List<Object> or char[] are not recognized nor accepted by jRestrict
	 */
	@Override
	public Void visit(ClauseVarType clause, Void ctx) {
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(VariableDeclarator n, Object xxx) {
                if (n.getType().toString().equals(argument)){
                	String pos = n.getBegin().get().toString();
                	String location = n.getParentNode() ==  null ? n.toString() : n.getParentNode().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + location);
                }
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		return null;
	}

	@Override
	public Void visit(ClauseLoop clause, Void ctx) {
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(ContinueStmt n, Object xxx) {
                if (!n.toString().equals("") && argument.equals("continue")){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
            @Override
            public void visit(BreakStmt n, Object xxx) {
                if (!n.toString().equals("") && argument.equals("break")){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
            @Override
            public void visit(WhileStmt n, Object xxx) {
                if (!n.toString().equals("") && argument.equals("while")){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
            @Override
            public void visit(ForStmt n, Object xxx) {
                if (!n.toString().equals("") && argument.equals("for")){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
            @Override
            public void visit(DoStmt n, Object xxx) {
                if (!n.toString().equals("") && argument.equals("do")){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
            @Override
            public void visit(ForeachStmt n, Object xxx) {
                if (!n.toString().equals("") && argument.equals("foreach")){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		return null;
	}

	@Override
	public Void visit(ClauseBranch clause, Void ctx) {
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(IfStmt n, Object xxx) {
                if (!n.toString().equals("") && argument.equals("if")){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
            @Override
            public void visit(SwitchStmt n, Object xxx) {
                if (!n.toString().equals("") && argument.equals("switch")){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		return null;
	}

	@Override
	public Void visit(ClauseImport clause, Void ctx) {
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(ImportDeclaration n, Object xxx) {
                if (argument.contains(n.getNameAsString()) || n.getNameAsString().contains(argument)){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		return null;
	}

	@Override
	public Void visit(ClauseModifier clause, Void ctx) {
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(ClassOrInterfaceDeclaration n, Object xxx) {
            	for(Object m: n.getModifiers().toArray()){
            		String modifier = m.toString().toLowerCase();
	                if (modifier.equals(argument)){
	                	String pos = n.getBegin().get().toString();
	                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
	                }
            	}
                super.visit(n, null);
            }
            @Override
            public void visit(MethodDeclaration n, Object xxx) {
            	for(Object m: n.getModifiers().toArray()){
            		String modifier = m.toString().toLowerCase();
	                if (modifier.equals(argument)){
	                	String pos = n.getBegin().get().toString();
	                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
	                }
            	}
                super.visit(n, null);
            }
            @Override
            public void visit(FieldDeclaration n, Object xxx) {
            	for(Object m: n.getModifiers().toArray()){
            		String modifier = m.toString().toLowerCase();
	                if (modifier.equals(argument)){
	                	String pos = n.getBegin().get().toString();
	                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
	                }
            	}
                super.visit(n, null);
            }
		}.visit(compilationUnit, null);
		return null;
	}

	@Override
	public Void visit(ClauseOperator clause, Void ctx) {
		new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(AssignExpr n, Object xxx) {
                if (n.getOperator().asString().equals(argument)){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
            @Override
            public void visit(UnaryExpr n, Object xxx) {
                if (n.getOperator().asString().equals(argument)){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
                super.visit(n, null);
            }
            @Override
            public void visit(BinaryExpr n, Object xxx) {
                if (n.getOperator().asString().equals(argument)){
                	String pos = n.getBegin().get().toString();
                	errors.add("[jRestrict] " + filename + " contains prohibited " + clause.type + " (" + argument + ") at " + pos  + ": " + n);
                }
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
		return null;
	}
}
