package Imacro;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import com.jacob.activeX.ActiveXComponent;
import com.opencsv.CSVWriter;
//import imacro.DBTransactions;
// This code include extra invoice download functionality ,running correctly for deployment.
public class ImacroRepeatDownloaderWithAMC {
	boolean isLoginFlag =false;
	ActiveXComponent iim = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	public static void main(String args[]){
		
		Instant now = Instant.now();
		DayOfWeek dayOfWeek = DayOfWeek.from(LocalDate.now());
		System.out.println(dayOfWeek.toString()+"-----------------");
		//for(int w =12; w>1; w--) {
		Instant yesterday = now.minus(2,ChronoUnit.DAYS);
		System.out.println(yesterday+"yesterday");
		ImacroRepeatDownloaderWithAMC iid=new ImacroRepeatDownloaderWithAMC();
    	try {
			iid.unitIterationDownload(yesterday.toString());
		} catch (Exception e) {
			System.out.println("Exception in downloading--------");
		}
		
		File directoryPath = null;
		//File directoryPathactual = null;
        if(!imformat(yesterday.toString()).equals(imformat(Instant.now().toString())))	{
	    directoryPath = new File("D:\\imacro\\"+imformat(Instant.now().toString()));
	    SFTPFileTransferIteration sftpFileTransfer=new SFTPFileTransferIteration();
		sftpFileTransfer.createRootDirectory(directoryPath,imformat(yesterday.toString()));
        }else{
        directoryPath = new File("D:\\imacro\\"+imformat(yesterday.toString()));
        SFTPFileTransfer sftpFileTransfer=new SFTPFileTransfer();
		sftpFileTransfer.createRootDirectory(directoryPath);
         }	
		//}
	}
	
	public void unitIterationDownload(String yesterday)
	{
		String filePath="D:\\ETinvoicesSuccess.csv";
		writeDataLineByLineSuccess(filePath,"Success");
		unitListAttempt++; 
		DBTransactions dbt = new DBTransactions();
		if(unitListAttempt<2){//3/2
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
			if(unit.getCategory().equalsIgnoreCase("BP") || unit.getCategory().equalsIgnoreCase("BS") || unit.getCategory().equalsIgnoreCase("GS") || unit.getCategory().equalsIgnoreCase("BSB") || unit.getCategory().equalsIgnoreCase("EW") ||  unit.getCategory().equalsIgnoreCase("SSB")) {
			unitWiseDownload( unit,yesterday);
			}
			else if(unit.getCategory().equalsIgnoreCase("SW") || unit.getCategory().equalsIgnoreCase("FMS")) {
				if(unitListAttempt==1 && unitAttempt==0) {
				//ConnectTest iid= new ConnectTest();
				//iid.unitIterationDownload(yesterday.substring(0,10));
				}
			}
			else if(unit.getCategory().equalsIgnoreCase("AS")) {
				if(unitListAttempt==1 && unitAttempt==0) {
					PartsTest5 iid=new PartsTest5();
				//iid.unitIterationDownload(yesterday);
				}
			}
		  }
		  boolean isMissing=anyInvoiceOrCountInAnyUnitPending(yesterday);
		  if(isMissing){
			  if(isLoginFlag) {
				  iim.invoke("iimExit");
					isLoginFlag=false;
				//	dbt.insertExceptionLog("NA", "isMissing true so unitIterationDownload called-Attempt number= "+unitListAttempt, yesterday);
					System.out.println("iMacro closed");
			  }
			  System.out.println("is missing true");
			unitIterationDownload(yesterday);
		  }
		  else {
try {
				//dbt.insertMailBox( "NA",  "Downloaded updates",  "iMacroInvoiceDownloader-unitIterationDownload", dbt.getUnitConfiguration(), yesterday, 1,"");
				iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
				iim.invoke("iimExit");
				isLoginFlag=false;
				System.out.println("iMacro closed");
}catch(Exception e) {}
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
							//  dbt.insertExceptionLog(inv, "IMACRO", "Download failed Unit iteration exceeded by"+unitListAttempt+""+unit.getUnitCode()+" "+unit.getCategory()+" "+yesterday);
							  
							  dbt.updateSFTPUploadStatus(unit, yesterday, "F", inv);
						  }
					  }
				  }
				  
			  }
			//dbt.insertMailBox( "NA",  "unitIterationDownload attempt exceeded as Attempt number= "+unitListAttempt+"",  "iMacroInvoiceDownloader-unitIterationDownload", dbt.getUnitConfiguration(), yesterday, 1, "Unit iteration exceeded by "+unitListAttempt+"");
		}
		File directoryPath = null;
		File directoryPathactual = null;
