import java.util.List;
import java.util.Map;

import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.RepositoryFile;
import org.repodriller.scm.SCMRepository;

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
		String repositoryName = getRepositoryName(repository.getOrigin());
		String repositoryPath = repository.getPath();
		for (RepositoryFile file : files) {
			String filePath = relativePath(repositoryPath, file.getFullName());
			Map<String, String> roleMap = this.mappingStrategy.applyHeuristics(file);
			if (roleMap.size() == 0) {
				writer.write(repositoryName, filePath, "unknown", "");
			} else {
				for (Map.Entry<String, String> entry : roleMap.entrySet()) {
					writer.write(repositoryName, filePath, entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
	protected String getRepositoryName(String origin) {
		return origin.substring("https://github.com/".length(), origin.length()-4);
	}
	
	protected String relativePath(String repositoryPath, String filePath) {
		return filePath.substring(repositoryPath.length()+1);
	}
}
