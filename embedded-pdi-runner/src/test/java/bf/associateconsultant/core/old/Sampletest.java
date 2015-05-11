package bf.associateconsultant.core.old;

import java.io.IOException;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;

import bf.associateconsultant.core.XRepository;

public class Sampletest {
	public static void main(String[] args) throws IOException {
		try {
			XRepository xrepository = new XRepository().withDbHost("localhost")
					.withDbName("pdirepo").withDbType("postgresql")
					.withDbPort("5432").withDbUsername("postgres")
					.withDbPassword("unkmapied");

			xrepository.build();
			KettleDatabaseRepository repository = xrepository
					.getKettleDatabaseRepository();

			repository.connect("admin", "admin");

			boolean ook = repository.isConnected();
			System.out.println(ook ? "connexion succeed" : "connexion failed");

		} catch (KettleException e) {
			e.printStackTrace();
		}
	}
}
