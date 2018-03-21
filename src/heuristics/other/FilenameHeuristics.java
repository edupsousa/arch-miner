package heuristics.other;

import java.util.ArrayList;
import java.util.List;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.UnrecognizedHeuristicKey;

public class FilenameHeuristics implements ConfigurableHeuristics {

	protected List<String> extensions = new ArrayList<>();
	protected List<String> filenames = new ArrayList<>();
	
	@Override
	public String getName() {
		return "filename";
	}

	@Override
	public Boolean getRole(AnalysedFile file) {
		String filename = file.getFilename();
		if (this.filenames.contains(filename))
			return true;
		
		String extension = file.getExtension();
		if (this.extensions.contains(extension))
			return true;
		
		return false;
	}

	public FilenameHeuristics mapExtension(String extension) {
		this.extensions.add(extension);
		return this;
	}
	
	public FilenameHeuristics mapExtensions(String ... extensions) {
		for (String extension : extensions) {
			this.mapExtension(extension);
		}
		return this;
	}
	
	public FilenameHeuristics mapFilenames(String ... filenames) {
		for (String filename : filenames) {
			this.mapFilename(filename);
		}
		return this;
	}
	
	public FilenameHeuristics mapFilename(String filename) {
		this.filenames.add(filename);
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String ... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("name")) {
			return this.mapFilenames(parameters);
		} else if (key.equals("extension")) {
			return this.mapExtensions(parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}
}
