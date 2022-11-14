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
public class PartsTestLive {
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
		Instant yesterday = now.minus(2, ChronoUnit.DAYS);
		System.out.println(yesterday);
		PartsTestLive iid=new PartsTestLive();
		DBTransactions dbt = new DBTransactions();
		List<UnitDTO>  unitlist=dbt.getUnitConfigurationASPart();
		iid.unitIterationDownload(yesterday.toString().substring(0,10));
	//}
	}
	public void unitIterationDownload(String yesterday)
	{
		unitListAttempt++; 
		DBTransactions dbt = new DBTransactions();
		if(unitListAttempt<5){
		  List<UnitDTO>  unitlist=dbt. getUnitConfigurationASPart();
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
			if(unit.getCategory().equalsIgnoreCase("AS")) {
			unitWiseDownload( unit,yesterday);
			}
		  }
		  boolean isMissing=anyInvoiceOrCountInAnyUnitPending(yesterday);
		  if(isMissing){
			  if(isLoginFlag) {
				  iim.invoke("iimExit");
					isLoginFlag=false;
					dbt.insertExceptionLog("NA", "isMissing true so unitIterationDownload called-Attempt number= "+unitListAttempt, yesterday);
					System.out.println("iMacro closed");
			  }
			  System.out.println("is missing true");
			  PartsTestLive iid=new PartsTestLive();
			iid.unitIterationDownload(yesterday);
		  }
		  else {

			//dbt.insertMailBox( "NA",  "Downloaded updates",  "iMacroInvoiceDownloader-unitIterationDownload", dbt.getUnitConfiguration(), yesterday, 1,"");
				//iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
				//iim.invoke("iimExit");
				//isLoginFlag=false;
				//System.out.println("iMacro closed");
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
			List<UnitDTO>  unitlist=dbt. getUnitConfigurationASPart();
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
			dbt.insertMailBox( "NA",  "unitIterationDownload attempt exceeded",  "iMacroInvoiceDownloader-unitIterationDownload", dbt. getUnitConfigurationASPart(), yesterday, 1,"");
		}
	}
public void unitWiseDownload(UnitDTO unit,String date){
  unitAttempt++;
  loginAttempt=0;
  DBTransactions dbt = new DBTransactions();
  if(unitAttempt<6){
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
	//iim exit
   }else{
	    dbt.insertExceptionLog("NA", unit.getUnitCode()+"-"+unit.getCategory()+ "-unitWiseDownload attempt exceeded-Attempt number= "+unitAttempt, date);
	   System.out.println("unitWiseDownload attempt exceeded");
	   
	}	
  //iim exit
  //iim.invoke("iimExit");

  //isLoginFlag=false;
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
    if(loginAttempt>5) {
    	iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
    }
    iim.invoke("iimPlay", "CODE:TAB T=1");
    // iim.invoke("iimPlay","CODE:URL GOTO=https://sc2.tkm.co.in/cas/login?&locale=en_US" );
    iim.invoke("iimPlay","CODE:URL GOTO="+unit.getApplicationURL() );
   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT=1901035" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:username CONTENT="+unit.getApplicationLoginID() );
    iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT=Toyota@12345" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:password CONTENT="+unit.getApplicationPassword() );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=CLASS:\"submit button ui-button ui-widget ui-state-default ui-corner-all\"" );
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:TOPSERV" );
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
   // iim.invoke("iimPlay","CODE:URL GOTO=http://issms.tkm.co.in/tops/do/sprt032?NAVIGATION=MENU&modulename=prt&formName=fprt032" );

    iim.invoke("iimPlay","CODE:URL GOTO="+unit.getCategoryLandingURL() );
    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    System.out.println("inset count note"+urlCheck());
   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateFromSPRT032 CONTENT=31052021" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateFromSPRT032 CONTENT="+imformat(date,3)+"" );
    System.out.println("Date"+imformat(date,3));
   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateToSPRT032 CONTENT=31052021" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateToSPRT032 CONTENT="+imformat(date,3)+"" );
    iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnSearch" );
    iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );

	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:totRecord  EXTRACT=VALUE");

