package bf.associateconsultant.core;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;

//import org.pentaho.di.trans.StepLoader;

public abstract class XAbstractTransformation {

	protected static final String XML = "XML";
	protected static final String JSON = "JSON";

	protected String[] pluginDirectories;
	protected String outputFormat = JSON;
	protected Boolean success;
	protected List<RowMetaAndData> rowMetaAndData;

	protected KettleDatabaseRepository initializeAndConnectToRepository(
			XRepository repository) throws KettleException {
		repository.build();
		KettleDatabaseRepository kettleDBRepository = repository
				.getKettleDatabaseRepository();
		kettleDBRepository.connect(repository.getRepositoryuser(),
				repository.getRepositoryPassword());
		return kettleDBRepository;
	}

	/**
	 * @return the outputFormat
	 */
	public String getOutputFormat() {
		return outputFormat;
	}

	/**
	 * @param outputFormat
	 *            the outputFormat to set
	 */
	public void setOutputFormat(String outputFormat) {
		if (outputFormat != null && !outputFormat.isEmpty())
			this.outputFormat = outputFormat;
	}

	/**
	 * @return the pluginDirectories
	 */
	public String[] getPluginDirectories() {
		return pluginDirectories;
	}

	/**
	 * @param pluginDirectory
	 *            the pluginDirectories to set
	 */
	public void setPluginDirectories(String[] pluginDirectories) {
		this.pluginDirectories = pluginDirectories;
	}

	/**
	 * @return the rowMetaAndData
	 */
	public List<RowMetaAndData> getRowMetaAndData() {
		return rowMetaAndData;
	}

	public abstract String execute(XRepository repository, String path,
			String chainName, Map<String, String> parameters)
			throws KettleException;

	protected void environnementInit() throws KettleException {
		KettleEnvironment.init();
	}

	@SuppressWarnings("unchecked")
	protected String toJSON(List<RowMetaAndData> rowMetaAndData)
			throws KettleException, JSONException {
		JSONObject dataReturnedFromTransOrJob = new JSONObject();
		JSONArray result = new JSONArray();
		try {
			dataReturnedFromTransOrJob.put("success", this.success);
			for (RowMetaAndData o : rowMetaAndData) {
				String[] fieldName = o.getRowMeta().getFieldNames();
				Object[] row = o.getData();
				int idx = 0;
				JSONObject obj = new JSONObject();
				while (idx < fieldName.length) {
					obj.put(fieldName[idx], row[idx]);
					idx++;
				}
				result.add(obj);
			}
			dataReturnedFromTransOrJob.put("data", result);
		} catch (Exception e) {
			dataReturnedFromTransOrJob.put("success", false);
			dataReturnedFromTransOrJob.put("message", e.getMessage());
		}
		return dataReturnedFromTransOrJob.toString();

	}

	protected String toXML(List<RowMetaAndData> rowMetaAndData)
			throws KettleException {
		StringBuilder stepdata = new StringBuilder();
		stepdata.append("<response>");
		stepdata.append("<success>");
		stepdata.append(this.success);
		stepdata.append("</success>");
		stepdata.append("<data>");
		for (RowMetaAndData o : rowMetaAndData) {
			String[] fieldName = o.getRowMeta().getFieldNames();
			Object[] row = o.getData();
			int idx = 0;
			stepdata.append("<row>");
			while (idx < fieldName.length) {
				stepdata.append("<" + fieldName[idx] + ">");
				stepdata.append(row[idx]);
				stepdata.append("</" + fieldName[idx] + ">");
				idx++;
			}
			stepdata.append("</row>");
		}
		stepdata.append("</data>");
		stepdata.append("</response>");
		return stepdata.toString();
	}

}
