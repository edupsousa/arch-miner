package heuristics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassAnnotationHeuristics implements RoleHeuristics {
	private Map<String, String> annotatedWith;
	
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
					if (eModifier.isAnnotation() && annotatedWith.containsKey(eModifier.toString())) {
						this.setRole(annotatedWith.get(eModifier.toString()));
						return true;
					}
				}
				return false;
			}
		};
		root.accept(visitor);
		return visitor.getRole();
	}
	
	public ClassAnnotationHeuristics mapAnnotatedWith(String role, String annotation) {
		if (this.annotatedWith == null)
			this.annotatedWith = new HashMap<>();
		this.annotatedWith.put(annotation, role);
		return this;
	}
}
