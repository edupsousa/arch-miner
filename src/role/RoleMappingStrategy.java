package role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.repodriller.scm.RepositoryFile;

import heuristics.AnalysedFile;
import heuristics.RoleHeuristics;

public class RoleMappingStrategy {
	
	protected Map<String, List<RoleHeuristics>> heuristics;
	
	public RoleMappingStrategy() {
		super();
		this.heuristics = new HashMap<>();
	}
	
	public RoleMappingStrategy addHeuristic(String role, RoleHeuristics heuristic) {
		List<RoleHeuristics> updatedHeuristics = this.heuristics.get(role);
		if (updatedHeuristics == null) {
			updatedHeuristics = new ArrayList<>();
		}
		updatedHeuristics.add(heuristic);
		this.heuristics.put(role, updatedHeuristics);
		return this;
	}

	public Map<String, String> applyHeuristics(RepositoryFile repositoryFile) {
		HashMap<String, String> results = new HashMap<>();
		AnalysedFile file = new AnalysedFile(repositoryFile);
		for (Map.Entry<String, List<RoleHeuristics>> entry : this.heuristics.entrySet()) {
			for (RoleHeuristics heuristic : entry.getValue()) {
				if (heuristic.getRole(file)) {
					results.put(entry.getKey(), heuristic.getName());
				}
			}
		}
		return results;
	}
}
