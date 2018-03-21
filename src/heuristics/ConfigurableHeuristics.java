package heuristics;

public interface ConfigurableHeuristics extends RoleHeuristics {
	public ConfigurableHeuristics configureHeuristic(String key, String ... parameters) throws UnrecognizedHeuristicKey;
}
