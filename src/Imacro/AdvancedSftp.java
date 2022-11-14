package Imacro;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;


public class AdvancedSftp {
    private static final String REMOTE_HOST = "172.29.54.40";
    private static final String USERNAME = "mind/mukesh.prajapati";
    private static final String PASSWORD = "Mind@1234567";

    private static final int REMOTE_PORT = 22;
    public static void main(String args[]) throws IOException {
    	Instant now = Instant.now();
		Instant yesterday = now.minus(0, ChronoUnit.DAYS);//for directory path and databse
		System.out.println();
		String yesterday1=yesterday.toString();
		System.out.println(yesterday1);
 	   File directoryPath = new File("D:\\imacro\\"+imformat(yesterday1));

 	   AdvancedSftp sftpFileTransfer=new AdvancedSftp();
 	   sftpFileTransfer.createRootDirectory(directoryPath);
    }
    public void createRootDirectory(File directoryPath) {
        System.out.println(directoryPath+"/------//------/stp upload called");
    	Session jschSession = null;
        try {
            JSch jsch = new JSch();
            jsch.setKnownHosts("");
            java.util.Properties configuration = new java.util.Properties();
            configuration.put("StrictHostKeyChecking", "no");
            jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
            jschSession.setConfig(configuration);
            System.out.println("session obtained...");
            jschSession.setPassword(PASSWORD);
            jschSession.connect();
            System.out.println("connected..");
            Channel sftp = jschSession.openChannel("sftp");
            sftp.connect();
            ChannelSftp channelSftp = (ChannelSftp) sftp;
            String rootdirectory=directoryPath.getAbsolutePath();
            String filepath=rootdirectory.replace("D:\\imacro", "");
			//SftpATTRS attrs=channelSftp.stat(filepath);
			//if(attrs == null) {
            try {
            	channelSftp.mkdir(filepath);//creating root directory with date
            	System.out.println("Root directory created :"+rootdirectory);
            }catch(Exception e) {
            	System.out.println("Root Directory exists:"+filepath);
            }
			//}  
            createChildDirectory(channelSftp,directoryPath);
            channelSftp.exit();
        } catch (JSchException  e) {
            e.printStackTrace();
        }  finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
        System.out.println("Done");    	
    }
    public void createChildDirectory(ChannelSftp channelSftp,File directoryPath) {
    	  File filesList[] = directoryPath.listFiles();
    	 DBTransactions dbt = new DBTransactions();
    	 // if(getiMacroDownloadCount(UnitDTO, String)==getUploadCount(UnitDTO, String))
		if(filesList!=null){	 
    	  for(File file : filesList) {
    		  if(file.isDirectory()) {
    			  String filepath=file.getAbsolutePath().replace("D:\\imacro", "");
    			//  SftpATTRS attrs=channelSftp.stat(filepath);
    			//  if(attrs == null) {
    			    try {
    	            	channelSftp.mkdir(filepath);//creating root directory with date
    	            	System.out.println("Subdirectory directory created :"+file.getAbsolutePath().replace("D:\\imacro", ""));
    	            }catch(Exception e) {
    	            	System.out.println("Subdirectory exists:"+filepath);
    	            }
    			//  }  
				  createChildDirectory(channelSftp,file);
			  }else if(file.isFile()) {
				    String filepath= file.getAbsolutePath().replace("D:\\imacro", "").replace("\\", "/");
				    try {
					    channelSftp.lstat(filepath);
					} catch (SftpException e){
					    if(e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE){
					    	//if(dbt.getUploadCount(unit, fileUploadOn)==dbt.getStpUploadCount(unit, fileUploadOn) && dbt.getStpUploadCount(unit,fileUploadOn)!=0) {
					    	
					    	
					    	try {
					    		String[] sx	=filepath.split("/");
								String datex=sx[1];
								String unitx=sx[2];
								//String date= filepath.substring(1,9);
								datex = datex.substring(4)+"-"+datex.substring(2,4)+"-"+datex.substring(0,2)+" 00:00:00.000";
								String fnamex="";
								if(sx[5].substring(0,1).contains("F")){
								 fnamex=sx[5].substring(0,13);//change
								}else if(sx[5].substring(0,1).contains("A")){
									fnamex=sx[5].substring(0,10);
									
								}else{
									 fnamex=sx[5].substring(0,11);
								}
								DBTransactions dbt2=new DBTransactions();
								List<String>invL2=dbt2.getInvoiceNameforsftp(unitx,datex);
								if(invL2.contains(fnamex)) {
								channelSftp.put(file.getAbsolutePath(),file.getAbsolutePath().replace("D:\\imacro", "").replace("\\", "/"));
							String[] s	=filepath.split("/");
								String date=s[1];
								//String date= filepath.substring(1,9);
								date = date.substring(4)+"-"+date.substring(2,4)+"-"+date.substring(0,2)+" 00:00:00.000";
								String fname="";
								if(s[5].substring(0,1).contains("F")){
								 fname=s[5].substring(0,13);//change
								}else if(s[5].substring(0,1).contains("A")){
									fname=s[5].substring(0,10);
									
								}else{
									 fname=s[5].substring(0,11);
								}
								String uc=s[2];
								String ucat=s[4];
					//String fname=filepath.substring(26,37);//08042021E01InvoicesGSBSB21-00060.pdf /24032021/E01/Invoices/BP/INB20-01188.pdf
							System.out.println(date+" "+fname);
							
							dbt.updateSFTPUploadStatus(dbt.getUnitConfig(uc,ucat), date,"Y", fname);
							
							//upload pdf file
						String filename1 = fname.replace(".pdf","");
							new Thread() {
							public void run() {
							dbt.WritePdfInMidas( file,filename1,uc);
							}; }.start();
							System.out.println(uc+" "+ucat);
								}
							} catch (SftpException e1) {
							//} catch (Exception e1) {	
								e1.printStackTrace();
							}}
					    	System.out.println("File Uploaded:"+filepath);
					    	
					    } 
					}
			  }
		    } 
		  }
		  
    public String[] getDownloadedFilelist(String loc) {
		   File file = new File("D:\\iMacro\\"+loc);
		    // returns an array of all files
		   String[] fileList = file.list();
		    return fileList;
	}
    public static String imformat(String date) {
		String d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
		return d;
	}
    }

    
