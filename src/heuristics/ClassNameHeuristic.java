package heuristics;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassNameHeuristic implements RoleHeuristics {

	private Map<String, String> nameSuffixes;
	
	private class Visitor extends ASTVisitor {
		
		public String role = "unknown";
		
		@Override
		public boolean visit(TypeDeclaration node) {
			if (node.isInterface())
				return false;
			String name = node.getName().toString();
			for (Map.Entry<String, String> entry : nameSuffixes.entrySet()) {
				if (name.endsWith(entry.getKey())) {
					this.role = entry.getValue();
					return false;
				}
			}
			return false;
		}
	}
	
	@Override
	public String getName() {
		return "class-name";
	}

	@Override
	public String getRole(AnalysedFile file) {
		CompilationUnit root = file.getASTRoot();
		if (root == null)
			return "unknown";
		
		Visitor visitor = new Visitor();
		root.accept(visitor);
		return visitor.role;
	}
	
	public ClassNameHeuristic mapClassNameSuffix(String role, String suffix) {
		if (nameSuffixes == null)
			this.nameSuffixes = new HashMap<>();
		this.nameSuffixes.put(suffix, role);
		return this;
	}

}
