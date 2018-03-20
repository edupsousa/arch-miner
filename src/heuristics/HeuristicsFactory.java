package heuristics;

import heuristics.java.ClassAnnotationHeuristics;
import heuristics.java.ClassNameHeuristics;
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
		case "classAnnotation":
			return new ClassAnnotationHeuristics();
		case "imports":
			return new ImportsHeuristics();
		case "package":
			return new PackageHeuristics();
		default:
			throw new UnrecognizedHeuristics();	
		}
	}
}
