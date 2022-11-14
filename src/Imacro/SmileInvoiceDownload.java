package Imacro;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
//import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jacob.activeX.ActiveXComponent;
//import imacro.DBTransactions;
// This code include extra invoice download functionality ,running correctly for deployment
public class SmileInvoiceDownload {
	boolean isLoginFlag =false;
	ActiveXComponent iim = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	public static void main(String args[]){
		Instant now = Instant.now();
		Instant yesterday = now.minus(42,ChronoUnit.DAYS);
		System.out.println(yesterday);
		SmileInvoiceDownload iid=new SmileInvoiceDownload();
		iid.unitIterationDownload(yesterday.toString());
	
	}
	public void unitIterationDownload(String yesterday)
	{
		unitListAttempt++; 
		DBTransactions dbt = new DBTransactions();
		if(unitListAttempt<5){//3/2
		  List<UnitDTO>  unitlist=dbt.getUnitConfiguration();
		  //code to fetch unit list
		  for(UnitDTO unit:unitlist){
			unitAttempt=0;
			String application=unit.getApplicationName();
			if(application==previousApplication && previousUnit==unit.getUnitCode()
					&& isLoginFlag==true) {
				isLoginFlag=true;
			}else if(isLoginFlag){
				iim.invoke("iimExit");
				isLoginFlag=false;				
			}
			else{
				isLoginFlag=false;
			}
			previousApplication=unit.getApplicationName();
			previousUnit=unit.getUnitCode();
			if(unit.getCategory().equalsIgnoreCase("SM")) {
			unitWiseDownload( unit,yesterday);
			}
		  }
		  boolean isMissing=anyInvoiceOrCountInAnyUnitPending(yesterday);
		  if(isMissing){
			  if(isLoginFlag) {
				  iim.invoke("iimExit");
					isLoginFlag=false;
				//	dbt.insertExceptionLog("NA", "isMissing true so unitIterationDownload called-Attempt number= "+unitListAttempt, yesterday);
					//System.out.println("iMacro closed");
			  }
			  System.out.println("is missing true");
			unitIterationDownload(yesterday);
		  }
		  else {

				//dbt.insertMailBox( "NA",  "Downloaded updates",  "iMacroInvoiceDownloader-unitIterationDownload", dbt.getUnitConfiguration(), yesterday, 1,"");
				iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
				iim.invoke("iimExit");
				isLoginFlag=false;
				System.out.println("iMacro closed");
		  }
		}else {
		   // dbt.insertExceptionLog("NA", "", "All attempts exceeded", yesterday);
			System.out.println("unitIterationDownload attempt exceeded");
			List<UnitDTO>  unitlist=dbt.getUnitConfiguration();
			  //code to fetch unit list
			  for(UnitDTO unit:unitlist){
				  List<String> invoices=dbt.missingInvoiceList(unit, yesterday);
				  if(invoices.size()>0) {
					  for(String inv:invoices) {
						  if(inv.trim().length()>0) {
							  dbt.insertExceptionLog(inv, "IMACRO", "Download failed Unit iteration exceeded by"+unitListAttempt+""+unit.getUnitCode()+" "+unit.getCategory()+" "+yesterday);
							  
							  dbt.updateSFTPUploadStatus(unit, yesterday, "F", inv);
						  }
					  }
				  }
				  
			  }
			//dbt.insertMailBox( "NA",  "unitIterationDownload attempt exceeded as Attempt number= "+unitListAttempt+"",  "iMacroInvoiceDownloader-unitIterationDownload", dbt.getUnitConfiguration(), yesterday, 1, "Unit iteration exceeded by "+unitListAttempt+"");
		}
	}
	

public void unitWiseDownload(UnitDTO unit,String date){
  unitAttempt++;
  loginAttempt=0;
  DBTransactions dbt = new DBTransactions();
  if(unitAttempt<3){//3 /2
	//fetchDBCredentials
    if(!dbt.checkCountLog(unit, date)){
		if(!isLoginFlag){
		  login(unit);
		  isLoginFlag=true;
		} 
		insertCount(unit, date);
	}
	 if(!dbt.flowCountCheck(unit, date,unitListAttempt, unitAttempt)){
		if(!isLoginFlag){
		  login(unit);
		  isLoginFlag=true;
		} 
		insertInvoices(unit, date);//delete all invoices and insert all
	} 
	//reorderInvoices(unit, date);
	List<String> invoices=dbt.missingInvoiceList(unit, date);
	System.out.println(unit.getUnitCode()+" "+unit.getCategory()+" "+date );
	System.out.println(invoices);
	if(invoices.size()>0) {
		System.out.println("List of invoices");
		System.out.println(invoices);
	downloadInvoices(invoices, date, unit);
	}
	
	if(anyInvoiceOrCountOfMentionUnitIsPending(unit, date)){
		System.out.println("recalled"+unit.getUnitCode()+ unit.getCategory());
		//dbt.insertExceptionLog(unit.getUnitCode(), "", unit.getUnitCode()+"-"+unit.getCategory()
       // +"-InvoiceOrCountOfMentionUnitIsPending so unitWiseDownload called-Attempt number= "+unitAttempt, date);
		unitWiseDownload( unit, date);
		
	}

//iim.invoke("iimExit");
 // isLoginFlag=false;
	
   }else{
	    //dbt.insertExceptionLog("NA", "",unit.getUnitCode()+"-"+unit.getCategory()+ "-unitWiseDownload attempt exceeded-Attempt number= "+unitAttempt, date);
	   System.out.println("unitWiseDownload attempt exceeded");
	}	
}

/**
 * Method to start iMacros and Login
 */	
public void login(UnitDTO unit) {
    loginAttempt++;
    if(loginAttempt<10) {//10 ko 7
	isLoginFlag=true;
	iim = new ActiveXComponent("imacros"); 
    iim.invoke("iimInit"); 
    System.out.println("Calling iimInit"); 
    if(loginAttempt>3) {
    	iim.invoke("iimPlay", "CODE:WAIT SECONDS=3");
    }
    if(loginAttempt>7) {
    	iim.invoke("iimPlay", "CODE:WAIT SECONDS=7");
    }
    iim.invoke("iimPlay", "CODE:TAB T=1");
    iim.invoke("iimPlay", "CODE:TAB CLOSEOTHERS");
    String URL1 = "CODE:URL GOTO="+unit.getApplicationURL();
    iim.invoke("iimPlay", URL1);
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=3");
    System.out.println("login note"+urlCheck());
    String URL2="CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT="+unit.getApplicationLoginID();
    iim.invoke("iimPlay", URL2);
   // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT=1806943");
    iim.invoke("iimPlay", "CODE:SET !ENCRYPTION NO");
    String URL3="CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT="+unit.getApplicationPassword();
    iim.invoke("iimPlay", URL3);
  //iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT=Msaril@2021");
    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=CLASS:\"submit button ui-button ui-widget ui-state-default ui-corner-all\"");
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
    iim.invoke("iimPlay", "CODE:SET !ERRORIGNORE YES");
    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm");
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV");
    iim.invoke("iimPlay", "CODE:ONERRORDIALOG CONTINUE=YES");
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=4");
    if(!(urlCheck().equalsIgnoreCase("http://sc2.tkm.co.in/")) ){
        ////dbt.insertExceptionLog(unit.getUnitCode(), "", unit.getUnitCode()+"-"+unit.getCategory()+"-login failed-Attempt Number="+loginAttempt, Instant.now().toString());       
        iim.invoke("iimExit");
        isLoginFlag=false;
        System.out.println("login called again");
        login(unit);
         }
    }
     }

