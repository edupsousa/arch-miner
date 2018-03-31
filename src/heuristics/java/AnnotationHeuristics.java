package heuristics.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.RoleVisitor;
import heuristics.UnrecognizedHeuristicKey;

public class AnnotationHeuristics implements ConfigurableHeuristics {
	
	private List<String> classAnnotations = new ArrayList<>();
	private List<String> interfaceAnnotations = new ArrayList<>();
	private List<String> fieldAnnotations = new ArrayList<>();

	@Override
	public String getName() {
		return "annotation";
	}

	@Override
	public Boolean getRole(AnalysedFile file) {
		CompilationUnit root = file.getASTRoot();
		if (root == null)
			return false;
		
		RoleVisitor visitor = new RoleVisitor() {
			private List<String> importedClassAnnotations = new ArrayList<>();
			private List<String> importedInterfaceAnnotations = new ArrayList<>();
			private List<String> importedFieldAnnotations = new ArrayList<>();
			
			@Override
			public boolean visit(ImportDeclaration node) {
				String importFQName = node.getName().getFullyQualifiedName();
				String importSimpleName = importFQName.substring(importFQName.lastIndexOf('.')+1);
				
				if (classAnnotations.contains(importFQName)) {
					importedClassAnnotations.add(importSimpleName);
				}
				if (interfaceAnnotations.contains(importFQName)) {
					importedInterfaceAnnotations.add(importSimpleName);
				}
				if (fieldAnnotations.contains(importFQName)) {
					importedFieldAnnotations.add(importSimpleName);
				}
				
				return true;
			}
			
			private void matchModifiers(List<?> modifiers, List<String> annotations) {
				for (Object modifier : modifiers) {
					if (((IExtendedModifier)modifier).isAnnotation()) {
						String name = ((Annotation)modifier).getTypeName().getFullyQualifiedName();
						if (annotations.contains(name)) {
							this.setMatches(true);
							return;
						}
					}
				}
			}
			
			@Override
			public boolean visit(TypeDeclaration node) {
				if (importedClassAnnotations.size() == 0)
					return true;
				List<?> modifiers = node.modifiers();
				if (node.isInterface()) {
					matchModifiers(modifiers, importedInterfaceAnnotations);
				} else {					
					matchModifiers(modifiers, importedClassAnnotations);
				}
				return true;
			}
			
			@Override
			public boolean visit(FieldDeclaration node) {
				if (this.getMatches() || importedFieldAnnotations.size() == 0)
					return true;
				List<?> modifiers = node.modifiers();
				matchModifiers(modifiers, importedFieldAnnotations);
				return true;
			}
		};
		
		root.accept(visitor);
		
		return visitor.getMatches();
	}
	
	public AnnotationHeuristics mapOnClass(String ... annotations) {
		for (String annotation : annotations) {
			this.classAnnotations.add(annotation);
		}
		return this;
	}
	
	public AnnotationHeuristics mapOnInterface(String ... annotations) {
		for (String annotation : annotations) {
			this.interfaceAnnotations.add(annotation);
		}
		return this;
	}
	
	public AnnotationHeuristics mapOnField(String ... annotations) {
		for (String annotation : annotations) {
			this.fieldAnnotations.add(annotation);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String... parameters) throws UnrecognizedHeuristicKey {
		if (key.equals("onClass")) {
			return this.mapOnClass(parameters);
		} else if (key.equals("onInterface")) {
			return this.mapOnInterface(parameters);
		} else if (key.equals("onField")) {
			return this.mapOnField(parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}

}
