package heuristics.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.RoleVisitor;
import heuristics.UnrecognizedHeuristicKey;

public class ClassStructureHeuristics implements ConfigurableHeuristics {
	
	private boolean matchBeans = false;

	@Override
	public String getName() {
		return "class-structure";
	}

	@Override
	public Boolean getRole(AnalysedFile file) {
		CompilationUnit root = file.getASTRoot();
		if (root == null)
			return false;
		
		RoleVisitor visitor = new RoleVisitor() {
			private int gettersCount = 0;
			private int settersCount = 0;
			private int constructorsCount = 0;
			private int fieldsCount = 0;
			private int otherMethodsCount = 0;
			
			@Override
			public boolean visit(TypeDeclaration node) {
				if (node.isInterface())
					return false;
				return true;
			}
			
			@Override
			public boolean visit(MethodDeclaration method) {
				if (method.isConstructor()) {
					constructorsCount++;
				} else if (method.getName().getIdentifier().length() > 3) {
					if (method.getName().getIdentifier().substring(0, 3).equals("get")) {
						gettersCount++;
					} else if (method.getName().getIdentifier().substring(0, 3).equals("set")) {
						settersCount++;
					} 
				} else {
					otherMethodsCount++;
				}
				return super.visit(method);
			}
			
			@Override
			public boolean visit(FieldDeclaration field) {
				fieldsCount += field.fragments().size();
				return super.visit(field);
			}
			
			@Override
			public Boolean getMatches() {
				if (matchBeans) {
					if (constructorsCount >= 0 && gettersCount > 0 && settersCount > 0 && fieldsCount > 0 && otherMethodsCount == 0) {
						return true;
					}
				}
				return false;
			}
		};
		root.accept(visitor);
		return visitor.getMatches();
	}
	
	public ClassStructureHeuristics mapStructureType(String ... types) {
		for (String type : types) {
			switch (type) {
			case "bean":
				this.matchBeans = true;
				break;
			}
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String... parameters) throws UnrecognizedHeuristicKey {
		if (key.equals("types")) {
			return this.mapStructureType(parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}

}
