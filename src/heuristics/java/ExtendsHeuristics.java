package heuristics.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.RoleVisitor;
import heuristics.UnrecognizedHeuristicKey;

public class ExtendsHeuristics implements ConfigurableHeuristics {

	private List<String> superInterfaces = new ArrayList<>();

	@Override
	public String getName() {
		return "extends";
	}

	@Override
	public Boolean getRole(AnalysedFile file) {
		CompilationUnit root = file.getASTRoot();
		if (root == null)
			return false;

		RoleVisitor visitor = new RoleVisitor() {
			private List<String> importedSuperInterfaces = new ArrayList<>();

			@Override
			public boolean visit(ImportDeclaration node) {
				String importFQName = node.getName().getFullyQualifiedName();
				String importSimpleName = importFQName.substring(importFQName.lastIndexOf('.') + 1);

				if (superInterfaces.contains(importFQName)) {
					importedSuperInterfaces.add(importSimpleName);
				}
				return true;
			}

			@Override
			public boolean visit(TypeDeclaration node) {
				if (node.isInterface()) {
					List<?> superTypes = node.superInterfaceTypes();
					for (Object oSuperType : superTypes) {
						Type superType = (Type) oSuperType;
						String typeName = "";
						if (superType.isSimpleType()) {
							typeName = ((SimpleType) superType).getName().getFullyQualifiedName();
						} else if (superType.isParameterizedType()) {
							typeName = ((ParameterizedType) superType).getType().toString();
						}
						if ((typeName.contains(".") && superInterfaces.contains(typeName))
								|| (!typeName.contains(".") && importedSuperInterfaces.contains(typeName))) {
							this.setMatches(true);
							return true;
						}
					}
				}

				return true;
			}
		};
		root.accept(visitor);
		return visitor.getMatches();
	}

	public ExtendsHeuristics mapSuperInterfaces(String... superInterfaces) {
		for (String superInterface : superInterfaces) {
			this.superInterfaces.add(superInterface);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String... parameters) throws UnrecognizedHeuristicKey {
		if (key.equals("interface")) {
			return this.mapSuperInterfaces(parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}

}
