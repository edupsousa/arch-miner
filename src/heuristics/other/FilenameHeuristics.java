package heuristics.other;

import java.util.HashMap;
import java.util.Map;

import heuristics.AnalysedFile;
import heuristics.RoleHeuristics;

public class FilenameHeuristics implements RoleHeuristics {

	protected Map<String, String> extensionMap;
	protected Map<String, String> filenameMap;
	
	@Override
	public String getName() {
		return "filename";
	}

	@Override
	public String getRole(AnalysedFile file) {
		String filename = file.getFilename();
		if (this.filenameMap.containsKey(filename))
			return this.filenameMap.get(filename);
		
		String extension = file.getExtension();
		if (!this.extensionMap.containsKey(extension))
			return "unknown";
		
		return this.extensionMap.get(extension);
	}

	public FilenameHeuristics mapExtension(String role, String extension) {
		if (this.extensionMap == null)
			this.extensionMap = new HashMap<>();
		this.extensionMap.put(extension, role);
		return this;
	}
	
	public FilenameHeuristics mapExtensions(String role, String ... extensions) {
		for (String extension : extensions) {
			this.mapExtension(role, extension);
		}
		return this;
	}
	
	public FilenameHeuristics mapFilenames(String role, String ... filenames) {
		for (String filename : filenames) {
			this.mapFilename(role, filename);
		}
		return this;
	}
	
	public FilenameHeuristics mapFilename(String role, String filename) {
		if (this.filenameMap == null)
			this.filenameMap = new HashMap<>();
		this.filenameMap.put(filename, role);
		return this;
	}
}
