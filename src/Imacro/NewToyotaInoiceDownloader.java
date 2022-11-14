package Imacro;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.List;

import com.jacob.activeX.ActiveXComponent;


//import imacro.DBTransactions;

public class NewToyotaInoiceDownloader {
	boolean isLoginFlag =false;
	ActiveXComponent iim = null;
	int unitListAttempt=0;
	int loginAttempt=0;
	int unitAttempt=0;
	String previousApplication="";
	String previousUnit="";
	public static void main(String args[]){
		//for(int i=29;i<=30;i++) {
		Instant now = Instant.now();
		Instant yesterday = now.minus(1, ChronoUnit.DAYS);
		System.out.println(yesterday);
		NewToyotaInoiceDownloader iid=new NewToyotaInoiceDownloader();
		iid.unitIterationDownload(yesterday.toString());
		//}
	}


	public void unitIterationDownload(String yesterday)
	{
		unitListAttempt++; 
		DBTransactions dbt = new DBTransactions();
		if(unitListAttempt<3){
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
				//
			}
		  }
		  boolean isMissing=anyInvoiceOrCountInAnyUnitPending(yesterday);
		  if(isMissing){
			  if(isLoginFlag) {
				  iim.invoke("iimExit");
					isLoginFlag=false;
					//dbt.insertExceptionLog("NA", "", "isMissing true so unitIterationDownload called-Attempt number= "+unitListAttempt, yesterday);
					System.out.println("iMacro closed");
			  }
			  System.out.println("is missing true");
			unitIterationDownload(yesterday);
		  }
		  else {
			
//				try {
//		        	File file = new File("D:/iMacro/"+imformat(yesterday)+"/success.txt");
//					//file.createNewFile();
//				} 
//		        catch (IOException e) {
//					e.printStackTrace();
//				}
		  }
		}else {
		    //dbt.insertExceptionLog("NA", "", "All attempts exceeded", yesterday);
			System.out.println("unitIterationDownload attempt exceeded");
			List<UnitDTO>  unitlist=dbt.getUnitConfiguration();
			  //code to fetch unit list
			  for(UnitDTO unit:unitlist){
				  List<String> invoices=dbt.missingInvoiceList(unit, yesterday);
				  if(invoices.size()>0) {
					  for(String inv:invoices) {
						  if(inv.trim().length()>0) {
							  dbt.insertExceptionLog(inv, "IMACRO", "Download failed "+unit.getUnitCode()+" "+unit.getCategory()+" "+yesterday);
							  dbt.updateSFTPUploadStatus(unit, yesterday, "F", inv);
						  }
					  }
				  }
				  
			  }
			//dbt.insertMailBox( "NA",  "unitIterationDownload attempt exceeded",  "iMacroInvoiceDownloader-unitIterationDownload", dbt.getUnitConfiguration(), yesterday, 1);
		}

		//File directoryPath = new File("D:\\imacro\\"+imformat(yesterday));
		//SFTPFileTransfer sftpFileTransfer=new SFTPFileTransfer();
		//sftpFileTransfer.createRootDirectory(directoryPath);
	}
	

public void unitWiseDownload(UnitDTO unit,String date){
  unitAttempt++;
  loginAttempt=0;
  DBTransactions dbt = new DBTransactions();
  if(unitAttempt<3){
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
	reorderInvoices(unit, date);
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
        //+"-InvoiceOrCountOfMentionUnitIsPending so unitWiseDownload called-Attempt number= "+unitAttempt, date);
		unitWiseDownload( unit, date);
		
	}
	else {
//		try {
//			String location="D:/iMacro/"+imformat(date)+"/"+unit.getUnitCode()+"/Invoices/"+unit.getCategory()+"/success.txt";
//			Path path = Paths.get(location);
//			if (Files.exists(path)) {}
//			else {
//        	File file = new File(location);
//			file.createNewFile();
//			}
//		} 
//        catch (IOException e) {
//			e.printStackTrace();
//		}
	}
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
    if(loginAttempt<9) {//10 ko 9
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
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
    iim.invoke("iimPlay", "CODE:SET !ERRORIGNORE YES");
    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm");
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV");
    iim.invoke("iimPlay", "CODE:ONERRORDIALOG CONTINUE=YES");
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=4");
  //  System.out.println(urlCheck());
    
    //DBTransactions dbt = new DBTransactions();
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

	int noofrecInt=Integer.parseInt(strNoOfRecord);
	DBTransactions dbt = new DBTransactions();
	dbt.insertDateWiseCount(unit, "INV", noofrecInt, date);
	
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
System.out.println(strNoOfRecord);

	int	noofrecInt = Integer.parseInt(strNoOfRecord);
	

for(int i=1; i<=(noofrecInt/10)+1 ; i++) {


int k=0;
iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+k+"].documentNo EXTRACT=VALUE");

String invo=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
System.out.println(invo+"1111111111111");
dbt.recordDateWiseInvoiceNumbers(unit, "INV", invo,date);
while(!invo.equalsIgnoreCase("#EANF#")) {


if(k>0){
iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+(k-1)+"].selected CONTENT=NO");
}
iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+k+"].selected CONTENT=YES"); 



k++;
iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+k+"].documentNo EXTRACT=VALUE");

invo=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");

}
System.out.println(i);
iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=A ATTR=TXT:Next");
iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
}




System.out.println("All MissingInvoices downloaded successfully.");
// iim.invoke("iimExit");
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
			//dbt.insertMailBox(unit.getUnitCode(), "Given Unit pending",  "iMacroInvoiceDownloader-anyInvoiceOrCountOfMentionUnitIsPending", unitlist1,date, 2); 
		}
		else {
			//dbt.insertMailBox(unit.getUnitCode(), "Given Unit pending",  "iMacroInvoiceDownloader-anyInvoiceOrCountOfMentionUnitIsPending", unitlist1,date, 3); 
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
			   System.out.println(strNoOfRecord);
			   int noofrecInt=0;
			   try {
					noofrecInt = Integer.parseInt(strNoOfRecord);
				} 
				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					iim.invoke("iimExit");
				    isLoginFlag=false;	
				    unitWiseDownload(unit,yesterday);
				}
	       
	       for(int i=1; i<=(noofrecInt/10)+1 ; i++) {
	       
	       
	   	int k=0;
	   	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+k+"].documentNo EXTRACT=VALUE");

	   	String invo=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
	   while(!invo.equalsIgnoreCase("#EANF#")) {

		   
		   if(k>0){
			   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+(k-1)+"].selected CONTENT=NO");
		   }
		   	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:value["+k+"].selected CONTENT=YES"); 
		    
			/*
			 * int index=invoices.indexOf(invo); if(index==-1) { j++; continue; }
			 */
		    System.out.println("invo"+(k+1)+"="+invo);
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
		    iim.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER=D:\\iMacroTest\\"+imformat(yesterday)+"\\"+unit.getUnitCode()+"\\Invoices\\"+foldername+" FILE="+invo+".pdf WAIT=YES");
		    //iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrintInv");
		    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnPrintInv");
		    
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
		  
		    k++;
			iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:value["+k+"].documentNo EXTRACT=VALUE");

		    invo=iim.invoke("iimGetLastExtract").toString().replace("[EXTRACT]", "");
		    
	   }
	   System.out.println(i);
	   iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
	   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=A ATTR=TXT:Next");
	   iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
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
			dbt.delFlowLogFNGSC(unit, date, "SSB");
		}
		
		}
}
	

