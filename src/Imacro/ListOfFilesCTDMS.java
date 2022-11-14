package Imacro;
import java.io.File;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.*;
import java.io.IOException;
public class ListOfFilesCTDMS {
	static String fileDate="28102021";
	static String yesterday="2021-10-28";
	 public static void main(String args[]) throws IOException {
	      //Creating a File object for directory
	      File file = new File("D:\\Invoices");
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
            if(file.getName().substring(0,1).contains("F") && file.getName().substring(3,5).contains("NO")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\FMS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	    }
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\FMS\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\FMS\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\FMS").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of FMS invoices of E01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("E01", "INV","FMS", "TX",fname,yesterday);
            	    dbt.updateStatus1("E01","FMS",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("FMS", yesterday,"E01"))){
            		   dbt.insertDateWiseCount1("iConnect", "E01", "INV","FMS",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("E01","FMS",yesterday);
            			   dbt.updateDownloadCount("E01","FMS", yesterday);
            			   System.out.println("There is only one file in FMS");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("E01","FMS",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists in structured folder.\n-------------------------------------------------------");
            	}
            }else if(file.getName().substring(2,4).contains("NO")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\SW");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try{
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\SW\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\SW\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\SW").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of SW invoices of E01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("E01", "INV","SW", "TX",fname,yesterday);
            	    dbt.updateStatus1("E01","SW",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("SW", yesterday,"E01"))){
            		   dbt.insertDateWiseCount1("iConnect", "E01", "INV","SW",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("E01","SW",yesterday);
            			   dbt.updateDownloadCount("E01","SW", yesterday);
            			   System.out.println("There is only one file in SW");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("E01","SW",yesterday);
            		       }
                     } catch (Exception e) {
                    	 System.out.println ("File already exists in structured folder.\n -----------------------------------------------------------");
                    }
                 }else if(file.getName().substring(0,3).contains("EWB")) {
               	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\EW");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\EW\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\EW\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\EW").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of EW invoices of E01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("E01", "INV","EW", "TX",fname,yesterday);
            	    dbt.updateStatus1("E01","EW",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("EW", yesterday,"E01"))){
            		   dbt.insertDateWiseCount1("CTDMS", "E01", "INV","EW",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("E01","EW",yesterday);
            			   dbt.updateDownloadCount("E01","EW", yesterday);
            			   System.out.println("There is only one file in EW");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("E01","EW",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists\n----------------------------------------------------------------------------");
            		}
            	
            }else if(file.getName().substring(0,3).contains("TXB")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\GS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\GS\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\GS\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\GS").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of GS invoices of E01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("E01", "INV","GS", "TX",fname,yesterday);
            	    dbt.updateStatus1("E01","GS",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("GS", yesterday,"E01"))){
            		   dbt.insertDateWiseCount1("CTDMS", "E01", "INV","GS",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("E01","GS",yesterday);
            			   dbt.updateDownloadCount("E01","GS", yesterday);
            			   System.out.println("There is only one file in GS");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("E01","GS",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists.\n-------------------------------------------------------------------------------------------");
            		}
            }else if(file.getName().substring(0,3).contains("SSB")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\SSB");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\SSB\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\SSB\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\SSB").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of SSB invoices of E01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("E01", "INV","SSB", "TX",fname,yesterday);
            	    dbt.updateStatus1("E01","SSB",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("SSB", yesterday,"E01"))){
            		   dbt.insertDateWiseCount1("CTDMS", "E01", "INV","SSB",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("E01","SSB",yesterday);
            			   dbt.updateDownloadCount("E01","SSB", yesterday);
            			   System.out.println("There is only one file in SSB");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("E01","SSB",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists.\n-------------------------------------------------------------------------------------------------");
            		}
            }else if(file.getName().substring(0,3).contains("BSB")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\BSB");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\BSB\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\BSB\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\BSB").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of BSB invoices of E01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("E01", "INV","BSB", "TX",fname,yesterday);
            	    dbt.updateStatus1("E01","BSB",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("BSB", yesterday,"E01"))){
            		   dbt.insertDateWiseCount1("CTDMS", "E01", "INV","BSB",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("E01","BSB",yesterday);
            			   dbt.updateDownloadCount("E01","BSB", yesterday);
            			   System.out.println("There is only one file in BSB");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("E01","BSB",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists.\n------------------------------------------------------------------------------------");
            		}
            }else if(file.getName().substring(0,3).contains("INB")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\BS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	    System.out.println("New Directory Created"+theDir);
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\BS\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\BS\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\BS").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of BS invoices of E01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("E01", "INV","BS", "TX",fname,yesterday);
            	    dbt.updateStatus1("E01","BS",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("BS", yesterday,"E01"))){
            		   dbt.insertDateWiseCount1("CTDMS", "E01", "INV","BS",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("E01","BS",yesterday);
            			   dbt.updateDownloadCount("E01","BS", yesterday);
            			   System.out.println("There is only one file in BS");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("E01","BS",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists.\n---------------------------------------------------------------------------------------------");
            		}
            }else if(file.getName().substring(0,2).contains("AB")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\AS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\AS\\"+file.getName()));
            	System.out.println("Path temp- file moved in location-"+temp);
            	File f = new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\AS\\"+file.getName());
            	int totalCount=new File("D:\\iMacro\\"+fileDate+"\\E01\\Invoices\\AS").list().length;
            	Thread.sleep(1000);
            	System.out.println("Total number of AS invoices of E01 =="+totalCount);
            	if(f.exists()){
            	    System.out.println("File is present named=> "+f);
            	    String fname=file.getName().replace(".pdf","");
            	    dbt.recordDateWiseInvoiceNumbers1("E01", "INV","AS", "TX",fname,yesterday);
            	    dbt.updateStatus1("E01","AS",fname,yesterday,"Y");
            	     }
            	      if(!(dbt.checkCountLog1("AS", yesterday,"E01"))){
            		   dbt.insertDateWiseCount1("CTDMS", "E01", "INV","AS",0,yesterday);
            		   if(totalCount<2 && totalCount!=0) { 
            			   dbt.updateUploadFileCount1("E01","AS",yesterday);
            			   dbt.updateDownloadCount("E01","AS", yesterday);
            			   System.out.println("There is only one file in AS");
            		   }
            	     }else {
            		      DBTransactionCTDMSInvoices dbt1=new DBTransactionCTDMSInvoices();
            		      dbt1.updateUploadFileCount1("E01","AS",yesterday);
            		       }
            	} catch (Exception e) {
            		System.out.println ("File already exists.\n--------------------------------------------------------------------------------------------");
            		}
            }else if(file.getName().substring(0,1).contains("F") && file.getName().substring(3,5).contains("GE")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\FMS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\FMS\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(2,4).contains("GE")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\SW");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\SW\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,3).contains("TXD")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\GS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\GS\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,3).contains("EWD")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\EW");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\EW\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,3).contains("SSD")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\SSB");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\SSB\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,3).contains("BSD")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\BSB");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\BSB\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,3).contains("IND")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\BS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\BS\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,3).contains("TXD")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\FMS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\GS\\"+file.getName()));
            } catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,2).contains("AD")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\AS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E02\\Invoices\\AS\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,1).contains("F") && file.getName().substring(3,5).contains("DL")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\FMS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\FMS\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(2,4).contains("DL")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\SW");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\SW\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,3).contains("TXA")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\GS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\GS\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,3).contains("EWA")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\EW");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\EW\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,3).contains("SSA")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\SSB");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\SSB\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,3).contains("BSA")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\BSB");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\BSB\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,3).contains("INA")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\BS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\BS\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }else if(file.getName().substring(0,2).contains("AA")) {
            	File theDir = new File("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\AS");
            	if (!theDir.exists()){
            	    theDir.mkdirs();
            	}
            	try {
            	Path temp = Files.move
            	        (Paths.get("D:\\Invoices\\"+file.getName()),
            	        Paths.get("D:\\iMacro\\"+fileDate+"\\E03\\Invoices\\AS\\"+file.getName()));
            	} catch (Exception e) {System.out.println ("File already exists");}
            }
         } else {
            listOfFiles(file);
         }
      }
   }
  
  
   
}