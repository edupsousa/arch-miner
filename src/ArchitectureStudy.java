import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class ArchitectureStudy implements Study {

	public static void main(String[] args) {
		new RepoDriller().start(new ArchitectureStudy());
	}
	
	@Override
	public void execute() {
		new RepositoryMining()
			.in(GitRepository.singleProject("~/Documents/Mestrado/Projetos/springmvc-angular/alf.io"))
			.through(Commits.onlyInHead())
			.process(ArchitectureVisitor.createAndConfigure("./resources/heuristics.new.json") , new CSVFile("~/Documents/Mestrado/driller.csv"))
			.mine();
	}

}
