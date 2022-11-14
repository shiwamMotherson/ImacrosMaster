package Imacro;
import java.io.File;
import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.nio.file.*;
import java.io.IOException;
public class ETCTDMFolderReadAndUploadSftpE03 {
	//static String fileDate="30122021";
	static String fileDate="";
	//static String yesterday="2021-12-30";
	static String yesterday="";
	//static String imacroS="imacro";
	static String UnitCode="E03";
	 public static void main(String args[]) throws IOException {
		 ETCTDMFolderReadAndUploadSftpE03 ctdms= new ETCTDMFolderReadAndUploadSftpE03();
		 for(int i=1;i<=30;i++) {
		 Instant now = Instant.now();
			Instant yesterday1 = now.minus(i, ChronoUnit.DAYS);
			yesterday= yesterday1.toString().substring(0,10);
			fileDate=imformat(yesterday,3);
			File directoryGS=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\GS");
			if(directoryGS.exists()) {ctdms.ReadGS();}else {directoryGS.mkdirs();ctdms.ReadGS();}
			File directoryBP=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BP");
			if(directoryBP.exists()) {ctdms.ReadBP();}else {directoryBP.mkdirs();ctdms.ReadBP();}
			File directoryBS=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BS");
			if(directoryBS.exists()) {ctdms.ReadBS();}else {directoryBS.mkdirs();ctdms.ReadBS();}
			File directoryBSB=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BSB");
			if(directoryBSB.exists()) {ctdms.ReadBSB();}else {directoryBSB.mkdirs();ctdms.ReadBSB();}
			File directorySSB=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SSB");
			if(directorySSB.exists()) {ctdms.ReadSSB();}else {directorySSB.mkdirs();ctdms.ReadSSB();}
			File directoryFMS=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\FMS");
			if(directoryFMS.exists()) {ctdms.ReadFMS();} else {directoryFMS.mkdirs();ctdms.ReadFMS();}
			File directorySW=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SW");
			if(directorySW.exists()) {ctdms.ReadSW();}else {directorySW.mkdirs();ctdms.ReadSW();}
			File directoryEW=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\EW");
			if(directoryEW.exists()) {ctdms.ReadEW();}else {directoryEW.mkdirs();ctdms.ReadEW();}
			File directoryAS=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\AS");
			if(directoryAS.exists()) {ctdms.ReadAS();}else {directoryAS.mkdirs();ctdms.ReadAS();}
		
	  	File directoryPath = new File("D:\\imacro\\"+fileDate);
	  SFTPFileTransfer sftpFileTransfer=new SFTPFileTransfer();
	  sftpFileTransfer.createRootDirectory(directoryPath);
		 }
	   }
	 
	 
	   public void ReadGS(){
		   File fileGS = new File("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\GS\\");
		   if(fileGS.exists()) {
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();
		  
		   FMLDBTransactionCTDMSInvoices dbtB=new FMLDBTransactionCTDMSInvoices();
		   List<String>invL2=dbtB.getInvoiceNamePlanB(UnitCode,"GS",yesterday);
		  
		   if(!(dbt.checkCountLog1("GS", yesterday,UnitCode))){
      		   dbt.insertDateWiseCount1("CTDMS", UnitCode, "INV","GS",0,yesterday);
		   }
		      File filesList[] = fileGS.listFiles();
		      try {
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,3).contains("TXA")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\GS");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\GS\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\GS\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\GS\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\GS").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of GS invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	  if(invL2.contains(fname)) {
		     				 System.out.println(fname+"--------------Invoice already exist in database");
		     				dbt.updateStatus1URLUPDATE(UnitCode,"GS",fname,yesterday,"OLD");
		     			 }else {
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","GS", "TX",fname,yesterday);
		     			 }
		              	    dbt.updateStatus1(UnitCode,"GS",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("GS", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("CTDMS", UnitCode, "INV","GS",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"GS",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"GS", yesterday);
		              			   System.out.println("There is only one file in GS");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"GS",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
		      }catch(Exception e) { System.out.println ("No files in GS list Array");}
		   }else {System.out.println ("Directory GS not exists");}
	   }
	   
	   
	   public void ReadBP(){
		   File fileGS = new File("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\BP\\");
		   if(fileGS.exists()) {
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();
		   
		   FMLDBTransactionCTDMSInvoices dbtB=new FMLDBTransactionCTDMSInvoices();
		   List<String>invL2=dbtB.getInvoiceNamePlanB(UnitCode,"BP",yesterday);
		      File filesList[] = fileGS.listFiles();
		      try {
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,3).contains("TXA")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BP");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\BP\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BP\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BP\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BP").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of BP invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	  if(invL2.contains(fname)) {
			     				 System.out.println(fname+"--------------Invoice already exist in database");
			     				dbt.updateStatus1URLUPDATE(UnitCode,"BP",fname,yesterday,"OLD");
			     			 }else {
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","BP", "TX",fname,yesterday);
			     			 }
		              	    dbt.updateStatus1(UnitCode,"BP",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("BP", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("CTDMS", UnitCode, "INV","BP",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"BP",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"BP", yesterday);
		              			   System.out.println("There is only one file in BP");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"BP",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
		      }catch(Exception e) { System.out.println ("No files in BP list Array");}
		   }else {System.out.println ("Directory BP not exists");}
	   }
	   
