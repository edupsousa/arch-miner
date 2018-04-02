package role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.repodriller.domain.Modification;
import org.repodriller.scm.RepositoryFile;

import heuristics.AnalysedFile;
import heuristics.RoleHeuristics;
import postprocessors.RolePostProcessor;
import postprocessors.WeakRolesProcessor;

public class RoleMappingStrategy {
	
	protected Map<String, List<RoleHeuristics>> heuristics;
	private List<RolePostProcessor> postProcessors;
	
	public RoleMappingStrategy() {
		super();
		this.heuristics = new HashMap<>();
		this.postProcessors = new ArrayList<>();
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

	public AnalysedFile applyHeuristics(Modification modification) {
		HashMap<String, String> results = new HashMap<>();
		AnalysedFile file = new AnalysedFile(modification);
		for (Map.Entry<String, List<RoleHeuristics>> entry : this.heuristics.entrySet()) {
			for (RoleHeuristics heuristic : entry.getValue()) {
				if (heuristic.getRole(file)) {
					results.put(entry.getKey(), heuristic.getName());
				}
			}
		}
		file.setRoles(results);
		return postProcess(file);
	}
	
	public AnalysedFile postProcess(AnalysedFile file) {
		for (RolePostProcessor rpp : this.postProcessors) {
			file = rpp.postProcess(file);
		}
		return file;
	}
	
	public RoleMappingStrategy addPostProcessor(RolePostProcessor postProcessor) {
		this.postProcessors.add(postProcessor);
		return this;
	}
}
