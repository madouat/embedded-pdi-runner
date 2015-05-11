package bf.associateconsultant.core.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import bf.associateconsultant.core.XTransformation;

@Ignore
public class TestRunTransformationWithoutRepository {

	private static final String TRANSFORMATION_PATH = "C:\\tatatest";
	private XTransformation xtranformation;

	@Before
	public void setUp() throws Exception {
		xtranformation = new XTransformation();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTransformationRunningWithoutParameter() {
		Map<String, String> parameters = null;
		String returningdata = xtranformation.execute(null,
				TRANSFORMATION_PATH, "test.ktr", parameters);
		System.out.println(returningdata);
	}

	@Test
	// @Ignore
	public void testTransformationRunningWithParameter() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID_PARAM", "2");
		String returningdata = new XTransformation().execute(null,
				TRANSFORMATION_PATH, "trans_parameter.ktr", parameters);
		System.out.println(returningdata);
	}

}
