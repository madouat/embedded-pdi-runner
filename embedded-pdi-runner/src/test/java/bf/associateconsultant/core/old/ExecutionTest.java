package bf.associateconsultant.core.old;

import java.util.Map;

import org.pentaho.di.core.exception.KettleException;

import bf.associateconsultant.core.XJob;
import bf.associateconsultant.core.XRepository;
import bf.associateconsultant.core.XTransformation;

public class ExecutionTest {
	public static void main(String args[]) throws KettleException {
		XTransformation transformation = new XTransformation();
		XJob jc = new XJob();
		XRepository repository = new XRepository().withDbHost("localhost")
				.withDbName("pdirepo").withDbType("postgresql")
				.withDbPort("5432").withDbUsername("postgres")
				.withDbPassword("unkmapied").withRepositoryuser("admin")
				.withRepositoryPassword("admin");

		String transformationPath = "/";
		String transformationName = "trans_test_repo";

		Map<String, String> parameters = null;
		String resultat = transformation.execute(repository,
				transformationPath, transformationName, parameters);
		System.out.println(resultat);

		resultat = new XTransformation().execute(null, "C:\\tatatest",
				"test.ktr", parameters);

		System.out.println("blablabla " + resultat);

		String data = jc.execute(repository, "/dev", "myjob_rep", parameters);
		System.out.println(data);
	}
}
