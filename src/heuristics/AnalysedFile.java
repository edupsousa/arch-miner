package heuristics;

import org.apache.commons.io.FilenameUtils;
import org.repodriller.scm.RepositoryFile;

public class AnalysedFile {
	protected RepositoryFile file;

	public AnalysedFile(RepositoryFile file) {
		super();
		this.file = file;
	}
	
	public String getExtension() {
		return FilenameUtils.getExtension(file.getFile().getName()).toLowerCase();
	}
	
	public String getFilename() {
		return file.getFile().getName().toLowerCase();
	}
}
