package bf.associateconsultant.core.test;

import org.junit.Test;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.Assert;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;

import bf.associateconsultant.core.XRepository;

public class TestRepositoryWorking {

	@Test
	public void testRepositoryisConnected() throws KettleException {
		XRepository xrepository = new XRepository().withDbHost("localhost")
				.withDbName("pdirepo").withDbType("postgresql")
				.withDbPort("5432").withDbUsername("postgres")
				.withDbPassword("unkmapied");
		xrepository.build();
		KettleDatabaseRepository repository = xrepository
				.getKettleDatabaseRepository();
		repository.connect("admin", "admin");
		Assert.assertTrue(repository.isConnected());
	}

}
