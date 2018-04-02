package heuristics;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.repodriller.scm.RepositoryFile;

public class AnalysedFile {
	private RepositoryFile file;
	private CompilationUnit astRoot = null;
	private Map<String, String> roles = new HashMap<>();

	public AnalysedFile(RepositoryFile file) {
		super();
		this.file = file;
	}
	
	public Map<String, String> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, String> roles) {
		this.roles = roles;
	}

	public String getExtension() {
		return FilenameUtils.getExtension(file.getFile().getName()).toLowerCase();
	}
	
	public String getFilename() {
		return file.getFile().getName().toLowerCase();
	}
	
	public String getPath() {
		return FilenameUtils.getPath(file.getFullName());
	}
	
	public CompilationUnit getASTRoot() {
		if (!this.getExtension().equals("java"))
			return null;
		
		if (this.astRoot == null) {
			ASTParser parser = ASTParser.newParser(AST.JLS9);
			parser.setCompilerOptions(JavaCore.getOptions());
			parser.setSource(this.file.getSourceCode().toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			this.astRoot = (CompilationUnit) parser.createAST(null);
		}
		return this.astRoot;
	}
}
