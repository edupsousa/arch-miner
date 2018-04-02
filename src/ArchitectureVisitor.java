import java.util.List;
import java.util.Map;

import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.RepositoryFile;
import org.repodriller.scm.SCMRepository;

import heuristics.AnalysedFile;
import role.MappingStrategyConfigurator;
import role.RoleMappingStrategy;

public class ArchitectureVisitor implements CommitVisitor {

	protected RoleMappingStrategy mappingStrategy;

	public ArchitectureVisitor() {
		this.mappingStrategy = new RoleMappingStrategy();
	}

	public static ArchitectureVisitor createAndConfigure(String configurationPath) {
		ArchitectureVisitor visitor = new ArchitectureVisitor();
		visitor.setStrategy(MappingStrategyConfigurator.fromJSON(configurationPath));
		return visitor;
	}

	public void setStrategy(RoleMappingStrategy strategy) {
		this.mappingStrategy = strategy;
	}

	@Override
	public void process(SCMRepository repository, Commit commit, PersistenceMechanism writer) {
		List<RepositoryFile> files = repository.getScm().files();
		String repositoryID = getRepositoryID(repository.getPath());
		String repositoryPath = repository.getPath();
		for (RepositoryFile repoFile : files) {
			AnalysedFile file = this.mappingStrategy.applyHeuristics(repoFile);
			String filePath = relativePath(repositoryPath, repoFile.getFullName());
			Map<String, String> roleMap = file.getRoles();
			if (roleMap.size() == 0) {
				writer.write(repositoryID, filePath, "unknown", "");
			} else {
				for (Map.Entry<String, String> entry : roleMap.entrySet()) {
					writer.write(repositoryID, filePath, entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
	protected String getRepositoryID(String repositoryPath) {
		String lastDir = repositoryPath.substring(repositoryPath.lastIndexOf('/')+1);
		return lastDir.substring(0, lastDir.lastIndexOf('-'));
	}
	
	protected String relativePath(String repositoryPath, String filePath) {
		return filePath.substring(repositoryPath.length()+1);
	}
}
