package heuristics;

import heuristics.java.TypeAnnotationHeuristics;
import heuristics.java.ClassNameHeuristics;
import heuristics.java.ClassStructureHeuristics;
import heuristics.java.ImportsHeuristics;
import heuristics.java.PackageHeuristics;
import heuristics.other.FilePathHeuristics;
import heuristics.other.FilenameHeuristics;

public class HeuristicsFactory {
	
	public static ConfigurableHeuristics createConfigurable(String name) throws UnrecognizedHeuristics {
		switch (name) {
		case "fileName":
			return new FilenameHeuristics();
		case "filePath":
			return new FilePathHeuristics();
		case "className":
			return new ClassNameHeuristics();
		case "typeAnnotation":
			return new TypeAnnotationHeuristics();
		case "imports":
			return new ImportsHeuristics();
		case "package":
			return new PackageHeuristics();
		case "classStructure":
			return new ClassStructureHeuristics();
		default:
			throw new UnrecognizedHeuristics();	
		}
	}
}
