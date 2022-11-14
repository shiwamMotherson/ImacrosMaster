package Imacro;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import com.jacob.activeX.ActiveXComponent;
import com.opencsv.CSVWriter;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.Insurance__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.sobject.Service_Job_Order__c;
import com.sforce.soap.enterprise.sobject.Upload_Details__c;
import com.sforce.ws.ConnectionException;

import iMacroSfReadUpload.ConnectionUtility;
import iMacroSfReadUpload.DestinationConnectionUtility;


public class CSReportDwnRead {
	boolean isLoginFlag =false;
	ActiveXComponent iimCSReportDwnRead = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	Double Suc=0.0;
	Double Size=0.0;
	public static void main(String[] args) {
		CSReportDwnRead re= new CSReportDwnRead();
		Instant now = Instant.now();
		Instant yesterday = now.minus(1, ChronoUnit.DAYS);
		System.out.println("Total Start time"+Instant.now().toString());
		
		re.unitAttempt=0;
		re.unitDownloader(yesterday.toString());
		re.unitDownloader(yesterday.toString());
		re.unitDownloader(yesterday.toString());
		re.unitDownloader(yesterday.toString());
		re.unitDownloader(yesterday.toString());
		
		
		System.out.println("Total End time"+Instant.now().toString());
	    
}

	public void unitDownloader(String yesterday){

		DBTransactions dbt = new DBTransactions();
		if(unitAttempt<10) {
			unitAttempt++;
			System.out.println("unit Attempt = "+unitAttempt);
			//E01 
			UnitDTO ut= dbt.getUnitConfig("E03", "GS", "Invoice");
			ToyotaRODownload(yesterday, ut);
		    
			//E02
			ut= dbt.getUnitConfig("E02", "GS", "Invoice");
			ToyotaRODownload(yesterday, ut);
			
			//E03
			ut= dbt.getUnitConfig("E01", "GS", "Invoice");
			ToyotaRODownload(yesterday, ut);
		}
		
	}
	
	public void deleteFolder(File file){
		for (File subFile : file.listFiles()) {
			if(subFile.isDirectory()) {
				deleteFolder(subFile);
			} 
			else {
				subFile.delete();
			}
	     }
		file.delete();
	}
	
public void ToyotaRODownload(String yesterday, UnitDTO ut){
	String yesterday1=yesterday.substring(0,10);
	Instant now = Instant.now();
	System.out.println("Start1:"+now.toString());      
	String loc1 = "D:\\ROReports\\"+imformat(yesterday1, 3)+"\\"+ut.getUnitCode()+"\\GS\\";
	String loc2 = "D:\\ROReports\\"+imformat(yesterday1, 3)+"\\"+ut.getUnitCode()+"\\BP\\";
	String fn1=ut.getUnitCode()+"CrystalViewer.xls";
	runDownload(ut,ut.getApplicationLoginID(), ut.getApplicationPassword(),imformat(yesterday1, 3), loc1, fn1, loc2, fn1, yesterday);
	
}	


public String imformat(String date, int f) {
	String d="";
	if(date.length()>=9) {
		if(f==1) {
			d=date.substring(8, 10)+"-"+date.substring(5, 7)+"-"+date.substring(0, 4);
		}
		else if(f==2) {
			d=date.substring(8, 10)+"/"+date.substring(5, 7)+"/"+date.substring(0, 4);
		}
		else if(f==3) {
			d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
		}
	}
	if(date.length()>8 && f==4) {
		d=date.substring(5, 7)+""+date.substring(0, 4);
	}
	else if(date.length()>8 && f==5) {
		d=""+date.substring(0, 4)+date.substring(5, 7);
	}
    
	return d;
	}

/**
 * Method to check if file has been successfully downloaded
 */	
	public boolean checkStatus( String location) {
		System.out.println("called 1");
		boolean status= false;
		Path path = Paths.get(location);
		DBTransactions dbt2 = new DBTransactions();
		if (Files.exists(path)) {
			File file = new File(location);
			System.out.println(file.length());
			if(file.length()>102) {
			System.out.println("called 2 "+location);
			status=true;
			}			
		}
		System.out.println(status);
		return status;
	}

