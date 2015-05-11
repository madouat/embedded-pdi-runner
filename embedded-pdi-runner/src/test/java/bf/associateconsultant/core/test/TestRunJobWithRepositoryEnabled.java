package bf.associateconsultant.core.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bf.associateconsultant.core.XJob;
import bf.associateconsultant.core.XRepository;

public class TestRunJobWithRepositoryEnabled {
	XRepository repository;

	@Before
	public void setUp() throws Exception {
		repository = new XRepository().withDbHost("localhost")
				.withDbName("pdirepo").withDbType("postgresql")
				.withDbPort("5432").withDbUsername("postgres")
				.withDbPassword("unkmapied").withRepositoryuser("admin")
				.withRepositoryPassword("admin");

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testJobRunningWithoutParameter() {
		Map<String, String> parameters = null;
		String returningdata = new XJob().execute(repository, "/dev",
				"myjob_rep", parameters);
		System.out.println(returningdata);
	}

	@Test
	public void testJobRunningWithParameter() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID_PARAM", "2");
		String returningdata = new XJob().execute(repository, "/param",
				"myjob_parameters", parameters);
		System.out.println(returningdata);
	}

}
