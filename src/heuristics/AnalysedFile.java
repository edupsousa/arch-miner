package heuristics;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.repodriller.domain.Modification;
import org.repodriller.scm.RepositoryFile;

public class AnalysedFile {
	private Modification mod;
	private CompilationUnit astRoot = null;
	private Map<String, String> roles = new HashMap<>();

	public AnalysedFile(Modification modification) {
		super();
		this.mod = modification;
	}
	
	public Map<String, String> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, String> roles) {
		this.roles = roles;
	}

	public String getExtension() {
		return FilenameUtils.getExtension(mod.getNewPath()).toLowerCase();
	}
	
	public String getFilename() {
		return mod.getFileName().toLowerCase();
	}
	
	public String getPath() {
		return FilenameUtils.getPath(mod.getNewPath());
	}
	
	public CompilationUnit getASTRoot() {
		if (!this.getExtension().equals("java"))
			return null;
		
		if (this.astRoot == null) {
			ASTParser parser = ASTParser.newParser(AST.JLS9);
			parser.setCompilerOptions(JavaCore.getOptions());
			parser.setSource(this.mod.getSourceCode().toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			this.astRoot = (CompilationUnit) parser.createAST(null);
		}
		return this.astRoot;
	}
}