	/**
	 * Method to download CS Report
	 */	
	public  void runDownload(UnitDTO ut, String Username, String Pwd, String d1, String folderLoc1, String f11, String folderLoc2,  String f21, String yesterday)
	{
		Instant now;
		File f;
		File f1 = new File(folderLoc1+"\\"+f11);
		File f2 = new File(folderLoc2+"\\"+f21);
		if(!f1.exists() || !f2.exists()) {

			writeDataLineByLine( "D:\\MidasCS.csv",  Username, Pwd, d1, folderLoc1 , f11, folderLoc2 , f21);
			try {
				String[] command = {"cmd.exe", "/C", "Start", "D:\\batMidasCS.bat"};
				Process p = Runtime.getRuntime().exec(command);
			} 
			catch (Exception ex) {
				System.out.println(ex);
			}
		    try {
		    Thread.sleep(80000);
		    } catch (InterruptedException ie) {
		    Thread.currentThread().interrupt();
		    }
		}
		if(!f1.exists() || !f2.exists()) {
		    try {
		    Thread.sleep(20000);
		    } catch (InterruptedException ie) {
		    Thread.currentThread().interrupt();
		    }
		}
		if(!f1.exists() || !f2.exists()) {
		    try {
		    Thread.sleep(20000);
		    } catch (InterruptedException ie) {
		    Thread.currentThread().interrupt();
		    }
		}
		if(f1.exists()) {
			now = Instant.now();
			System.out.println("Start 03:"+now.toString());    
			Suc=0.0;
			Size=0.0;
			setService_Job_Order__c(ROXLSSheetRead1(folderLoc1+"\\"+f11),3,ut);
			insertUploadDetails(ut.getUnitName(), "GS GatePass");	
			f = new File(folderLoc1+"\\"+f11);
			f.delete();
		}
		else {
			Suc=0.0;
			Size=0.0; 
			insertUploadDetails(ut.getUnitName(), "GS GatePass download Failed");	
		}
		if(f2.exists()) {
			now = Instant.now();
			System.out.println("Start 06:"+now.toString());  
			Suc=0.0;
			Size=0.0; 
			setService_Job_Order__c(ROXLSSheetRead1(folderLoc2+"\\"+f21),3,ut);
			insertUploadDetails(ut.getUnitName(),"BP GatePass");	
			f = new File(folderLoc2+"\\"+f21);
			f.delete();
		}
		else {
			Suc=0.0;
			Size=0.0; 
			insertUploadDetails(ut.getUnitName(), "BP GatePass download Failed");			
		}
		unitAttempt++;
	}