	   public void ReadBS(){
		   File fileGS = new File("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\BS\\");
		   if(fileGS.exists()) {
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();

		   FMLDBTransactionCTDMSInvoices dbtB=new FMLDBTransactionCTDMSInvoices();
		   List<String>invL2=dbtB.getInvoiceNamePlanB(UnitCode,"BS",yesterday);
		      File filesList[] = fileGS.listFiles();
		      try {
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,3).contains("INA")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BS");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\BS\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BS\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BS\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BS").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of BS invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	  if(invL2.contains(fname)) {
			     				 System.out.println(fname+"--------------Invoice already exist in database");
			     				dbt.updateStatus1URLUPDATE(UnitCode,"BS",fname,yesterday,"OLD");
			     			 }else {
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","BS", "TX",fname,yesterday);
			     			 }
		              	    dbt.updateStatus1(UnitCode,"BS",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("BS", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("CTDMS", UnitCode, "INV","BS",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"BS",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"BS", yesterday);
		              			   System.out.println("There is only one file in BS");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"BS",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
		      }catch(Exception e) { System.out.println ("No files in BS list Array");}
		   }else {System.out.println ("Directory BS not exists");}
	   }
	   
	   public void ReadBSB(){
		   File fileGS1 = new File("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\BSB\\");
		   if(fileGS1.exists()){
			   File fileGS = new File("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\BSB\\");
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();

		   FMLDBTransactionCTDMSInvoices dbtB=new FMLDBTransactionCTDMSInvoices();
		   List<String>invL2=dbtB.getInvoiceNamePlanB(UnitCode,"BSB",yesterday);
		      File filesList[] = fileGS.listFiles();
		    try {
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,3).contains("BSA")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BSB");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\BSB\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BSB\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BSB\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BSB").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of BSB invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	  if(invL2.contains(fname)) {
			     				 System.out.println(fname+"--------------Invoice already exist in database");
			     				dbt.updateStatus1URLUPDATE(UnitCode,"BSB",fname,yesterday,"OLD");
			     			 }else {
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","BSB", "TX",fname,yesterday);
			     			 }
		              	    dbt.updateStatus1(UnitCode,"BSB",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("BSB", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("CTDMS", UnitCode, "INV","BSB",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"BSB",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"BSB", yesterday);
		              			   System.out.println("There is only one file in BSB");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"BSB",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
		   }catch(Exception e) { System.out.println ("No files in BSB list Array");}
	   }else {System.out.println ("Directory BSB not exists");}
	   
}
	   
