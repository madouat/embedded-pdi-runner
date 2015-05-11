package bf.associateconsultant.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.pentaho.di.core.ProgressMonitorListener;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.parameters.UnknownParamException;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;

import bf.associateconsultant.core.listener.StepRowListener;

public class XTransformation extends XAbstractTransformation {

	public XTransformation() {
	}

	private TransMeta transMeta;
	private int nb = 0;

	/**
	 * @return the transMeta
	 */
	public TransMeta getTransMeta() {
		return transMeta;
	}

	/**
	 * @param transMeta
	 *            the transMeta to set
	 */
	public void setTransMeta(TransMeta transMeta) {
		this.transMeta = transMeta;
	}

	public String execute(XRepository repository, String transformationPath,
			String transformationName, Map<String, String> parameters) {
		String result = null;
		try {
			if (transMeta == null)
				transMeta = loadTransformation(repository, transformationPath,
						transformationName);
			result = getFormattedData(getLastEntry().getName(), parameters);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;

	}

	private String getFormattedData(String stepname,
			Map<String, String> parameters) throws KettleException,
			JSONException {
		String result = null;
		getStepData(stepname, new Trans(transMeta), parameters);
		if (outputFormat.equalsIgnoreCase(JSON)) {
			result = toJSON(rowMetaAndData);
		} else if (outputFormat.equalsIgnoreCase(XML)) {
			result = toXML(rowMetaAndData);
		} else
			result = toJSON(rowMetaAndData);
		return result;
	}

	private void setParameters(Trans transfo, Map<String, String> parameters)
			throws UnknownParamException {
		String[] paramList = transfo.listParameters();
		if (parameters != null && paramList.length > 0) {
			for (String param : paramList) {
				String key = contains(parameters, param);
				if (key != null) {
					transfo.setParameterValue(param, parameters.get(key));
				}
			}

		}
	}

	private String contains(Map<String, String> map, String pattern) {
		String key = null;
		Iterator<String> it = map.keySet().iterator();
		boolean find = false;
		while (it.hasNext() && !find) {
			String next = it.next();
			if (pattern.equalsIgnoreCase(next)) {
				find = true;
				key = next;
			}
		}
		return key;
	}

	public StepMeta getFirstEntry() {
		List<StepMeta> steps = transMeta.getSteps();
		boolean find = false;
		StepMeta stepmeta = null;
		Iterator<StepMeta> it = steps.iterator();
		while (it.hasNext() && !find) {
			stepmeta = it.next();
			if (transMeta.findPreviousSteps(stepmeta).size() == 0) {
				find = true;
			}
		}
		return stepmeta;
	}

	public StepMeta getLastEntry() {
		List<StepMeta> steps = transMeta.getSteps();
		List<StepMeta> stepsArray = new ArrayList<StepMeta>();
		// boolean find=false;
		StepMeta stepmeta = null;
		Iterator<StepMeta> it = steps.iterator();
		while (it.hasNext()) {
			stepmeta = it.next();
			if (transMeta.findNextSteps(stepmeta).size() == 0) {
				// find=true;
				stepsArray.add(stepmeta);
			}
		}

		getPrevstepSize(stepsArray.get(0));
		int prevStepSize = nb;
		StepMeta stepmetaCand = stepsArray.get(0);
		for (StepMeta tmpStepmeta : stepsArray) {
			nb = 0;
			getPrevstepSize(tmpStepmeta);
			if (nb > prevStepSize) {
				prevStepSize = nb;// transMeta.findPreviousSteps(tmpStepmeta).size();
				stepmetaCand = tmpStepmeta;
			}

		}

		return stepmetaCand;
	}

	private void getPrevstepSize(StepMeta s) {
		if (s != null) {
			nb = nb + 1;
			StepMeta[] prevSteps = transMeta.getPrevSteps(s);
			getPrevstepSize((prevSteps != null && prevSteps.length > 0) ? prevSteps[0]
					: null);
		}
	}

	private void getStepData(String stepname, Trans trans,
			Map<String, String> paramMap) throws UnknownParamException,
			KettleException {
		if (paramMap != null) {
			setParameters(trans, paramMap);
		}
		trans.prepareExecution(null);
		StepInterface stepinterface = trans.getStepInterface(stepname, 0);
		StepRowListener rowListener = new StepRowListener();
		stepinterface.addRowListener(rowListener);
		trans.startThreads();
		trans.waitUntilFinished();
		success = (trans.getErrors() == 0) ? true : false;
		rowMetaAndData = rowListener.getRowsWritten();
	}

	private TransMeta loadTransformation(XRepository repository,
			String directoryName, String transName) {
		TransMeta transMeta;
		try {
			environnementInit();
			if (repository != null) {
				KettleDatabaseRepository kettleDBRepository = initializeAndConnectToRepository(repository);
				boolean setInternalVariables = false;
				ProgressMonitorListener monitor = null;
				transMeta = kettleDBRepository.transDelegate
						.loadTransformation(
								new TransMeta(),
								transName,
								kettleDBRepository.findDirectory(directoryName),
								monitor, setInternalVariables);
			} else
				transMeta = new TransMeta(directoryName + File.separator
						+ transName);
		} catch (KettleException e) {
			transMeta = null;
		}
		return transMeta;
	}

}
