package heuristics.other;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import heuristics.AnalysedFile;
import heuristics.ConfigurableHeuristics;
import heuristics.UnrecognizedHeuristicKey;

public class FilePathHeuristics implements ConfigurableHeuristics {

	protected Map<String, String> containsDirectoryMap;
	
	@Override
	public String getName() {
		return "path";
	}

	@Override
	public String getRole(AnalysedFile file) {
		String filePath = FilenameUtils.normalize(file.getPath().toLowerCase(), true);
		for (Map.Entry<String, String> entry : this.containsDirectoryMap.entrySet()) {
			String key = entry.getKey();
			if (filePath.contains(key)) {
				return entry.getValue();
			}
		}
		return "unknown";
	}
	
	public FilePathHeuristics mapDirectory(String role, String directory) {
		if (this.containsDirectoryMap == null) {
			this.containsDirectoryMap = new HashMap<>();
		}
		directory = FilenameUtils.normalize(directory, true);
		if (!directory.startsWith("/")) {
			directory = "/".concat(directory);
		}
		if (!directory.endsWith("/")) {
			directory = directory.concat("/");
		}
		this.containsDirectoryMap.put(directory, role);
		return this;
	}
	
	public FilePathHeuristics mapDirectories(String role, String ... directories) {
		for (String directory : directories) {
			this.mapDirectory(role, directory);
		}
		return this;
	}

	@Override
	public ConfigurableHeuristics configureHeuristic(String key, String role, String... parameters)
			throws UnrecognizedHeuristicKey {
		if (key.equals("directory")) {
			return this.mapDirectories(role, parameters);
		} else {
			throw new UnrecognizedHeuristicKey();
		}
	}

}