/**
 * Method to insert row count from UploadCount_log
 */	
public void insertCount(UnitDTO unit,String date){
    iim.invoke("iimPlay", "CODE:'New tab opened");
    iim.invoke("iimPlay","CODE:TAB T=2" );
    iim.invoke("iimPlay","CODE:URL GOTO=http://issms.tkm.co.in/tops/do/ssrv254?NAVIGATION=MENU&modulename=srv&formName=fsrv254&reqSesId=h1ZF-R7V-u6h8CQ8d2Hky2t,1901035" );
    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:smilesPackSalesFromDate CONTENT=17/06/2021" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:smilesPackSalesToDate CONTENT=17/06/2021" );
    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    iim.invoke("iimPlay","CODE:ONDIALOG POS=1 BUTTON=YES" );
    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    iim.invoke("iimPlay","CODE:ONDOWNLOAD FOLDER=D:\\SmileExcelsheet\\"+date+"\\"+unit.getUnitCode()+" FILE=smile.xls WAIT=YES" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:download" );
    iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );

//excel read function is called
    String location = "D:/SmileExcelsheet/"+date+"/"+unit.getUnitCode()+"/smile.xls";
	 //String location = "C:/Users/Satvik.Singh/XlFiles/IssuedPolicyReport.xls";
	 FileInputStream fis;
	try {
		fis = new FileInputStream(new File(location));
	
    System.out.println("xlRead called");
	 Path path = Paths.get(location);
	  if (Files.exists(path)) {}
	  else {
		  //Log file not existing exception
		 // insertExceptionLog("NA","imacro","",+o+");
	  }
     XSSFWorkbook workbook;
		workbook = new XSSFWorkbook(fis);
     XSSFSheet spreadsheet = workbook.getSheetAt(0);
     InvoiceListRead(spreadsheet);
     
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
    
    
	String strNoOfRecord="";//=total number of record count from excel sheet store in strNoOfRecord
   
try {
	int noofrecInt=Integer.parseInt(strNoOfRecord);
	DBTransactions dbt = new DBTransactions();
	dbt.insertDateWiseCount(unit, "INV", noofrecInt, date);
}catch(Exception e) {
}
	
	
}

