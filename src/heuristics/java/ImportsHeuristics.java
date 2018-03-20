package heuristics.java;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.RoleVisitor;
import heuristics.UnrecognizedHeuristicKey;

public class ImportsHeuristics implements ConfigurableHeuristics {
	private Map<String, String> startsWith;
	
	@Override
	public String getName() {
		return "imports";
	}

	@Override
	public String getRole(AnalysedFile file) {
		CompilationUnit root = file.getASTRoot();
		if (root == null)
			return "unknown";
		
		RoleVisitor visitor = new RoleVisitor() {
			
			@Override
			public boolean visit(ImportDeclaration node) {
				String name = node.getName().toString();
				if (setRoleByImport(name))
					return false;
				
				return false;
			}
			
			boolean setRoleByImport(String name) {
				for (Map.Entry<String, String> entry : startsWith.entrySet()) {
					if (name.startsWith(entry.getKey())) {
						this.setRole(entry.getValue());
						return true;
					}
				}
				return false;
			}
		};
		root.accept(visitor);
		return visitor.getRole();
	}
	
	public ImportsHeuristics mapImportStartsWith(String role, String ... importStarts) {
		if (this.startsWith == null)
			this.startsWith = new HashMap<>();
		for (String importStart : importStarts) {
			this.startsWith.put(importStart, role);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String role, String... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("startsWith")) {
			return this.mapImportStartsWith(role, parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}

}
