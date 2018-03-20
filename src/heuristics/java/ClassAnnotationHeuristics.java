package heuristics.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.RoleVisitor;
import heuristics.UnrecognizedHeuristicKey;

public class ClassAnnotationHeuristics implements ConfigurableHeuristics {
	private Map<String, String> annotation;
	
	@Override
	public String getName() {
		return "class-annotation";
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
				
				List<?> modifiers = node.modifiers();
				if (setRoleByAnnotation(modifiers))
					return false;
				
				return false;
			}
			
			boolean setRoleByAnnotation(List<?> modifiers) {
				for (Object modifier : modifiers) {
					IExtendedModifier eModifier = (IExtendedModifier) modifier;
					if (eModifier.isAnnotation() && annotation.containsKey(eModifier.toString())) {
						this.setRole(annotation.get(eModifier.toString()));
						return true;
					}
				}
				return false;
			}
		};
		root.accept(visitor);
		return visitor.getRole();
	}
	
	public ClassAnnotationHeuristics mapAnnotation(String role, String annotation) {
		if (this.annotation == null)
			this.annotation = new HashMap<>();
		this.annotation.put(annotation, role);
		return this;
	}
	
	public ClassAnnotationHeuristics mapAnnotations(String role, String ... annotations) {
		for (String annotation : annotations) {
			this.mapAnnotation(role, annotation);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String role, String... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("annotation")) {
			return this.mapAnnotations(role, parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}
}