/**
 * Method to insert Invoice details into Flow_Log
 */	
public void insertInvoices(UnitDTO unit,String date){
	DBTransactions dbt = new DBTransactions();
	//excel read list of invoices is called,looping on list to insert in database
	dbt.recordDateWiseInvoiceNumbers(unit, "INV", invnumber,date);//invnumber is the invoice number exact name
   System.out.println("All Invoices added to DB  successfully.");
}

/**
 * Method to check if unit downloading and DB details are complete
 */	
public boolean anyInvoiceOrCountOfMentionUnitIsPending(UnitDTO unit,String date){
	DBTransactions dbt = new DBTransactions();
	boolean flag =false;
	if((!dbt.flowCountCheck(unit, date, unitListAttempt, unitAttempt)) || (dbt.isInvoiceMissing(unit, date, unitListAttempt, unitAttempt))) {
		flag = true;
	}
	if (flag==true && unitAttempt==5) {
		List<UnitDTO> unitlist1=new ArrayList<UnitDTO>();
		unitlist1.add(unit);
		if(!dbt.flowCountCheck(unit, date, unitListAttempt, unitAttempt)) {
			dbt.insertMailBox(unit.getUnitCode(), "Given Unit pending",  "iMacroInvoiceDownloader-anyInvoiceOrCountOfMentionUnitIsPending", unitlist1,date, 2,""); 
		}
		else {
			dbt.insertMailBox(unit.getUnitCode(), "Given Unit pending",  "iMacroInvoiceDownloader-anyInvoiceOrCountOfMentionUnitIsPending", unitlist1,date, 3,""); 
		}
	}
	return flag;	
}

/**
 * Method to check if all unit's downloading and DB details are complete
 */	
public boolean anyInvoiceOrCountInAnyUnitPending(String date){
	DBTransactions dbt = new DBTransactions(); 
	List<UnitDTO>  unitlist=dbt.getUnitConfiguration();
	boolean flag =false;
	for(UnitDTO unit:unitlist){
		if(anyInvoiceOrCountOfMentionUnitIsPending(unit,date)) {
			flag = true;
		}
	}
	return flag;	
}

/**
 * Method to download all invoices listed in the parameter
 */	