if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
	 directoryPath = new File("D:\\imacro\\"+imformat(Instant.now().toString()));
	// directoryPathactual=new File("D:\\imacro\\"+imformat(yesterday));
	 SFTPFileTransferIteration sftpFileTransfer=new SFTPFileTransferIteration();
		//sftpFileTransfer.createRootDirectory(directoryPath,imformat(Instant.now().toString()));
		sftpFileTransfer.createRootDirectory(directoryPath,imformat(yesterday));
}else{
 directoryPath = new File("D:\\imacro\\"+imformat(yesterday));
 SFTPFileTransfer sftpFileTransfer=new SFTPFileTransfer();
		sftpFileTransfer.createRootDirectory(directoryPath);
}	
		
	}
	
	public void unitWiseDownload1(UnitDTO unit,String date){
		  unitAttempt++;
		  loginAttempt=0;
		  DBTransactions dbt = new DBTransactions();
		  if(unitAttempt<3){//3 /2
			//fetchDBCredentials
		    if(!dbt.checkCountLog(unit, date)){
		    	dbt.insertDateWiseCount(unit, "INV", 0, date);
				/*if(!isLoginFlag){
				  login(unit);
				  isLoginFlag=true;
				} 
				insertCount(unit, date);*/
			}
			/* if(!dbt.flowCountCheck(unit, date,unitListAttempt, unitAttempt)){
				if(!isLoginFlag){
				  login(unit);
				  isLoginFlag=true;
				} 
				insertInvoices(unit, date);//delete all invoices and insert all
			} 
			reorderInvoices(unit, date);*/
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
				unitWiseDownload1( unit, date);
				
			}

		//iim.invoke("iimExit");
		 // isLoginFlag=false;
			
		   }else{
			    //dbt.insertExceptionLog("NA", "",unit.getUnitCode()+"-"+unit.getCategory()+ "-unitWiseDownload attempt exceeded-Attempt number= "+unitAttempt, date);
			   System.out.println("unitWiseDownload attempt exceeded");
			}	
		}


public void unitWiseDownload(UnitDTO unit,String date){
  unitAttempt++;
  loginAttempt=0;
  DBTransactions dbt = new DBTransactions();
  if(unitAttempt<3){//3 /2
	//fetchDBCredentials
    if(!dbt.checkCountLog(unit, date)){
    	dbt.insertDateWiseCount(unit, "INV", 0, date);
		/*if(!isLoginFlag){
		  login(unit);
		  isLoginFlag=true;
		} 
		insertCount(unit, date);*/
	}
	/* if(!dbt.flowCountCheck(unit, date,unitListAttempt, unitAttempt)){
		if(!isLoginFlag){
		  login(unit);
		  isLoginFlag=true;
		} 
		insertInvoices(unit, date);//delete all invoices and insert all
	} 
	reorderInvoices(unit, date);*/
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
    if(loginAttempt<6) {//10 ko 7
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
    //iim.invoke("iimPlay", "CODE:URL GOTO=https://sc2.tkm.co.in/cas/login?service=http%3A%2F%2Fsc2.tkm.co.in%2F&locale=");
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
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=15");
    iim.invoke("iimPlay", "CODE:SET !ERRORIGNORE YES");
    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm");
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV");
    iim.invoke("iimPlay", "CODE:ONERRORDIALOG CONTINUE=YES");
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=4");
 System.out.println(urlCheck());
   
    	  if((urlCheck().equalsIgnoreCase("http://sc2.tkm.co.in/?logoutmsg=MSCD5031AERR")) ){
    	        ////dbt.insertExceptionLog(unit.getUnitCode(), "", unit.getUnitCode()+"-"+unit.getCategory()+"-login failed-Attempt Number="+loginAttempt, Instant.now().toString());       
    	        iim.invoke("iimExit");
    	        isLoginFlag=false;
    	        System.out.println("login called again");
    	        SendAttachment sd=new SendAttachment();
    	        sd.sendmailForETLogin(unit.getUnitCode().toString());
    	        login(unit);
    	  }
    //DBTransactions dbt = new DBTransactions();
    if(!(urlCheck().equalsIgnoreCase("http://sc2.tkm.co.in/")) ){
        ////dbt.insertExceptionLog(unit.getUnitCode(), "", unit.getUnitCode()+"-"+unit.getCategory()+"-login failed-Attempt Number="+loginAttempt, Instant.now().toString());       
        iim.invoke("iimExit");
        isLoginFlag=false;
        System.out.println("login called again");
        SendAttachment sd=new SendAttachment();
        sd.sendmailForETLogin(unit.getUnitCode().toString());
        login(unit);
         }
    }
     }

/**
 * Method to insert row count from UploadCount_log
 */	
public void insertCount(UnitDTO unit,String date){
    iim.invoke("iimPlay", "CODE:'New tab opened");
    iim.invoke("iimPlay", "CODE:TAB T=2");
	iim.invoke("iimPlay", "CODE:URL GOTO="+unit.getCategoryLandingURL()+"");
	iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
	System.out.println("inset count note"+urlCheck());
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:documentStatus CONTENT=%G");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateFrom CONTENT="+imformat(date)+"");
	iim.invoke("iimPlay", "CODE:TAG POS=2 TYPE=TD ATTR=TXT:'Job Order No. : Reg. No. : Invoice Status : Show All Generated Not Generated Ad*'");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateTo CONTENT="+imformat(date)+"");
	iim.invoke("iimPlay", "CODE:TAG POS=2 TYPE=TD ATTR=TXT:'Job Order No. : Reg. No. : Invoice Status : Show All Generated Not Generated Ad*'");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_search.gif");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:totRecord  EXTRACT=VALUE");

