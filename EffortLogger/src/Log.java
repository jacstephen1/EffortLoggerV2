
public class Log {
	private String type;
	
	//Effort
	private String startTime;
	private String endTime;
	private String date;
	private String project;
	private String lifeCycleStep;
	private String effortCategory;
	private String plan;
	private String deliverable;
	private String interuption;
	private String defect;
	
	
	//Defect
	private String defectCategory;
	private String defectName;
	private String defectSymptom;
	private String injectedStep;
	private String removedStep;
	private String fix;
	
	public Log(String t, String p)
	{
		if (t.trim().equalsIgnoreCase("effort") || t.trim().equalsIgnoreCase("defect"))
		{
			type = t;
		}
		else
		{
			type = null;
		}
		
		//ADD CHECK TO DETERMINE IF PROJECT VALID, IF NOT THROW ALERT
		project = p;
		
		//Default
		startTime = null;
		endTime = null;
		date = null;
		lifeCycleStep = null;
		effortCategory = null;
		plan = null;
		deliverable = null;
		interuption = null;
		defect = null;
		defectCategory = null;
		defectName = null;
		defectSymptom = null;
		injectedStep = null;
		removedStep = null;
		fix = null;
	}
	
	public void createEffortLog(String sTime, String eTime, String d, String lcStep, String eCategory, String planType, String deliver, String interupt, String defec)
	{
		startTime = sTime;
		endTime = eTime;
		date = d;
		lifeCycleStep = lcStep;
		effortCategory = eCategory;
		plan = planType;
		deliverable = deliver;
		interuption = interupt;
		defect = defec;
		defectCategory = null;
		defectName = null;
		defectSymptom = null;
		injectedStep = null;
		removedStep = null;
		fix = null;
	}
	
	public void createDefectLog(String defect, String name, String symptom, String inject, String remove, String fixed)
	{
		startTime = null;
		endTime = null;
		date = null;
		lifeCycleStep = null;
		effortCategory = null;
		plan = null;
		deliverable = null;
		interuption = null;
		defect = null;
		defectCategory = defect;
		defectName = name;
		defectSymptom = symptom;
		injectedStep = inject;
		removedStep = remove;
		fix = fixed;
	}
	
	public void saveToFiles(String logType)
	{
		
	}
}
