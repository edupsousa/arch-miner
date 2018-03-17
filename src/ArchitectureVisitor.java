import java.util.List;
import java.util.Map;

import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.RepositoryFile;
import org.repodriller.scm.SCMRepository;

import heuristics.FilenameHeuristics;
import role.RoleMappingStrategy;

public class ArchitectureVisitor implements CommitVisitor {
	
	protected RoleMappingStrategy mappingStrategy;
	
	public ArchitectureVisitor() {
		this.mappingStrategy = this.createMappingStrategy();
	}

	@Override
	public void process(SCMRepository repository, Commit commit, PersistenceMechanism writer) {
		try {
			repository.getScm().checkout(commit.getHash());
			List<RepositoryFile> files = repository.getScm().files();
			for (RepositoryFile file : files ) {
				Map<String, String> roleMap = this.mappingStrategy.getRole(file);
				for (Map.Entry<String, String> entry : roleMap.entrySet()) {
					writer.write(file.getFullName(), entry.getKey(), entry.getValue());
				}
			}
		} finally {
			repository.getScm().reset();
		}
	}
	
	public RoleMappingStrategy createMappingStrategy() {
		RoleMappingStrategy strategy = new RoleMappingStrategy();
		FilenameHeuristics extensionHeuristic = new FilenameHeuristics()
				.mapExtensions("view:page", "htm", "html", "jsp", "jspx")
				.mapExtensions("view:script", "js", "ts", "coffee")
				.mapExtensions("view:style", "css", "sass", "less")
				.mapExtensions("view:image", "png", "gif", "jpg", "jpeg")
				.mapExtensions("configuration:generic", "xml", "json", "properties", "yml", "gradle", "config")
				.mapExtensions("model:sql", "sql")
				.mapExtensions("documentation", "md")
				.mapFilename("configuration:docker", "dockerfile");
		strategy.addOtherHeuristic(extensionHeuristic);
		
		return strategy;
	}

}