public void downloadInvoices(List<String> invoices,String yesterday,UnitDTO unit){
		System.out.println("Started.");
		if(!isLoginFlag) {
		login(unit);
		}
		String foldername=unit.getCategory();
		String manuurl=unit.getCategoryLandingURL();
		        iim.invoke("iimPlay", "CODE:'New tab opened");
		       iim.invoke("iimPlay", "CODE:TAB T=2");
		      // iim.invoke("iimPlay", "CODE:URL GOTO="+manuurl+"");
		       iim.invoke("iimPlay","CODE:URL GOTO=http://issms.tkm.co.in/tops/do/ssrv250?NAVIGATION=MENU&modulename=srv&formName=fsrv250&reqSesId=Gf3K3058WfSlyREL6xoJIMD,1901035" );
		       iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
		       System.out.println(urlCheck());
		       System.out.println(manuurl);
		       if(!(urlCheck().equalsIgnoreCase(manuurl)) ){
		    	   	//downloadInvoices(invoices,yesterday,unit);
		       }else {
		    	   System.out.println("mannual url reached");
		       }
		       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:searchFor CONTENT=%SML" );
		       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:searchParam CONTENT=SLB2100005" );
		      // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:taxType CONTENT=% *" );
		       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:taxType CONTENT=%I" );
		       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnSearch" );
		       iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
			    DBTransactions dbt = new DBTransactions();
			    dbt.updateSubCategory(unit, yesterday, invFirst2Char,invo);
			    iim.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER=D:\\iMacro\\"+imformat(yesterday)+"\\"+unit.getUnitCode()+"\\Invoices\\"+foldername+" FILE="+invo+".pdf WAIT=YES");
		       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrint" );
		    iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
		    int dLoop=0;
		    while(!(checkStatus(unit,yesterday,foldername,invo)) && dLoop<6) {
		    	dLoop++;
			    iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
		    }
		    iim.invoke("iimPlay", "CODE:TAB T=3");
		    //System.out.println(urlCheck());
		    if((urlCheck().equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFreport.jsp")) ){
		    	iim.invoke("iimPlay", "CODE:TAB CLOSE");
		    }
		    iim.invoke("iimPlay", "CODE:TAB T=2");
		   System.out.println(foldername+" Invoices MissingInvoices Sdownloaded successfully.");
	        System.out.println("All MissingInvoices downloaded successfully.");
	}

/**
 * Method to check if file has been successfully downloaded
 */	
	public boolean checkStatus( UnitDTO unit, String yesterday, String foldername, String invo) {
		String location = "D:/iMacro/"+imformat(yesterday)+"/"+unit.getUnitCode()+"/Invoices/"+unit.getCategory()+"/"+invo+".pdf";
		System.out.println("called 1");
		boolean status= false;
		Path path = Paths.get(location);
		DBTransactions dbt2 = new DBTransactions();
		if (Files.exists(path)) {
			File file = new File(location);
			System.out.println(file.length());
			if(file.length()>1024) {
			System.out.println("called 2 "+location);
			dbt2.updateStatus(unit, invo, yesterday,"Y");
			status=true;
			}else {
				file.delete();
			}
			
		}
		
		return status;
	}

/**
 * Method to return date in the DDMMYYYY format from the YYYY-MM-DD format
 */	
	public String imformat(String date) {
		String d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
		return d;
	}
/**
 * Method to return the current Tab url for iMacros
 */	
	public String urlCheck() {
		String s="";
		   iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
		   iim.invoke("iimPlay", "CODE: ADD !EXTRACT {{!URLCURRENT}}");
		   s=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
		return s;
		}
	
	/**
	 * Method read excel sheet for invoices name
	 */	
	public List <String> InvoiceListRead(XSSFSheet spreadsheet) {
		 System.out.println("xlRead called");
		 
	    List <String> Ins=new ArrayList<String>();
	    Iterator < Row >  rowIterator = spreadsheet.iterator();
	    String In = null;
	    XSSFRow row;
	    //String p="";
	    while (rowIterator.hasNext()) {
	        In = "";
	        row = (XSSFRow) rowIterator.next();
	        try {
	        if(row.getRowNum()>0) {
	            In = (cellReturn(row.getCell(6)));
	           
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        if(In.length()>1) {
	            Ins.add(In);
	                            
	        }
	    }
	    return Ins;
	    }
	 
	}
	
