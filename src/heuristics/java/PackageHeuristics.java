package heuristics.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.RoleVisitor;
import heuristics.UnrecognizedHeuristicKey;

public class PackageHeuristics implements ConfigurableHeuristics {
	private List<String> nameRegex = new ArrayList<>();
	
	@Override
	public String getName() {
		return "package";
	}

	@Override
	public Boolean getRole(AnalysedFile file) {
		CompilationUnit root = file.getASTRoot();
		if (root == null)
			return false;
		
		RoleVisitor visitor = new RoleVisitor() {
			
			@Override
			public boolean visit(PackageDeclaration node) {
				String name = node.getName().toString();
				if (setRoleByPackageName(name))
					return false;
				
				return false;
			}
			
			boolean setRoleByPackageName(String name) {
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
	
	public PackageHeuristics mapPackageNameRegex(String ... regexps) {
		for (String regex : regexps) {
			this.nameRegex.add(regex);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("regex")) {
			return this.mapPackageNameRegex(parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}

}
