import java.util.*;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import heuristics.AnalysedFile;
import role.MappingStrategyConfigurator;
import role.RoleMappingStrategy;

public class ArchitectureVisitor implements CommitVisitor {

	protected RoleMappingStrategy mappingStrategy;
	private Set<String> visitedFiles = new HashSet<>();

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
		String repositoryID = getRepositoryID(repository.getPath());
	
		for (Modification mod : commit.getModifications()) {
			if (mod.wasDeleted())
				continue;
			String filePath = mod.getNewPath();
			if (visitedFiles.contains(filePath))
				continue;
			visitedFiles.add(filePath);
			AnalysedFile file = this.mappingStrategy.applyHeuristics(mod);
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