//manually cast return values to correct type
	String iret = iim.invoke("iimGetLastExtract").toString();
	String strNoOfRecord=iret.replace("[EXTRACT]", "");
   
	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:recPerPage  EXTRACT=VALUE");

	String iretRecPerPage = iim.invoke("iimGetLastExtract").toString();
	String strRecPerPage=iretRecPerPage.replace("[EXTRACT]", "");

	System.out.println("strNoOfRecord="+strNoOfRecord+",strRecPerPage="+strRecPerPage);
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
	
	iim.invoke("iimPlay", "CODE:'New tab opened");
    iim.invoke("iimPlay", "CODE:TAB T=2");
	iim.invoke("iimPlay", "CODE:URL GOTO="+unit.getCategoryLandingURL()+"");
	iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
	System.out.println("insert invoices note"+urlCheck());
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:documentStatus CONTENT=%G");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateFrom CONTENT="+imformat(date)+"");
	iim.invoke("iimPlay", "CODE:TAG POS=2 TYPE=TD ATTR=TXT:'Job Order No. : Reg. No. : Invoice Status : Show All Generated Not Generated Ad*'");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateTo CONTENT="+imformat(date)+"");
	iim.invoke("iimPlay", "CODE:TAG POS=2 TYPE=TD ATTR=TXT:'Job Order No. : Reg. No. : Invoice Status : Show All Generated Not Generated Ad*'");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_search.gif");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:totRecord  EXTRACT=VALUE");

//manually cast return values to correct type
	String iret = iim.invoke("iimGetLastExtract").toString();
	String strNoOfRecord=iret.replace("[EXTRACT]", "");
   
	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:recPerPage  EXTRACT=VALUE");

	String iretRecPerPage = iim.invoke("iimGetLastExtract").toString();
	String strRecPerPage=iretRecPerPage.replace("[EXTRACT]", "");

	System.out.println("strNoOfRecord="+strNoOfRecord+",strRecPerPage="+strRecPerPage);

	int noofrecInt=Integer.parseInt(strNoOfRecord);
    int recPerPageInt=Integer.parseInt(strRecPerPage);
	int lastpagedata=0;
	int outerIteration=1;
	if(noofrecInt>recPerPageInt) {
	outerIteration=noofrecInt/recPerPageInt;
	lastpagedata=noofrecInt%recPerPageInt;
	if(lastpagedata>0) {
	outerIteration++;
	}
	else {
	lastpagedata=recPerPageInt;
	}
	}
	else {
	outerIteration=1;
	recPerPageInt=noofrecInt;
	lastpagedata=noofrecInt;
	}

	int i=0;
	while(i<outerIteration) {
	int j=0;
	if(i==(outerIteration-1)) {
	recPerPageInt=lastpagedata;
	}
	while(j<recPerPageInt) {
	if(j>0){
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+(j-1)+"].selected CONTENT=NO");
	}

	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+j+"].selected CONTENT=YES");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+j+"].documentNo EXTRACT=VALUE");

	String invnumber=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
	System.out.println(invnumber+"1111111111111");
	dbt.recordDateWiseInvoiceNumbers(unit, "INV", invnumber,date);

	j++;
