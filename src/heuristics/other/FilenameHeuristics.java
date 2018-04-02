package heuristics.other;

import java.util.ArrayList;
import java.util.List;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.UnrecognizedHeuristicKey;

public class FilenameHeuristics implements ConfigurableHeuristics {

	protected List<String> extensions = new ArrayList<>();
	protected List<String> filenames = new ArrayList<>();
	protected List<String> regexps = new ArrayList<>();
	
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
		
		for (String regex : this.regexps) {
			if (filename.matches(regex)) {
				return true;
			}
		}
		
		return false;
	}

	public FilenameHeuristics mapExtensions(String ... extensions) {
		for (String extension : extensions) {
			this.extensions.add(extension);
		}
		return this;
	}
	
	public FilenameHeuristics mapFilenames(String ... filenames) {
		for (String filename : filenames) {
			this.filenames.add(filename);
		}
		return this;
	}
	
	public FilenameHeuristics mapRegexps(String ... regexps) {
		for (String regex : regexps) {
			this.regexps.add(regex);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String ... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("name")) {
			return this.mapFilenames(parameters);
		} else if (key.equals("extension")) {
			return this.mapExtensions(parameters);
		} else if (key.equals("regex")) {
			return this.mapRegexps(parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}
}
