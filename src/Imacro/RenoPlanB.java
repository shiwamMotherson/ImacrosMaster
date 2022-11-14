package Imacro;
import java.io.File;
import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.nio.file.*;
import java.io.IOException;
public class RenoPlanB {
	//static String fileDate="30122021"; 
	static String fileDate="25052022 ";
	//static String yesterday="2021-12-30";
	static String yesterday="2022-05-25";
	//static String imacroS="imacro";
	static String UnitCode="P01";
	 public static void main(String args[]) throws IOException {
		 RenoPlanB ctdms= new RenoPlanB();
		 for(int i=0;i<=30;i++) {
		 Instant now = Instant.now();
			Instant yesterday1 = now.minus(i, ChronoUnit.DAYS);
			yesterday= yesterday1.toString().substring(0,10);
			fileDate=imformat(yesterday,3);
			File directoryBSI=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BSI");
			if(directoryBSI.exists()) {ctdms.ReadBSI();}
			File directorySEC=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SEC");
			if(directorySEC.exists()) {ctdms.ReadSEC();}
			File directoryWSW=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSW");
			if(directoryWSW.exists()) {ctdms.ReadWSW();}
			File directoryWSC=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSC");
			if(directoryWSC.exists()) {ctdms.ReadWSC();}
			File directoryWSP=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSP");
			if(directoryWSP.exists()) {ctdms.ReadWSP();}
			File directorySEW=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SEW");
			if(directorySEW.exists()) {ctdms.ReadSEW();}
	  	File directoryPath = new File("D:\\imacro\\"+fileDate);
	  SFTPFileTransfer sftpFileTransfer=new SFTPFileTransfer();
	  sftpFileTransfer.createRootDirectory(directoryPath);
		 }
	   }
	 
	 
	   public void ReadBSI(){
		   File fileBSI = new File("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\BSI\\");
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();
		   if(!(dbt.checkCountLog1("BSI", yesterday,UnitCode))){
      		   dbt.insertDateWiseCount1("Siebel", UnitCode, "INV","BSI",0,yesterday);
		   }
		      File filesList[] = fileBSI.listFiles();
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,8).contains("INSUDERB")) {//INSUDERB
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BSI");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\BSI\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BSI\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BSI\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\BSI").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of BSI invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","BSI", "TX",fname,yesterday);
		              	    dbt.updateStatus1(UnitCode,"BSI",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("BSI", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("Siebel", UnitCode, "INV","BSI",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"BSI",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"BSI", yesterday);
		              			   System.out.println("There is only one file in BSI");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"BSI",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
	   }
	   
	   
	   public void ReadSEC(){
		   File fileSEC = new File("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\SEC\\");
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();
		      File filesList[] = fileSEC.listFiles();
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,8).contains("AMCIDERB")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SEC");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\SEC\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SEC\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SEC\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SEC").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of SEC invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","SEC", "TX",fname,yesterday);
		              	    dbt.updateStatus1(UnitCode,"SEC",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("SEC", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("Siebel", UnitCode, "INV","SEC",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"SEC",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"SEC", yesterday);
		              			   System.out.println("There is only one file in SEC");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"SEC",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
	   }
	   
	   public void ReadWSW(){
		   File fileWSW = new File("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\WSW\\");
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();
		      File filesList[] = fileWSW.listFiles();
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,8).contains("WINVDERB")) {//------------------------------------------------------------------------------------------------------------------
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSW");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\WSW\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSW\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSW\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSW").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of WSW invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","WSW", "TX",fname,yesterday);
		              	    dbt.updateStatus1(UnitCode,"WSW",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("WSW", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("Siebel", UnitCode, "INV","WSW",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"WSW",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"WSW", yesterday);
		              			   System.out.println("There is only one file in WSW");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"WSW",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
	   }
	   
	   public void ReadWSC(){
		   File fileGS1 = new File("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\WSC\\");
		   if(fileGS1.exists()){
			   File fileGS = new File("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\WSC\\");
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();
		      File filesList[] = fileGS.listFiles();
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,8).contains("SERVDERB")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSC");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\WSC\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSC\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSC\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSC").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of WSC invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","WSC", "TX",fname,yesterday);
		              	    dbt.updateStatus1(UnitCode,"WSC",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("WSC", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("Siebel", UnitCode, "INV","WSC",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"WSC",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"WSC", yesterday);
		              			   System.out.println("There is only one file in WSC");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"WSC",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
	   }else {System.out.println ("Directory WSC not exists");}
}
	   
	   public void ReadWSP(){
		   File fileGS = new File("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\WSP\\");
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();
		      File filesList[] = fileGS.listFiles();
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().substring(0,3).contains("SSB")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSP");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\WSP\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSP\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSP\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\WSP").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of WSP invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","WSP", "TX",fname,yesterday);
		              	    dbt.updateStatus1(UnitCode,"WSP",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("WSP", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("Siebel", UnitCode, "INV","WSP",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"WSP",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"WSP", yesterday);
		              			   System.out.println("There is only one file in WSP");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"WSP",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
	   }
	   
	   public void ReadSEW(){
		   File fileGS = new File("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\SEW\\");
		   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();
		      File filesList[] = fileGS.listFiles();
		      for(File file : filesList) {
		          if(file.isFile()) {
		              System.out.println("Actual File Name: "+file.getName());
		              System.out.println("File path: "+file.getAbsolutePath());
		              if(file.getName().trim().substring(0,8).contains("EWINDERB")) {
		              	File theDir = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SEW");
		              	if (!theDir.exists()){
		              	    theDir.mkdirs();
		              	    System.out.println("New Directory Created"+theDir);
		              	    }
		              	try {
		              	Path temp = Files.move
		              	        (Paths.get("D:\\Renault\\Motinagar("+UnitCode+")\\"+fileDate+"\\SEW\\"+file.getName()),
		              	        Paths.get("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SEW\\"+file.getName()));
		              	System.out.println("Path temp- file moved in location-"+temp);
		              	File f = new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SEW\\"+file.getName());
		              	int totalCount=new File("D:\\imacro\\"+fileDate+"\\"+UnitCode+"\\Invoices\\SEW").list().length;
		              	Thread.sleep(1000);
		              	System.out.println("Total number of SEW invoices of "+UnitCode+" =="+totalCount);
		              	if(f.exists()){
		              	    System.out.println("File is present named=> "+f);
		              	    String fname=file.getName().replace(".pdf","");
		              	    dbt.recordDateWiseInvoiceNumbers1(UnitCode, "INV","SEW", "TX",fname,yesterday);
		              	    dbt.updateStatus1(UnitCode,"SEW",fname,yesterday,"Y");
		              	     }
		              	      if(!(dbt.checkCountLog1("SEW", yesterday,UnitCode))){
		              		   dbt.insertDateWiseCount1("Siebel", UnitCode, "INV","SEW",0,yesterday);
		              		   if(totalCount<2 && totalCount!=0) { 
		              			   dbt.updateUploadFileCount1(UnitCode,"SEW",yesterday);
		              			   dbt.updateDownloadCount(UnitCode,"SEW", yesterday);
		              			   System.out.println("There is only one file in SEW");
		              		   }
		              	     }else {
		              		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
		              		      dbt1.updateUploadFileCount1(UnitCode,"SEW",yesterday);
		              		       }
		              	} catch (Exception e) {
		              		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
		              	}
		              }
		    	  
		      }
	   }
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