System.out.println(recPerPageInt);
	}
	//Extra invoice insertrd
	ExtraInvoiceinsert(unit,date,recPerPageInt);
	if(outerIteration>1) {
	System.out.println("index change "+(i+2));

	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT="+(i+2)+"");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo");
	 iim.invoke("iimPlay", "CODE:WAIT SECONDS=3"); 
	}
	i++;
	}

   System.out.println("All Invoices added to DB  successfully.");

	//iim.invoke("iimExit");
	//isLoginFlag=false;
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
		//DBTransactions dbt=new DBTransactions();
		if(!isLoginFlag) {
		login(unit);
		}else {
    		//iim.invoke("iimPlay", "CODE:TAB T=2");
    		//iim.invoke("iimPlay", "CODE:TAB CLOSE");
		}
		String foldername=unit.getCategory();
		String manuurl=unit.getCategoryLandingURL();
		        iim.invoke("iimPlay", "CODE:'New tab opened");
		       iim.invoke("iimPlay", "CODE:TAB T=2");
		       iim.invoke("iimPlay", "CODE:URL GOTO="+manuurl+"");
		       iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
		       System.out.println(urlCheck());
		       System.out.println(manuurl);
		       if(!(urlCheck().equalsIgnoreCase(manuurl)) ){
		    	   	//downloadInvoices(invoices,yesterday,unit);
		       }else {
		    	   System.out.println("mannual url reached");
		       }
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:documentStatus CONTENT=%G");
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateFrom CONTENT="+imformat(yesterday)+"");
		       iim.invoke("iimPlay", "CODE:TAG POS=2 TYPE=TD ATTR=TXT:'Job Order No. : Reg. No. : Invoice Status : Show All Generated Not Generated Ad*'");
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:closeJobDateTo CONTENT="+imformat(yesterday)+"");
		       iim.invoke("iimPlay", "CODE:TAG POS=2 TYPE=TD ATTR=TXT:'Job Order No. : Reg. No. : Invoice Status : Show All Generated Not Generated Ad*'");
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_search.gif");
		       iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:totRecord  EXTRACT=VALUE");
		
		//manually cast return values to correct type
		   String iret = iim.invoke("iimGetLastExtract").toString();
		   String strNoOfRecord=iret.replace("[EXTRACT]", "");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:recPerPage  EXTRACT=VALUE");
		   String iretRecPerPage = iim.invoke("iimGetLastExtract").toString();
		   String strRecPerPage=iretRecPerPage.replace("[EXTRACT]", "");
		   System.out.println(urlCheck());
		   System.out.println("strNoOfRecord="+strNoOfRecord+",strRecPerPage="+strRecPerPage);
		   
		   int noofrecInt=0;
		   int recPerPageInt=0;
			try {
				noofrecInt = Integer.parseInt(strNoOfRecord);
				recPerPageInt = Integer.parseInt(strRecPerPage);
			} 
			catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				iim.invoke("iimExit");
			    isLoginFlag=false;	
			    unitWiseDownload(unit,yesterday);
			}
		   int lastpagedata=0;
		   int outerIteration=1;
		   if(noofrecInt>recPerPageInt) {
		    outerIteration=noofrecInt/recPerPageInt;
		    lastpagedata=noofrecInt%recPerPageInt;
		    if(lastpagedata>0) {
		    outerIteration++; 
		    }
		    
		   }else {
		    outerIteration=1;
		    recPerPageInt=noofrecInt;
		    lastpagedata=noofrecInt;
		   }
		   
		   int i=0;
		while(i<outerIteration) {
		    int j=0;
		    if(i==(outerIteration-1)) {
		    recPerPageInt=lastpagedata;
		    }
		    while(j<recPerPageInt) {//outerIteration
		     if(j>0){
		     //iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+(j-1)+"].selected CONTENT=NO");
             
					iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT="+(i+1)+"");
					iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
					iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo");
		    //can add 493

			 iim.invoke("iimPlay", "CODE:WAIT SECONDS=2"); 		    
			} 
		    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+j+"].selected CONTENT=YES");
		    //iim.invoke("iimPlay", "CODE:TAG XPATH=\"/html/id('DataTable')/tbody/tr[1]/td[14]/div\" EXTRACT=VALUE");
		    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+j+"].documentNo EXTRACT=VALUE");
		    String invo=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
		    
			int index=invoices.indexOf(invo);
		    if(index==-1) {
		    j++;
		    continue;
		    }
		    System.out.println("invo="+invo);
		    String invFirst2Char=invo.substring(0, 2);
		    String finalchar="T";
		    if(invFirst2Char.equalsIgnoreCase("TX")) {
		    finalchar="T";
		    }else if(invFirst2Char.equalsIgnoreCase("IN")) {
		    finalchar="P";
		    }else if(invFirst2Char.equalsIgnoreCase("BS")) {
		    finalchar="W";
		    }else if(invFirst2Char.equalsIgnoreCase("SS")) {
		    finalchar="S";
		   // if(invo.substring(0, 3).equalsIgnoreCase("SSB")) {
		    //	invFirst2Char="SSB";			    
		  //  }
		    }else if(invFirst2Char.equalsIgnoreCase("EW")) {
		    finalchar="E";
		    }else if(invFirst2Char.equalsIgnoreCase("AM")) {
		    finalchar="A";
		    }else if(invFirst2Char.equalsIgnoreCase("ID")) {
		    finalchar="I";
		    }else {
		    //j++;
		    //continue;
		    }
		    iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:invoiceType CONTENT=%"+finalchar+"");
		    System.out.println(finalchar);
		    DBTransactions dbt = new DBTransactions();
		    dbt.updateSubCategory(unit, yesterday, invFirst2Char,invo);
		    //Following if else added to only download TX invoices -479, 495-500
		    //if(invFirst2Char.equalsIgnoreCase("TX")) {
		    iim.invoke("iimPlay", "CODE:WAIT SECONDS=40");
		 if(unit.getUnitCode().equalsIgnoreCase("E01")) {
		  iim.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER=D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+unit.getUnitCode()+"\\Invoices\\"+foldername+" FILE="+invo+".pdf WAIT=YES");
		// iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrintInv");
		 }
		 iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrintInv");
		 
			    if (!((urlCheck().contains("http://issms.tkm.co.in/tops/common/topsPDFJasperReport.jsp?")))) {
			    	 //iim.invoke("iimPlay", "CODE:REFRESH");
			    	System.out.println(urlCheck());
			    	   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:invoiceType CONTENT=%"+finalchar+"");
			    	  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrintInv");
				}
			    
			    iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
			    iim.invoke("iimPlay", "CODE:TAB T=3");
			    iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
			    String DownloadUrl=" ";
			    DownloadUrl=urlCheck();
			    int dwnLoop=0;
			    while((!DownloadUrl.equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFreport.jsp")) && (!DownloadUrl.substring(0,59).equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFJasperReport.jsp?"))  &&  dwnLoop<5 ) {
			    	 dwnLoop++;
			    if(!(DownloadUrl.equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFreport.jsp")) && !(DownloadUrl.substring(0,59).equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFJasperReport.jsp?")) )
			    {
			    	try {
			    	SendAttachment sd=new SendAttachment();
			        sd.sendmailForETLPrintButtonIssue(unit.getUnitCode().toString());
			         iim.invoke("iimExit"); 
			         boolean isMissing=anyInvoiceOrCountInAnyUnitPending(yesterday);
					  if(isMissing){
						  if(isLoginFlag) {
							  iim.invoke("iimExit");
								isLoginFlag=false;
							//	dbt.insertExceptionLog("NA", "isMissing true so unitIterationDownload called-Attempt number= "+unitListAttempt, yesterday);
								System.out.println("iMacro closed");
						  }
						  System.out.println("is missing true");
						unitIterationDownload(yesterday);
					  }
			    	}catch(Exception e){}
			        
			    }else { System.out.println("Download is going Smooth");}
		    }
			    if(DownloadUrl.equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFreport.jsp")){
					// update old
					 dbt.updatDownloadURLFlag(unit, yesterday, "OLD",invo);
				 }else {}
					if(DownloadUrl.length()>59) {
			    if(DownloadUrl.contains("http://issms.tkm.co.in/tops/common/topsPDFJasperReport.jsp?")){
					 //update new
					 dbt.updatDownloadURLFlag(unit, yesterday, "NEW",invo);
				 }else {}
			 //   }
			    String filePath="D:\\ETinvoices.csv";
				String loc="";
				if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
				 loc = "D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+unit.getUnitCode()+"\\Invoices\\"+foldername+"\\";
				}else {
					loc="D:\\iMacro\\"+imformat(yesterday)+"\\"+unit.getUnitCode()+"\\Invoices\\"+foldername+"\\";
				}
				writeDataLineByLine(filePath, invo, loc,unit.getApplicationLoginID(),unit.getApplicationPassword(),DownloadUrl);
		
				try {
					String[] command = {"cmd.exe", "/C", "Start", "D:\\ETInvoiceDownload.bat"};//ETInvoiceDownload.bat
					Process p = Runtime.getRuntime().exec(command);
					try {
						int exitVal = p.waitFor();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} 
				catch (Exception ex) {
					System.out.println(ex);
				}
				
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			  }
				  int dLoop=0;
				    while(!(checkStatus(unit,yesterday,foldername,invo)) && dLoop<3) {
				    	dLoop++;
				    	
						iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
				    }
					 String filePathB="D:\\ETinvoices.csv";
						 String invB=" "; String locB=""; String UsernameB=""; String PasswordB=""; String downloadDateB="";
					        writeDataLineByLineBlank(filePathB,invB,locB,UsernameB ,PasswordB,downloadDateB );
					 System.out.println(urlCheck()+"[===================================================");
						String url=urlCheck();
						if(url.length()>59) {
							url=url.substring(0,59);
							System.out.println(url+"[====88888888888===================");
						}
						//if ((urlCheck().equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFreport.jsp"))) {
						if ((url.equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFJasperReport.jsp?")) || (url.equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFreport.jsp")) ) {
							iim.invoke("iimPlay", "CODE:TAB CLOSE");
						}
		    iim.invoke("iimPlay", "CODE:TAB T=2");
