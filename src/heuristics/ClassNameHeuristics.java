package heuristics;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassNameHeuristics implements RoleHeuristics {
	private Map<String, String> nameRegex;
	
	@Override
	public String getName() {
		return "class-name";
	}

	@Override
	public String getRole(AnalysedFile file) {
		CompilationUnit root = file.getASTRoot();
		if (root == null)
			return "unknown";
		
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
				for (Map.Entry<String, String> entry : nameRegex.entrySet()) {
					if (name.matches(entry.getKey())) {
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
	
	public ClassNameHeuristics mapClassNameRegex(String role, String regex) {
		if (this.nameRegex == null)
			this.nameRegex = new HashMap<>();
		this.nameRegex.put(regex, role);
		return this;
	}

}
