
public class Log {
	private String type;
	private int id;
	
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
	private String other;
	
	//Defect
	private String defectCategory;
	private String defectName;
	private String defectSymptom;
	private String injectedStep;
	private String removedStep;
	private String fix;
	private int openClose;
	
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
		id = 0;
		startTime = null;
		endTime = null;
		date = null;
		lifeCycleStep = null;
		effortCategory = null;
		plan = null;
		deliverable = null;
		interuption = null;
		defect = null;
		other = null;
		defectCategory = null;
		defectName = null;
		defectSymptom = null;
		injectedStep = null;
		removedStep = null;
		fix = null;
	}
	
	public void createEffortLog(int inid, String sTime, String eTime, String d, String lcStep, String eCategory, String planType, String deliver, String interupt, String defec, String oth)
	{
		id = inid;
		startTime = sTime;
		endTime = eTime;
		date = d;
		lifeCycleStep = lcStep;
		effortCategory = eCategory;
		plan = planType;
		deliverable = deliver;
		interuption = interupt;
		defect = defec;
		other = oth;
		defectCategory = null;
		defectName = null;
		defectSymptom = null;
		injectedStep = null;
		removedStep = null;
		fix = null;
		openClose = 0;
	}
	
	public void createDefectLog(int inId, String defCat, String name, String symptom, String inject, String remove, String fixed, int oc)
	{
		id = inId;
		startTime = null;
		endTime = null;
		date = null;
		lifeCycleStep = null;
		effortCategory = null;
		plan = null;
		deliverable = null;
		interuption = null;
		defect = null;
		other = null;
		defectCategory = defCat;
		defectName = name;
		defectSymptom = symptom;
		injectedStep = inject;
		removedStep = remove;
		fix = fixed;
		openClose = oc;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int inId) {
		id = inId;
	}
	public String getProject() {
		return project; 
	}
	public String getStartTime() {
		return startTime; 
	}
	public String getEndTime() {
		return endTime; 
	}
	public String getDate() {
		return date; 
	}
	public void setDate(String inDate) {
		date = inDate;
	}
	public String getLifeCycleStep() {
		return lifeCycleStep; 
	}
	public String getEffortCategory() {
		return effortCategory; 
	}
	public String getPlan() {
		return plan; 
	}
	public String getDeliverable() {
		return deliverable; 
	}
	public String getInteruption() {
		return interuption; 
	}
	public String getDefect() {
		return defect; 
	}
	public String getOther() {
		return other;
	}
	public String getDefectCategory() {
		return defectCategory;
	}
	public String getDefectName() {
		return defectName;
	}
	public String getDefectSymptom() {
		return defectSymptom;
	}
	public String getDefectInjection() {
		return injectedStep;
	}
	public String getDefectRemoval() {
		return removedStep;
	}
	public String getDefectFix() {
		return fix;
	}
	public int getOpenClose() {
		return openClose;
	}
	
	public void saveToFiles(String logType)
	{
		
	}
}