//		    }
//		    else {
//		    	//Else statement to only allow TX downloads
//				dbt.updateStatus(unit, invo, yesterday,"Y");
//				dbt.updateSFTPUploadStatus(unit, yesterday, "F", invo);
//		    }
		    j++;
		    }
		    //extra invoice download
		    ExtraInvoiceDownload(invoices, unit, yesterday,recPerPageInt );
		    if(outerIteration>1) {
		    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT="+(i+2)+"");
		    iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo");
			 iim.invoke("iimPlay", "CODE:WAIT SECONDS=2"); 
		   }
		    i++;
		   }
		   System.out.println(foldername+" Invoices MissingInvoices Sdownloaded successfully.");
		   
	        System.out.println("All MissingInvoices downloaded successfully.");
	  // iim.invoke("iimExit");
	   //isLoginFlag=false;
	}

/**
 * Method to check if file has been successfully downloaded
 */	
	public boolean checkStatus( UnitDTO unit, String yesterday, String foldername, String invo) {
		String location = "D:/iMacro/"+imformat(Instant.now().toString())+"/"+unit.getUnitCode()+"/Invoices/"+unit.getCategory()+"/"+invo+".pdf";
		System.out.println("called 1");
		boolean status= false;
		Path path = Paths.get(location);
		DBTransactions dbt2 = new DBTransactions();
		if (Files.exists(path)) {
			File file = new File(location);
			System.out.println(file.length());
			if(file.length()>51200) {
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
	public static String imformat(String date) {
		String d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
		return d;
	}

/**
 * Method to return the current Tab url for iMacros
 */	
	public String urlCheck() {
		String s="";
		   iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
		   iim.invoke("iimPlay", "CODE: ADD !EXTRACT {{!URLCURRENT}}");
		   s=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
		return s;
		}

/**
 * Method to Reorder Invoice List
 */	
	public void reorderInvoices(UnitDTO unit, String date) {
		DBTransactions dbt = new DBTransactions();
		//System.out.println("ReOrder Called1"+unit.getCategory());
		if(unit.getCategory().equalsIgnoreCase("GS")) {
			//Only have Given(TX) invoices in flow Log -GS and Corresponding count in countlog-GS
			dbt.delFlowLogFNGSC(unit, date, "TX");
			
		}
		else if(unit.getCategory().contentEquals("BP")) {
			//Only have Given(TX) invoices in flow Log -BP and Corresponding count in countlog-BP
			//System.out.println("ReOrder Called2");
			dbt.delFlowLogFNGSC(unit, date, "TX");
		}
		else if(unit.getCategory().equalsIgnoreCase("BS")) {
			//Only have Given(IN) invoices in flow Log -BS and Corresponding count in countlog-BS
			dbt.delFlowLogFNGSC(unit, date, "IN");
		}
		else if(unit.getCategory().equalsIgnoreCase("BSB")) {
			//Only have Given(IN) invoices in flow Log -BS and Corresponding count in countlog-BS
			dbt.delFlowLogFNGSC(unit, date, "BS");
		}
		else if(unit.getCategory().equalsIgnoreCase("EW")) {
			//Only have Given(IN) invoices in flow Log -BS and Corresponding count in countlog-BS
			dbt.delFlowLogFNGSC(unit, date, "EW");
		}
		
		else if(unit.getCategory().equalsIgnoreCase("SSB")) {
			//Only have Given(IN) invoices in flow Log -BS and Corresponding count in countlog-BS
			dbt.delFlowLogFNGSC(unit, date, "SS");
		}
		
		}
	
	
	//function to check extra invoice download
	public void ExtraInvoiceDownload(List<String> invoices ,UnitDTO unit,String yesterday,int recPerPageIntb) {
		String foldername=unit.getCategory();
		 iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+(recPerPageIntb-1)+"].selected CONTENT=NO");
	   	int k=recPerPageIntb;
	   	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+k+"].documentNo EXTRACT=VALUE");

	   	String invo=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
	   	int index=invoices.indexOf(invo);
	   while(!invo.equalsIgnoreCase("#EANF#")) {
		   	index=invoices.indexOf(invo);
		  if(index!=-1) {
		   if(k>recPerPageIntb){
			   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+(k-1)+"].selected CONTENT=NO");
		   }
		   	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+k+"].selected CONTENT=YES"); 
		
		    System.out.println("invo"+(k+1)+"="+invo);
		    String invFirst2Char=invo.substring(0, 2);
		    if((unit.getCategory().equalsIgnoreCase("GS") && invFirst2Char.equalsIgnoreCase("TX")) ||
		    		(unit.getCategory().equalsIgnoreCase("BSB") && invFirst2Char.equalsIgnoreCase("BS")) || 
		    		(unit.getCategory().equalsIgnoreCase("AMC") && invFirst2Char.equalsIgnoreCase("AS"))||
		    		(unit.getCategory().equalsIgnoreCase("EW") && invFirst2Char.equalsIgnoreCase("EW")) ||
		    		(unit.getCategory().equalsIgnoreCase("SSB") && invFirst2Char.equalsIgnoreCase("SS"))){
		    String finalchar="T";
		    if(invFirst2Char.equalsIgnoreCase("TX")) {
		    finalchar="T";
		    }else if(invFirst2Char.equalsIgnoreCase("IN")) {
		    finalchar="P";
		    }else if(invFirst2Char.equalsIgnoreCase("BS")) {
		    finalchar="W";
		    }else if(invFirst2Char.equalsIgnoreCase("SS")) {
		    finalchar="S";
		   // if(invo.substring(0, 3).equalsIgnoreCase("SSB")) {
		    //	invFirst2Char="SSB";			    
		   // }
		    }else if(invFirst2Char.equalsIgnoreCase("EW")) {
		    finalchar="E";
		    }else if(invFirst2Char.equalsIgnoreCase("AM")) {
		    finalchar="A";
		    }else if(invFirst2Char.equalsIgnoreCase("ID")) {
		    finalchar="I";
		    }else {
		    //j++;
		    //continue;
		    }
		    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:invoiceType CONTENT=%"+finalchar+"");
		    System.out.println(finalchar);
		    DBTransactions dbt = new DBTransactions();
		    dbt.updateSubCategory(unit, yesterday, invFirst2Char,invo);
		    //Following if else added to only download TX invoices -479, 495-500
		    //if(invFirst2Char.equalsIgnoreCase("TX")) {
		    iim.invoke("iimPlay", "CODE:WAIT SECONDS=10");
			 if(unit.getUnitCode().equalsIgnoreCase("E01")) {
				  iim.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER=D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+unit.getUnitCode()+"\\Invoices\\"+foldername+" FILE="+invo+".pdf WAIT=YES");
				// iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrintInv");
				 }
				 iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrintInv");
				 
					    if (!((urlCheck().contains("http://issms.tkm.co.in/tops/common/topsPDFJasperReport.jsp?")))) {
					    	 //iim.invoke("iimPlay", "CODE:REFRESH");
					    	   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SELECT ATTR=NAME:invoiceType CONTENT=%"+finalchar+"");
					    	  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrintInv");
						}
					    
					    iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
					    iim.invoke("iimPlay", "CODE:TAB T=3");
					    iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
					    String DownloadUrl=" ";
					    DownloadUrl=urlCheck();
					    if(!(DownloadUrl.equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFreport.jsp")) || !(DownloadUrl.substring(0,59).equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFJasperReport.jsp?")) )
					    {
					    	
					    }else { System.out.println("Download is going Smooth");}
					    if(DownloadUrl.equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFreport.jsp")){
							// update old
							 dbt.updatDownloadURLFlag(unit, yesterday, "OLD",invo);
						 }
					    if(DownloadUrl.length()>59) {
					    if(DownloadUrl.substring(0,59).equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFJasperReport.jsp?")){
							 //update new
							 dbt.updatDownloadURLFlag(unit, yesterday, "NEW",invo);
						 }else {
							
						 }
					    }
					    String filePath="D:\\ETinvoices.csv";
						String loc="";
						if(!imformat(yesterday).equals(imformat(Instant.now().toString())))	{
						 loc = "D:\\iMacro\\"+imformat(Instant.now().toString())+"\\"+unit.getUnitCode()+"\\Invoices\\"+foldername+"\\";
						}else {
							loc="D:\\iMacro\\"+imformat(yesterday)+"\\"+unit.getUnitCode()+"\\Invoices\\"+foldername+"\\";
						}
						writeDataLineByLine(filePath, invo, loc,unit.getApplicationLoginID(),unit.getApplicationPassword(),DownloadUrl);
				
						try {
							String[] command = {"cmd.exe", "/C", "Start", "D:\\ETInvoiceDownload.bat"};//ETInvoiceDownload.bat
							Process p = Runtime.getRuntime().exec(command);
							try {
								int exitVal = p.waitFor();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} 
						catch (Exception ex) {
							System.out.println(ex);
						}
						
						try {
							Thread.sleep(20000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					    
						  int dLoop=0;
						    while(!(checkStatus(unit,yesterday,foldername,invo)) && dLoop<3) {
						    	dLoop++;
						    	
								iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
						    }
					 String filePathB="D:\\ETinvoices.csv";
						 String invB=" "; String locB=""; String UsernameB=""; String PasswordB=""; String downloadDateB="";
					        writeDataLineByLineBlank(filePathB,invB,locB,UsernameB ,PasswordB,downloadDateB );
					 System.out.println(urlCheck()+"[===================================================");
					String url=urlCheck();
					if(url.length()>59) {
						url=url.substring(0,59);
					
					}
					//if ((urlCheck().equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFreport.jsp"))) {
					if ((url.equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFJasperReport.jsp?")) || (url.equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFreport.jsp")) ) {
						iim.invoke("iimPlay", "CODE:TAB CLOSE");
					}

		    iim.invoke("iimPlay", "CODE:TAB T=2");
		  }
	  }
		    k++;
			iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+k+"].documentNo EXTRACT=VALUE");

		    invo=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
		    
	   }
	}
	