//manually cast return values to correct type
	String iret = iim.invoke("iimGetLastExtract").toString();
	String strNoOfRecord=iret.replace("[EXTRACT]", "");
	System.out.println(strNoOfRecord);
	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:recPerPage  EXTRACT=VALUE");
	String iretRecPerPage = iim.invoke("iimGetLastExtract").toString();
	String strRecPerPage=iretRecPerPage.replace("[EXTRACT]", "");
	System.out.println("strNoOfRecord="+strNoOfRecord+",strRecPerPage="+strRecPerPage);
	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
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
	iim.invoke("iimPlay","CODE:TAB T=2" );
    // iim.invoke("iimPlay","CODE:URL GOTO=http://issms.tkm.co.in/tops/do/sprt032?NAVIGATION=MENU&modulename=prt&formName=fprt032" );

     iim.invoke("iimPlay","CODE:URL GOTO="+unit.getCategoryLandingURL() );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     System.out.println("inset count note"+urlCheck());
    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateFromSPRT032 CONTENT=31052021" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateFromSPRT032 CONTENT="+imformat(date,3)+"" );
     System.out.println("Date"+imformat(date,3));
    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateToSPRT032 CONTENT=31052021" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateToSPRT032 CONTENT="+imformat(date,3)+"" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnSearch" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );

 	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:totRecord  EXTRACT=VALUE");

 //manually cast return values to correct type
 	String iret = iim.invoke("iimGetLastExtract").toString();
 	String strNoOfRecord=iret.replace("[EXTRACT]", "");
 	System.out.println(strNoOfRecord);
 	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
 	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
 	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:recPerPage  EXTRACT=VALUE");

 	String iretRecPerPage = iim.invoke("iimGetLastExtract").toString();
 	String strRecPerPage=iretRecPerPage.replace("[EXTRACT]", "");
 	System.out.println("strNoOfRecord="+strNoOfRecord+",strRecPerPage="+strRecPerPage);

 	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
 	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
 	 int noofrecInt=0;
	 int recPerPageInt=0;
	 noofrecInt = Integer.parseInt(strNoOfRecord);
	// DBTransactions dbt = new DBTransactions();
		//dbt.insertDateWiseCount(unit, "INV", noofrecInt, date);
	 recPerPageInt = Integer.parseInt(strRecPerPage);
	 
		
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
	   
	   int k=1;
	while(k<=outerIteration) {
	    int j=0;
	    if(k==(outerIteration-1)) {
	    recPerPageInt=lastpagedata;
	    }
	    while(j<recPerPageInt) {
	     //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=ONCLICK:\"return link(0)\" EXTRACT=TXT" ); 
	      iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=ONCLICK:\"return link("+(j)+")\" EXTRACT=TXT" ); 
	      System.out.println("J value after extracting"+(j));
	      String iret1 = iim.invoke("iimGetLastExtract").toString();
	        
	      String strNoOfRecord1=iret1.replace("[EXTRACT]", "");//TAG POS=1 TYPE=A ATTR=TXT:CB21-002556
	      System.out.println(strNoOfRecord1.substring(0,1));
		  	 iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		 	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		 	   
		 	   //if(strNoOfRecord1.substring(0,1).contains("A")) {
	     	  //code for ib21
	         // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:"+strNoOfRecord1+"" );
		     // iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
		      //TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:select CONTENT=YES
		      // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:select CONTENT=YES" );
		      // iim.invoke("iimPlay","CODE:ONSCRIPTERROR CONTINUE=YES" );
		      //iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
		 		   
		      dbt.recordDateWiseInvoiceNumbers(unit, "INV",strNoOfRecord1 , date);
		    //  dbt.updateSubCategory(unit, date, "AS", strNoOfRecord1);
		    //  checkStatus(unit,date,strNoOfRecord1);
		      System.out.println(unit.getUnitCode());
		    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_back.gif" );
		    //  iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
		      j++;
		      //if(k>1) {
		    	//  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT="+(k)+"" );
		  	   // System.out.println("Page change successful"+(k));//TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_back.gif
		  	  // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo" );
		  	 //  iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		     // }
	     //}
	   
	    	//j++;
	    	System.out.println("Value of j "+j);
	    	// if(strNoOfRecord1.contains("CB21")) {
	    	//	j= recPerPageInt;
	    	//	k=(noofrecInt/recPerPageInt+1);
	 	   // }
	  }
	   // if(k>1) {
	    	//  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT="+(k)+"" );
	  	  //  System.out.println("Page change successful"+(k));//TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_back.gif
	  	  // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo" );
	  	  // iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
	     // }
	    k++;
	    if(outerIteration>1) {
	    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT="+(k)+"" );
	    System.out.println("Page change successful"+(k));
	   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo" );
	   iim.invoke("iimPlay", "CODE:WAIT SECONDS=3");
	   //iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billNo EXTRACT=TXT");
	   //TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billNoSPRT032 EXTRACT=TXT
	   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billNoSPRT032 EXTRACT=TXT");
	   String iret100 = iim.invoke("iimGetLastExtract").toString();
       
	      String strNoOfRecord100=iret100.replace("[EXTRACT]", "");//TAG POS=1 TYPE=A ATTR=TXT:CB21-002556
	      System.out.println(strNoOfRecord100);
		  	 iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		 	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	   if(strNoOfRecord100.contains("null") || strNoOfRecord100.contains("NULL")) {
		 iim.invoke("iimExit");
			isLoginFlag=false;
	   }
	    }
	    System.out.println(k);
	   }
   System.out.println("All Invoices added to DB  successfully.");
	iim.invoke("iimExit");
	isLoginFlag=false;
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
	List<UnitDTO>  unitlist=dbt. getUnitConfigurationASPart();
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
		       iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		       System.out.println(urlCheck());
		       System.out.println(manuurl);
		       if(!(urlCheck().equalsIgnoreCase(manuurl)) ){
		    	   	//downloadInvoices(invoices,yesterday,unit);
		       }else {
		    	   System.out.println("mannual url reached");
		       }
		       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateFromSPRT032 CONTENT="+imformat(yesterday,3)+"" );
		       System.out.println("Date"+imformat(yesterday,3));
		      // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateToSPRT032 CONTENT=31052021" );
		       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billDateToSPRT032 CONTENT="+imformat(yesterday,3)+"" );
		       iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=NAME:btnSearch" );
		       iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );

		   	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:totRecord  EXTRACT=VALUE");

		   //manually cast return values to correct type
		   	String iret = iim.invoke("iimGetLastExtract").toString();
		   	String strNoOfRecord=iret.replace("[EXTRACT]", "");
		   	System.out.println(strNoOfRecord);
		   	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:HIDDEN ATTR=NAME:recPerPage  EXTRACT=VALUE");

		   	String iretRecPerPage = iim.invoke("iimGetLastExtract").toString();
		   	String strRecPerPage=iretRecPerPage.replace("[EXTRACT]", "");
		   	System.out.println("strNoOfRecord="+strNoOfRecord+",strRecPerPage="+strRecPerPage);

		   	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		   	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		   	 int noofrecInt=0;
		  	 int recPerPageInt=0;
		     //try {
			   	 noofrecInt = Integer.parseInt(strNoOfRecord);/////try catch
			   //}
			  // catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//iim.invoke("iimExit");
				   // isLoginFlag=false;	
				   // unitWiseDownload(unit,yesterday);
				//}
		  DBTransactions dbt = new DBTransactions();
		 	//dbt.insertDateWiseCount(unit, "INV", noofrecInt, yesterday);
		  	 recPerPageInt = Integer.parseInt(strRecPerPage);
		  	 
		  		
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
		  	   
		  	   int k=1;
		  	while(k<=outerIteration) {
		  	    int j=0;
		  	    if(k==(outerIteration-1)) {
		  	    recPerPageInt=lastpagedata;
		  	    }
		  	    while(j<recPerPageInt) {
		  	     //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=ONCLICK:\"return link(0)\" EXTRACT=TXT" ); 
		  	      iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=ONCLICK:\"return link("+(j)+")\" EXTRACT=TXT" ); 
		  	      System.out.println("J value after extracting"+(j));
		  	      String iret1 = iim.invoke("iimGetLastExtract").toString();
		  	        
		  	      String strNoOfRecord1=iret1.replace("[EXTRACT]", "");//TAG POS=1 TYPE=A ATTR=TXT:CB21-002556
		  	      System.out.println(strNoOfRecord1);

		  		  	 iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
		  		 	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
		  				int index=invoices.indexOf(strNoOfRecord1);
		  			    if(index==-1) {
		  			    j++;
		  			    continue;
		  			    }
		  			    System.out.println("invo="+strNoOfRecord1);
		  			    String invFirst2Char=strNoOfRecord1.substring(0, 2);
		  			    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:"+strNoOfRecord1+"" );
		  			 // dbt.updateSubCategory(unit, yesterday, invFirst2Char, strNoOfRecord1);
		  			    iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
		  			    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:CHECKBOX ATTR=NAME:select CONTENT=YES" );
		  			    iim.invoke("iimPlay","CODE:ONSCRIPTERROR CONTINUE=YES" );
						
						iim.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER=D:\\iMacro\\"+imformat(Instant.now().toString(),3)+"\\"+unit.getUnitCode()+"\\Invoices\\"+unit.getCategory()+" FILE="+strNoOfRecord1+" WAIT=YES" );
						
		  			//  iim.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER=D:\\iMacro\\"+imformat(yesterday,3)+"\\"+unit.getUnitCode()+"\\Invoices\\"+unit.getCategory()+" FILE="+strNoOfRecord1+" WAIT=YES" );
		  			    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=ID:btnGenerateBill" );
		  			    iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
		  			  int dLoop=0;
		  		    while(!(checkStatus(unit,yesterday,strNoOfRecord1)) && dLoop<6) {
		  		    	dLoop++;
		  			    iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
		  		    }
		  		  iim.invoke("iimPlay", "CODE:TAB T=3");
				    //System.out.println(urlCheck());
				    if((urlCheck().equalsIgnoreCase("http://issms.tkm.co.in/tops/common/topsPDFreport.jsp")) ){
				    	iim.invoke("iimPlay", "CODE:TAB CLOSE");
				    }
				    iim.invoke("iimPlay", "CODE:TAB T=2");
		  			  //	dbt.recordDateWiseInvoiceNumbers(unit, "INV",strNoOfRecord1 , yesterday);
		  			  // dbt.updateSubCategory(unit, yesterday, "AB", strNoOfRecord1);
		  			    //checkStatus(unit,yesterday,strNoOfRecord1);
		  			    System.out.println(unit.getUnitCode());
		  			    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_back.gif" );
		  			    iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
		  			    j++;
		  		     // if(k>1) {
		  		    	//  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT="+(k)+"" );
		  		  	    //System.out.println("Page change successful"+(k));//TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_back.gif
		  		  	  // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo" );
		  		  	  // iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
		  		     // }
		  	    	    
		  	    //  }
		       
		         
		  	   /*  codes for type CB-21 IB-21 etc............
		  	      }*/
		  	      
		  	      
		  	    //iim.invoke("iimPlay", "CODE:TAB T=3");
		  	   // iim.invoke("iimPlay", "CODE:TAB T=2");
		  	    	//j++;
		  	    	System.out.println("Value of j "+j);
		  	    	// if(strNoOfRecord1.contains("CB21")) {
		 	    		//j= recPerPageInt;
		 	    		//k=(noofrecInt/recPerPageInt+1);
		 	 	    //}
		  	    }
		 // if(k>1) {
  		    	 // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT="+(k)+"" );
  		  	  // System.out.println("Page change successful"+(k));//TAG POS=1 TYPE=INPUT:IMAGE ATTR=SRC:../images/btn_back.gif
  		  	 // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo" );
  		  	// iim.invoke("iimPlay", "CODE:WAIT SECONDS=3");
  		   //   }
		  	    k++;
		  	    if(outerIteration>1) {
		  	    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:gotoPage CONTENT="+(k)+"" );
		  	    System.out.println("Page change successful"+(k));
		  	   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:BUTTON ATTR=NAME:btnGo" );
		  	   iim.invoke("iimPlay", "CODE:WAIT SECONDS=3");
			  // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billNo EXTRACT=TXT");
			   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:billNoSPRT032 EXTRACT=TXT");
			   String iret100 = iim.invoke("iimGetLastExtract").toString();
		       
			      String strNoOfRecord100=iret100.replace("[EXTRACT]", "");//TAG POS=1 TYPE=A ATTR=TXT:CB21-002556
			      System.out.println(strNoOfRecord100);
				  	 iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
				 	   iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
			   if(strNoOfRecord100.contains("null") || strNoOfRecord100.contains("NULL")) {
				 iim.invoke("iimExit");
					isLoginFlag=false;
			   }
		  	    }
		  	    System.out.println(k);
		  	   }
	        System.out.println("All MissingInvoices downloaded successfully.");
	   //iim.invoke("iimExit");
	   //isLoginFlag=false;
	}
