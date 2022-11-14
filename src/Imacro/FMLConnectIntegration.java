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
//import imacro.DBTransactionFML;
public class FMLConnectIntegration {
boolean isLoginFlag =false;
ActiveXComponent iim = null;
int unitListAttempt=0;
int loginAttempt=0;
int unitAttempt=0;
String previousApplication="";
String previousUnit="";
public static void main(String args[]){
//for(int i=29;i<=30;i++) {//live code of 5 daya iteration fms sw
//run in IE 10
//this code is tested  login also running correctly for deployment
//file name change from connetTest to connectIntegration
//for(int u=11;u>1;u--) {
Instant now = Instant.now();
Instant yesterday = now.minus(7, ChronoUnit.DAYS);//31 RUNNING FOR TEST
System.out.println(yesterday);
FMLConnectIntegration iid=new FMLConnectIntegration();
DBTransactionFML dbt = new DBTransactionFML();
List<UnitDTO>  unitlist=dbt.getUnitConfigurationFMSSW();
iid.unitIterationDownload(yesterday.toString().substring(0,10));
//}
}
public void unitIterationDownload(String yesterday)
{
unitListAttempt++; 
DBTransactionFML dbt = new DBTransactionFML();
if(unitListAttempt<2){
 List<UnitDTO>  unitlist=dbt.getUnitConfigurationFMSSW();
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
if(unit.getCategory().equalsIgnoreCase("SW") || unit.getCategory().equalsIgnoreCase("FMS")) {
unitWiseDownload( unit,yesterday);
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
 UnitDTO u=null;
//  ConnectTest iid=new ConnectTest();
unitIterationDownload(yesterday);
 }
 else {
//dbt.insertMailBox( "NA",  "Downloaded updates",  "iMacroInvoiceDownloader-unitIterationDownload", dbt.getUnitConfiguration(), yesterday, 1,"");
try{
iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
iim.invoke("iimExit");
isLoginFlag=false;
System.out.println("iMacro closed");
} catch(Exception e){}
// try {
//         File file = new File("D:/iMacro/"+imformat(yesterday)+"/success.txt");
// //file.createNewFile();
// } 
//        catch (IOException e) {
// e.printStackTrace();
// }
 }
}else {
   //dbt.insertExceptionLog("NA", "", "All attempts exceeded", yesterday);
System.out.println("unitIterationDownload attempt exceeded");
List<UnitDTO>  unitlist=dbt.getUnitConfigurationFMSSW();
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
//dbt.insertMailBox( "NA",  "unitIterationDownload attempt exceeded",  "iMacroInvoiceDownloader-unitIterationDownload", dbt.getUnitConfigurationFMSSW(), yesterday, 1,"");
}
/*File directoryPath = null;
File directoryPathactual = null;
if(!imformat(yesterday,3).equals(imformat(Instant.now().toString(),3))) {
directoryPath = new File("D:\\iMacro\\"+imformat(Instant.now().toString(),3));
// directoryPathactual=new File("D:\\iMacro\\"+imformat(yesterday));
SFTPFileTransferIteration sftpFileTransfer=new SFTPFileTransferIteration();
//sftpFileTransfer.createRootDirectory(directoryPath,imformat(Instant.now().toString()));
sftpFileTransfer.createRootDirectory(directoryPath,imformat(yesterday,3));
}else{
 directoryPath = new File("D:\\iMacro\\"+imformat(yesterday,3));
 SFTPFileTransfer sftpFileTransfer=new SFTPFileTransfer();
sftpFileTransfer.createRootDirectory(directoryPath);
} */
 File directoryPath = null;
File directoryPathactual = null;
if(!imformat(yesterday,3).equals(imformat(Instant.now().minus(1,ChronoUnit.DAYS).toString(),3))) {
directoryPath = new File("D:\\imacro\\"+imformat(Instant.now().minus(1,ChronoUnit.DAYS).toString(),3));
// directoryPathactual=new File("D:\\iMacro\\"+imformat(yesterday));
SFTPFileTransferIterationSWFMS sftpFileTransfer=new SFTPFileTransferIterationSWFMS();
//sftpFileTransfer.createRootDirectory(directoryPath,imformat(Instant.now().toString()));
sftpFileTransfer.createRootDirectory(directoryPath,imformat(yesterday,3));
}else{
directoryPath = new File("D:\\imacro\\"+imformat(yesterday,3));
 SFTPFileTransfer sftpFileTransfer=new SFTPFileTransfer();
sftpFileTransfer.createRootDirectory(directoryPath);
} 

//File directoryPath = new File("D:\\iMacro\\"+imformat(yesterday,3));
//SFTPFileTransfer sftpFileTransfer=new SFTPFileTransfer();
//sftpFileTransfer.createRootDirectory(directoryPath);
}

public void unitWiseDownload(UnitDTO unit,String date){
  unitAttempt++;
  loginAttempt=0;
  DBTransactionFML dbt = new DBTransactionFML();
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
    if(loginAttempt<9) {//10 ko 9
isLoginFlag=true;
iim = new ActiveXComponent("imacros"); 
    iim.invoke("iimInit"); 
    System.out.println("Calling iimInit"); 
    if(loginAttempt>3) {
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=3");
    }
    if(loginAttempt>7) {
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
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
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
    iim.invoke("iimPlay", "CODE:SET !ERRORIGNORE YES");
   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:SUBMIT ATTR=NAME:submitconfirm");
    iim.invoke("iimPlay", "CODE:WAIT SECONDS=1");
    if(!(urlCheck().equalsIgnoreCase("http://sc2.tkm.co.in/")) ){
        ////dbt.insertExceptionLog(unit.getUnitCode(), "", unit.getUnitCode()+"-"+unit.getCategory()+"-login failed-Attempt Number="+loginAttempt, Instant.now().toString());       
        iim.invoke("iimExit");
        isLoginFlag=false;
        System.out.println("login called again");
        login(unit);
         }
   
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:CONNECT" );
iim.invoke("iimPlay","CODE:TAB T=2" );
iim.invoke("iimPlay", "CODE:ONERRORDIALOG CONTINUE=YES");
   iim.invoke("iimPlay", "CODE:WAIT SECONDS=2");
  //  System.out.println(urlCheck());
  System.out.println(urlCheck().substring(0,52));
    //DBTransactionFML dbt = new DBTransactionFML();
    if(!(urlCheck().substring(0,52).trim().equalsIgnoreCase("https://connect.tkm.co.in/Connect/Connect.html?auth=")) ){
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
   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:CONNECT" );
   iim.invoke("iimPlay","CODE:TAB T=2" );
   iim.invoke("iimPlay","CODE:WAIT SECONDS =2" );
   System.out.println("insertcount"+urlCheck().substring(0,52));
   if(!urlCheck().substring(0,52).trim().equalsIgnoreCase("https://connect.tkm.co.in/Connect/Connect.html?auth=")) {
    iim.invoke("iimExit");
        isLoginFlag=false;
        System.out.println("login called again");
        login(unit);
   }else {}
  // iim.invoke("iimPlay", "CODE:URL GOTO="+unit.getCategoryLandingURL()+"");
//iim.invoke("iimPlay", "CODE:WAIT SECONDS=3");
System.out.println("inset count note"+urlCheck());
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-2" );
    iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-19" );
    iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-56" );
    iim.invoke("iimPlay","CODE:WAIT SECONDS =4" );
  //extracting invoice type
    iim.invoke("iimPlay","CODE:TAG POS=8 TYPE=DIV ATTR=CLASS:\"nameLabel nameLabelMedium\" EXTRACT=TXT" ); 
    String iretxyz = iim.invoke("iimGetLastExtract").toString();
    String strNoOfRecordxyz=iretxyz.replace("[EXTRACT]", "");
    iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    if(strNoOfRecordxyz.equalsIgnoreCase("#EANF#")) {//if extraxt=null
  iim.invoke("iimExit");
      isLoginFlag=false;
      login(unit);
   }else {}
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT="+imformat(date,2)+"");
   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT=20/04/2021");
    iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT="+imformat(date,2)+"");
   // iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT=30/04/2021");
    iim.invoke("iimPlay","CODE:WAIT SECONDS=1");
    String sel="";
    if(unit.getCategory().equalsIgnoreCase("SW")) {
    sel="W";
    }
    else if(unit.getCategory().equalsIgnoreCase("FMS")){
    sel="F";
    }
  // iim.invoke("iimPlay","CODE:TAG POS=4 TYPE=SELECT ATTR=CLASS:\"gwt-ListBox fieldWidget-mandatory fieldWidgetMedium fieldWidget fieldWidgetFull\" CONTENT=%"+sel );
   iim.invoke("iimPlay","CODE:TAG POS=4 TYPE=SELECT ATTR=CLASS:\"gwt-ListBox fieldWidget-mandatory fieldWidgetMedium fieldWidget fieldWidgetFull\" CONTENT=%"+sel );
    iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=TXT:Search" );
    iim.invoke("iimPlay","CODE:WAIT SECONDS =8" );
/*
* iim.invoke("iimPlay","CODE:TAG POS=23 TYPE=TD ATTR=* EXTRACT=TXT" ); String
* msgerr = iim.invoke("iimGetLastExtract").toString(); String
* strMsgErr=msgerr.replace("[EXTRACT]", "").substring(14,28).trim();
* System.out.println("Error MSG NO data Found--->"+strMsgErr);
* if(strMsgErr.contains("No data found!")) { int noofrecInt1=0; DBTransactionFML
* dbt = new DBTransactionFML(); dbt.insertDateWiseCount(unit, "INV", noofrecInt1,
* date); }else {}
*/
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=CLASS:recordsCount EXTRACT=TXT" );//extract the number of record

    String iret = iim.invoke("iimGetLastExtract").toString();
  String strNoOfRecord=iret.replace("[EXTRACT]", "").trim();
 
  strNoOfRecord=strNoOfRecord.substring(strNoOfRecord.length()-2).trim();
  System.out.println("Number of record"+strNoOfRecord);
  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
  iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
  
  if(iret.contains("%") || iret.contains("#") || iret.contains("TA") ) {//
  strNoOfRecord="0";
  int noofrecInt=Integer.parseInt(strNoOfRecord);
DBTransactionFML dbt = new DBTransactionFML();
dbt.insertDateWiseCount(unit, "INV", noofrecInt, date);
   iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
iim.invoke("iimExit");
 }else {
int noofrecInt=Integer.parseInt(strNoOfRecord);
DBTransactionFML dbt = new DBTransactionFML();
dbt.insertDateWiseCount(unit, "INV", noofrecInt, date);
 }
}

/**
 * Method to insert Invoice details into Flow_Log
 */ 
public void insertInvoices(UnitDTO unit,String date){
DBTransactionFML dbt = new DBTransactionFML();
iim.invoke("iimPlay", "CODE:'New tab opened");
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:CONNECT" );
     iim.invoke("iimPlay","CODE:TAB T=2" );
  iim.invoke("iimPlay", "CODE:WAIT SECONDS=8");
  System.out.println("insertinvoices"+urlCheck().substring(0,52));
   if(!urlCheck().substring(0,52).trim().equalsIgnoreCase("https://connect.tkm.co.in/Connect/Connect.html?auth=")) {
    iim.invoke("iimExit");
        isLoginFlag=false;
        System.out.println("login called again");
        login(unit);
   }else {}
  //System.out.println("insert invoices note"+urlCheck());

//TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-2
//TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-19
//TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-56
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-2" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-19" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =2" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-56" );
 //  iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-56" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =4" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT="+imformat(date,2)+"");
    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT=20/04/2021");
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT="+imformat(date,2)+"");
    // iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT=30/04/2021");
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1");
     String sel="";
     if(unit.getCategory().equalsIgnoreCase("SW")) {
    sel="W";
     }
     else if(unit.getCategory().equalsIgnoreCase("FMS")){
    sel="F";
     }
   // iim.invoke("iimPlay","CODE:TAG POS=4 TYPE=SELECT ATTR=CLASS:\"gwt-ListBox fieldWidget-mandatory fieldWidgetMedium fieldWidget fieldWidgetFull\" CONTENT=%"+sel );
    iim.invoke("iimPlay","CODE:TAG POS=4 TYPE=SELECT ATTR=CLASS:\"gwt-ListBox fieldWidget-mandatory fieldWidgetMedium fieldWidget fieldWidgetFull\" CONTENT=%"+sel );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=TXT:Search" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS =8" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=CLASS:recordsCount EXTRACT=TXT" );//extract the number of record
     System.out.println("records count extracting");
     String iret = iim.invoke("iimGetLastExtract").toString();
  String strNoOfRecord=iret.replace("[EXTRACT]", "").trim();
  strNoOfRecord=strNoOfRecord.substring(strNoOfRecord.length()-2).trim();
  System.out.println(strNoOfRecord);
  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
  iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
/*
* if(strNoOfRecord.contains("#%")) {//if extraxt=null iim.invoke("iimExit");
* isLoginFlag=false; login(unit); }else {}
*/
int noofrecInt = Integer.parseInt(strNoOfRecord);
       int lastpagedata=0;
  int recPerPageInt=10;
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
  }
  
  int i=0;
  int j=0;
  System.out.println("Outer:"+outerIteration+"Recs"+recPerPageInt);
  while(i<outerIteration) {
   j=0;
    System.out.println("1i="+i+" j="+j);
   if(i==(outerIteration-1) && outerIteration>1) {
   recPerPageInt=lastpagedata;
   }
   if(i>0){
    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=CLASS:pageNo CONTENT="+(i+1)+"");
  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=BUTTON ATTR=TXT:Go");
  iim.invoke("iimPlay","CODE:WAIT SECONDS =6" );
    } 
   while(j<recPerPageInt) {
    System.out.println("i="+i+" j="+j);
   // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=__GWT_CELL:cell-gwt-uid-203 EXTRACT=TXT " );   //Extaraction of invoice number
   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TR ATTR=__gwt_row:\""+(j)+"\" EXTRACT=TXT " ); 
   System.out.println("Attribute do not taken");
     String iret1 = iim.invoke("iimGetLastExtract").toString();
     System.out.println(iret1);
  String strNoOfRecord1=iret1.replace("[EXTRACT]", "");
  System.out.println("Attribute do not taken1111111");
  /* if(strNoOfRecord1.contains("Digital Signature Upload Pending") || strNoOfRecord1.contains("Error in E-Invoice")) {//Error in E-Invoice
  int bgn1=strNoOfRecord1.indexOf(""+(j+1));
System.out.println(bgn1);
   int end1=bgn1;
   if(unit.getCategory().equalsIgnoreCase("FMS")) {
    end1=bgn1+18;
   }else if(unit.getCategory().equalsIgnoreCase("SW")) {
    end1=bgn1+16;
   }
   System.out.println(end1+"--------------------------------------");
System.out.println(strNoOfRecord1.substring(bgn1+3,end1)+"reach0");
String recordNameFmsSw=strNoOfRecord1.substring(bgn1+3,end1).trim();
dbt.insertExceptionLog(recordNameFmsSw, "IMACRO", "Download Failed Due to Digital Signature Upload Pending "+unit.getUnitCode()+" "+unit.getCategory()+" "+imformat(date,2));
}*/
  
  int bgn1=strNoOfRecord1.indexOf(""+(j+1));
  System.out.println(bgn1);
    int end1=bgn1;
    if(unit.getCategory().equalsIgnoreCase("FMS")) {
    end1=bgn1+18;
    }else if(unit.getCategory().equalsIgnoreCase("SW")) {
    end1=bgn1+16;
    }
//    System.out.println(end1+"--------------------------------------");
// System.out.println(strNoOfRecord1.substring(bgn1+3,end1)+"reach0");
// String recordNameFmsSw=strNoOfRecord1.substring(bgn1+3,end1).trim();
// dbt.recordDateWiseInvoiceNumbers(unit, "INV",recordNameFmsSw , date);
    if((j==9) ||(i<=1) ) {
   	 String recordNameFmsSw=strNoOfRecord1.substring(bgn1+3,end1+1).trim();
   	 dbt.recordDateWiseInvoiceNumbers(unit, "INV",recordNameFmsSw , date);
    }else {
    String recordNameFmsSw=strNoOfRecord1.substring(bgn1+3,end1).trim();
    dbt.recordDateWiseInvoiceNumbers(unit, "INV",recordNameFmsSw , date);
    }
  
  
  //if(!strNoOfRecord1.contains("Submitted")) {
  //dbt.insertExceptionLog("NA", "IMACROS","Invoice status  is not Submitted"+" "+date );
  //}
  /*int bgn=strNoOfRecord1.indexOf("Submitted");
  System.out.println(bgn);
  
    int end=strNoOfRecord1.indexOf("pdf");
    System.out.println(end);
  System.out.println(strNoOfRecord1+"reach0");
  
  /*if(end<=strNoOfRecord1.length()-3 && end>0) {
    System.out.println("Attribute do not taken222222");
  if(strNoOfRecord1.substring(end, end+3).trim().equalsIgnoreCase("pdf")) {
  System.out.println("Final:"+strNoOfRecord1.substring(bgn+9, end+3).trim());
  String strNoOfRecord2 = strNoOfRecord1.substring(bgn+9, end+3).trim();
  String strNoOfRecord3=strNoOfRecord2.substring(14,25).trim();
  System.out.println(strNoOfRecord3+"reach1");
  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
  iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
  String  recName1="";
if(unit.getCategory().equalsIgnoreCase("SW")) {
recName1=strNoOfRecord2.substring(14,25);
       }
       else if(unit.getCategory().equalsIgnoreCase("FMS")){
        recName1=strNoOfRecord2.substring(14,27);
       }
  System.out.println(recName1+"------------------name of record");
  dbt.recordDateWiseInvoiceNumbers(unit, "INV",recName1 , date);
  
//iim.invoke("iimExit");
  }
    }*/
j++;
//for inner loop used for row change
    }
i++;
}
   System.out.println("All Invoices added to DB  successfully.");

  // iim.invoke("iimExit");
//isLoginFlag=false;
}
/**
 * Method to check if unit downloading and DB details are complete
 */ 
public boolean anyInvoiceOrCountOfMentionUnitIsPending(UnitDTO unit,String date){
DBTransactionFML dbt = new DBTransactionFML();
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
DBTransactionFML dbt = new DBTransactionFML(); 
List<UnitDTO>  unitlist=dbt.getUnitConfigurationFMSSW();
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
//DBTransactionFML dbt=new DBTransactionFML();
if(!isLoginFlag) {
login(unit);
}else {
    //iim.invoke("iimPlay", "CODE:TAB T=2");
    //iim.invoke("iimPlay", "CODE:TAB CLOSE");
}
//String foldername=unit.getCategory();
// String manuurl=unit.getCategoryLandingURL();
//        //iim.invoke("iimPlay", "CODE:'New tab opened");
    //  iim.invoke("iimPlay","CODE:TAB T=2" );

iim.invoke("iimPlay","CODE:WAIT SECONDS =2" );
       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=TXT:CONNECT" );
       iim.invoke("iimPlay","CODE:TAB T=2" );
       iim.invoke("iimPlay","CODE:WAIT SECONDS =5" );
       System.out.println("downloadinvoices"+urlCheck().substring(0,52));
       if(!urlCheck().substring(0,52).trim().equalsIgnoreCase("https://connect.tkm.co.in/Connect/Connect.html?auth=")) {
    iim.invoke("iimExit");
        isLoginFlag=false;
        System.out.println("login called again");
        login(unit);
   }else {}
      
       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-2" );
       iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-19" );
       iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:gwt-uid-56" );
       iim.invoke("iimPlay","CODE:WAIT SECONDS =4" );
       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT="+imformat(yesterday,2)+"");
      // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT=20/04/2021");
       iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
       iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT="+imformat(yesterday,2)+"");
      // iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=INPUT ATTR=TITLE:\"Input in 'DD/MM/YYYY' format.\" CONTENT=30/04/2021");
       iim.invoke("iimPlay","CODE:WAIT SECONDS=1");
       String sel="";
       if(unit.getCategory().equalsIgnoreCase("SW")) {
      sel="W";
       }
       else if(unit.getCategory().equalsIgnoreCase("FMS")){
      sel="F";
       }
     // iim.invoke("iimPlay","CODE:TAG POS=4 TYPE=SELECT ATTR=CLASS:\"gwt-ListBox fieldWidget-mandatory fieldWidgetMedium fieldWidget fieldWidgetFull\" CONTENT=%"+sel );
      iim.invoke("iimPlay","CODE:TAG POS=4 TYPE=SELECT ATTR=CLASS:\"gwt-ListBox fieldWidget-mandatory fieldWidgetMedium fieldWidget fieldWidgetFull\" CONTENT=%"+sel );
       iim.invoke("iimPlay","CODE:WAIT SECONDS =1" );
       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=TXT:Search" );
       iim.invoke("iimPlay","CODE:WAIT SECONDS =4" );
       iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=CLASS:recordsCount EXTRACT=TXT" );//extract the number of record
       System.out.println("records count extracting");
       String iret = iim.invoke("iimGetLastExtract").toString();
    String strNoOfRecord=iret.replace("[EXTRACT]", "").trim();
    strNoOfRecord=strNoOfRecord.substring(strNoOfRecord.length()-2).trim();
    System.out.println(strNoOfRecord);
    iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   int noofrecInt=0;
  try {
  noofrecInt = Integer.parseInt(strNoOfRecord);/////try catch
  }
  catch (NumberFormatException e) {
// TODO Auto-generated catch block
e.printStackTrace();
iim.invoke("iimExit");
   isLoginFlag=false; 
   unitWiseDownload(unit,yesterday);
}
   int lastpagedata=0;
  int recPerPageInt=10;
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
  }
  
