package Imacro;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FMLDBTransactionCTDMSInvoices {

//		    public static void main(String[] args) {
//		    	DBTransactions s = new DBTransactions();
//		    	//List<UnitDTO> unitlist1=s.getUnitConfiguration();
//		    	//System.out.println(s.getMailBody(unitlist1,"2021-03-18T09:32:50.908745Z",3));
//		    	//s.insertMailBox("na", "test",  "test", unitlist1,"2021-03-26T09:32:50.908745Z", 1); 
//		    }
	

	/**
	 * Insert data into Count_Log
	 */	
	public void insertDateWiseCount1(String unitName, String unitCode,String DocumentType,String Category, int noOfInv, String fileUploadOn) {
		DocumentType = DocumentType.substring(0, 3);
		
		String insertSql = "Insert Into [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] 	("
      		+ "		[SERInt_SourceApp]"
      		+ "  	,[SERInt_DocumentUnit]"    
      		+ "		,[SERInt_DocumentType]"
      		+ "      ,[SERInt_DocumentCategory]"
      		+ "      ,[SERInt_UploadFileCount]"
      		+ "      ,[SERInt_FileUploadOn]) "
      		//+ "	  values ('CTDMS','"+unit+"','PDF','"+invType+"', "+noOfInv+" ,GETDATE() );";
		+ "	  values ('"+unitName+"','"+unitCode+"','"+DocumentType+"','"+Category+"', '"+noOfInv+"' ,CONVERT(date,'"+fileUploadOn+"') );";

      ResultSet resultSet = null;

      try {
      	Connection connection = ConnectionUtil.getConnection();
          PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

          prepsInsertProduct.execute();
          // Retrieve the generated key from the insert.
          resultSet = prepsInsertProduct.getGeneratedKeys();

          // Print the ID of the inserted row.
          while (resultSet.next()) {
              System.out.println("Generated: " + resultSet.getString(1));
          }
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
		System.out.println("Date:" +fileUploadOn +",Unit:"+unitName+",Total:"+noOfInv);
	}	
	
	
	
	/**
	 * Insert data into Flow_Log-----checking to insert into flow log by reading from directory
	 */		
	/*public void recordDateWiseInvoiceNumbersReadingDirectory(String unit,String DocumentType,String invoicenumber,String fileUploadOn) {
		String subcat=invoicenumber.substring(0,2);
		
		//if(invoicenumber.substring(0,3).equalsIgnoreCase("BSA")) {
		//	subcat="BSA";
		//}
		//fileUploadOn="2021-03-18T09:32:50.908745Z";
		String insertSql = "Insert into [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] ("
				+ "      [SERInt_DocumentUnit]"
				+ "		 ,[SERInt_DocumentType] "
				+ "      ,[SERInt_DocumentCategory] "
				+ "      ,[SERInt_DocumentSubCategory] "
				+ "      ,[SERInt_DocumentNo] "
				+ "      ,[SERInt_iMicroReceivedStatus] "
				+ "      ,[SERInt_InvoiceDate]"
				+ "      ,[SERInt_SFTPUploadStatus])  "
				+ "	  values('"+unit+"','"+DocumentType+"','"+unit+"','"+subcat+"','"+invoicenumber+"','N', CONVERT(date,'"+fileUploadOn+"'),'N' );";

      ResultSet resultSet = null;

      try {
      	Connection connection = ConnectionUtil.getConnection();
          PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

          prepsInsertProduct.execute();
          // Retrieve the generated key from the insert.
          resultSet = prepsInsertProduct.getGeneratedKeys();

          // Print the ID of the inserted row.
          while (resultSet.next()) {
              System.out.println("Generated: " + resultSet.getString(1));
          }
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
		//insert invoice numbers
		System.out.println("Inserted data Date:" +fileUploadOn +",Unit:"+unit+",Total:"+invoicenumber);
	}*/
	
	
	
	/**
	 * Insert data into Flow_Log where print button is disabled
	 */		
	public void recordDateWiseInvoiceNumbersJSPRINTBUTTON(UnitDTO unit,String DocumentType,String invoicenumber,String fileUploadOn) {
		String subcat=invoicenumber.substring(0,2);
		
		//if(invoicenumber.substring(0,3).equalsIgnoreCase("BSA")) {
		//	subcat="BSA";
		//}
		//fileUploadOn="2021-03-18T09:32:50.908745Z";
		String insertSql = "Insert into [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] ("
				+ "      [SERInt_DocumentUnit]"
				+ "		 ,[SERInt_DocumentType] "
				+ "      ,[SERInt_DocumentCategory] "
				+ "      ,[SERInt_DocumentSubCategory] "
				+ "      ,[SERInt_DocumentNo] "
				+ "      ,[SERInt_iMicroReceivedStatus] "
				+ "      ,[SERInt_InvoiceDate]"
				+ "      ,[SERInt_SFTPUploadStatus]"
				+ "      ,[SERInt_InvoiceFlagStatus])  "
				+ "	  values('"+unit.getUnitCode()+"', '"+DocumentType+"','"+unit.getCategory()+"','"+subcat+"','"+invoicenumber+"','N', CONVERT(date,'"+fileUploadOn+"'),'N','Y' );";

      ResultSet resultSet = null;

      try {
      	Connection connection = ConnectionUtil.getConnection();
          PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

          prepsInsertProduct.execute();
          // Retrieve the generated key from the insert.
          resultSet = prepsInsertProduct.getGeneratedKeys();

          // Print the ID of the inserted row.
          while (resultSet.next()) {
              System.out.println("Generated: " + resultSet.getString(1));
          }
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
		//insert invoice numbers
		System.out.println("Inserted data Date:" +fileUploadOn +",Unit:"+unit+",Total:"+invoicenumber);
	}
	
	/**
	 * Insert data into Flow_Log
	 */		
	public void recordDateWiseInvoiceNumbers1(String unit,String DocumentType,String category,String subCategory,String invoicenumber,String fileUploadOn) {
		String subcat=invoicenumber.substring(0,2);
		
		//if(invoicenumber.substring(0,3).equalsIgnoreCase("BSA")) {
		//	subcat="BSA";
		//}
		//fileUploadOn="2021-03-18T09:32:50.908745Z";
		String insertSql = "Insert into [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] ("
				+ "      [SERInt_DocumentUnit]"
				+ "		 ,[SERInt_DocumentType] "
				+ "      ,[SERInt_DocumentCategory] "
				+ "      ,[SERInt_DocumentSubCategory] "
				+ "      ,[SERInt_DocumentNo] "
				+ "      ,[SERInt_iMicroReceivedStatus] "
				+ "      ,[SERInt_InvoiceDate]"
				+ "      ,[Download_Url]"
				+ "      ,[SERInt_SFTPUploadStatus])  "
				+ "	  values('"+unit+"', '"+DocumentType+"','"+category+"','"+subCategory+"','"+invoicenumber+"','N', CONVERT(date,'"+fileUploadOn+"'),'OLD','N' );";

      ResultSet resultSet = null;

      try {
      	Connection connection = ConnectionUtil.getConnection();
          PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

          prepsInsertProduct.execute();
          // Retrieve the generated key from the insert.
          resultSet = prepsInsertProduct.getGeneratedKeys();

          // Print the ID of the inserted row.
          while (resultSet.next()) {
              System.out.println("Generated: " + resultSet.getString(1));
          }
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
		//insert invoice numbers
		System.out.println("Inserted data Date:" +fileUploadOn +",Unit:"+unit+",Total:"+invoicenumber);
	}
	
	/**
	 * Method to insert the errors into Exception_log.
	 */
	public void insertExceptionLog(String DocumentNo, String appRef, String ExceptionDetails) {
		//appRef="Toyota- TKM-TOPSERV";
		String insertSql = "Insert into [MIDAS].[dbo].[ServiceInt_DocumentException_Log]( "
				+ "		[SERInt_DocumentNo] "
				+ "      ,[SERInt_AppReference] "
				+ "      ,[SERInt_ExceptionDetails] "
				+ "      ,[SERInt_ExceptionReceivedOn] "
				+ "  ) values "
				+"('"+DocumentNo+"','"+appRef+"','"+ExceptionDetails+"' ,GETDATE() );";

      ResultSet resultSet = null;

      try {
    	  Connection connection = ConnectionUtil.getConnection();
          PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
          prepsInsertProduct.execute();
          resultSet = prepsInsertProduct.getGeneratedKeys();

          while (resultSet.next()) {
              System.out.println("ExceptionLog Generated: " + resultSet.getString(1));
          }
      }
      catch (Exception e) {
          e.printStackTrace();
      }
		System.out.println("Date:" +",Unit:"+DocumentNo+",error:"+ExceptionDetails);
	}
	
	/**
	 * Method to insert into Mail_box.
	 */
	public void insertMailBox(String invno, String Specifc_Notification,  String classMethod, List<UnitDTO> unitlist,String date, int  bodyFormat, String errorMsg) {
		String HtmlBody=getMailBody(unitlist, date,bodyFormat,errorMsg);
		if(invno.length()>99) {
			System.out.println("dbt1---------"+invno);
			invno=invno.substring(0,99);
		}
		if(Specifc_Notification.length()>269) {
			System.out.println("dbt2---------"+Specifc_Notification);
			Specifc_Notification=Specifc_Notification.substring(0,269);
		}
		if(classMethod.length()>42) {
			System.out.println("db3t---------"+classMethod);
			classMethod=classMethod.substring(0,42);
		}
		String insertSql = "  Insert into [MIDAS].[dbo].[ServiceInt_MailBox]( "
				+ "       [DOC_NUMBER] "
				+ "      ,[RECEIPENT_TO] "
				+ "      ,[RECEIPENT_FROM] "
				+ "      ,[RECEIPMENT_CC] "
				+ "      ,[MAIL_SUBJECT] "
				+ "      ,[MAIL_MSG] "
				+ "      ,[TRIES] "
				+ "      ,[ERROR_SUCCESS] "
				+ "      ,[MAIL_CREATED_DATE] "
				+ "      ,[MAIL_CREATOR] "
				+ "      ,[DOC_STATUS] "
				+ "      ,[SOURCE_PAGE] "
				+ "      ,[ATTACHMENT_ID] "
				+ "  )  "
				+ "  values( "
				+ "  '"+invno+"', "
				//+ "  'Mukesh.yadav@mind-infotech.com;RPAGroup@mothersongroup.onmicrosoft.com;iMacroAdministrator@mothersongroup.onmicrosoft.com;Depain.Srivastava@mind-infotech.com;harshit.singh@mind-infotech.com;Sunil.Joshi@mind-infotech.com;Satvik.Singh@mind-infotech.com',"
				+ "  'Mukesh.yadav@mind-infotech.com;Satvik.Singh@mind-infotech.com',"
				+ "  'Mukesh.yadav@mind-infotech.com',"
				//+ "  'Mukesh.Prajapati@mind-infotech.com;Udham.Sorout@mind-infotech.com;Saurabh.Sharma@mind-infotech.com;Chandran.Soundararadjan@mind-infotech.com', "
				+ "  'Mukesh.Prajapati@mind-infotech.com', "
				//+ "  'IMacro log Mail notification-"+Specifc_Notification+"', "
				+ "  'IMacro log Mail notification-Espirit Toyota', "
				+ "  '"+HtmlBody+"', "
				+ "  0, "
				+ "  'newnew', "
				+ "  GETDATE(), "
				+ "  '0', "
				+ "  '-', "
				+ "  'iMacro-"+classMethod+"', "
				+ "  '0' "
				+ "  );";

      ResultSet resultSet = null;

      try {
      	Connection connection = ConnectionUtil.getConnection();
          PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
          prepsInsertProduct.execute();
          resultSet = prepsInsertProduct.getGeneratedKeys();

          while (resultSet.next()) {
              System.out.println("MailBox updated: " + resultSet.getString(1));
          }
      }
      catch (Exception e) {
          e.printStackTrace();
      }
		//System.out.println("Date:" +date1 +",Unit:"+unitCode+",error:"+ExceptionDetails);
	}
	
	/*
	 * Insert Mailbox for Daimler
	 */
	public void insertMailBoxDaimler(String invno, String Specifc_Notification,  String classMethod, List<UnitDTO> unitlist,String date, int  bodyFormat, String errorMsg) {
		String HtmlBody=getMailBody(unitlist, date,bodyFormat,errorMsg);
		if(invno.length()>99) {
			System.out.println("dbt1---------"+invno);
			invno=invno.substring(0,99);
		}
		if(Specifc_Notification.length()>269) {
			System.out.println("dbt2---------"+Specifc_Notification);
			Specifc_Notification=Specifc_Notification.substring(0,269);
		}
		if(classMethod.length()>42) {
			System.out.println("db3t---------"+classMethod);
			classMethod=classMethod.substring(0,42);
		}
		String insertSql = "  Insert into [MIDAS].[dbo].[ServiceInt_MailBox]( "
				+ "       [DOC_NUMBER] "
				+ "      ,[RECEIPENT_TO] "
				+ "      ,[RECEIPENT_FROM] "
				+ "      ,[RECEIPMENT_CC] "
				+ "      ,[MAIL_SUBJECT] "
				+ "      ,[MAIL_MSG] "
				+ "      ,[TRIES] "
				+ "      ,[ERROR_SUCCESS] "
				+ "      ,[MAIL_CREATED_DATE] "
				+ "      ,[MAIL_CREATOR] "
				+ "      ,[DOC_STATUS] "
				+ "      ,[SOURCE_PAGE] "
				+ "      ,[ATTACHMENT_ID] "
				+ "  )  "
				+ "  values( "
				+ "  '"+invno+"', "
				//+ "  'Mukesh.yadav@mind-infotech.com;Depain.Srivastava@mind-infotech.com;harshit.singh@mind-infotech.com;Sunil.Joshi@mind-infotech.com;Satvik.Singh@mind-infotech.com',"
				+ "  'Mukesh.yadav@mind-infotech.com;Satvik.Singh@mind-infotech.com',"
				+ "  'Mukesh.yadav@mind-infotech.com',"
				//+ "  'Mukesh.Prajapati@mind-infotech.com;Udham.Sorout@mind-infotech.com;Saurabh.Sharma@mind-infotech.com;Chandran.Soundararadjan@mind-infotech.com', "
				+ "  'Mukesh.Prajapati@mind-infotech.com', "
				//+ "  'IMacro Log Mail Notification-Trucking(Daimler)"+Specifc_Notification+"', "
				+ "  'IMacro Log Mail Notification-Trucking(Daimler)', "
				+ "  '"+HtmlBody+"', "
				+ "  0, "
				+ "  'newnew', "
				+ "  GETDATE(), "
				+ "  '0', "
				+ "  '-', "
				+ "  'iMacro-"+classMethod+"', "
				+ "  '0' "
				+ "  );";

      ResultSet resultSet = null;

      try {
      	Connection connection = ConnectionUtil.getConnection();
          PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
          prepsInsertProduct.execute();
          resultSet = prepsInsertProduct.getGeneratedKeys();

          while (resultSet.next()) {
              System.out.println("MailBox updated: " + resultSet.getString(1));
          }
      }
      catch (Exception e) {
          e.printStackTrace();
      }
		//System.out.println("Date:" +date1 +",Unit:"+unitCode+",error:"+ExceptionDetails);
	}
	
	/*
	 * Insert Mailbox for Renault
	 */
	public void insertMailBoxRenault(String invno, String Specifc_Notification,  String classMethod, List<UnitDTO> unitlist,String date, int  bodyFormat, String errorMsg) {
		String HtmlBody=getMailBody(unitlist, date,bodyFormat,errorMsg);
		if(invno.length()>99) {
			System.out.println("dbt1---------"+invno);
			invno=invno.substring(0,99);
		}
		if(Specifc_Notification.length()>269) {
			System.out.println("dbt2---------"+Specifc_Notification);
			Specifc_Notification=Specifc_Notification.substring(0,269);
		}
		if(classMethod.length()>42) {
			System.out.println("db3t---------"+classMethod);
			classMethod=classMethod.substring(0,42);
		}
		String insertSql = "  Insert into [MIDAS].[dbo].[ServiceInt_MailBox]( "
				+ "       [DOC_NUMBER] "
				+ "      ,[RECEIPENT_TO] "
				+ "      ,[RECEIPENT_FROM] "
				+ "      ,[RECEIPMENT_CC] "
				+ "      ,[MAIL_SUBJECT] "
				+ "      ,[MAIL_MSG] "
				+ "      ,[TRIES] "
				+ "      ,[ERROR_SUCCESS] "
				+ "      ,[MAIL_CREATED_DATE] "
				+ "      ,[MAIL_CREATOR] "
				+ "      ,[DOC_STATUS] "
				+ "      ,[SOURCE_PAGE] "
				+ "      ,[ATTACHMENT_ID] "
				+ "  )  "
				+ "  values( "
				+ "  '"+invno+"', "
				//+ "  'Mukesh.yadav@mind-infotech.com;Depain.Srivastava@mind-infotech.com;harshit.singh@mind-infotech.com;Sunil.Joshi@mind-infotech.com;Satvik.Singh@mind-infotech.com',"
				+ "  'Mukesh.yadav@mind-infotech.com;Satvik.Singh@mind-infotech.com',"
				+ "  'Mukesh.yadav@mind-infotech.com',"
				//+ "  'Mukesh.Prajapati@mind-infotech.com;Udham.Sorout@mind-infotech.com;Saurabh.Sharma@mind-infotech.com;Chandran.Soundararadjan@mind-infotech.com', "
				+ "  'Mukesh.Prajapati@mind-infotech.com', "
				//+ "  'IMacro Log Mail Notification-Trucking(Daimler)"+Specifc_Notification+"', "
				+ "  'IMacro Log Mail Notification-Prime Auto', "
				+ "  '"+HtmlBody+"', "
				+ "  0, "
				+ "  'newnew', "
				+ "  GETDATE(), "
				+ "  '0', "
				+ "  '-', "
				+ "  'iMacro-"+classMethod+"', "
				+ "  '0' "
				+ "  );";

      ResultSet resultSet = null;

      try {
      	Connection connection = ConnectionUtil.getConnection();
          PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
          prepsInsertProduct.execute();
          resultSet = prepsInsertProduct.getGeneratedKeys();

          while (resultSet.next()) {
              System.out.println("MailBox updated: " + resultSet.getString(1));
          }
      }
      catch (Exception e) {
          e.printStackTrace();
      }
		//System.out.println("Date:" +date1 +",Unit:"+unitCode+",error:"+ExceptionDetails);
	}
	
	public boolean ETCycleExeCheck(String date5) {
		//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
		 String insertSql = " SELECT COUNT([SERInt_SeqID]) as cSeqID from [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] "
		 	
		 		+ "where SERInt_FileUploadOn = DATEADD(day, -4, convert(date, GETDATE())) and SERInt_SourceApp='CTDMS'";
			boolean flag = true;
	        ResultSet resultSet = null;
	        try {
	        	Connection connection = ConnectionUtil.getConnection();
				Statement stmt=connection.createStatement(); 
	        	resultSet = stmt.executeQuery(insertSql);
	            while (resultSet.next()) {
	                System.out.println("ET DATA " + resultSet.getString("cSeqID"));
	                if((resultSet.getString("cSeqID").equalsIgnoreCase("0"))) {
	                	flag=false;
	                	 FMLDBTransactionCTDMSInvoices dbt = new FMLDBTransactionCTDMSInvoices();
	                	//dbt.insertMailBox( "NA",  "Imacro is not executed for ET "+date5,  "Imacro is not executed for ET.", dbt.getUnitConfiguration(), date5, 1,"");
	                	 dbt.insertMailBox( "NA",  "Invoices not downloaded for ET" +date5.substring(0,10)+ "due to N/w connectivity.",  "iMacroInvoiceDownloader-unitIterationDownload", dbt.getUnitConfiguration(), date5, 4, "Imacro is not executed for ET "+date5);
	                //	new Thread() { public void run() { IMacroAutomailer IMA= new
	                	//IMacroAutomailer(); IMA.sendMail(); }; }.start();
	                }                
	            }
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        return flag;
		}	

	
	/**
	 * Method to update invoice status in FlowLog
	 */	
	public void updateStatus1(String unitCode,String Category,String  DocumentNo,String iMicroReceivedOn,String status) {
		//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
		 String insertSql = "	  Update [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] set [SERInt_iMicroReceivedStatus]='"+status+"',  [SERInt_iMicroReceivedOn]=GETDATE() "
		 		+ "	  where [SERInt_DocumentNo]='"+DocumentNo+"' and [SERInt_InvoiceDate]=CONVERT(date,'"+iMicroReceivedOn+"') and [SERInt_DocumentCategory]='"+Category
		 		+"' and SERInt_DocumentUnit='"+unitCode+"'";

			System.out.println("called 3 "+DocumentNo);
	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
	            ResultSet resultSet = null;
	            prepsInsertProduct.execute();
	            resultSet = prepsInsertProduct.getGeneratedKeys();
	            // Print the ID of the inserted row.
	            while (resultSet.next()) {
	                System.out.println("iMicroReceivedStatus updated"+resultSet);
	                if(status.equalsIgnoreCase("Y")) {
	                	updateDownloadCount(unitCode,Category, iMicroReceivedOn);
	                }		                
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * Method to update SERInt_UploadFileCount in CountLog
	 */	
	public void updateUploadFileCount(UnitDTO unit,String iMicroReceivedOn) {
		//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
		 String insertSql2 = "Update [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] Set [SERInt_UploadFileCount]=( "
		 		+ "  Select count([SERInt_DocumentNo]) FROM [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] "
				 +" where [SERInt_DocumentUnit]='"+unit.getUnitCode()+"' and [SERInt_DocumentCategory]='"+unit.getCategory()+"' and [SERInt_InvoiceDate]=Convert(date,'"+iMicroReceivedOn+"')"
		 		+ ") "
		 		+ "  where [SERInt_DocumentUnit]='"+unit.getUnitCode()+"' and [SERInt_DocumentCategory]='"+unit.getCategory()+"' and [SERInt_FileUploadOn]=Convert(date,'"+iMicroReceivedOn+"')";
	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            PreparedStatement prepsInsertProduct2 = connection.prepareStatement(insertSql2, Statement.RETURN_GENERATED_KEYS);
	            ResultSet resultSet2 = null;
	            prepsInsertProduct2.execute();
	            resultSet2 = prepsInsertProduct2.getGeneratedKeys();
	            // Print the ID of the inserted row.
	            while (resultSet2.next()) {
	                System.out.println("SERInt_UploadFileCount updated");
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	/**
	 * Method to update iMacroUploadFileCount in CountLog
	 */	
	public void updateUploadFileCount2(String UC,String uCat,String iMicroReceivedOn) {
		//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
		 String insertSql2 = "Update [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] Set [SERInt_UploadFileCount]=( "
		 		+ "  Select count([SERInt_iMicroReceivedStatus]) FROM [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] "
				 +" where [SERInt_iMicroReceivedStatus] in ('N','Y') and [SERInt_DocumentUnit]='"+UC+"' and [SERInt_DocumentCategory]='"+uCat+"' and [SERInt_InvoiceDate]=Convert(date,'"+iMicroReceivedOn+"')"
		 		+ ") "
		 		+ "  where [SERInt_DocumentUnit]='"+UC+"' and [SERInt_DocumentCategory]='"+uCat+"' and [SERInt_FileUploadOn]=Convert(date,'"+iMicroReceivedOn+"')";
	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            PreparedStatement prepsInsertProduct2 = connection.prepareStatement(insertSql2, Statement.RETURN_GENERATED_KEYS);
	            ResultSet resultSet2 = null;
	            prepsInsertProduct2.execute();
	            resultSet2 = prepsInsertProduct2.getGeneratedKeys();
	            // Print the ID of the inserted row.
	            while (resultSet2.next()) {
	                System.out.println("iMicroDownloadCount updated");
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * Method to update iMacroUploadFileCount in CountLog
	 */	
	public void updateUploadFileCount2Renault(UnitDTO UC,String uCat,String iMicroReceivedOn) {
		//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
		 String insertSql2 = "Update [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] Set [SERInt_UploadFileCount]=( "
		 		+ "  Select count([SERInt_iMicroReceivedStatus]) FROM [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] "
				 +" where [SERInt_iMicroReceivedStatus] in ('N','Y') and [SERInt_DocumentUnit]='"+UC+"' and [SERInt_DocumentCategory]='"+uCat+"' and [SERInt_InvoiceDate]=Convert(date,'"+iMicroReceivedOn+"')"
		 		+ ") "
		 		+ "  where [SERInt_DocumentUnit]='"+UC+"' and [SERInt_DocumentCategory]='"+uCat+"' and [SERInt_FileUploadOn]=Convert(date,'"+iMicroReceivedOn+"')";
	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            PreparedStatement prepsInsertProduct2 = connection.prepareStatement(insertSql2, Statement.RETURN_GENERATED_KEYS);
	            ResultSet resultSet2 = null;
	            prepsInsertProduct2.execute();
	            resultSet2 = prepsInsertProduct2.getGeneratedKeys();
	            // Print the ID of the inserted row.
	            while (resultSet2.next()) {
	                System.out.println("iMicroDownloadCount updated");
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	
	
	
	/**
	 * Method to update iMacroUploadFileCount in CountLog
	 */	
	public void updateUploadFileCount1(String UC,String uCat,String iMicroReceivedOn) {
		//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
		 String insertSql2 = "Update [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] Set [SERInt_UploadFileCount]=( "
		 		+ "  Select count([SERInt_iMicroReceivedStatus]) FROM [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] "
				 +" where [SERInt_iMicroReceivedStatus]='Y' and [SERInt_DocumentUnit]='"+UC+"' and [SERInt_DocumentCategory]='"+uCat+"' and [SERInt_InvoiceDate]=Convert(date,'"+iMicroReceivedOn+"')"
		 		+ ") "
		 		+ "  where [SERInt_DocumentUnit]='"+UC+"' and [SERInt_DocumentCategory]='"+uCat+"' and [SERInt_FileUploadOn]=Convert(date,'"+iMicroReceivedOn+"')";
	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            PreparedStatement prepsInsertProduct2 = connection.prepareStatement(insertSql2, Statement.RETURN_GENERATED_KEYS);
	            ResultSet resultSet2 = null;
	            prepsInsertProduct2.execute();
	            resultSet2 = prepsInsertProduct2.getGeneratedKeys();
	            // Print the ID of the inserted row.
	            while (resultSet2.next()) {
	                System.out.println("iMicroDownloadCount updated");
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	/**
	 * Method to update iMacroDownloadCount in CountLog
	 */	
	public void updateDownloadCount(String UC,String uCat,String iMicroReceivedOn) {
		//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
		 String insertSql2 = "Update [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] Set [SERInt_iMicroDownloadCount]=( "
		 		+ "  Select count([SERInt_iMicroReceivedStatus]) FROM [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] "
				 +" where [SERInt_iMicroReceivedStatus]='Y' and [SERInt_DocumentUnit]='"+UC+"' and [SERInt_DocumentCategory]='"+uCat+"' and [SERInt_InvoiceDate]=Convert(date,'"+iMicroReceivedOn+"')"
		 		+ ") "
		 		+ "  where [SERInt_DocumentUnit]='"+UC+"' and [SERInt_DocumentCategory]='"+uCat+"' and [SERInt_FileUploadOn]=Convert(date,'"+iMicroReceivedOn+"')";
	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            PreparedStatement prepsInsertProduct2 = connection.prepareStatement(insertSql2, Statement.RETURN_GENERATED_KEYS);
	            ResultSet resultSet2 = null;
	            prepsInsertProduct2.execute();
	            resultSet2 = prepsInsertProduct2.getGeneratedKeys();
	            // Print the ID of the inserted row.
	            while (resultSet2.next()) {
	                System.out.println("iMicroDownloadCount updated");
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * update subCategory data into Flow_Log
	 */		
	public void updateSubCategory(UnitDTO unit,String date, String subCat, String invo) {
		String insertSql = "Update [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] "
				+ "	  Set [SERInt_DocumentSubCategory]='"+subCat+"'"
				+ "	  where [SERInt_InvoiceDate]=CONVERT(date,'"+date+"') and [SERInt_DocumentCategory]='"+unit.getCategory()
		 		+"' and SERInt_DocumentUnit='"+unit.getUnitCode()+"' and [SERInt_DocumentNo]='"+invo+"'";
      ResultSet resultSet = null;

      try {
      	Connection connection = ConnectionUtil.getConnection();
          PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
          prepsInsertProduct.execute();
          resultSet = prepsInsertProduct.getGeneratedKeys();
          while (resultSet.next()) {
              System.out.println("SubCategory updated ");
          }
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
	}
	
	/**
	 * update SFTPUploadStatus data into Flow_Log
	 */		
	public void updateSFTPUploadStatus(UnitDTO unit,String date, String status1, String docNo) {
		String insertSql = "Update [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] "
				+ "	  Set [SERInt_SFTPUploadStatus]='"+status1+"' , [SERInt_SFTPUploadDate]= Convert(datetime,GETDATE()) "
				+ "	  where [SERInt_InvoiceDate]=CONVERT(date,'"+date+"') and [SERInt_DocumentCategory]='"+unit.getCategory()
		 		+"' and SERInt_DocumentUnit='"+unit.getUnitCode()+"' and [SERInt_DocumentNo]='"+docNo+"'";
      ResultSet resultSet = null;
      //System.out.println("\n\n"+insertSql+"\n\n");

      try {
      	Connection connection = ConnectionUtil.getConnection();
          PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
          prepsInsertProduct.execute();
          resultSet = prepsInsertProduct.getGeneratedKeys();
          while (resultSet.next()) {
              System.out.println("SFTPUploadStatus updated ");
          }
          updateSFTPUploadCount(unit,date);
          updateSFTPUploadFailedCount(unit,date);
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
	}
	
	/**
	 * update SFTPUploadCount data into Count_Log
	 */		
	public void updateSFTPUploadCount(UnitDTO unit,String date) {
		String insertSql2 = "Update [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] Set [SERInt_iMicroSFTPUploadCount]=( "
		 		+ "  Select count([SERInt_SFTPUploadStatus]) FROM [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] "
				 +" where [SERInt_SFTPUploadStatus]='Y' and [SERInt_DocumentUnit]='"+unit.getUnitCode()+"' and [SERInt_DocumentCategory]='"+unit.getCategory()+"' and [SERInt_InvoiceDate]=Convert(date,'"+date+"')"
		 		+ ") "
		 		+ "  where [SERInt_DocumentUnit]='"+unit.getUnitCode()+"' and [SERInt_DocumentCategory]='"+unit.getCategory()+"' and [SERInt_FileUploadOn]=Convert(date,'"+date+"')";
	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            PreparedStatement prepsInsertProduct2 = connection.prepareStatement(insertSql2, Statement.RETURN_GENERATED_KEYS);
	            ResultSet resultSet2 = null;
	            prepsInsertProduct2.execute();
	            resultSet2 = prepsInsertProduct2.getGeneratedKeys();
	            // Print the ID of the inserted row.
	            while (resultSet2.next()) {
	                System.out.println("SERInt_iMicroSFTPUploadCount updated");
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * uploading the pdf invoices in databae WritePdfInMidas
	 */	
	public void WritePdfInMidas(File file,String fname) {
		
	//	File pdfFile = new File(file);
	
		try {
		
			byte[] pdfData = new byte[(int) file.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			dis.readFully(pdfData);  // read from file into byte[] array
			dis.close();

			   Connection connection = ConnectionUtil.getConnection();
			PreparedStatement ps = connection.prepareStatement(
			        "INSERT INTO ServiceInt_MasterJSON_Doc (" +
			                "Inv_CTDMSInvoiceNo, " +
			                "Inv_InvoicePdf, Inv_CreatedOn" +
			            ") VALUES (?,?,?)");
			ps.setString(1, fname);
			ps.setBytes(2, pdfData);
			java.util.Date today = new java.util.Date(); 
			ps.setDate(3, new java.sql.Date(today.getTime()));// byte[] array
			ps.executeUpdate();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	/**
	 * update SFTPUploadFailedCount data into Count_Log
	 */		
	public void updateSFTPUploadFailedCount(UnitDTO unit,String date) {
		String insertSql2 = "Update [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] Set [SERInt_iMicroSFTPUploadFailedCount]=( "
		 		+ "  Select count([SERInt_SFTPUploadStatus]) FROM [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] "
				 +" where [SERInt_SFTPUploadStatus] IN ('F','N') and [SERInt_DocumentUnit]='"+unit.getUnitCode()+"' and [SERInt_DocumentCategory]='"+unit.getCategory()+"' and [SERInt_InvoiceDate]=Convert(date,'"+date+"')"
		 		+ ") "
		 		+ "  where [SERInt_DocumentUnit]='"+unit.getUnitCode()+"' and [SERInt_DocumentCategory]='"+unit.getCategory()+"' and [SERInt_FileUploadOn]=Convert(date,'"+date+"')";
	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            PreparedStatement prepsInsertProduct2 = connection.prepareStatement(insertSql2, Statement.RETURN_GENERATED_KEYS);
	            ResultSet resultSet2 = null;
	            prepsInsertProduct2.execute();
	            resultSet2 = prepsInsertProduct2.getGeneratedKeys();
	            // Print the ID of the inserted row.
	            while (resultSet2.next()) {
	                System.out.println("SERInt_iMicroSFTPUploadFailedCount updated");
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * Method to update Remarks in CountLog
	 */	
	public void updateCountLogRemarks(UnitDTO unit,String iMicroReceivedOn, String remarks) {
		//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
		 String insertSql2 = "Update [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] Set [SERInt_Remarks]='"
		 		+ remarks
		 		+ "'  where [SERInt_DocumentUnit]='"+unit.getUnitCode()+"' and [SERInt_DocumentCategory]='"+unit.getCategory()+"' and [SERInt_FileUploadOn]=Convert(date,'"+iMicroReceivedOn+"')";
	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            PreparedStatement prepsInsertProduct2 = connection.prepareStatement(insertSql2, Statement.RETURN_GENERATED_KEYS);
	            ResultSet resultSet2 = null;
	            prepsInsertProduct2.execute();
	            resultSet2 = prepsInsertProduct2.getGeneratedKeys();
	            // Print the ID of the inserted row.
	            while (resultSet2.next()) {
	                System.out.println("Remarks updated");
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * Method to check if number of invoices in FLow_Log matches the number of invoices in Count_log
	 */
	public boolean flowCountCheck(UnitDTO unit,String fileUploadOn, int unitListAttempt, int unitAttempt) {
		

		//fileUploadOn="2021-03-18T09:32:50.908745Z";
		String insertSql = "Select SERInt_UploadFileCount As A from [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] where SERInt_DocumentUnit='"+unit.getUnitCode()+"' and SERInt_DocumentCategory='"+unit.getCategory()+"' and SERInt_FileUploadOn=CONVERT(date,'"+fileUploadOn+"') ";
		//System.out.println(insertSql);
      ResultSet resultSet = null;
      boolean r=true;

      try {
      	Connection connection = ConnectionUtil.getConnection();
			Statement stmt=connection.createStatement(); 
      	resultSet = stmt.executeQuery(insertSql);
      	while(resultSet.next()) {
          // Print the ID of the inserted row.
          if(Integer.parseInt(resultSet.getString("A"))!=getInvoiceCount(unit,fileUploadOn)) {
              	System.out.println("B");
              	r=false;
              	if(!(unitListAttempt==1 && unitAttempt==1)) {
              	 //insertExceptionLog(unit.getUnitCode(), "", unit.getUnitCode()+"-"+unit.getCategory() +"-Invoice Count incorrect and flow deleted",fileUploadOn);
              	}
              	delFlowLog(unit,fileUploadOn);              	
              }                
      	}
      
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
      return r;
	}

    /**
     * Method to get number of invoices in FLow_Log
     */
    public int getInvoiceCount(UnitDTO unit,String fileUploadOn) {
        //fileUploadOn="2021-03-18T09:32:50.908745Z";
        String insertSql = "Select COUNT(SERInt_DocumentType) As A from [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] where SERInt_DocumentUnit='"+unit.getUnitCode()+"' and SERInt_DocumentCategory='"+unit.getCategory()+"' and [SERInt_InvoiceDate]=CONVERT(date,'"+fileUploadOn+"') ";

      ResultSet resultSet = null;
      int r=0;
      try {
          Connection connection = ConnectionUtil.getConnection();
            Statement stmt=connection.createStatement(); 
          resultSet = stmt.executeQuery(insertSql);
          while(resultSet.next()) {
          r= Integer.parseInt(resultSet.getString("A"));
          }
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
      return r;
    }
    
    
    /**
     * Method to get number of invoices in Count_Log
     */
    public int getUploadCount(UnitDTO unit,String fileUploadOn) {
        //fileUploadOn="2021-03-18T09:32:50.908745Z";
        String insertSql = "Select [SERInt_UploadFileCount] As A from [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] where SERInt_DocumentUnit='"+unit.getUnitCode()+"' and SERInt_DocumentCategory='"+unit.getCategory()+"' and SERInt_FileUploadOn=CONVERT(date,'"+fileUploadOn+"') ";

      ResultSet resultSet = null;
      int r=0;
      try {
          Connection connection = ConnectionUtil.getConnection();
            Statement stmt=connection.createStatement(); 
          resultSet = stmt.executeQuery(insertSql);
          while(resultSet.next()) {
          r= Integer.parseInt(resultSet.getString("A"));
          }
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
      return r;
    }
    
    /**
     * Method to get number of invoices downloaded via iMacros from Count_Log
     */
    public int getiMacroDownloadCount(UnitDTO unit,String fileUploadOn) {
        //fileUploadOn="2021-03-18T09:32:50.908745Z";
        String insertSql = "Select [SERInt_iMicroDownloadCount] As A from [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] where SERInt_DocumentUnit='"+unit.getUnitCode()+"' and SERInt_DocumentCategory='"+unit.getCategory()+"' and SERInt_FileUploadOn=CONVERT(date,'"+fileUploadOn+"') ";

      ResultSet resultSet = null;
      int r=0;
      try {
          Connection connection = ConnectionUtil.getConnection();
            Statement stmt=connection.createStatement(); 
          resultSet = stmt.executeQuery(insertSql);
          while(resultSet.next()) {
          r= Integer.parseInt(resultSet.getString("A"));
          }
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
      return r;
    }
    
    /**
     * Method to get number of invoices uploaded to SftpServer from Count_Log
     */
    public int getStpUploadCount(UnitDTO unit,String fileUploadOn) {
        //fileUploadOn="2021-03-18T09:32:50.908745Z";
        String insertSql = "Select [SERInt_iMicroSFTPUploadCount] As A from [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] where SERInt_DocumentUnit='"+unit.getUnitCode()+"' and SERInt_DocumentCategory='"+unit.getCategory()+"' and SERInt_FileUploadOn=CONVERT(date,'"+fileUploadOn+"') ";

      ResultSet resultSet = null;
      int r=0;
      try {
          Connection connection = ConnectionUtil.getConnection();
            Statement stmt=connection.createStatement(); 
          resultSet = stmt.executeQuery(insertSql);
          while(resultSet.next()) {
          r= Integer.parseInt(resultSet.getString("A"));
          }
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
      return r;
    }
    
    /**
     * Method to get number of invoices not to be uploaded to SftpServer from Count_Log
     */
    public int getStpUploadFailedCount(UnitDTO unit,String fileUploadOn) {
        //fileUploadOn="2021-03-18T09:32:50.908745Z";
        String insertSql = "Select [SERInt_iMicroSFTPUploadFailedCount] As A from [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] where SERInt_DocumentUnit='"+unit.getUnitCode()+"' and SERInt_DocumentCategory='"+unit.getCategory()+"' and SERInt_FileUploadOn=CONVERT(date,'"+fileUploadOn+"') ";

      ResultSet resultSet = null;
      int r=0;
      try {
          Connection connection = ConnectionUtil.getConnection();
            Statement stmt=connection.createStatement(); 
          resultSet = stmt.executeQuery(insertSql);
          while(resultSet.next()) {
          r= Integer.parseInt(resultSet.getString("A"));
          }
      }
      // Handle any errors that may have occurred.
      catch (Exception e) {
          e.printStackTrace();
      }
      return r;
    }
    
    /**
     * Method to get HTML body
     */
    public String getMailBody(List<UnitDTO> unitlist,String date, int bodyFormat, String errorMsg) {
    	//String s1="Importance: High</br> </br><table>";
    	String s1="</br> </br><table>";
    	String rw="";
    	//bodyFormat==1 all 3 counts for all units are listed
    	if(bodyFormat==1) {
    		s1=s1+"<tr><th>Unit</th> <th>Category</th> <th>Date</th> <th>Total Invoices</th> <th>Download Count</th> <th>Sftp Upload Count</th></tr>";
    		for(UnitDTO unit: unitlist) {
    			rw="<tr><td>"+unit.getUnitCode()+"</td><td style=\"text-align:center;\">"+unit.getCategory()+"</td><td style=\"text-align:center;\">"+date.substring(0,10)+"</td><td style=\"text-align:center;\">" 
    					//getUploadCount(unit,(date))+"</td><td>"
    					
    					+getInvoiceCount(unit,(date))+"</td><td style=\"text-align:center;\">"
    					+getiMacroDownloadCount(unit,(date))+"</td><td style=\"text-align:center;\">"
    					+getStpUploadCount(unit,(date))+"</td></tr>";
    			s1=s1+rw;
    		}
    	}
    	//bodyFormat==2 flowCountCheck failed
    	else if(bodyFormat==2) {
    		s1=s1+"<tr><th>Unit</th> <th>Category</th> <th>Date</th> <th>uploadCount</th> <th>No of Invoices</th> </tr> ";
    		for(UnitDTO unit: unitlist) {
    			rw="<tr><td>"+unit.getUnitCode()+"</td><td>"+unit.getCategory()+"</td><td>"+imformat1(date)+"</td><td>"+
    					getUploadCount(unit,date)+"</td><td>"
    					+getInvoiceCount(unit,date)+"</td></tr>";
    			s1=s1+rw;
    		}
    	}
    	
    	//bodyFormat==2 if cycle not executed
    	else if(bodyFormat==4) {
    		s1="<tr><th></th> <th></th> <th></th> <th></th> <th></th> </tr> ";
    		for(UnitDTO unit: unitlist) {
    			rw="";
    			s1=s1+rw;
    		}
    	}
    	
    	//bodyFormat==3 some invoices missing
    	else if(bodyFormat==3){
    		s1=s1+"<tr><th>Unit</th> <th>Category</th> <th>Date</th> </tr> ";
    		s1=s1+"<br><table><tr><th>Missing Invoice list</th></tr>";
    		for(UnitDTO unit: unitlist) {
    			for (String l:missingInvoiceList(unit,date)) {
    				rw="<tr><td>"+l+"</td></tr>";
    				s1=s1+rw;
    			}
    		}
    	}
    	String s=s1+"</table>"+errorMsg;
    	String mB="<html lang=\"en\"><head><meta charset=\"utf-8\"><title>Mailer</title> <script src=\"chrome-extension://mooikfkahbdckldjjndioackbalphokd/assets/prompt.js\"></script></head><body style=\"background:#d2d3d5;\"><table bgcolor=\"#ffffff\" border=\"0\" celpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"font-family:Calibri;font-size:12px;color:#666666;margin:0 auto; width:100%\"><tbody><tr><td style=\"background:#aa1220;color:#fff;font-size:28px;padding:12px 12px 0\" valign=\"top\"><strong>iMacros Execution Summary</strong></td><td valign=\"top\" style=\"background:#aa1220;color:#fff;padding:12px\" align=\"right\">["+(imformat1(date)).substring(0,10)+"]<br>Mail Ref No. : [485399/2021]</td></tr>  <tr><td colspan=\"2\" style=\"padding:10px 12px;\"><strong>Dear Team</strong>,<br><br>"
    		  +"Please find below iMacros Execution Summary for your reference:<br><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border:1px solid #e7e8e9;font-family:Calibri;font-size:12px;color:#666666;\"></table></td></tr><tr><td style=\"padding:0 0 12px 12px;font-weight:300\">"
    		  +s
    		  +"</td> <td style=\"padding:0 12px 12px;font-weight:300\" valign=\"right\" align=\"right\"></td></tr><tr><td style=\"padding:0 0 12px 12px;font-weight:300\"><strong>From,</strong><br>iMacros Administrator</td><td style=\"padding:0 12px 12px;font-weight:300\" valign=\"right\" align=\"right\">iMacro<br> </td></tr></tbody></table>"
    		  +"</body></html>";
    	return mB;
    }
	
	/**
	 * Method to check the upload and download count i.e. if all the listed invoices are successfully downloaded
	 */	
	public boolean isInvoiceMissing(UnitDTO unit, String fileUploadOn, int unitListAttempt, int unitAttempt) {

			//fileUploadOn="2021-03-18T09:32:50.908745Z";
	        String insertSql = "  select SERInt_DocumentCategory, SERInt_UploadFileCount, SERInt_iMicroDownloadCount "
	                + "  FROM [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] "
	                + "  where SERInt_DocumentCategory='"+unit.getCategory()+"' and SERInt_DocumentUnit='"+unit.getUnitCode()+"' and SERInt_FileUploadOn=CONVERT(date,'"+fileUploadOn+"')";

	        ResultSet resultSet = null;
	        boolean r=false;

	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            Statement stmt=connection.createStatement(); 
	            resultSet = stmt.executeQuery(insertSql);

	            // Print the ID of the inserted row.
	            while (resultSet.next()) {
	               // System.out.println("SERInt_UploadFileCount: " + resultSet.getString("SERInt_UploadFileCount")+
	                //        " SERInt_iMicroDownloadCount: " + resultSet.getString("SERInt_iMicroDownloadCount"));
	                if(!(resultSet.getString("SERInt_UploadFileCount").equalsIgnoreCase(resultSet.getString("SERInt_iMicroDownloadCount")))
	               ) {
	                	
	                	  if(!resultSet.getString("SERInt_DocumentCategory").equalsIgnoreCase("FMS") && !resultSet.getString("SERInt_DocumentCategory").equalsIgnoreCase("SW") ) {
	                		  r=true;
	 	                }
	                	  
	                	if(!(unitListAttempt==1 && unitAttempt==1)) {
	                	// insertExceptionLog(unit.getUnitCode(), "", unit.getUnitCode()+"-"+unit.getCategory() +"-invoice missing",fileUploadOn);
	                	}
	                }
	               
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        return r;
		}

	/**
	 * Method to check number of entries in Count Log
	 */	
	public boolean checkCountLog1(String unitCategory, String fileUploadOn,String unitCode) {

		//fileUploadOn="2021-03-18T09:32:50.908745Z";
		String insertSql = "	  SELECT COUNT([SERInt_SeqID]) as cSeqID "
				+ "  FROM [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] where "+
				"SERInt_DocumentCategory='"+unitCategory+"' and SERInt_FileUploadOn=CONVERT(date,'"+fileUploadOn+"') "+
				"  and SERInt_DocumentUnit='"+unitCode+"'";
		boolean flag = true;
        ResultSet resultSet = null;
        try {
        	Connection connection = ConnectionUtil.getConnection();
			Statement stmt=connection.createStatement(); 
        	resultSet = stmt.executeQuery(insertSql);
            while (resultSet.next()) {
                System.out.println("A: " + resultSet.getString("cSeqID"));
                if((resultSet.getString("cSeqID").equalsIgnoreCase("0"))) {
                	flag=false;
                }                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
	}		
	
	/**
	 * Method to check number of entries in Count Log
	 */	
	public boolean checkCountLog1Renault(String unitCategory, String fileUploadOn,UnitDTO unitCode) {

		//fileUploadOn="2021-03-18T09:32:50.908745Z";
		String insertSql = "	  SELECT COUNT([SERInt_SeqID]) as cSeqID "
				+ "  FROM [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] where "+
				"SERInt_DocumentCategory='"+unitCategory+"' and SERInt_FileUploadOn=CONVERT(date,'"+fileUploadOn+"') "+
				"  and SERInt_DocumentUnit='"+unitCode+"'";
		boolean flag = true;
        ResultSet resultSet = null;
        try {
        	Connection connection = ConnectionUtil.getConnection();
			Statement stmt=connection.createStatement(); 
        	resultSet = stmt.executeQuery(insertSql);
            while (resultSet.next()) {
                System.out.println("A: " + resultSet.getString("cSeqID"));
                if((resultSet.getString("cSeqID").equalsIgnoreCase("0"))) {
                	flag=false;
                }                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
	}		
	/**
	 * Method to recall list of missing invoice
	 */	
		public List<String> missingInvoiceList(UnitDTO unit, String iMicroReceivedOn){
			List<String> Invoices2 = new ArrayList<String>();
			//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
	        String insertSql = "  SELECT [SERInt_DocumentNo]  FROM [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] where "
	        		+ "  [SERInt_iMicroReceivedStatus]<>'Y' and SERInt_DocumentCategory='"+unit.getCategory()
	        		+"' and [SERInt_InvoiceDate]=CONVERT(date,'"+iMicroReceivedOn+"') and SERInt_DocumentUnit='"+unit.getUnitCode()+"'";

	        ResultSet resultSet = null;

	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            Statement stmt=connection.createStatement(); 
	            resultSet = stmt.executeQuery(insertSql);

	            // Print the ID of the inserted row.
	            while (resultSet.next()) {
	            	Invoices2.add(resultSet.getString("SERInt_DocumentNo"));	                
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
			return Invoices2;
		}	
		
		// for connect SW and FMS invoices
		public List<UnitDTO> getUnitConfigurationFMSSW(){
			//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
	        String insertSql = "  SELECT [SERInt_UnitCode] "
	        		+ "      ,[SERInt_UnitName] "
	        		+ "      ,[SERInt_ApplicationType] "
	        		+ "      ,[SERInt_ApplicationName] "
	        		+ "      ,[SERInt_ApplicationURL] "
	        		+ "      ,[SERInt_ApplicationLoginID] "
	        		+ "      ,[SERInt_ApplicationPassword] "
	        		+ "      ,[SERInt_UnitStatus] "
	        		+ "      ,[SERInt_CreatedBy] "
	        		+ "      ,[SERInt_UnitCreatedOn] "
	        		+ "      ,[SERSInt_Category] "
	        		+ "      ,[SERSInt_CategoryLandingURL] "
	        		+ "  FROM [MIDAS].[dbo].[ServiceInt_UnitConfiguration] where [SERInt_UnitStatus]=1 and [SERInt_UnitCode] in ('E01','E02','E03') and [SERSInt_Category] in ('FMS','SW')";

	        ResultSet resultSet = null;
	        List<UnitDTO> Un = new ArrayList<UnitDTO>(); 

	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            Statement stmt=connection.createStatement(); 
	            resultSet = stmt.executeQuery(insertSql);

	            // Print the ID of the inserted row.
	            while (resultSet.next()) {
	    	        UnitDTO u = new UnitDTO(resultSet.getString("SERInt_UnitCode"), resultSet.getString("SERInt_UnitName"),
	    	        		resultSet.getString("SERInt_ApplicationType"), resultSet.getString("SERInt_ApplicationName"),
	    	        		resultSet.getString("SERInt_ApplicationURL"), resultSet.getString("SERInt_ApplicationLoginID"),
	    	        		resultSet.getString("SERInt_ApplicationPassword"), resultSet.getString("SERInt_UnitStatus"),
	    	        		resultSet.getString("SERInt_CreatedBy"), resultSet.getString("SERInt_UnitCreatedOn"),
	    	        		resultSet.getString("SERSInt_Category"), resultSet.getString("SERSInt_CategoryLandingURL"));
	    	        Un.add(u);	                
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
			return Un;
		}	
		
		//for Part sale invoices
		public List<UnitDTO> getUnitConfigurationASPart(){
			//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
	        String insertSql = "  SELECT [SERInt_UnitCode] "
	        		+ "      ,[SERInt_UnitName] "
	        		+ "      ,[SERInt_ApplicationType] "
	        		+ "      ,[SERInt_ApplicationName] "
	        		+ "      ,[SERInt_ApplicationURL] "
	        		+ "      ,[SERInt_ApplicationLoginID] "
	        		+ "      ,[SERInt_ApplicationPassword] "
	        		+ "      ,[SERInt_UnitStatus] "
	        		+ "      ,[SERInt_CreatedBy] "
	        		+ "      ,[SERInt_UnitCreatedOn] "
	        		+ "      ,[SERSInt_Category] "
	        		+ "      ,[SERSInt_CategoryLandingURL] "
	        		+ "  FROM [MIDAS].[dbo].[ServiceInt_UnitConfiguration] where [SERInt_UnitStatus]=1 and [SERSInt_Category]='AS'";

	        ResultSet resultSet = null;
	        List<UnitDTO> Un = new ArrayList<UnitDTO>(); 

	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            Statement stmt=connection.createStatement(); 
	            resultSet = stmt.executeQuery(insertSql);

	            // Print the ID of the inserted row.
	            while (resultSet.next()) {
	    	        UnitDTO u = new UnitDTO(resultSet.getString("SERInt_UnitCode"), resultSet.getString("SERInt_UnitName"),
	    	        		resultSet.getString("SERInt_ApplicationType"), resultSet.getString("SERInt_ApplicationName"),
	    	        		resultSet.getString("SERInt_ApplicationURL"), resultSet.getString("SERInt_ApplicationLoginID"),
	    	        		resultSet.getString("SERInt_ApplicationPassword"), resultSet.getString("SERInt_UnitStatus"),
	    	        		resultSet.getString("SERInt_CreatedBy"), resultSet.getString("SERInt_UnitCreatedOn"),
	    	        		resultSet.getString("SERSInt_Category"), resultSet.getString("SERSInt_CategoryLandingURL"));
	    	        Un.add(u);	                
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
			return Un;
		}	
		/**
		 * Method to recall Unit configuration details for table
		 */			
		public List<UnitDTO> getUnitConfiguration(){
			//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
	        String insertSql = "  SELECT [SERInt_UnitCode] "
	        		+ "      ,[SERInt_UnitName] "
	        		+ "      ,[SERInt_ApplicationType] "
	        		+ "      ,[SERInt_ApplicationName] "
	        		+ "      ,[SERInt_ApplicationURL] "
	        		+ "      ,[SERInt_ApplicationLoginID] "
	        		+ "      ,[SERInt_ApplicationPassword] "
	        		+ "      ,[SERInt_UnitStatus] "
	        		+ "      ,[SERInt_CreatedBy] "
	        		+ "      ,[SERInt_UnitCreatedOn] "
	        		+ "      ,[SERSInt_Category] "
	        		+ "      ,[SERSInt_CategoryLandingURL] "
	        		+ "  FROM [MIDAS].[dbo].[ServiceInt_UnitConfiguration] where [SERInt_UnitStatus]=1 and [SERInt_UnitCode] in('E01','E02','E03')";

	        ResultSet resultSet = null;
	        List<UnitDTO> Un = new ArrayList<UnitDTO>(); 

	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            Statement stmt=connection.createStatement(); 
	            resultSet = stmt.executeQuery(insertSql);

	            // Print the ID of the inserted row.
	            while (resultSet.next()) {
	    	        UnitDTO u = new UnitDTO(resultSet.getString("SERInt_UnitCode"), resultSet.getString("SERInt_UnitName"),
	    	        		resultSet.getString("SERInt_ApplicationType"), resultSet.getString("SERInt_ApplicationName"),
	    	        		resultSet.getString("SERInt_ApplicationURL"), resultSet.getString("SERInt_ApplicationLoginID"),
	    	        		resultSet.getString("SERInt_ApplicationPassword"), resultSet.getString("SERInt_UnitStatus"),
	    	        		resultSet.getString("SERInt_CreatedBy"), resultSet.getString("SERInt_UnitCreatedOn"),
	    	        		resultSet.getString("SERSInt_Category"), resultSet.getString("SERSInt_CategoryLandingURL"));
	    	        Un.add(u);	                
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
			return Un;
		}	
		
		
		/**
		 * Method to recall Unit configuration details for table
		 */			
		public List<UnitDTO> getUnitConfigurationDaimlerSW(){
			//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
	        String insertSql = "  SELECT [SERInt_UnitCode] "
	        		+ "      ,[SERInt_UnitName] "
	        		+ "      ,[SERInt_ApplicationType] "
	        		+ "      ,[SERInt_ApplicationName] "
	        		+ "      ,[SERInt_ApplicationURL] "
	        		+ "      ,[SERInt_ApplicationLoginID] "
	        		+ "      ,[SERInt_ApplicationPassword] "
	        		+ "      ,[SERInt_UnitStatus] "
	        		+ "      ,[SERInt_CreatedBy] "
	        		+ "      ,[SERInt_UnitCreatedOn] "
	        		+ "      ,[SERSInt_Category] "
	        		+ "      ,[SERSInt_CategoryLandingURL] "
	        		+ "  FROM [MIDAS].[dbo].[ServiceInt_UnitConfiguration] where [SERInt_UnitStatus]=1 and [SERSInt_Category]='SW' and [SERInt_UnitCode]='E04' ";

	        ResultSet resultSet = null;
	        List<UnitDTO> Un = new ArrayList<UnitDTO>(); 

	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            Statement stmt=connection.createStatement(); 
	            resultSet = stmt.executeQuery(insertSql);

	            // Print the ID of the inserted row.
	            while (resultSet.next()) {
	    	        UnitDTO u = new UnitDTO(resultSet.getString("SERInt_UnitCode"), resultSet.getString("SERInt_UnitName"),
	    	        		resultSet.getString("SERInt_ApplicationType"), resultSet.getString("SERInt_ApplicationName"),
	    	        		resultSet.getString("SERInt_ApplicationURL"), resultSet.getString("SERInt_ApplicationLoginID"),
	    	        		resultSet.getString("SERInt_ApplicationPassword"), resultSet.getString("SERInt_UnitStatus"),
	    	        		resultSet.getString("SERInt_CreatedBy"), resultSet.getString("SERInt_UnitCreatedOn"),
	    	        		resultSet.getString("SERSInt_Category"), resultSet.getString("SERSInt_CategoryLandingURL"));
	    	        Un.add(u);	                
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
			return Un;
		}	
		
		
		/**
		 * Method to recall Unit configuration details for table
		 */			
		public List<UnitDTO> getUnitConfigurationRenoInvoice(){
			//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
	        String insertSql = "  SELECT [SERInt_UnitCode] "
	        		+ "      ,[SERInt_UnitName] "
	        		+ "      ,[SERInt_ApplicationType] "
	        		+ "      ,[SERInt_ApplicationName] "
	        		+ "      ,[SERInt_ApplicationURL] "
	        		+ "      ,[SERInt_ApplicationLoginID] "
	        		+ "      ,[SERInt_ApplicationPassword] "
	        		+ "      ,[SERInt_UnitStatus] "
	        		+ "      ,[SERInt_CreatedBy] "
	        		+ "      ,[SERInt_UnitCreatedOn] "
	        		+ "      ,[SERSInt_Category] "
	        		+ "      ,[SERSInt_CategoryLandingURL] "
	        		+ "  FROM [MIDAS].[dbo].[ServiceInt_UnitConfiguration] where [SERInt_UnitStatus]=1 and [SERInt_UnitCode] in ('P01','P05') ";

	        ResultSet resultSet = null;
	        List<UnitDTO> Un = new ArrayList<UnitDTO>(); 

	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            Statement stmt=connection.createStatement(); 
	            resultSet = stmt.executeQuery(insertSql);

	            // Print the ID of the inserted row.
	            while (resultSet.next()) {
	    	        UnitDTO u = new UnitDTO(resultSet.getString("SERInt_UnitCode"), resultSet.getString("SERInt_UnitName"),
	    	        		resultSet.getString("SERInt_ApplicationType"), resultSet.getString("SERInt_ApplicationName"),
	    	        		resultSet.getString("SERInt_ApplicationURL"), resultSet.getString("SERInt_ApplicationLoginID"),
	    	        		resultSet.getString("SERInt_ApplicationPassword"), resultSet.getString("SERInt_UnitStatus"),
	    	        		resultSet.getString("SERInt_CreatedBy"), resultSet.getString("SERInt_UnitCreatedOn"),
	    	        		resultSet.getString("SERSInt_Category"), resultSet.getString("SERSInt_CategoryLandingURL"));
	    	        Un.add(u);	                
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
			return Un;
		}	
		
		/**
		 * Method to recall Unit configuration details for table
		 */			
		public List<UnitDTO> getUnitConfigurationFML(){
			//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
	        String insertSql = "  SELECT [SERInt_UnitCode] "
	        		+ "      ,[SERInt_UnitName] "
	        		+ "      ,[SERInt_ApplicationType] "
	        		+ "      ,[SERInt_ApplicationName] "
	        		+ "      ,[SERInt_ApplicationURL] "
	        		+ "      ,[SERInt_ApplicationLoginID] "
	        		+ "      ,[SERInt_ApplicationPassword] "
	        		+ "      ,[SERInt_UnitStatus] "
	        		+ "      ,[SERInt_CreatedBy] "
	        		+ "      ,[SERInt_UnitCreatedOn] "
	        		+ "      ,[SERSInt_Category] "
	        		+ "      ,[SERSInt_CategoryLandingURL] "
	        		+ "  FROM [MIDAS].[dbo].[ServiceInt_UnitConfiguration] where [SERInt_UnitStatus]=1 and [SERInt_UnitCode] in ('F01','F02','F03','F04') ";

	        ResultSet resultSet = null;
	        List<UnitDTO> Un = new ArrayList<UnitDTO>(); 

	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            Statement stmt=connection.createStatement(); 
	            resultSet = stmt.executeQuery(insertSql);

	            // Print the ID of the inserted row.
	            while (resultSet.next()) {
	    	        UnitDTO u = new UnitDTO(resultSet.getString("SERInt_UnitCode"), resultSet.getString("SERInt_UnitName"),
	    	        		resultSet.getString("SERInt_ApplicationType"), resultSet.getString("SERInt_ApplicationName"),
	    	        		resultSet.getString("SERInt_ApplicationURL"), resultSet.getString("SERInt_ApplicationLoginID"),
	    	        		resultSet.getString("SERInt_ApplicationPassword"), resultSet.getString("SERInt_UnitStatus"),
	    	        		resultSet.getString("SERInt_CreatedBy"), resultSet.getString("SERInt_UnitCreatedOn"),
	    	        		resultSet.getString("SERSInt_Category"), resultSet.getString("SERSInt_CategoryLandingURL"));
	    	        Un.add(u);	                
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
			return Un;
		}	
		
		
		
		/**
		 * Method to recall Unit configuration details for table
		 */			
		public List<UnitDTO> getUnitConfigurationDaimler2(){
			//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
	        String insertSql = "  SELECT [SERInt_UnitCode] "
	        		+ "      ,[SERInt_UnitName] "
	        		+ "      ,[SERInt_ApplicationType] "
	        		+ "      ,[SERInt_ApplicationName] "
	        		+ "      ,[SERInt_ApplicationURL] "
	        		+ "      ,[SERInt_ApplicationLoginID] "
	        		+ "      ,[SERInt_ApplicationPassword] "
	        		+ "      ,[SERInt_UnitStatus] "
	        		+ "      ,[SERInt_CreatedBy] "
	        		+ "      ,[SERInt_UnitCreatedOn] "
	        		+ "      ,[SERSInt_Category] "
	        		+ "      ,[SERSInt_CategoryLandingURL] "
	        		+ "  FROM [MIDAS].[dbo].[ServiceInt_UnitConfiguration] where [SERInt_UnitStatus]=1 and [SERInt_UnitCode] IN ('E04','E07','E09','E05','E08','E10') ";

	        ResultSet resultSet = null;
	        List<UnitDTO> Un = new ArrayList<UnitDTO>(); 

	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            Statement stmt=connection.createStatement(); 
	            resultSet = stmt.executeQuery(insertSql);

	            // Print the ID of the inserted row.
	            while (resultSet.next()) {
	    	        UnitDTO u = new UnitDTO(resultSet.getString("SERInt_UnitCode"), resultSet.getString("SERInt_UnitName"),
	    	        		resultSet.getString("SERInt_ApplicationType"), resultSet.getString("SERInt_ApplicationName"),
	    	        		resultSet.getString("SERInt_ApplicationURL"), resultSet.getString("SERInt_ApplicationLoginID"),
	    	        		resultSet.getString("SERInt_ApplicationPassword"), resultSet.getString("SERInt_UnitStatus"),
	    	        		resultSet.getString("SERInt_CreatedBy"), resultSet.getString("SERInt_UnitCreatedOn"),
	    	        		resultSet.getString("SERSInt_Category"), resultSet.getString("SERSInt_CategoryLandingURL"));
	    	        Un.add(u);	                
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
			return Un;
		}	
		
		
		/**
		 * Method to recall Unit configuration details for table
		 */			
		public UnitDTO getUnitConfig(String UnitCode, String UnitCategory){
			//iMicroReceivedOn="2021-03-18T09:32:50.908745Z";
	        String insertSql = "  SELECT [SERInt_UnitCode] "
	        		+ "      ,[SERInt_UnitName] "
	        		+ "      ,[SERInt_ApplicationType] "
	        		+ "      ,[SERInt_ApplicationName] "
	        		+ "      ,[SERInt_ApplicationURL] "
	        		+ "      ,[SERInt_ApplicationLoginID] "
	        		+ "      ,[SERInt_ApplicationPassword] "
	        		+ "      ,[SERInt_UnitStatus] "
	        		+ "      ,[SERInt_CreatedBy] "
	        		+ "      ,[SERInt_UnitCreatedOn] "
	        		+ "      ,[SERSInt_Category] "
	        		+ "      ,[SERSInt_CategoryLandingURL] "
	        		+ "  FROM [MIDAS].[dbo].[ServiceInt_UnitConfiguration] where [SERInt_UnitCode]='"+UnitCode+"' and [SERSInt_Category]='"+UnitCategory+"'";

	        ResultSet resultSet = null;
	        UnitDTO u = null; 

	        try {
	            Connection connection = ConnectionUtil.getConnection();
	            Statement stmt=connection.createStatement(); 
	            resultSet = stmt.executeQuery(insertSql);

	            // Print the ID of the inserted row.
	            while (resultSet.next()) {
	    	         u = new UnitDTO(resultSet.getString("SERInt_UnitCode"), resultSet.getString("SERInt_UnitName"),
	    	        		resultSet.getString("SERInt_ApplicationType"), resultSet.getString("SERInt_ApplicationName"),
	    	        		resultSet.getString("SERInt_ApplicationURL"), resultSet.getString("SERInt_ApplicationLoginID"),
	    	        		resultSet.getString("SERInt_ApplicationPassword"), resultSet.getString("SERInt_UnitStatus"),
	    	        		resultSet.getString("SERInt_CreatedBy"), resultSet.getString("SERInt_UnitCreatedOn"),
	    	        		resultSet.getString("SERSInt_Category"), resultSet.getString("SERSInt_CategoryLandingURL"));	                
	            }
	        }
	        // Handle any errors that may have occurred.
	        catch (Exception e) {
	            e.printStackTrace();
	        }
			return u;
		}

		/**
		 * Method to delete rows from countLog
		 */	
		public void delCountLog(String unit, String DocumentType, String invType, String fileUploadOn) {

			//fileUploadOn="2021-03-18T09:32:50.908745Z";
			 String insertSql = " Delete "
						+ "FROM [MIDAS].[dbo].[ServiceInt_DocumentUploadCount_Log] where "+
						"SERInt_DocumentCategory='"+invType+"' and SERInt_FileUploadOn=CONVERT(date,'"+fileUploadOn+"') "+
						"and SERInt_DocumentType='"+DocumentType+"'  and SERInt_DocumentUnit='"+unit+"'";


		        try {
		            Connection connection = ConnectionUtil.getConnection();
		            PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

		            ResultSet resultSet = null;
		            prepsInsertProduct.execute();
		            resultSet = prepsInsertProduct.getGeneratedKeys();
		            while (resultSet.next()) {
		                System.out.println("deleted from countlog");
		            }
		        }
		        catch (Exception e) {
		            e.printStackTrace();
		        }
		}

		/**
		 * Method to delete rows from FlowLog
		 */	
		public void delFlowLog(UnitDTO unit, String fileUploadOn) {

			//fileUploadOn="2021-03-18T09:32:50.908745Z";
			 String insertSql = " Delete "
						+ "FROM [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] where "+
						"SERInt_DocumentCategory='"+unit.getCategory()+"' and [SERInt_InvoiceDate]=CONVERT(date,'"+fileUploadOn+"') "+
						" and SERInt_DocumentUnit='"+unit.getUnitCode()+"'";


		        try {
		            Connection connection = ConnectionUtil.getConnection();
		            PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

		            ResultSet resultSet = null;
		            prepsInsertProduct.execute();
		            resultSet = prepsInsertProduct.getGeneratedKeys();
		            while (resultSet.next()) {
		                System.out.println("deleted from flowlog");
		            }
		        }
		        catch (Exception e) {
		            e.printStackTrace();
		        }
		}

		/**
		 * Method to delete rows from FlowLog for all of not given SubCategory
		 */	
		public void delFlowLogFNGSC(UnitDTO unit, String fileUploadOn, String SubCat) {

			//fileUploadOn="2021-03-18T09:32:50.908745Z";
			 String insertSql = " Delete "
						+ "FROM [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] where "+
						"SERInt_DocumentCategory='"+unit.getCategory()+"' and [SERInt_InvoiceDate]=CONVERT(date,'"+fileUploadOn+"') "+
						"and SERInt_DocumentSubCategory<>'"+SubCat+"' and SERInt_DocumentUnit='"+unit.getUnitCode()+"'";

			 //System.out.println("ReOrder Called3");
             //System.out.println("\n\ndeleted from flowlogFNGSC\n"+insertSql+"\n\n");

		        try {
		            Connection connection = ConnectionUtil.getConnection();
		            PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

		            ResultSet resultSet = null;
		            prepsInsertProduct.execute();
		            resultSet = prepsInsertProduct.getGeneratedKeys();
		            while (resultSet.next()) {
		                System.out.println("deleted from flowlogFNGSC");
		            }
		        }
		        catch (Exception e) {
		            e.printStackTrace();
		        }
		        updateUploadFileCount(unit, fileUploadOn);
		}

		/**
		 * Method to delete rows from FlowLog for all of not given SubCategory
		 */	
		public void delFlowLogFNGSCInRegressionReading(UnitDTO unit, String fileUploadOn, String SubCat) {

			//fileUploadOn="2021-03-18T09:32:50.908745Z";
			 String insertSql = " Delete "
						+ "FROM [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] where "+
						"SERInt_DocumentCategory='"+unit.getCategory()+"' and [SERInt_InvoiceDate]=CONVERT(date,'"+fileUploadOn+"') "+
						"and SERInt_DocumentSubCategory<>'"+SubCat+"' and SERInt_SFTPUploadStatus='N' and  SERInt_DocumentUnit='"+unit.getUnitCode()+"'";

			 //System.out.println("ReOrder Called3");
             //System.out.println("\n\ndeleted from flowlogFNGSC\n"+insertSql+"\n\n");

		        try {
		            Connection connection = ConnectionUtil.getConnection();
		            PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

		            ResultSet resultSet = null;
		            prepsInsertProduct.execute();
		            resultSet = prepsInsertProduct.getGeneratedKeys();
		            while (resultSet.next()) {
		                System.out.println("deleted from flowlogFNGSC");
		            }
		        }
		        catch (Exception e) {
		            e.printStackTrace();
		        }
		        updateUploadFileCount(unit, fileUploadOn);
		}

		/**
		* Method to get invoices names from flow_Log
		*/
		public List<String> getInvoiceNamePlanB(String unit,String cat,String fileUploadOn) {
		//fileUploadOn="2021-03-18T09:32:50.908745Z";
		String insertSql = "Select [SERInt_DocumentNo] As A from [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] where SERInt_DocumentUnit='"+unit+"' and SERInt_DocumentCategory='"+cat+"' and  SERInt_InvoiceDate=CONVERT(date,'"+fileUploadOn+"') ";



		ResultSet resultSet = null;
		List <String> r1=new ArrayList<String>();
		String r="";
		try {
		Connection connection = ConnectionUtil.getConnection();
		Statement stmt=connection.createStatement();
		resultSet = stmt.executeQuery(insertSql);
		while(resultSet.next()) {
		r= (resultSet.getString("A"));
		if(!r1.contains(r)) {
		r1.add(r);
		}
		}
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
		e.printStackTrace();
		}
		return r1;
		}	
		
		
		
		/**
		* Method to get invoices names from flow_Log
		*/
		public List<String> getInvoiceName(UnitDTO unit,String fileUploadOn) {
		//fileUploadOn="2021-03-18T09:32:50.908745Z";
		String insertSql = "Select [SERInt_DocumentNo] As A from [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] where SERInt_DocumentUnit='"+unit.getUnitCode()+"' and  SERInt_InvoiceDate=CONVERT(date,'"+fileUploadOn+"') ";



		ResultSet resultSet = null;
		List <String> r1=new ArrayList<String>();
		String r="";
		try {
		Connection connection = ConnectionUtil.getConnection();
		Statement stmt=connection.createStatement();
		resultSet = stmt.executeQuery(insertSql);
		while(resultSet.next()) {
		r= (resultSet.getString("A"));
		if(!r1.contains(r)) {
		r1.add(r);
		}
		}
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
		e.printStackTrace();
		}
		return r1;
		}	
		
		/**
		* Method to get invoices names from flow_Log
		*/
		public Map<String,String> getInvoiceNameRC(String unit,String fileUploadOn,String yesterday2) {
		//fileUploadOn="2021-03-18T09:32:50.908745Z";
		//String insertSql = "Select [SERInt_DocumentNo] As A from [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] where  SERInt_InvoiceDate=CONVERT(date,'"+fileUploadOn+"') ";
		String insertSql = "SELECT [SERInt_CTDMSInvoiceNo]  As A,SERInt_InvoiceDate As B from [MIDAS].[dbo].[ServiceInt_KPIInterface] where SERInt_DocumentUnit in ('E01') and  SERInt_InvoiceDate between CONVERT(date,'"+fileUploadOn+"') and CONVERT(date,'"+yesterday2+"') ";
		


		ResultSet resultSet = null;
		 Map<String, String> r1 = new HashMap<String, String>();
		//String r2="";
		 Map<String, String> r = new HashMap<String, String>();
		try {
		Connection connection = ConnectionUtil.getConnection();
		Statement stmt=connection.createStatement();
		resultSet = stmt.executeQuery(insertSql);
		while(resultSet.next()) {
		r.put(resultSet.getString("A"),resultSet.getString("B"));
		if(!r1.containsKey(r)) {
		r1.putAll(r);
		}
		}
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
		e.printStackTrace();
		}
		return r1;
		}	
		
		/**
		* Method to get invoices names from flow_Log
		*/
		public Map<String,String> getInvoiceNameASFMSSW(String unit,String fileUploadOn,String yesterday2) {
		//fileUploadOn="2021-03-18T09:32:50.908745Z";
		//String insertSql = "Select [SERInt_DocumentNo] As A from [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] where  SERInt_InvoiceDate=CONVERT(date,'"+fileUploadOn+"') ";
		String insertSql = "SELECT SERInt_DocumentNo  As A,SERInt_InvoiceDate As B from [MIDAS].[dbo].[ServiceInt_DocumentFlow_Log] where SERInt_DocumentUnit in ('E01') and SERInt_InvoiceDate between CONVERT(date,'"+fileUploadOn+"') and CONVERT(date,'"+yesterday2+"') and SERInt_DocumentCategory in ('AS','FMS','SW')  ";
		ResultSet resultSet = null;
		 Map<String, String> r1 = new HashMap<String, String>();
		//String r2="";
		 Map<String, String> r = new HashMap<String, String>();
		try {
		Connection connection = ConnectionUtil.getConnection();
		Statement stmt=connection.createStatement();
		resultSet = stmt.executeQuery(insertSql);
		while(resultSet.next()) {
		r.put(resultSet.getString("A"),resultSet.getString("B").substring(0, 10));
		if(!r1.containsKey(r)) {
		r1.putAll(r);
		}
		}
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
		e.printStackTrace();
		}
		return r1;
		}	
/**
 * Method to return date in the DD/MM/YYYY format from the YYYY-MM-DD format
 */	
	public String imformat1(String date1) {
		String d=date1.substring(8, 10)+"/"+date1.substring(5, 7)+"/"+date1.substring(0, 4)+""+date1.substring(10, 24);
		System.out.println(d+" "+"0000000000000000");
		return d;
	}

}