/**
 * Method to check if file has been successfully downloaded
 */	
public boolean checkStatus( UnitDTO unit, String yesterday, String invo) {
	// "D:/iMacro/"+imformat(yesterday)+"/"+unit.getUnitCode()+"/Invoices/"+unit.getCategory()+"/"+invo+".pdf";
	//String location = "D:/iMacro/"+imformat(yesterday,3)+"/"+unit.getUnitCode()+"/Invoices/"+unit.getCategory()+"/"+invo+".pdf";
	String location = "D:/iMacro/"+imformat(Instant.now().toString(),3)+"/"+unit.getUnitCode()+"/Invoices/"+unit.getCategory()+"/"+invo+".pdf";
	
	System.out.println(location);
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

public String imformat(String date, int f) {
	String d="";
	if(f==1) {
		d=date.substring(8, 10)+"-"+date.substring(5, 7)+"-"+date.substring(0, 4);
	}
	else if(f==2) {
		d=date.substring(8, 10)+"/"+date.substring(5, 7)+"/"+date.substring(0, 4);
	}
	else if(f==3) {
		d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
	}else if(f==4) {
		d=date.substring(5, 7)+""+date.substring(0, 4);
	}
    
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
		if(unit.getCategory().equalsIgnoreCase("AS")) {
			if(unit.getUnitName().equalsIgnoreCase("ET Toyota Noida")) {
			//Only have Given(TX) invoices in flow Log -GS and Corresponding count in countlog-GS
			dbt.delFlowLogFNGSC(unit, date, "AB");
			}else if(unit.getUnitName().equalsIgnoreCase("ET Toyota G. Noida")) {
				dbt.delFlowLogFNGSC(unit, date, "AD");
			}
			else if(unit.getUnitName().equalsIgnoreCase("ET Toyota Delhi")) {
				dbt.delFlowLogFNGSC(unit, date, "AA");
		}
		}
		 
		//List<String> invoices=dbt.missingInvoiceList(unit, date);
		//for(String invoice : invoices) {
		//	dbt.updateSubCategory(unit, date, "AS", invoice);
		//}
	}	
}
