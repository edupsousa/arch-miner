package heuristics.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.RoleVisitor;
import heuristics.UnrecognizedHeuristicKey;

public class ClassAnnotationHeuristics implements ConfigurableHeuristics {
	private List<String> annotation = new ArrayList<>();
	
	@Override
	public String getName() {
		return "class-annotation";
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
				
				List<?> modifiers = node.modifiers();
				if (setRoleByAnnotation(modifiers))
					return false;
				
				return false;
			}
			
			boolean setRoleByAnnotation(List<?> modifiers) {
				for (Object modifier : modifiers) {
					IExtendedModifier eModifier = (IExtendedModifier) modifier;
					if (eModifier.isAnnotation() && annotation.contains(eModifier.toString())) {
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
	
	public ClassAnnotationHeuristics mapAnnotation(String annotation) {
		this.annotation.add(annotation);
		return this;
	}
	
	public ClassAnnotationHeuristics mapAnnotations(String ... annotations) {
		for (String annotation : annotations) {
			this.mapAnnotation(annotation);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("annotation")) {
			return this.mapAnnotations(parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}
}
