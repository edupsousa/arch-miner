import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.GitRepository;

import persistence.SQLFile;

public class ArchitectureStudy implements Study {

	public static void main(String[] args) {
		new RepoDriller().start(new ArchitectureStudy());
	}
	
	@Override
	public void execute() {
		String[] columns = {"project", "file", "role", "heuristic"};
		PersistenceMechanism outputFile = new SQLFile("~/Documents/Mestrado/jsf.sql", "roles", columns, false);
		new RepositoryMining()
			.in(GitRepository.allProjectsIn("/home/edupsousa/Documents/Mestrado/Projetos/jsf/"))
			.through(Commits.onlyInHead())
			.process(ArchitectureVisitor.createAndConfigure("./resources/heuristics.json") , outputFile)
			.mine();
	}

}
