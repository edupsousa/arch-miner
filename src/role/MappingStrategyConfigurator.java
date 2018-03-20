package role;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;

import heuristics.ConfigurableHeuristics;
import heuristics.HeuristicsFactory;

public class MappingStrategyConfigurator {
	public static RoleMappingStrategy fromJSON(String path) {
		String jsonContents = readJSONFile(path);
		JSONObject config = new JSONObject(jsonContents);		
		RoleMappingStrategy strategy = new RoleMappingStrategy();

		createHeuristics(config.getJSONObject("other"))
			.forEach(heuristic -> strategy.addOtherHeuristic(heuristic));
		
		createHeuristics(config.getJSONObject("java"))
			.forEach(heuristic -> strategy.addJavaHeuristic(heuristic));
		
		return strategy;
	}

	private static String readJSONFile(String path) {
		String jsonConfig;
		try {
			Scanner reader = new Scanner(new File(path));
			jsonConfig = reader.useDelimiter("\\A").next();
			reader.close();
		} catch (FileNotFoundException e) {
			return null;
		}
		return jsonConfig;
	}
	
	private static List<ConfigurableHeuristics> createHeuristics(JSONObject heuristicsMap) {
		ArrayList<ConfigurableHeuristics> heuristicsList = new ArrayList<>();
		for (String heuristicName : heuristicsMap.keySet()) {
			JSONObject methodsMap = heuristicsMap.getJSONObject(heuristicName);
			ConfigurableHeuristics heuristics = HeuristicsFactory.createConfigurable(heuristicName);
			heuristics = configureHeuristicMethods(heuristics, methodsMap);
			heuristicsList.add(heuristics);
		}
		return heuristicsList;
	}

	private static ConfigurableHeuristics configureHeuristicMethods(ConfigurableHeuristics heuristics,
			JSONObject methodsMap) {
		for (String heuristicMethod : methodsMap.keySet()) {
			heuristics = configureMethodRoles(heuristics, methodsMap, heuristicMethod);
		}
		return heuristics;
	}

	private static ConfigurableHeuristics configureMethodRoles(ConfigurableHeuristics heuristics, JSONObject methodsMap,
			String heuristicMethod) {
		JSONObject rolesMap = methodsMap.getJSONObject(heuristicMethod);
		for (String role: rolesMap.keySet()) {
			heuristics.configureHeuristic(heuristicMethod, role, readParametersForRole(role,  rolesMap));
		}
		return heuristics;
	}
	
	private static String[] readParametersForRole(String roleName, JSONObject rolesMap) {
		ArrayList<String> params = new ArrayList<>();
		rolesMap.getJSONArray(roleName).forEach(param -> params.add((String)param));
		return params.toArray(new String[params.size()]);
	}
	
}
