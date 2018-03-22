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
		for (RepositoryFile file : files ) {
			Map<String, Boolean> roleMap = this.mappingStrategy.applyHeuristics(file);
			boolean unknownRole = true;
			for (Map.Entry<String, Boolean> entry : roleMap.entrySet()) {
				if (entry.getValue()) {
					writer.write(file.getFullName(), entry.getKey());
					unknownRole = false;
				}
			}
			if (unknownRole)
				writer.write(file.getFullName(), "unknown");
		}
	}
}