  int i=0;
  int j=0;
  System.out.println("Outer:"+outerIteration+"Recs"+recPerPageInt);
  while(i<outerIteration) {
   j=0;
    System.out.println("1i="+i+" j="+j);
   if(i==(outerIteration-1) && outerIteration>1) {
   recPerPageInt=lastpagedata;
   }
   if(i>0){
    iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=CLASS:pageNo CONTENT="+(i+1)+"");
  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=BUTTON ATTR=TXT:Go");
  iim.invoke("iimPlay","CODE:WAIT SECONDS =6" );
    } 
   while(j<recPerPageInt) {
    System.out.println("i="+i+" j="+j);
  // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=__GWT_CELL:cell-gwt-uid-203 EXTRACT=TXT " );   //Extaraction of invoice number
   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TR ATTR=__gwt_row:\""+(j)+"\" EXTRACT=TXT " ); 
   System.out.println("Attribute do not taken");
    String iret1 = iim.invoke("iimGetLastExtract").toString();
    System.out.println(iret1);
String strNoOfRecord1=iret1.replace("[EXTRACT]", "");
//DBTransactionFML dbt2 = new DBTransactionFML();
//if(!strNoOfRecord1.contains("Submitted")) {
//dbt2.insertExceptionLog("NA", "IMACROS","Invoice status  is not Submitted"+" "+yesterday );
//}
System.out.println("Attribute do not taken1111111");
int bgn=strNoOfRecord1.indexOf("Submitted");
System.out.println(bgn);
   int end=strNoOfRecord1.indexOf("pdf");
   System.out.println(end);
System.out.println(strNoOfRecord1+"reach0");
 if(end<=strNoOfRecord1.length()-3 && end>0) {
    System.out.println("Attribute do not taken222222");
if(strNoOfRecord1.substring(end, end+3).trim().equalsIgnoreCase("pdf")) {
System.out.println("Final:"+strNoOfRecord1.substring(bgn+9, end+3).trim());
String strNoOfRecord2 = strNoOfRecord1.substring(bgn+9, end+3).trim();
String strNoOfRecord3=strNoOfRecord2.substring(14,25).trim();
System.out.println(strNoOfRecord3+"reach1");
iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
  iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
  String  recName1="";
if(unit.getCategory().equalsIgnoreCase("SW")) {
recName1=strNoOfRecord2.substring(14,25);
       }
       else if(unit.getCategory().equalsIgnoreCase("FMS")){
        recName1=strNoOfRecord2.substring(14,27);
       }
 // iim.invoke("iimPlay","CODE:ONDOWNLOAD FOLDER=D:\\iMacro123\\Date\\E01\\Invoices\\SW FILE=* WAIT=YES" );     
//iim.invoke("iimPlay","CODE:ONDOWNLOAD FOLDER=D:\\iMacro\\"+unit.getUnitCode()+"\\Connect\\"+imformat(yesterday1,3)+"-"+imformat(yesterday1,3)+" FILE=* WAIT=YES" );  
DBTransactionFML dbt = new DBTransactionFML();
 List<String> invoicesConnect=dbt.missingInvoiceList(unit, yesterday);
 if(invoicesConnect.size()>0) {
 for(String invConnect:invoicesConnect) {
 if(invConnect.trim().length()>0 && invConnect.equalsIgnoreCase(recName1)) {
  // iim.invoke("iimPlay", "CODE:ONDOWNLOAD FOLDER=D:\\iMacro\\"+imformat(yesterday)+"\\"+unit.getUnitCode()+"\\Invoices\\"+foldername+" FILE="+invo+".pdf WAIT=YES");
  //m.invoke("iimPlay","CODE:ONDOWNLOAD FOLDER=D:\\iMacro\\"+imformat(Instant.now().toString().substring(0,10),3)+"\\"+unit.getUnitCode()+"\\Invoices\\"+unit.getCategory()+" FILE="+recName1+" WAIT=YES" );//Download location
iim.invoke("iimPlay","CODE:ONDOWNLOAD FOLDER=D:\\iMacro\\"+imformat(yesterday,3)+"\\"+unit.getUnitCode()+"\\Invoices\\"+unit.getCategory()+" FILE="+recName1+" WAIT=YES" );//Download lo
//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:/202104/NO02A/21NO02A0050-NO02A-210230.pdf" );  //Download click
System.out.println("reach2");//+imformat(Instant.now().toString())+
iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:"+strNoOfRecord2+"" );                         
iim.invoke("iimPlay","CODE:WAIT SECONDS =30" );
System.out.println("reach3");
//rename function call
System.out.println(recName1+"------------------name of record");
//dbt.recordDateWiseInvoiceNumbers(unit, "INV",recName1 , yesterday);
System.out.println("reach4");
   dbt.updateSubCategory(unit, yesterday, "TX", recName1);
   System.out.println("reach5");
  checkStatus(unit,yesterday,recName1);
  
 }
 }
 }
 
  DBTransactionFML dbtx = new DBTransactionFML();
  if(!(checkStatus(unit,yesterday,recName1))){
      
      dbtx.insertExceptionLog(recName1, "IMACRO", "Download Failed attempt="+unitAttempt +" "+unit.getUnitCode()+" "+unit.getCategory()+" "+yesterday);
     }
  System.out.println("reach6");
//iim.invoke("iimExit");
}
   }
j++;
//for inner loop used for row change
    }
i++;
}
  //iim.invoke("iim
       System.out.println("All MissingInvoices downloaded successfully.");
 iim.invoke("iimExit");
  isLoginFlag=false;
}

/**
 * Method to check if file has been successfully downloaded
 */ 
public boolean checkStatus( UnitDTO unit, String yesterday, String invo) {
String location = "D:/iMacro/"+imformat(yesterday,3)+"/"+unit.getUnitCode()+"/Invoices/"+unit.getCategory()+"/"+invo+".pdf";
//String location = "D:/iMacro/"+imformat(yesterday,3)+"/"+unit.getUnitCode()+"/Invoices/"+unit.getCategory()+"/"+invo+".pdf";
// "D:/iMacro/"+imformat(yesterday)+"/"+unit.getUnitCode()+"/Invoices/"+unit.getCategory()+"/"+invo+".pdf";
//String location = "D:\\iMacro\\"+imformat(yesterday1,3)+"\\"+unit.getUnitCode()+"\\Invoices\\"+unit.getCategory()+"\\"+invo+"";//earlier
System.out.println("called 1");
System.out.println(location);
boolean status= false;
Path path = Paths.get(location);
DBTransactionFML dbt2 = new DBTransactionFML();
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
public String imformat(String date , int f) {
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

}


