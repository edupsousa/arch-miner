package heuristics;

import org.eclipse.jdt.core.dom.ASTVisitor;

public abstract class RoleVisitor extends ASTVisitor {
	private String role = "unknown";

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
