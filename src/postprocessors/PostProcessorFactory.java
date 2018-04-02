package postprocessors;

public class PostProcessorFactory {
	public static RolePostProcessor createPostProcessor(String name) {
		switch (name) {
		case "removeWeakRoles":
			return new WeakRolesProcessor();
		}
		return null;
	}
}
