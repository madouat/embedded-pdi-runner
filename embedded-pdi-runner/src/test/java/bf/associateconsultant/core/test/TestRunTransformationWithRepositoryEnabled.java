package bf.associateconsultant.core.test;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import bf.associateconsultant.core.XRepository;
import bf.associateconsultant.core.XTransformation;

public class TestRunTransformationWithRepositoryEnabled {
	XRepository repository;
	private XTransformation xTransformation;

	@Before
	public void setUp() throws Exception {
		repository = new XRepository().withDbHost("localhost")
				.withDbName("pdirepo").withDbType("postgresql")
				.withDbPort("5432").withDbUsername("postgres")
				.withDbPassword("unkmapied").withRepositoryuser("admin")
				.withRepositoryPassword("admin");
		xTransformation = new XTransformation();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTranformationRunningWithoutParameter() {
		Map<String, String> parameters = null;
		String transformationPath = "/";
		String transformationName = "trans_test_repo";
		String returningdata = xTransformation.execute(repository,
				transformationPath, transformationName, parameters);
		System.out.println(returningdata);
	}

	@Test
	@Ignore
	public void testTransformationRunningWithParameter() {
		Map<String, String> parameters = null;
		String transformationPath = "/";
		String transformationName = "trans_test_repo";
		String returningdata = xTransformation.execute(repository,
				transformationPath, transformationName, parameters);
		System.out.println(returningdata);
	}

}
