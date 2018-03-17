package role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.repodriller.scm.RepositoryFile;

import heuristics.AnalysedFile;
import heuristics.RoleHeuristics;

public class RoleMappingStrategy {
	
	protected List<RoleHeuristics> javaHeuristics;
	protected List<RoleHeuristics> otherHeuristics;
	
	
	
	public RoleMappingStrategy() {
		super();
		this.javaHeuristics = new ArrayList<>();
		this.otherHeuristics = new ArrayList<>();
	}

	public RoleMappingStrategy addJavaHeuristic(RoleHeuristics heuristic) {
		this.javaHeuristics.add(heuristic);
		return this;
	}
	
	public RoleMappingStrategy addOtherHeuristic(RoleHeuristics heuristic) {
		this.otherHeuristics.add(heuristic);
		return this;
	}
	
	public Map<String, String> getRole(RepositoryFile repositoryFile) {
		AnalysedFile file = new AnalysedFile(repositoryFile);
		if (file.getExtension().equals("java")) {
			return applyHeuristics(this.javaHeuristics, file);
		} else {
			return applyHeuristics(this.otherHeuristics, file);
		}
	}
	
	protected Map<String, String> applyHeuristics(List<RoleHeuristics> heuristics, AnalysedFile file) {
		Map<String, String> results = new HashMap<>();
		for (RoleHeuristics heuristic : heuristics) {
			results.put(heuristic.getName(), heuristic.getRole(file));
		}
		return results;
	}
}
