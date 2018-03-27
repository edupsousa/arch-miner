package heuristics.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.RoleVisitor;
import heuristics.UnrecognizedHeuristicKey;

public class TypeAnnotationHeuristics implements ConfigurableHeuristics {
	private List<String> annotationNames = new ArrayList<>();
	
	@Override
	public String getName() {
		return "type-annotation";
	}

	@Override
	public Boolean getRole(AnalysedFile file) {
		CompilationUnit root = file.getASTRoot();
		if (root == null)
			return false;
		
		RoleVisitor visitor = new RoleVisitor() {
			@Override
			public boolean visit(TypeDeclaration node) {
				List<?> modifiers = node.modifiers();
				if (setRoleByAnnotation(modifiers))
					return false;
				
				return false;
			}
			
			boolean setRoleByAnnotation(List<?> modifiers) {
				for (Object modifier : modifiers) {
					if (((IExtendedModifier) modifier).isAnnotation()) {
						Annotation ann = (Annotation)modifier;
						if (annotationNames.contains(ann.getTypeName().toString())) {
							this.setMatches(true);
							return true;
						}
					}
				}
				return false;
			}
		};
		root.accept(visitor);
		return visitor.getMatches();
	}
	
	public TypeAnnotationHeuristics mapAnnotation(String annotation) {
		this.annotationNames.add(annotation);
		return this;
	}
	
	public TypeAnnotationHeuristics mapAnnotations(String ... annotations) {
		for (String annotation : annotations) {
			this.mapAnnotation(annotation);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("names")) {
			return this.mapAnnotations(parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}
}
