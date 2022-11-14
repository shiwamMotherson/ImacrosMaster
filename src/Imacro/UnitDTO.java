package Imacro;

public class UnitDTO {
	private String UnitCode;
	private String UnitName;	
	private String ApplicationType;	
	private String ApplicationName;	
	private String ApplicationURL;
	private String ApplicationLoginID;	
	private String ApplicationPassword;	
	private String UnitStatus;	
	private String CreatedBy;	
	private String UnitCreatedOn;	
	private String Category;	
	private String CategoryLandingURL;
//	E01	ET Toyota	Invoice	CTDMS	https://sc2.tkm.co.in/cas/login?service=http%3A%2F%2Fsc2.tkm.co.in%2F&parammessage=MSC20002AINF	1806943	Msaril@2021	1	Mukesh	2021-03-19 00:00:00.000	BP	http://issms.tkm.co.in/tops/do/ssrv060?NAVIGATION=MENU&modulename=srv&formName=fsrv060
	public UnitDTO(String UnitCode, String UnitName, String ApplicationType, String ApplicationName,	
				String ApplicationURL, String ApplicationLoginID, String ApplicationPassword, String UnitStatus, 	
				String CreatedBy, String UnitCreatedOn, String Category,String CategoryLandingURL) {
		this.UnitCode=UnitCode;
		this.UnitName=UnitName;	
		this.ApplicationType=ApplicationType;	
		this.ApplicationName=ApplicationName;	
		this.ApplicationURL=ApplicationURL;
		this.ApplicationLoginID=ApplicationLoginID;	
		this.ApplicationPassword=ApplicationPassword;	
		this.UnitStatus=UnitStatus;	
		this.CreatedBy=CreatedBy;	
		this.UnitCreatedOn=UnitCreatedOn;	
		this.Category=Category;	
		this.CategoryLandingURL=CategoryLandingURL;
	}
	public UnitDTO() {
		this.UnitCode="";
		this.UnitName="";	
		this.ApplicationType="";	
		this.ApplicationName="";	
		this.ApplicationURL="";
		this.ApplicationLoginID="";	
		this.ApplicationPassword="";	
		this.UnitStatus="";	
		this.CreatedBy="";	
		this.UnitCreatedOn="";	
		this.Category="";	
		this.CategoryLandingURL="";
	}
	
	public String getUnitCode() {
		return UnitCode;
	}
	public void setUnitCode(String unitCode) {
		UnitCode = unitCode;
	}
	public String getUnitName() {
		return UnitName;
	}
	public void setUnitName(String unitName) {
		UnitName = unitName;
	}
	public String getApplicationType() {
		return ApplicationType;
	}
	public void setApplicationType(String applicationType) {
		ApplicationType = applicationType;
	}
	public String getApplicationName() {
		return ApplicationName;
	}
	public void setApplicationName(String applicationName) {
		ApplicationName = applicationName;
	}
	public String getApplicationURL() {
		return ApplicationURL;
	}
	public void setApplicationURL(String applicationURL) {
		ApplicationURL = applicationURL;
	}
	public String getApplicationLoginID() {
		return ApplicationLoginID;
	}
	public void setApplicationLoginID(String applicationLoginID) {
		ApplicationLoginID = applicationLoginID;
	}
	public String getApplicationPassword() {
		return ApplicationPassword;
	}
	public void setApplicationPassword(String applicationPassword) {
		ApplicationPassword = applicationPassword;
	}
	public String getUnitStatus() {
		return UnitStatus;
	}
	public void setUnitStatus(String unitStatus) {
		UnitStatus = unitStatus;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	public String getUnitCreatedOn() {
		return UnitCreatedOn;
	}
	public void setUnitCreatedOn(String unitCreatedOn) {
		UnitCreatedOn = unitCreatedOn;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	public String getCategoryLandingURL() {
		return CategoryLandingURL;
	}
	public void setCategoryLandingURL(String categoryLandingURL) {
		CategoryLandingURL = categoryLandingURL;
	}

}
