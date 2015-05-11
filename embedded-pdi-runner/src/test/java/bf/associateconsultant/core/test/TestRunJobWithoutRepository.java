package bf.associateconsultant.core.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import bf.associateconsultant.core.XJob;

@Ignore
public class TestRunJobWithoutRepository {

	private static final String JOB_PATH = "C:\\tatatest";

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testJobRunningWithoutParameter() {
		Map<String, String> parameters = null;
		String returningdata = new XJob().execute(null, JOB_PATH, "myjob.kjb",
				parameters);
		System.out.println(returningdata);
	}

	@Test
	public void testJobRunningWithParameter() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID_PARAM", "2");
		String returningdata = new XJob().execute(null, JOB_PATH,
				"myjob_parameters.kjb", parameters);
		System.out.println(returningdata);
	}

}