	   public void ReadSSB(){
		   File fileGS = new File("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\SSB\\");
		   if(fileGS.exists()) {
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();

		   FMLDBTransactionCTDMSInvoices dbtB=new FMLDBTransactionCTDMSInvoices();
		   List<String>invL2=dbtB.getInvoiceNamePlanB(UnitCode,"SSB",yesterday);
		   System.out.println("------------------------------");
		   
		      File filesList[] = fileGS.listFiles();
		      try {
		       // if(fileGS.listFiles().length()>0)
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,3).contains("SSA")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SSB");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\SSB\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SSB\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SSB\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SSB").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of SSB invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	  if(invL2.contains(fname)) {
			     				 System.out.println(fname+"--------------Invoice already exist in database");
			     				dbt.updateStatus1URLUPDATE(UnitCode,"SSB",fname,yesterday,"OLD");
			     			 }else {
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","SSB", "TX",fname,yesterday);
			     			 }
		              	    dbt.updateStatus1(UnitCode,"SSB",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("SSB", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("CTDMS", UnitCode, "INV","SSB",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"SSB",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"SSB", yesterday);
		              			   System.out.println("There is only one file in SSB");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"SSB",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		          }
		      }
		      }catch(Exception e) { System.out.println ("No files in SSB list Array");}
		   }else {System.out.println ("Directory SSB not exists");}
		   
		   
	   }
	   
	   public void ReadEW(){
		   File fileGS = new File("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\EW\\");
		   if(fileGS.exists()) {
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();

		   FMLDBTransactionCTDMSInvoices dbtB=new FMLDBTransactionCTDMSInvoices();
		   List<String>invL2=dbtB.getInvoiceNamePlanB(UnitCode,"EW",yesterday);
		      File filesList[] = fileGS.listFiles();
		      try {
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().trim().substring(0,3).contains("EWA")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\EW");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\EW\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\EW\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\EW\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\EW").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of EW invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	  if(invL2.contains(fname)) {
			     				 System.out.println(fname+"--------------Invoice already exist in database");
			     				dbt.updateStatus1URLUPDATE(UnitCode,"EW",fname,yesterday,"OLD");
			     			 }else {
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","EW", "TX",fname,yesterday);
			     			 }
		              	    dbt.updateStatus1(UnitCode,"EW",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("EW", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("CTDMS", UnitCode, "INV","EW",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"EW",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"EW", yesterday);
		              			   System.out.println("There is only one file in EW");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"EW",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
	   }catch(Exception e) { System.out.println ("No files in EW list Array");}
		   }else {System.out.println ("Directory EW not exists");}
	   }
	   
	   public void ReadAS(){
		   File fileGS = new File("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\AS\\");
		   if(fileGS.exists()) {
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();
		   FMLDBTransactionCTDMSInvoices dbtB=new FMLDBTransactionCTDMSInvoices();
		   List<String>invL2=dbtB.getInvoiceNamePlanB(UnitCode,"AS",yesterday);
		   try {
		      File filesList[] = fileGS.listFiles();
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,2).contains("AA")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\AS");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\AS\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\AS\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\AS\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\AS").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of AS invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	  if(invL2.contains(fname)) {
		     				 System.out.println(fname+"--------------Invoice already exist in database");
		     			 }else {
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","AS", "TX",fname,yesterday);
		     			 }
		              	    dbt.updateStatus1(UnitCode,"AS",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("AS", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("CTDMS", UnitCode, "INV","AS",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"AS",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"AS", yesterday);
		              			   System.out.println("There is only one file in AS");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"AS",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
		   }catch(Exception e) { System.out.println ("No files in AS list Array");}
		   }else {System.out.println ("Directory AS not exists");}
	   }
	   
	   public void ReadFMS(){
		   File fileGS = new File("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\FMS\\");
		   if(fileGS.exists()) {
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();

		   FMLDBTransactionCTDMSInvoices dbtB=new FMLDBTransactionCTDMSInvoices();
		   List<String>invL2=dbtB.getInvoiceNamePlanB(UnitCode,"FMS",yesterday);
		      File filesList[] = fileGS.listFiles();
		      try {
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,1).contains("F") && file.getName().substring(3,5).contains("DL")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\FMS");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\FMS\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\FMS\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\FMS\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\FMS").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of FMS invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	  if(invL2.contains(fname)) {
			     				 System.out.println(fname+"--------------Invoice already exist in database");
			     			 }else {
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","FMS", "TX",fname,yesterday);
			     			 }
		              	    dbt.updateStatus1(UnitCode,"FMS",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("FMS", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("iConnect", UnitCode, "INV","FMS",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"FMS",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"FMS", yesterday);
		              			   System.out.println("There is only one file in FMS");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"FMS",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
		      }catch(Exception e) { System.out.println ("No files in  FMS list Array");}
		   }else {System.out.println ("Directory FMS not exists");}
	   } 
	   public void ReadSW(){
		   File fileGS = new File("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\SW\\");
		   if(fileGS.exists()) {
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();

		   FMLDBTransactionCTDMSInvoices dbtB=new FMLDBTransactionCTDMSInvoices();
		   List<String>invL2=dbtB.getInvoiceNamePlanB(UnitCode,"SW",yesterday);
		      File filesList[] = fileGS.listFiles();
		      try {
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(2,4).contains("DL")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SW");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Delhi("+UnitCode+")\\"+fileDate+"\\SW\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SW\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SW\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SW").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of FMS invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	  if(invL2.contains(fname)) {
			     				 System.out.println(fname+"--------------Invoice already exist in database");
			     			 }else {
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","SW", "TX",fname,yesterday);
			     			 }
		              	    dbt.updateStatus1(UnitCode,"SW",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("SW", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("iConnect",UnitCode, "INV","SW",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"SW",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"SW", yesterday);
		              			   System.out.println("There is only one file in SW");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"SW",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
		      }catch(Exception e) { System.out.println ("No files in SW list Array");}
		   }else {System.out.println ("Directory SW not exists");}
	   } 
	   
	   public static String imformat(String date, int f) {
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
   
}
