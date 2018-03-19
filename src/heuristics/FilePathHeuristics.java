package heuristics;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

public class FilePathHeuristics implements RoleHeuristics {

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

}
