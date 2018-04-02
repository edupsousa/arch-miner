package postprocessors;

import heuristics.AnalysedFile;

public interface RolePostProcessor {
	public AnalysedFile postProcess(AnalysedFile file);
}
