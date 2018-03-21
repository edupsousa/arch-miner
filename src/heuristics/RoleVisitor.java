package heuristics;

import org.eclipse.jdt.core.dom.ASTVisitor;

public abstract class RoleVisitor extends ASTVisitor {
	private Boolean matches = false;

	public Boolean getMatches() {
		return matches;
	}

	public void setMatches(Boolean matches) {
		this.matches = matches;
	}
}
