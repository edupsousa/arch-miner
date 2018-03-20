package heuristics.java;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.RoleVisitor;
import heuristics.UnrecognizedHeuristicKey;

public class PackageHeuristics implements ConfigurableHeuristics {
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
	
	public PackageHeuristics mapPackageNameRegex(String role, String ... regexps) {
		if (this.nameRegex == null)
			this.nameRegex = new HashMap<>();
		for (String regex : regexps) {
			this.nameRegex.put(regex, role);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String role, String... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("regex")) {
			return this.mapPackageNameRegex(role, parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}

}