	/**
	 * Method to Write csv file
	 */	
	public  void writeDataLineByLine(String filePath, String Username, 
			String Pwd, String d1, String folderLoc1, String f11, String folderLoc2, String f21)
	{
	    File file = new File(filePath);
	    try {
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
	        String[] data1 = { Username, Pwd, d1, folderLoc1, f11, folderLoc2, f21};
	        writer.writeNext(data1);
	        writer.close();
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	
	}
	/**
	 * Method to download CS Report
	 */	
	public  void runDownload1(UnitDTO ut, String Username, String Pwd, String d1, String folderLoc1, String fileName1,String folderLoc2, String fileName2, String yesterday)
	{
		Instant now = Instant.now();
		System.out.println("Start 01:"+now.toString());    
		File f = new File(folderLoc1+fileName1);
		iimCSReportDwnRead = new ActiveXComponent("imacros");
		iimCSReportDwnRead.invoke("iimInit");
		System.out.println("Calling iimPlay");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB CLOSEALLOTHERS");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:'SET !PLAYBACKDELAY 1.73");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:URL GOTO=https://sc2.tkm.co.in/cas/login?service=http%3A%2F%2Fsc2.tkm.co.in%2F&parammessage=MSC20002AINF");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT="+Username);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:SET !ENCRYPTION NO");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT="+Pwd);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=VALUE:Login");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=11");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:SET !ERRORIGNORE YES");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV");
		 
		//GS
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:URL GOTO=http://issms.tkm.co.in/tops/do/ssrv089?NAVIGATION=MENU&modulename=srv&formName=fsrv089&reportId=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		if(!urlCheck().equalsIgnoreCase("http://issms.tkm.co.in/tops/do/ssrv089?NAVIGATION=MENU&modulename=srv&formName=fsrv089&reportId=2")) {
			Suc=0.0;
			Size=0.0; 
			insertUploadDetails(ut.getUnitName(), "GS GatePass website access Failed");		
			unitDownloader(yesterday);
		}
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=4 TYPE=INPUT:RADIO ATTR=NAME:radioDate");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:workshopType2 CONTENT=%GS");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:forDate2 CONTENT="+d1);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=4 TYPE=INPUT:RADIO ATTR=NAME:radioDate");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER="+folderLoc1+" FILE="+fileName1+" WAIT=YES");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_print.gif");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=3");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
		if(!f.exists()) {
			iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
		}
		if(!f.exists()) {
			iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
			if(!f.exists()) {
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=2");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER="+folderLoc1+" FILE="+fileName1+" WAIT=YES");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_print.gif");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=3");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=20");				
			}
		}
		now = Instant.now();
		System.out.println("Start 02:"+now.toString());   
		if(f.exists()) {
			now = Instant.now();
			System.out.println("Start 03:"+now.toString());    
			Suc=0.0;
			Size=0.0;
			setService_Job_Order__c(ROXLSSheetRead1(folderLoc1+fileName1),1,ut);
			insertUploadDetails(ut.getUnitName(), "GS GatePass");	
			f = new File(folderLoc1);
			deleteFolder(f);
		}
		else {
			Suc=0.0;
			Size=0.0; 
			insertUploadDetails(ut.getUnitName(), "GS GatePass download Failed");			
		}
		now = Instant.now();
		System.out.println("Start 04:"+now.toString());   
		
		iimCSReportDwnRead.invoke("iimExit");		
		iimCSReportDwnRead = new ActiveXComponent("imacros");
		iimCSReportDwnRead.invoke("iimInit");
		System.out.println("Calling iimPlay");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB CLOSEALLOTHERS");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:'SET !PLAYBACKDELAY 1.73");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:URL GOTO=https://sc2.tkm.co.in/cas/login?service=http%3A%2F%2Fsc2.tkm.co.in%2F&parammessage=MSC20002AINF");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT="+Username);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:SET !ENCRYPTION NO");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT="+Pwd);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=VALUE:Login");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:SET !ERRORIGNORE YES");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=11");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV");
		//BP
		f = new File(folderLoc2+fileName2);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:URL GOTO=http://issms.tkm.co.in/tops/do/ssrv089?NAVIGATION=MENU&modulename=srv&formName=fsrv089&reportId=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		if(!urlCheck().equalsIgnoreCase("http://issms.tkm.co.in/tops/do/ssrv089?NAVIGATION=MENU&modulename=srv&formName=fsrv089&reportId=2")) {
			Suc=0.0;
			Size=0.0; 
			insertUploadDetails(ut.getUnitName(), "BP GatePass website access Failed");		
			unitDownloader(yesterday);
		}
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=4 TYPE=INPUT:RADIO ATTR=NAME:radioDate");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:workshopType2 CONTENT=%BP");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:forDate2 CONTENT="+d1);
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=4 TYPE=INPUT:RADIO ATTR=NAME:radioDate");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER="+folderLoc2+" FILE="+fileName2+" WAIT=YES");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_print.gif");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=3");
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
		if(!f.exists()) {
			iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
		}
		if(!f.exists()) {
			iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=10");
			if(!f.exists()) {
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=2");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER="+folderLoc2+" FILE="+fileName2+" WAIT=YES");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_print.gif");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:TAB T=3");
				iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=20");				
			}
		}
		now = Instant.now();
		System.out.println("Start 05:"+now.toString());   
		if(f.exists()) {
			now = Instant.now();
			System.out.println("Start 06:"+now.toString());  
			Suc=0.0;
			Size=0.0; 
			setService_Job_Order__c(ROXLSSheetRead1(folderLoc2+fileName1),1,ut);
			insertUploadDetails(ut.getUnitName(),"BP GatePass");		
			f = new File(folderLoc2);
			deleteFolder(f);	
		}
		else {
			Suc=0.0;
			Size=0.0; 
			insertUploadDetails(ut.getUnitName(), "BP GatePass download Failed");			
		}
		now = Instant.now();
		System.out.println("Start 07:"+now.toString());
		iimCSReportDwnRead.invoke("iimExit");
		unitAttempt++;
	}
	
	/**
	 * Method to Return cell value
	 */	
	public String cellReturn(Cell cell) {
		String s="";
		try {
		switch (cell.getCellType()) {
	    case NUMERIC:
	       s = ""+cell.getNumericCellValue();
	       break;
	    
	    case STRING:
	       s = cell.getStringCellValue();
	       break;
		    
		case BLANK:
		       s = "";
		       break;
	       
	    default:
	    	   break;
	 }
		}catch(Exception e) {
			//e.printStackTrace();
			}
		
		return s;		
	}
	/**
	 * Method to return the current Tab url for iMacros
	 */	
	public String urlCheck() {
		String s="";
		iimCSReportDwnRead.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		iimCSReportDwnRead.invoke("iimPlay", "CODE: ADD !EXTRACT {{!URLCURRENT}}");
		s=iimCSReportDwnRead.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
		return s;
	}
		
	/**
	 * Method to read data from RO sheet
	 */	
	public List <ROReportDTO> ROXLSSheetRead1(String location) {
		List <ROReportDTO> ROl=new ArrayList<ROReportDTO>();		
		 try {
			FileInputStream fis = new FileInputStream(new File(location));
			Path path = Paths.get(location);
			if (Files.exists(path)) {}
			else {
				  //Log file not existing exception
				  System.out.println("File not exist");
			  }
		    HSSFWorkbook workbook = new HSSFWorkbook(fis);
		    HSSFSheet spreadsheet = workbook.getSheetAt(0);
			Iterator < Row >  rowIterator = spreadsheet.iterator();
			ROReportDTO RO = null;
		    HSSFRow row;
		    String p="";
		    while (rowIterator.hasNext()) {
		    	RO = new ROReportDTO();
		    	row = (HSSFRow) rowIterator.next();
		    	p="";
		    	try {
		    	if(row.getRowNum()>0) {
		    		RO.setJob_Order_No__c(""+cellReturn(row.getCell(1)));
		    		RO.setPart_Supply_Doc_No__c(""+cellReturn(row.getCell(23)));
		    		//System.out.println(RO.getPart_Supply_Doc_No__c());
		    		if(RO.getJob_Order_No__c().length()>5) {
		    			try {
		    			//Open Job Date	
						 RO.setOpen_Job_Date__c(""+cellReturn(row.getCell(0)), 1);
						 //System.out.println(cellReturn(row.getCell(0)));
						 //Job Ord. No.	
						 RO.setJob_Order_No__c(""+cellReturn(row.getCell(1)));
						 //Reg No.	
						 RO.setVehicle_Registration_No__c(""+cellReturn(row.getCell(2)));
						 //Vin No.	
						 RO.setVIN_No__c(""+cellReturn(row.getCell(3)));
						 //Parts Cost 	
						 RO.setParts_Cost__c(""+cellReturn(row.getCell(4)));
						 //Parts Sale	
						 RO.setParts_Sale__c(""+cellReturn(row.getCell(5)));
						 //Part Tax	
						 RO.setParts_Tax__c(""+cellReturn(row.getCell(6)));
						 //Oil Cost	
						 RO.setOil_Cost__c(""+cellReturn(row.getCell(7)));
						 //Oil Sale	
						 RO.setOil_Sale__c(""+cellReturn(row.getCell(8)));
						 //Oil Tax	
						 RO.setOil_Tax__c(""+cellReturn(row.getCell(9)));
						 //Material Cost	
						 RO.setMaterial_Cost__c(""+cellReturn(row.getCell(10)));
						 //Labour Cost	
						 RO.setLabour_Cost__c(""+cellReturn(row.getCell(11)));
						 //Labour Sale
						 RO.setLabour_Sale__c(""+cellReturn(row.getCell(12)));
						 //Pnt Mat Cost	
						 RO.setPnt_Mat_Cost__c(""+cellReturn(row.getCell(13)));
						 //Pnt Mat Sale	
						 RO.setPnt_Mat_Sale__c(""+cellReturn(row.getCell(14)));
						 //Outside Cost	
						 RO.setOutside_Cost__c(""+cellReturn(row.getCell(15)));
						 //Labour Tax	
						 RO.setLabour_Tax__c(""+cellReturn(row.getCell(16)));
						 //Outside Sale	
						 RO.setOutside_Sale__c(""+cellReturn(row.getCell(17)));
						 //Out Labour Tax	
						 RO.setOutside_Labour_Tax__c(""+cellReturn(row.getCell(18)));
						 //Total Cost	
						 RO.setTotal_Cost__c(""+cellReturn(row.getCell(19)));
						 //Total Sale	
						 RO.setTotal_Sale__c(""+cellReturn(row.getCell(20)));
						 //Invoice Doc No.	
						 RO.setInvoice_No__c(""+cellReturn(row.getCell(21)));
						 //Invoice Doc Date	
						 RO.setInvoice_Date__c(""+cellReturn(row.getCell(22)), 1);
						 //Part Supply Doc  Date	
						 if(cellReturn(row.getCell(24)).length()>9) {
							 //System.out.println("outofbounds"+cellReturn(row.getCell(24)));
							 RO.setPart_Supply_Doc_Date__c(""+cellReturn(row.getCell(24)), 1);
						 }
						 //Open SA Name	
						 RO.setOpen_SA_Name__c(""+cellReturn(row.getCell(28)));
						 //Edit SA Name	
						 RO.setEdit_SA_Name__c(""+cellReturn(row.getCell(29)));
						 //Close SA Name	
						 RO.setClose_SA_Name__c(""+cellReturn(row.getCell(30)));
						 //Brand(Toyota/Grey)	
						 RO.setMake_Brand__c(""+cellReturn(row.getCell(31)));
						 //Cess Lbr_tax	
						 RO.setCess_Labour_Tax__c(""+cellReturn(row.getCell(32)));
						 //Cess part_tax	
						 RO.setCess_Part_Tax__c(""+cellReturn(row.getCell(33)));
						 if(RO.getTotal_Cost__c()>0.0) {
							 ROl.add(RO);	
							 //System.out.println(RO.getOpen_Job_Date__c());
						 }
		    		} catch (Exception e) {e.printStackTrace();} 
		    		}
		    		}
		    	} catch (Exception e) {e.printStackTrace();} 
		    }
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		return ROl;
	}
	
	/**
	 * Method to read data from RO sheet
	 */	
	public List <ROReportDTO> ROCSVSheetRead1(String location) {
		List <ROReportDTO> ROl=new ArrayList<ROReportDTO>();
		List <String>JobOrderList =new ArrayList<String>();
		int unzip=0;
		Path path11 = Paths.get(location.replace(".zip", ""));
		if (Files.exists(path11)) {
			unzip=1;
			location=location.replace(".zip", "");
		}
		if(location.contains(".zip") && unzip==0) {
		String locinsystem=location;
	    
	    String zipFilePath = locinsystem;
	    
	    String destDir =location.replace(".zip", "");
	    File dir = new File(destDir);
	    // create output directory if it doesn't exist
	    if(!dir.exists()) dir.mkdirs();
	    FileInputStream fis;
	    //buffer for read and write data to file
	    byte[] buffer = new byte[1024];
	    try {
	        fis = new FileInputStream(zipFilePath);
	        ZipInputStream zis = new ZipInputStream(fis);
	        ZipEntry ze = zis.getNextEntry();
	        while(ze != null){
	            String fileName = ze.getName();
	            File newFile = new File(destDir + File.separator + fileName);
	            //System.out.println("Unzipping to "+newFile.getAbsolutePath());
	            //create directories for sub directories in zip
	            new File(newFile.getParent()).mkdirs();
	            FileOutputStream fos = new FileOutputStream(newFile);
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	            fos.write(buffer, 0, len);
	            }
	            fos.close();
	            //close this ZipEntry
	            zis.closeEntry();
	            ze = zis.getNextEntry();
	        }
	        //close last ZipEntry
	        zis.closeEntry();
	        zis.close();
	        fis.close();
	        location=destDir;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		}
		 
        //System.out.println(location);

        List<String> files = new ArrayList<String>();
        List<String> valid = new ArrayList<String>();
        List<String> names = new ArrayList<String>();
		try{
	        //System.out.println("Finding file name");
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        String adress = location;
	        String exten = ".csv";
	
	        File directory = new File(adress);
	
	        File[] f = directory.listFiles();     
	        //System.out.println(f.toString());
	
	        for(int i=0; i < f.length; i++){
	            if(f[i].isFile()){
	                files.add(f[i].getName());
	            }   
		        //System.out.println(f[i].getName());
	        }
	
	        for(int i=0; i < files.size(); i++){
	            if (files.get(i).endsWith(exten)){
	                valid.add(files.get(i));
	            }
	        }
	
	        for(int i=0; i < valid.size(); i++){
	            int pos = valid.get(i).lastIndexOf(".");
	                names.add(valid.get(i).substring(0, pos));
	                //System.out.println(names.get(i));
	        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		 try {
			 if(names.size()>0) {
			 location=location+"\\"+names.get(0)+".csv";
			 }
			FileInputStream fis = new FileInputStream(new File(location));
			Path path = Paths.get(location);
			if (Files.exists(path)) {}
			else {
				  //Log file not existing exception
				  System.out.println("File not exist");
			  }
			ROReportDTO RO = null;
		    String p="";
			String line = "";  
			String splitBy = ",";  
			String[] csvLine = null;
		    BufferedReader br = new BufferedReader(new FileReader(location)); 
		    int lineNo=0;
				 while ((line = br.readLine()) != null)   //returns a Boolean value  
				 {  
					 lineNo++;
				 csvLine = line.replaceAll("\"","").split(splitBy);    // use comma as separator 
				 if(csvLine.length>=12) {
					 if(lineNo>1)  {
						 if(csvLine[0].length()>0) {
							 //System.out.println(lineNo+" "+csvLine[2]+" "+csvLine[csvLine.length-1]+" "+csvLine[7]);  
							 RO = new ROReportDTO();
							 //bill date
							 RO.setPart_Supply_Doc_No__c(csvLine[csvLine.length-1]);
							 RO.setJob_Order_No__c(csvLine[2]);
							// System.out.println(csvLine[csvLine.length-1]);
							 if(JobOrderList.contains(RO.getJob_Order_No__c())) {
								 for(ROReportDTO RO1: ROl) {
									 if(RO1.getJob_Order_No__c().equals(RO.getJob_Order_No__c())) {
										 //System.out.println("Repeat called"+csvLine[csvLine.length-1]);
										//Frnch cd	
										 RO1.setFrnch_Cd__c(RO1.getFrnch_Cd__c()+"; "+csvLine[5]);
										 //Part No	
										 RO1.setPart_No__c(RO1.getPart_No__c()+"; "+csvLine[6]);
										 //Part Name
										 for(int pnl=7; pnl<=csvLine.length-5;pnl++) {
											 p=p+","+csvLine[pnl];
										 }
										 RO1.setPart_Name__c(RO1.getPart_Name__c()+"; "+p);
										 //Part Issue Qty	
										 RO1.setPart_Issue_Qty__c(RO1.getPart_Issue_Qty__c()+"; "+csvLine[csvLine.length-4]);
										 //Dlr Unit Part cost	
										 RO1.setDlr_Unit_Part_cost__c(RO1.getDlr_Unit_Part_cost__c(),csvLine[csvLine.length-3]);
										 //Dlr Unit Part cost Str
										 RO1.setDlr_Unit_Part_cost_Str__c(RO1.getDlr_Unit_Part_cost_Str__c()+"; "+csvLine[csvLine.length-3]);
										 //Total Cost	
										 RO1.setTotal_Cost__c(RO1.getTotal_Cost__c(),csvLine[csvLine.length-2]);
									 }
								 }
							 }
							 else {
							 JobOrderList.add(RO.getJob_Order_No__c());							 
							 //Joborderno
							 RO.setJob_Order_No__c(csvLine[2]);
							 //Service In Date	
							 RO.setVehicle_In_Date__c(csvLine[3],1);
							 //Vehicle Reg No	
							 RO.setVehicle_Registration_No__c(csvLine[4]);
							 //Frnch cd	
							 RO.setFrnch_Cd__c(csvLine[5]);
							 //Part No	
							 RO.setPart_No__c(csvLine[6]);
							 //Part Name
							 for(int pnl=7; pnl<=csvLine.length-5;pnl++) {
								 p=p+","+csvLine[pnl];
							 }
							 RO.setPart_Name__c(p);
							 //Part Issue Qty	
							 RO.setPart_Issue_Qty__c(csvLine[csvLine.length-4]);
							 //Dlr Unit Part cost	
							 RO.setDlr_Unit_Part_cost__c(csvLine[csvLine.length-3]);
							 //Dlr Unit Part cost Str
							 RO.setDlr_Unit_Part_cost_Str__c(csvLine[csvLine.length-3]);
							 //Total Cost	
							 RO.setTotal_Cost__c(csvLine[csvLine.length-2]);
							 ROl.add(RO);
							 }
						 }
					 }			 
				 }
				 }
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(ROl.size());
			for(ROReportDTO RO11:ROl) {
				//System.out.println(RO11.getPart_Supply_Doc_No__c()+" "+RO11.getDlr_Unit_Part_cost_Str__c()+" "+RO11.getDlr_Unit_Part_cost__c());
			}
		return ROl;
	}
	
	 /**
		 * Method to set Service_Job_Order__c data
		 */	
	 public void setService_Job_Order__c(List<ROReportDTO> ROl, int ROType, UnitDTO ut) {
		 EnterpriseConnection serviceConn1 =null;
		 try {
			 serviceConn1 = DestinationConnectionUtility.getConnection();
			 if(serviceConn1==null) {
				 return;
			 }
		 }catch(Exception e) {
			 e.printStackTrace();
		  }
		 try {
			 //Map<String, String> IdMap = getMidasFlowLogIdList();
			 Service_Job_Order__c Sj1 = new Service_Job_Order__c();
			 Service_Job_Order__c Sj2 = new Service_Job_Order__c();
			 List<Service_Job_Order__c> Sjll = new ArrayList<Service_Job_Order__c>();
			 List<Service_Job_Order__c> Sjl2 = new ArrayList<Service_Job_Order__c>();
			 DBTransactions dbt = new DBTransactions();
			 Map<String, String > s = getServiceJobOrderIdList();
			 Size=Double.parseDouble(""+ROl.size());
			 Suc=0.0;
			 for(ROReportDTO RO: ROl) {
				 Sj1 = new Service_Job_Order__c();
				 //Bill Date Also called Part_Supply_Doc_No__c Used for ID
				 Sj1.setName(RO.getJob_Order_No__c().trim());
				 Sj1.setSite__c(ut.getUnitName());
				 
				 if(ROType==2) {
					 //Used for WIP reports
					 //Branch code	
					 Sj1.setVIN_No__c(RO.getVIN_No__c());
					 //Joborderno
					 //Sj1.setPart_Supply_Doc_No__c(RO.getPart_Supply_Doc_No__c());
					 Sj1.setJob_Order_No__c(RO.getPart_Supply_Doc_No__c());
					 //Service In Date	
					 Sj1.setVehicle_In_Date__c(RO.getVehicle_In_Date__c());
					 //Vehicle Reg No	
					 Sj1.setVehicle_Registration_No__c(RO.getVehicle_Registration_No__c());
					 //Frnch cd	
					 Sj1.setFrnch_Cd__c(RO.getFrnch_Cd__c());
					 //Part No	
					 Sj1.setPart_No__c(RO.getPart_No__c());
					 //Part Name
					 Sj1.setPart_Name1__c(RO.getPart_Name__c());
					 //Part Issue Qty	
					 Sj1.setPart_Issue_Qty__c(RO.getPart_Issue_Qty__c());
					 //Dlr Unit Part cost	
					 Sj1.setDlr_Unit_Part_cost__c(RO.getDlr_Unit_Part_cost__c());
					 //Dlr Unit Part cost Str
					 Sj1.setDlr_Unit_Part_cost_Str__c(RO.getDlr_Unit_Part_cost_Str__c());;
					 //Total Cost	
					 Sj1.setTotal_Cost__c(RO.getTotal_Cost__c());
					 Sj1.setStatus__c("Pending");
				 }
				 else if(ROType==1) {
					 //Used for cost and sale report
					 Sj1.setStatus__c("Invoiced");
					 //Open Job Date	
					 //System.out.println(RO.getOpen_Job_Date__c());
					 Sj1.setOpen_Job_Date__c(RO.getOpen_Job_Date__c());
					 //Job Ord. No.	
					 //Sj1.setPart_Supply_Doc_No__c(RO.getPart_Supply_Doc_No__c());
					 Sj1.setJob_Order_No__c(RO.getPart_Supply_Doc_No__c());
					 //Sj1.setpart
					 //Reg No.	
					 Sj1.setVehicle_Registration_No__c(RO.getVehicle_Registration_No__c());
					 //Vin No.	
					 Sj1.setVIN_No__c(RO.getVIN_No__c());
					 //Parts Cost 	
					 Sj1.setParts_Cost__c(RO.getParts_Cost__c());
					 //Parts Sale	
					 Sj1.setParts_Sale__c(RO.getParts_Sale__c());
					 //Part Tax	
					 Sj1.setParts_Tax__c(RO.getParts_Tax__c());
					 //Oil Cost	
					 Sj1.setOil_Cost__c(RO.getOil_Cost__c());
					 //Oil Sale	
					 Sj1.setOil_Sale__c(RO.getOil_Sale__c());
					 //Oil Tax	
					 Sj1.setOil_Tax__c(RO.getOil_Tax__c());
					 //Material Cost	
					 Sj1.setMaterial_Cost__c(RO.getMaterial_Cost__c());
					 //Labour Cost	
					 Sj1.setLabour_Cost__c(RO.getLabour_Cost__c());
					 //Labour Sale
					 Sj1.setLabour_Sale__c(RO.getLabour_Sale__c());
					 //Pnt Mat Cost	
					 Sj1.setPnt_Mat_Cost__c(RO.getPnt_Mat_Cost__c());
					 //Pnt Mat Sale	
					 Sj1.setPnt_Mat_Sale__c(RO.getPnt_Mat_Sale__c());
					 //Outside Cost	
					 Sj1.setOutside_Cost__c(RO.getOutside_Cost__c());
					 //Labour Tax	
					 Sj1.setLabour_Tax__c(RO.getLabour_Tax__c());
					 //Outside Sale	
					 Sj1.setOutside_Sale__c(RO.getOutside_Sale__c());
					 //Out Labour Tax	
					 Sj1.setOutside_Labour_Tax__c(RO.getOutside_Labour_Tax__c());
					 //Total Cost	
					 Sj1.setTotal_Cost__c(RO.getTotal_Cost__c());
					 //Total Sale	
					 Sj1.setTotal_Sale__c(RO.getTotal_Sale__c());
					 //Invoice Doc No.	
					 Sj1.setInvoice_No__c(RO.getInvoice_No__c());
					 //Invoice Doc Date	
					 Sj1.setInvoice_Date__c(RO.getInvoice_Date__c());
					 //Part Supply Doc  Date	
					 Sj1.setPart_Supply_Doc_Date__c(RO.getPart_Supply_Doc_Date__c());
					 //Open SA Name	
					 Sj1.setOpen_SA_Name__c(RO.getOpen_SA_Name__c());
					 //Edit SA Name	
					 Sj1.setEdit_SA_Name__c(RO.getEdit_SA_Name__c());
					 //Close SA Name	
					 Sj1.setClose_SA_Name__c(RO.getClose_SA_Name__c());
					 //Brand(Toyota/Grey)	
					 Sj1.setMake_Brand__c(RO.getMake_Brand__c());
					 //Cess Lbr_tax	
					 Sj1.setCess_Labour_Tax__c(RO.getCess_Labour_Tax__c());
					 //Cess part_tax	
					 Sj1.setCess_Part_Tax__c(RO.getCess_Part_Tax__c());
					 Sj1.setSource__c("Auto");

				 }
				 
				 if(Sj1.getName().length()>1 ) {
					 if(s.containsKey(Sj1.getName()+Sj1.getSite__c())) {
						 Sj1.setId(s.get(Sj1.getName()+Sj1.getSite__c()));
						 Sjll.add(Sj1);
					 }
					 else if(s.containsKey(Sj1.getName()+Sj1.getSite__c()+"Inv")) {
					 }
					 else {
						 Sjll.add(Sj1);
					 }
				 //Mfl1.add(Mf);
				 }
			 }
			 //System.out.println(MflIdList);
			 //System.out.println("eL size = "+RO.size()+"Mfl length = "+Mfl.size());
			 UpsertResult[] sr = null;
			 String SeqId="";
			 int IcSize = Sjll.size();
				System.out.println("\n\n IcSize = "+IcSize+"\n\n");  
				//&& !(MflIdList.contains(Mf.getId()))
				if(IcSize<1) {}
				else {
				SeqId="";
				int chunkSize =1;
				int noOfChunk=(int) IcSize/chunkSize;
				int lastChunkSize=IcSize%chunkSize;
				List<Service_Job_Order__c> Ic2 = null;
				List<Service_Job_Order__c> Ic21 = null;
				for(int i1 =0; i1<noOfChunk; i1++) {
					//System.out.println("i1="+i1);
					Ic2 = null;
					Ic21 = null;
					if(i1<noOfChunk) {
						Ic2 = Sjll.subList(i1*chunkSize, (i1+1)*chunkSize);
						Ic21 = Sjll.subList(i1*chunkSize, (i1+1)*chunkSize);
					}
					else {
						if(lastChunkSize>0) {
							Ic2 = Sjll.subList(i1*chunkSize, ((i1)*chunkSize)+lastChunkSize);
							Ic21 = Sjll.subList(i1*chunkSize, ((i1)*chunkSize)+lastChunkSize);
						}
					}
					//SeqId=""+Ic21.get(0).getSERInt_SeqID__c();
					//System.out.println("SId="+SeqId);
				if(Ic2!=null) {
			 
			 
			 try {
				SObject[] upserts = (Ic2).stream().toArray(Service_Job_Order__c[]::new);
				sr = serviceConn1.upsert("id", upserts);
			 }catch (ConnectionException e) {
					e.printStackTrace();
			    	System.out.println(e.getMessage().toString().replaceAll("\n", ""));
				}
			 
				for (int i = 0; i < sr.length; i++) {
					if (sr[i].isSuccess()) {
						//dbt.updateExceptionLogSFSync(""+Ic21.get(i).getSERInt_SeqID__c());
						System.out.println("Succesfully inserted RO Report log"+sr[i].getId()+" "+Ic21.get(i).getName());
						//result="Success";
						Suc++;
					} 
					else {
						System.out.println("Failed to insert RO Report log"+sr[i].getId()+" "+Ic21.get(i).getName());
				    	   
					}
				}
				}
				}
				}
		 }
		 catch(Exception e) {
					e.printStackTrace();
				}
		 }
	 
		/**
		 * Method to get User Id and Chassis No from Salesforce
		 */	
		public Map<String, String>  getServiceJobOrderIdList()
		{
			//Country, Region, Unit, Aggregate, Vehicle Segment, Sales plan-current stage, Sales type
			Map<String, String>  list= new HashMap<String, String>();
			   EnterpriseConnection serviceConn1 =null;
			   try {
				serviceConn1 = ConnectionUtility.getConnection();
				String soqlQuery = "select Id, Name, Site__c, Status__c, Total_Sale__c from Service_Job_Order__c "; 

		       try {
			         QueryResult qr = serviceConn1.query(soqlQuery);
			         boolean done = false;
			         if (qr.getSize() > 0) {
			            while (!done) {
			               SObject[] records = qr.getRecords();
			               for (int i = 0; i < records.length; i++) {
			            	   Service_Job_Order__c spm=(Service_Job_Order__c) records[i];
			            	   //spm.getStatus__c()
			            	   if(spm.getStatus__c().equalsIgnoreCase("Invoiced") && spm.getTotal_Sale__c()!=null) {
				            	   list.put( spm.getName()+spm.getSite__c()+"Inv",spm.getId());
			            	   }
			            	   else {
				            	   list.put( spm.getName()+spm.getSite__c(),spm.getId());
			            	   }
			            	   //System.out.println("Id="+spm.getId()+" CNo="+spm.getChassis_No__c());
						    }
						
						    if (qr.isDone()) {
						       done = true;
						    } else {
						       qr = serviceConn1.queryMore(qr.getQueryLocator());
						    }
						 }
						}
			         else {
//						 System.out.println("No records found.");
						}
						} 
		       catch (ConnectionException ce) {
		    	   //insertExceptionLog("NA","uploadToSalesForce","Query could not be executed correctly while retrieving User details "+ce);
		    	   ce.printStackTrace();
						}
						} 
			   catch (ConnectionException e) {
				   //insertExceptionLog("NA","uploadToSalesForce","Query could not be executed correctly while retrieving User details "+e);
		    	   e.printStackTrace();
						}
			return list;
		}
		
		/**
		 * Method to insert new User into Salesforce
		 */
		public void insertUploadDetails(String Site__c1, String Remark)
		{
			EnterpriseConnection serviceConn1 =null;
			try {	 
				 serviceConn1 = DestinationConnectionUtility.getConnection();
					if(serviceConn1==null) {
						//saveLogData("InsertBillingData", "Call by Java Schedular", json.toString(), "Exception in SalesForce Connection", resultSuccess);
						return;
					}
			}catch(Exception e) {
				//insertExceptionLog("NA","uploadToSalesForce","Connection could not be esatblished with Salesforce wile creating new User "+e);
	 	   }
			Upload_Details__c Ac = new Upload_Details__c();
			List<Upload_Details__c> Ac1 = new ArrayList<Upload_Details__c>();
			Ac.setSite__c(Site__c1);
			Ac.setUnit__c("Toyota");
			Ac.setSource__c("CS Report");
			Ac.setTotal_Records__c(Size);
			Ac.setSuccess_Count__c(Suc);
			Ac.setError_Count__c(Size-Suc);
			//Ac.setData_Provided_By__c(Data_Provided_By__c);

			String last_Interaction_Date__c1 = imformat(Instant.now().toString(),1);
			last_Interaction_Date__c1	=last_Interaction_Date__c1.replaceAll("/", "-").replaceAll(" ", "-");
			Calendar cal  = Calendar.getInstance();
			int y = Integer.parseInt(last_Interaction_Date__c1.split("-", 3)[2]);
			int m = Integer.parseInt(last_Interaction_Date__c1.split("-", 3)[1]);
			int d = Integer.parseInt(last_Interaction_Date__c1.split("-", 3)[0]);
			cal.set(y, m-1, d);
			Ac.setProvided_On__c(cal);
			Ac.setRemarks__c(Remark);
			Ac.setType__c("Service Gate Pass");
			Ac1.add(Ac);
			UpsertResult[] sr = null;
			try {
				SObject[] upserts = (Ac1).stream().toArray(Upload_Details__c[]::new);
				sr = serviceConn1.upsert("id", upserts);
				System.out.println("sr length ="+sr.length);
			} 
			catch (ConnectionException e) {
				//saveLogData("InsertBillingData", "Call by Java Schedular", json.toString(), "error in line no:100 serviceConn1.create(new SObject[] {billing}) ", resultSuccess);
				 e.printStackTrace();
			}
			
			for (int i = 0; i < sr.length; i++) {
				if (sr[i].isSuccess()) {
					System.out.println("Successfully added new Upload sync report with id of: " +
							sr[i].getId() + ".");
					//updateIsSynchedFlagFlag(bean.getInvoiceId());
					//resultSuccess="Success";
				} 
				else {
					System.out.println("Error while adding new Upload sync report to Lead: "+sr[i].toString()+" " +
							sr[i].getErrors()[0].getMessage()+" " );
					//resultError=sr[i].getErrors()[0].getMessage();
				}
			}
		}
}

