package Imacro;
import java.io.File;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.*;
import java.io.IOException;
public class UpdatedRenoPlanB {
	static String fileDate="25052022";
	static String yesterday="2022-05-25";
	 public static void main(String args[]) throws IOException {
	      //Creating a File object for directory
	      //File file = new File("D:\\Invoices");
	      File file = new File("D:\\Renault\\Motinagar");
	      //List of all files and directories
	      listOfFiles(file);
	    
	  	File directoryPath = new File("D:\\imacro\\"+fileDate);
	  	SFTPFileTransfer sftpFileTransfer=new SFTPFileTransfer();
	  	sftpFileTransfer.createRootDirectory(directoryPath);
	  	
	   }
	 
   public static void listOfFiles(File dirPath) throws IOException{
	   DBTransactionCTDMSInvoices dbt=new DBTransactionCTDMSInvoices();
      File filesList[] = dirPath.listFiles();
      for(File file : filesList) {
         if(file.isFile()) {
            System.out.println("Actual File Name: "+file.getName());
            System.out.println("File path: "+file.getAbsolutePath());
            if(file.getName().substring(0,8).contains("INSUDERB")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\BSI");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	    }
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\BSI\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\BSI\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\BSI").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of FMS invoices of P01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("P01", "INV","BSI", "TX",fname,yesterday);
            	    dbt.updateStatus1("P01","BSI",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("BSI", yesterday,"P01"))){
            		   dbt.insertDateWiseCount1("Siebel", "P01", "INV","BSI",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("P01","BSI",yesterday);
            			   dbt.updateDownloadCount("P01","BSI", yesterday);
            			   System.out.println("There is only one file in BSI");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("P01","BSI",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
            	}
            }else if(file.getName().substring(0,8).contains("AMCIDERB")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\SEC");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try{
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\SEC\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\SEC\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\SEC").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of SEC invoices of P01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("P01", "INV","SEC", "TX",fname,yesterday);
            	    dbt.updateStatus1("P01","SEC",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("SEC", yesterday,"P01"))){
            		   dbt.insertDateWiseCount1("Siebel", "P01", "INV","SEC",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("P01","SEC",yesterday);
            			   dbt.updateDownloadCount("P01","SEC", yesterday);
            			   System.out.println("There is only one file in SEC");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("P01","SEC",yesterday);
            		       }
                     } catch (Exception e) {
                    	 System.out.println ("File already exists in structured folder.\n -----------------------------------------------------------");
                    }
                 }else if(file.getName().substring(0,8).contains("WINVDERB")) {
               	File theDir = new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\WSW");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\WSW\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\WSW\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\WSW").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of WSW invoices of P01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("P01", "INV","WSW", "TX",fname,yesterday);
            	    dbt.updateStatus1("P01","WSW",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("WSW", yesterday,"P01"))){
            		   dbt.insertDateWiseCount1("Siebel", "P01", "INV","WSW",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("P01","WSW",yesterday);
            			   dbt.updateDownloadCount("P01","WSW", yesterday);
            			   System.out.println("There is only one file in WSW");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("P01","WSW",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists\n----------------------------------------------------------------------------");
            		}
            	
            }else if(file.getName().substring(0,8).contains("SERVDERB")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\WSC");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\WSC\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\WSC\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\WSC").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of WSC invoices of P01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("P01", "INV","WSC", "TX",fname,yesterday);
            	    dbt.updateStatus1("P01","WSC",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("WSC", yesterday,"P01"))){
            		   dbt.insertDateWiseCount1("Siebel", "P01", "INV","WSC",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("P01","WSC",yesterday);
            			   dbt.updateDownloadCount("P01","WSC", yesterday);
            			   System.out.println("There is only one file in WSC");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("P01","WSC",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists.\n-------------------------------------------------------------------------------------------");
            		}
            }else if(file.getName().substring(0,8).contains("EWINDERB")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\SEW");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\SEW\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\SEW\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\P01\\Invoices\\SEW").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of SEW invoices of P01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("P01", "INV","SEW", "TX",fname,yesterday);
            	    dbt.updateStatus1("P01","SEW",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("SEW", yesterday,"P01"))){
            		   dbt.insertDateWiseCount1("Siebel", "P01", "INV","SEW",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("P01","SEW",yesterday);
            			   dbt.updateDownloadCount("P01","SEW", yesterday);
            			   System.out.println("There is only one file in SEW");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("P01","SEW",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists.\n-------------------------------------------------------------------------------------------------");
            		}
            }else if(file.getName().substring(0,8).contains("INSUDERG")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\BSI");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	    }
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\BSI\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\BSI\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\BSI").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of FMS invoices of P05 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("P05", "INV","BSI", "TX",fname,yesterday);
            	    dbt.updateStatus1("P05","BSI",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("BSI", yesterday,"P05"))){
            		   dbt.insertDateWiseCount1("Siebel", "P05", "INV","BSI",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("P05","BSI",yesterday);
            			   dbt.updateDownloadCount("P05","BSI", yesterday);
            			   System.out.println("There is only one file in BSI");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("P05","BSI",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
            	}
            }else if(file.getName().substring(0,8).contains("AMCIDERG")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\SEC");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try{
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\SEC\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\SEC\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\SEC").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of SEC invoices of P05 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("P05", "INV","SEC", "TX",fname,yesterday);
            	    dbt.updateStatus1("P05","SEC",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("SEC", yesterday,"P05"))){
            		   dbt.insertDateWiseCount1("Siebel", "P05", "INV","SEC",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("P05","SEC",yesterday);
            			   dbt.updateDownloadCount("P05","SEC", yesterday);
            			   System.out.println("There is only one file in SEC");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("P05","SEC",yesterday);
            		       }
                     } catch (Exception e) {
                    	 System.out.println ("File already exists in structured folder.\n -----------------------------------------------------------");
                    }
                 }else if(file.getName().substring(0,8).contains("WINVDERG")) {
               	File theDir = new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\WSW");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\WSW\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\WSW\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\WSW").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of WSW invoices of P05 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("P05", "INV","WSW", "TX",fname,yesterday);
            	    dbt.updateStatus1("P05","WSW",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("WSW", yesterday,"P05"))){
            		   dbt.insertDateWiseCount1("Siebel", "P05", "INV","WSW",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("P05","WSW",yesterday);
            			   dbt.updateDownloadCount("P05","WSW", yesterday);
            			   System.out.println("There is only one file in WSW");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("P05","WSW",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists\n----------------------------------------------------------------------------");
            		}
            	
            }else if(file.getName().substring(0,8).contains("SERVDERG")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\WSC");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\WSC\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\WSC\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\WSC").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of WSC invoices of P05 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("P05", "INV","WSC", "TX",fname,yesterday);
            	    dbt.updateStatus1("P05","WSC",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("WSC", yesterday,"P05"))){
            		   dbt.insertDateWiseCount1("Siebel", "P05", "INV","WSC",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("P05","WSC",yesterday);
            			   dbt.updateDownloadCount("P05","WSC", yesterday);
            			   System.out.println("There is only one file in WSC");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("P05","WSC",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists.\n-------------------------------------------------------------------------------------------");
            		}
            }else if(file.getName().substring(0,8).contains("EWINDERG")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E05\\Invoices\\SEW");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\SEW\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\SEW\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\P05\\Invoices\\SEW").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of SEW invoices of P05 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("P05", "INV","SEW", "TX",fname,yesterday);
            	    dbt.updateStatus1("P05","SEW",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("SEW", yesterday,"P05"))){
            		   dbt.insertDateWiseCount1("Siebel", "P05", "INV","SEW",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("P05","SEW",yesterday);
            			   dbt.updateDownloadCount("P05","SEW", yesterday);
            			   System.out.println("There is only one file in SEW");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("P05","SEW",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists.\n-------------------------------------------------------------------------------------------------");
            		}
            }
         } else {
            listOfFiles(file);
         }
      }
   }
  
  
   
}