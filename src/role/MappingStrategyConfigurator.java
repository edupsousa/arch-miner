package role;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import heuristics.ConfigurableHeuristics;
import heuristics.HeuristicsFactory;

public class MappingStrategyConfigurator {
	public static RoleMappingStrategy fromJSON(String path) {
		String jsonContents = readJSONFile(path);
		JSONObject config = new JSONObject(jsonContents);		
		RoleMappingStrategy strategy = new RoleMappingStrategy();
		
		for (String layer : config.keySet()) {
			JSONObject layerHeuristics = config.getJSONObject(layer);
			for (String heuristicName : layerHeuristics.keySet()) {
				JSONObject heuristicMethods = layerHeuristics.getJSONObject(heuristicName);
				for (String methodName : heuristicMethods.keySet()) {
					JSONArray methodParams = heuristicMethods.getJSONArray(methodName);
					ConfigurableHeuristics heuristic = HeuristicsFactory.createConfigurable(heuristicName);
					heuristic.configureHeuristic(methodName, readParametersForRole(methodParams));
					strategy.addHeuristic(layer, heuristic);
				}
			}
		}
		
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
	
	private static String[] readParametersForRole(JSONArray methodParams) {
		ArrayList<String> params = new ArrayList<>();
		methodParams.forEach(param -> params.add((String)param));
		return params.toArray(new String[params.size()]);
	}
	
}
