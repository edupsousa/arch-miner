package heuristics.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.RoleVisitor;
import heuristics.UnrecognizedHeuristicKey;

public class ClassNameHeuristics implements ConfigurableHeuristics {
	private List<String> nameRegex = new ArrayList<>();
	
	@Override
	public String getName() {
		return "class-name";
	}

	@Override
	public Boolean getRole(AnalysedFile file) {
		CompilationUnit root = file.getASTRoot();
		if (root == null)
			return false;
		
		RoleVisitor visitor = new RoleVisitor() {
			@Override
			public boolean visit(TypeDeclaration node) {
				if (node.isInterface())
					return false;
				
				String name = node.getName().toString();
				if (setRoleByClassName(name))
					return false;
				
				return false;
			}
			
			boolean setRoleByClassName(String name) {
				for (String entry : nameRegex) {
					if (name.matches(entry)) {
						this.setMatches(true);
						return true;
					}
				}
				return false;
			}
		};
		root.accept(visitor);
		return visitor.getMatches();
	}
	
	public ClassNameHeuristics mapClassNameRegex(String ... regexps) {
		for (String regex : regexps) {
			this.nameRegex.add(regex);			
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("regex")) {
			return this.mapClassNameRegex(parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}

}
