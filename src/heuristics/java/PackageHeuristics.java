package heuristics.java;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import heuristics.AnalysedFile;
import heuristics.RoleHeuristics;
import heuristics.RoleVisitor;

public class PackageHeuristics implements RoleHeuristics {
	private Map<String, String> nameRegex;
	
	@Override
	public String getName() {
		return "package";
	}

	@Override
	public String getRole(AnalysedFile file) {
		CompilationUnit root = file.getASTRoot();
		if (root == null)
			return "unknown";
		
		RoleVisitor visitor = new RoleVisitor() {
			
			@Override
			public boolean visit(PackageDeclaration node) {
				String name = node.getName().toString();
				if (setRoleByPackageName(name))
					return false;
				
				return false;
			}
			
			boolean setRoleByPackageName(String name) {
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
	
	public PackageHeuristics mapPackageNameRegex(String role, String regex) {
		if (this.nameRegex == null)
			this.nameRegex = new HashMap<>();
		this.nameRegex.put(regex, role);
		return this;
	}

}
