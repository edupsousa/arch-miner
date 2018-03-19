package heuristics.java;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;

import heuristics.AnalysedFile;
import heuristics.RoleHeuristics;
import heuristics.RoleVisitor;

public class ImportsHeuristics implements RoleHeuristics {
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
	
	public ImportsHeuristics mapImportStartsWith(String role, String importStart) {
		if (this.startsWith == null)
			this.startsWith = new HashMap<>();
		this.startsWith.put(importStart, role);
		return this;
	}

}