/**
	 * Method to Write csv file
	 */	
	public  void writeDataLineByLine(String filePath,String inv, String loc, String Username ,String Password,String downloadDate )
	{
	    File file = new File(filePath);
	    try {
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
	        String[] data1 = { inv, loc, Username ,Password,downloadDate};
	        writer.writeNext(data1);
	        writer.close();
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	
	}

	public  void writeDataLineByLineSuccess(String filePath,String inv)
	{
	    File file = new File(filePath);
	    try {
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
	        String[] data1 = { inv};
	        writer.writeNext(data1);
	        writer.close();
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	
	}
	
	
	
	public  void writeDataLineByLineBlank(String filePath,String inv, String loc, String Username ,String Password,String downloadDate )
	{
	    File file = new File(filePath);
	    try {
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
	        String[] data1 = { inv, loc, Username ,Password,downloadDate};
	        writer.writeNext(data1);
	        writer.close();
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	
	}

	//Extra invoice insert
	public void ExtraInvoiceinsert(UnitDTO unit, String date ,int recPerPageInta) {
			 iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+(recPerPageInta-1)+"].selected CONTENT=NO");
		   	int k=recPerPageInta;
		   	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+k+"].documentNo EXTRACT=VALUE");

		   	String invo=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");

		   while(!invo.equalsIgnoreCase("#EANF#")) {

			   
			   if(k>recPerPageInta){
				   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+(k-1)+"].selected CONTENT=NO");
			   }
			   	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+k+"].selected CONTENT=YES"); 

				
				System.out.println(invo+"1111111111111");
				DBTransactions dbt = new DBTransactions();
				dbt.recordDateWiseInvoiceNumbers(unit, "INV", invo,date);
			    k++;
				iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+k+"].documentNo EXTRACT=VALUE");

			    invo=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
			    
		   
		}
	}
	
}
	

