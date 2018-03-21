package heuristics.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.RoleVisitor;
import heuristics.UnrecognizedHeuristicKey;

public class ImportsHeuristics implements ConfigurableHeuristics {
	private List<String> startsWith = new ArrayList<>();
	
	@Override
	public String getName() {
		return "imports";
	}

	@Override
	public Boolean getRole(AnalysedFile file) {
		CompilationUnit root = file.getASTRoot();
		if (root == null)
			return false;
		
		RoleVisitor visitor = new RoleVisitor() {
			
			@Override
			public boolean visit(ImportDeclaration node) {
				String name = node.getName().toString();
				if (setRoleByImport(name))
					return false;
				
				return false;
			}
			
			boolean setRoleByImport(String name) {
				for (String entry : startsWith) {
					if (name.startsWith(entry)) {
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
	
	public ImportsHeuristics mapImportStartsWith(String ... importStarts) {
		for (String importStart : importStarts) {
			this.startsWith.add(importStart);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("startsWith")) {
			return this.mapImportStartsWith(parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}

}
