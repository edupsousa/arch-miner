package heuristics.other;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.UnrecognizedHeuristicKey;

public class FilePathHeuristics implements ConfigurableHeuristics {

	protected List<String> containsDirectory = new ArrayList<>();
	
	@Override
	public String getName() {
		return "path";
	}

	@Override
	public Boolean getRole(AnalysedFile file) {
		String filePath = FilenameUtils.normalize(file.getPath().toLowerCase(), true);
		for (String entry : this.containsDirectory) {
			if (filePath.contains(entry)) {
				return true;
			}
		}
		return false;
	}
	
	public FilePathHeuristics mapDirectory(String directory) {
		directory = FilenameUtils.normalize(directory, true);
		if (!directory.startsWith("/")) {
			directory = "/".concat(directory);
		}
		if (!directory.endsWith("/")) {
			directory = directory.concat("/");
		}
		this.containsDirectory.add(directory);
		return this;
	}
	
	public FilePathHeuristics mapDirectories(String ... directories) {
		for (String directory : directories) {
			this.mapDirectory(directory);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("directory")) {
			return this.mapDirectories(parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}

}
