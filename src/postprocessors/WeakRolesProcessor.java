package postprocessors;

import java.util.HashMap;
import java.util.Map;

import heuristics.AnalysedFile;

public class WeakRolesProcessor implements RolePostProcessor {

	@Override
	public AnalysedFile postProcess(AnalysedFile file) {
		file.setRoles(removeWeakRoles(file.getRoles()));
		return file;
	}
	
	private Map<String, String> removeWeakRoles(Map<String, String> roleMap) {
		Map<String, String> consolidatedMap = new HashMap<>(roleMap);
		for (Map.Entry<String, String> entry : roleMap.entrySet()) {
			if (!entry.getKey().contains("*") && entry.getKey().contains(":")) {
				String[] splitRole = entry.getKey().split(":");
				splitRole[splitRole.length-1] = "*";
				String weakRole = String.join(":", splitRole);
				consolidatedMap.remove(weakRole);
			}
		}
		return consolidatedMap;
		
	}

}
