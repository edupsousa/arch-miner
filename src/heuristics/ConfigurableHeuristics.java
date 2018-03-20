package heuristics;

public interface ConfigurableHeuristics extends RoleHeuristics {
	public ConfigurableHeuristics configureHeuristic(String key, String role, String ... parameters) throws UnrecognizedHeuristicKey;
}
