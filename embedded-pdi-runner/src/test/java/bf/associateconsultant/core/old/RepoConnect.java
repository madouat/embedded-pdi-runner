package bf.associateconsultant.core.old;

import java.io.IOException;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;

import bf.associateconsultant.core.XRepository;

public class RepoConnect {

	public static boolean connect() throws KettleException, IOException {

		XRepository withDbPassword = new XRepository().withDbHost("localhost")
				.withDbName("pdirepo").withDbType("postgresql")
				.withDbPort("5432").withDbUsername("postgres")
				.withDbPassword("unkmapied");
		withDbPassword.build();
		KettleDatabaseRepository repository = withDbPassword
				.getKettleDatabaseRepository();

		repository.connect("admin", "admin");
		boolean ok = repository.isConnected();
		return ok;
	}
}
