package bf.associateconsultant.core;

import java.io.File;
import java.util.Map;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogWriter;
import org.pentaho.di.core.parameters.DuplicateParamException;
import org.pentaho.di.core.parameters.UnknownParamException;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;

import bf.associateconsultant.core.listener.JobListenerImpl;

public class XJob extends XAbstractTransformation {

	public XJob() {
	}

	private JobMeta jobMeta;

	LogWriter log = LogWriter.getInstance();

	/**
	 * @return the jobMeta
	 */
	public JobMeta getJobMeta() {
		return jobMeta;
	}

	/**
	 * @param transMeta
	 *            the transMeta to set
	 */
	public void setJobMeta(JobMeta jobMeta) {
		this.jobMeta = jobMeta;
	}

	public String execute(String jobPath, String jobFileName,
			Map<String, String> parameters) throws KettleException {
		String result = null;
		try {
			jobMeta = loadJob(jobPath + File.separator + jobFileName);
			result = getFormattedData(new Job(null, jobMeta), parameters);
		} catch (Exception e) {
			throw new KettleException(
					"An unexpected error occurred while executing a job", e);
		}
		return result;
	}

	public String execute(XRepository repository, String jobPath,
			String jobName, Map<String, String> parameters) {
		String result = null;
		Job job = null;
		if (jobMeta == null) {
			job = loadJob(repository, jobPath, jobName);
		}
		jobMeta = job.getJobMeta();

		result = getFormattedData(job, parameters);
		repository.getKettleDatabaseRepository().disconnect();
		return result;
	}

	private String getFormattedData(Job job, Map<String, String> parameters) {
		String result = null;
		try {
			getJobEntryData(job, parameters);
			if (outputFormat.equalsIgnoreCase(JSON)) {
				result = toJSON(rowMetaAndData);
			} else if (outputFormat.equalsIgnoreCase(XML)) {
				result = toXML(rowMetaAndData);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	private void getJobEntryData(Job job, Map<String, String> paramMap)
			throws UnknownParamException, KettleException {
		if (paramMap != null) {
			setParameters(job, paramMap);
		}
		JobListenerImpl jobListener = new JobListenerImpl();
		job.addJobListener(jobListener);
		job.run();
		job.waitUntilFinished();
		rowMetaAndData = jobListener.getRowsWritten();
		if (job.getErrors() == 0)
			this.success = true;
		else
			success = false;
	}

	private void setParameters(Job job, Map<String, String> paramMap)
			throws UnknownParamException, DuplicateParamException {
		job.getJobMeta().activateParameters();
		if (paramMap != null)

			for (String key : paramMap.keySet()) {
				String variableValue = paramMap.get(key);
				job.getJobMeta().setParameterValue(key, variableValue);
			}

	}

	public JobMeta loadJob(String jobfilename) throws KettleException {
		JobMeta jobMeta;
		try {
			environnementJobInit();
			jobMeta = new JobMeta(jobfilename, null);
		} catch (KettleException e) {
			throw new RuntimeException(e);
		}
		return jobMeta;
	}

	public Job loadJob(XRepository repository, String directoryName,
			String jobfilename) {
		JobMeta jobMeta;
		Job job = null;
		try {
			environnementJobInit();
			if (repository != null) {
				KettleDatabaseRepository kettleDBRepository = initializeAndConnectToRepository(repository);
				jobMeta = kettleDBRepository.jobDelegate.loadJobMeta(
						jobfilename,
						kettleDBRepository.findDirectory(directoryName));
				job = new Job(kettleDBRepository, jobMeta);
			} else {
				jobMeta = new JobMeta(directoryName + "\\" + jobfilename, null);
				job = new Job(null, jobMeta);
			}

		} catch (KettleException e) {
			throw new RuntimeException(e);
		}
		return job;
	}

	private void environnementJobInit() throws KettleException {

		EnvUtil.environmentInit();
	}

}
