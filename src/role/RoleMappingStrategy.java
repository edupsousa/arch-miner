package role;

import java.util.HashMap;
import java.util.Map;

import org.repodriller.scm.RepositoryFile;

import heuristics.AnalysedFile;
import heuristics.RoleHeuristics;

public class RoleMappingStrategy {
	
	protected Map<String, RoleHeuristics> heuristics;
	
	public RoleMappingStrategy() {
		super();
		
		this.heuristics = new HashMap<>();
	}
	
	public RoleMappingStrategy addHeuristic(String role, RoleHeuristics heuristic) {
		this.heuristics.put(role, heuristic);
		return this;
	}

	public Map<String, Boolean> applyHeuristics(RepositoryFile repositoryFile) {
		HashMap<String, Boolean> results = new HashMap<>();
		AnalysedFile file = new AnalysedFile(repositoryFile);
		for (Map.Entry<String, RoleHeuristics> entry : this.heuristics.entrySet()) {
			results.put(entry.getKey(), entry.getValue().getRole(file));
		}
		return results;
	}
}
