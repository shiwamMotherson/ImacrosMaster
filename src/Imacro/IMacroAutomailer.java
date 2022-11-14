package Imacro;
import java.sql.*;
import java.io.*;
import java.util.Date;
import java.text.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;
public class IMacroAutomailer
{
public static void main( String args[])
	{
	//IMacroAutomailer IMA= new IMacroAutomailer();
		//IMA.sendMail();
	}
public void sendMail() {
Connection	con			=	null;		// JDBC Connection Object
Connection	con_1		=	null;		// JDBC Connection Object
Connection	con_2		=	null;		// JDBC Connection Object
Statement 	stmt			=	null;	   // JDBC Object for Statement
ResultSet	rs				=	null;	  // JDBC ResultSet Object
Connection	con1			=	null;	 // JDBC Connection Object
String	 sqlstr				=	null;	// Query String
String	 Ssmtp_server		=	null;
String	 smtpPort			=	null;
String	 dbdriver				=	null;
String	 dburl					=  null;
String	 dbuser				=  null;
String	dbpwd				=  null;
CallableStatement cstmt=		null;			// Object for Callable Statement
CallableStatement cstmt_1=		null;			// Object for Callable Statement
 // Get Connection and SMTP variables
 try
		{
	 		String strPropertyPath = "";
	 		FileInputStream propfile = null;
	 		IMacroAutomailer IMA= new IMacroAutomailer();
	 		strPropertyPath = new String(IMA.getClass().getResource(
					"/Imacro/application.properties").getPath());
	 		if (strPropertyPath != null) {
				strPropertyPath = strPropertyPath.replaceAll("%20", " ");
	 		}
			propfile = new FileInputStream(strPropertyPath);
			//FileInputStream propfile = new FileInputStream("application.properties");
			if ( propfile != null)
			{
				System.out.println("propfile not null");
				Properties pmsprop = new Properties();
				pmsprop.load( propfile);
				propfile.close();
				Ssmtp_server		=	pmsprop.getProperty("smtp_server");
				smtpPort			=	pmsprop.getProperty("smtp_port"); 
				dbdriver				=	pmsprop.getProperty("dbdriver");
				dburl					=	pmsprop.getProperty("dburl");
				dbuser				=	pmsprop.getProperty("dbuser");
				dbpwd				=	pmsprop.getProperty("dbpwd");
				String strMailId						=	"";
				String	 strMailFrom				=	"";
				String	 strMailTo					=	"";
				String	 strMailSubject			=	"";
				String	 strMailBody				=	"";
				String	 strStatus					=	"";
				String strMailCc					=	"";
				int iTrials								=	0;
				int iMaxTrials						=	10; 
				String strMailCcMsg				=	"";
				String strMailToElement		=	"";
				String strAttachFileName		=	"";
				byte[] attachFileBytes		=	null;
					try
					{
						Class.forName(dbdriver);
						con	=DriverManager.getConnection(dburl,dbuser,dbpwd);
						con_1=DriverManager.getConnection(dburl,dbuser,dbpwd);
						con_2=DriverManager.getConnection(dburl,dbuser,dbpwd);

						//Make Connection to retrieve the Mail Data based on status
						// Query modified by Gaurav on 07-Nov-2012.
						sqlstr=" SELECT  Mail_ID, [MAIL_Ref_No], [DOC_NUMBER], ISNULL(RTRIM([RECEIPENT_TO]),'NA') AS Mail_To, ISNULL(RTRIM([RECEIPENT_FROM]),'NA') AS Mail_From,  "
								+ "			 ISNULL(RTRIM([RECEIPMENT_CC]),'NA') AS Mail_CC, ISNULL(RTRIM([MAIL_SUBJECT]),'NA') AS Mail_Subject,  "
								+ "			 ISNULL([MAIL_MSG],'NA') AS Mail_Body, ISNULL(TRIES,0) AS Trials, ISNULL([ERROR_SUCCESS],'NA') AS  Error_Success "
								+ "				   FROM [MIDAS].[dbo].[ServiceInt_MailBox] where  (Error_Success IN ('newnew', 'ERROR')) AND (TRIES <= 50) ";
						stmt = con.createStatement(); 
						rs	   = stmt.executeQuery(sqlstr);
						while(rs.next())
						{
							strMailId					=	rs.getString("Mail_ID");
							strMailTo 					=	rs.getString("Mail_To");
							strMailFrom				=	rs.getString("Mail_From");
							strMailCc					=	rs.getString("Mail_CC");
							strMailSubject			=	rs.getString("Mail_Subject");
							strMailBody				=	rs.getString("Mail_Body");
							iTrials						=	rs.getInt("Trials");
							strStatus					=	rs.getString("Error_Success");
							strMailCcMsg			=	rs.getString("Mail_Body");
							//strAttachFileName	=	rs.getString("MAIL_ATTACHMENT");
							attachFileBytes		=	null;
							 String encodingOption = "text/html; charset=UTF-8";
							if(iTrials>=50)
							{
									strMailSubject ="Imacro Mail Could not be send- Trials conducted=10";
									strMailFrom	 ="mukesh.yadav@mind-infotech.com";
									strMailTo		 ="mukesh.yadav@mind-infotech.com";
									strMailCc		 ="Satvik.Singh@mind-infotech.com";
							}
									try
									{
										//Send Mails for TO
										Properties    props    =  System.getProperties();
										
										props.put("mail.smtp.host", Ssmtp_server);  
										props.put("mail.smtp.port", smtpPort);	
										Session    session    =  Session.getDefaultInstance(props, null);
										session.setDebug(false);
										MimeMessage   msg    =  new MimeMessage(session);
										msg.setSubject(strMailSubject, encodingOption);
										msg.setFrom(new InternetAddress(strMailFrom));
										//msg.addRecipient(Message.RecipientType.TO, new InternetAddress(strMailTo));
									if(strMailTo==null || strMailTo.equals(""))
									{
									}
									else
									{
										StringTokenizer st=new StringTokenizer(strMailTo,";");
										while(st.hasMoreElements())
										{
											strMailToElement=st.nextToken();
											if(!strMailToElement.equals(""))
											{
												msg.addRecipient(Message.RecipientType.TO, new InternetAddress(strMailToElement));
											}
										}
									}
									if(strMailCc==null || strMailCc.equals(""))
									{
									}
									else
									{
										StringTokenizer st=new StringTokenizer(strMailCc,";");
										while(st.hasMoreElements())
										{
											
										   msg.addRecipient(Message.RecipientType.CC, new InternetAddress(st.nextToken()));
										}
									}

										MimeBodyPart    mbp  =  new MimeBodyPart();
										mbp.setContent(strMailBody,encodingOption);
										Multipart      mp   =  new MimeMultipart();
										mp.addBodyPart(mbp);
										msg.saveChanges();
										msg.setSentDate(new Date());
										msg.setHeader("X-Priority", "1"); 
										msg.setHeader("x-msmail-priority", "high"); 
										  msg.setHeader("Content-Type", encodingOption);
										if(!strAttachFileName.equals("") && attachFileBytes!=null)
										   {
											   try{
												File someFile = new File(strAttachFileName);
											   	FileOutputStream fos = new FileOutputStream(someFile);
										        fos.write(attachFileBytes);					        
										        FileDataSource ds =new FileDataSource(someFile);	
										        MimeBodyPart attachmentPart  =  new MimeBodyPart();
										        attachmentPart.setDataHandler(new DataHandler(ds));
										        attachmentPart.setFileName(strAttachFileName);
										        mp.addBodyPart(attachmentPart);
										        msg.setContent(mp,encodingOption);
										        fos.flush();
										        fos.close();
										        someFile.deleteOnExit();
											   }
											   catch (IOException e) {
												   System.out.println("in Automailer-- attachment---");
												   e.printStackTrace();
												// TODO: handle exception
											}
											   catch (Exception e) {
												   e.printStackTrace();
												// TODO: handle exception
											}
										   }
										msg.setContent(mp);
										Transport.send(msg);
											try
											{
												//Update Mail Status
													/*cstmt=con_1.prepareCall("{?=call [MIDAS].[dbo].[PROC_SersINT_MailStatus_UPDATE](?,?,?)}");
													cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
													cstmt.setString(2, strMailId);
													cstmt.setString(3, "SEND");
													cstmt.setInt(4, iTrials+1);
													cstmt.execute();*/

												//Update Mail Status
												System.out.println("updating status of id.."+strMailId);
												cstmt=con_1.prepareCall("{call [Midas].[dbo].PROC_SersINT_MailStatus_UPDATE(?,?,?)}");
												// cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
												cstmt.setString(1, strMailId);
												cstmt.setString(2, "SEND");
												cstmt.setInt(3, iTrials+1);
												cstmt.execute();
												System.out.println("After sent mail status updated..");
											}
											catch(Exception e_2)
											{
													System.out.println("Error------77--"+e_2);
											}
									}
									catch(Exception e_1)
									{
										System.out.println("Error---88---"+e_1);
												//Update Mail Status in case of errors
														/*cstmt_1=con_2.prepareCall("{?=call [MIDAS].[dbo].[PROC_SersINT_MailStatus_UPDATE](?,?,?)}");
														cstmt_1.registerOutParameter(1,java.sql.Types.INTEGER);
														cstmt_1.setString(2, strMailId);
														cstmt_1.setString(3, "ERROR");
														cstmt_1.setInt(4, iTrials+1);
														cstmt_1.execute();*/
										//Update Mail Status in case of errors
										cstmt_1=con_2.prepareCall("{call [Midas].[dbo].[PROC_SersINT_MailStatus_UPDATE](?,?,?)}");
										//cstmt_1.registerOutParameter(1,java.sql.Types.INTEGER);
										cstmt_1.setString(1, strMailId);
										cstmt_1.setString(2, "ERROR");
										cstmt_1.setInt(3, iTrials+1);
										cstmt_1.execute();
									}
						}// End of While
						if(rs!=null) {
							rs.close();
						}
						
						if(stmt!=null) {
						stmt.close();
						}
						if(cstmt!=null) {
						cstmt.close();
						}
					}
					catch(Exception e)
					{
e.printStackTrace();
							System.out.println("Errror-----99----"+e);
					}
			}
			else
			{
			propfile.close();
			}
		}
		catch(Exception ex)
		{
System.out.print("Errror-----109----"+ex);
		}
	}//end of main
}//end of class
