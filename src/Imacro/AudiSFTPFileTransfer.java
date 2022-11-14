package Imacro;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;


public class AudiSFTPFileTransfer {
    private static final String REMOTE_HOST = "172.29.54.40";
    private static final String USERNAME = "mind/mukesh.prajapati";
    private static final String PASSWORD = "Mind@123456";
    static String yesterday1="";
    private static final int REMOTE_PORT = 22;
    public static void main(String args[]) throws IOException {
    	Instant now = Instant.now();
		Instant yesterday = now.minus(0, ChronoUnit.DAYS);
		System.out.println();
		yesterday1=yesterday.toString();
		System.out.println(yesterday1);
 	   File directoryPath = new File("D:\\AudiDelhiServiceData");

 	   AudiSFTPFileTransfer sftpFileTransfer=new AudiSFTPFileTransfer();
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
            String filepath=rootdirectory.replace("D:\\AudiDelhiServiceData", "");
			
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
    	
		if(filesList!=null){	 
    	  for(File file : filesList) {
    		  if(file.isDirectory()) {
    			  String filepath=file.getAbsolutePath().replace("D:\\AudiDelhiServiceData", "");
    		
    			    try {
    	            	channelSftp.mkdir(filepath);//creating root directory with date
    	            	System.out.println("Subdirectory directory created :"+file.getAbsolutePath().replace("D:\\", ""));
    	            }catch(Exception e) {
    	            	System.out.println("Subdirectory exists:"+filepath);
    	            }
    			//  }  
				  createChildDirectory(channelSftp,file);
			  }else if(file.isFile()) {
				    String filepath= file.getAbsolutePath().replace("D:\\", "").replace("\\", "/");
				    try {
					    channelSftp.lstat(filepath);
					} catch (SftpException e){
					    if(e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE){
					    	//if(dbt.getUploadCount(unit, fileUploadOn)==dbt.getStpUploadCount(unit, fileUploadOn) && dbt.getStpUploadCount(unit,fileUploadOn)!=0) {
					    	try {
					    		String[] s	=filepath.split("/");
							
								String fname=s[1];
							System.out.println(fname);
							if(fname.substring(0,7).equalsIgnoreCase("Vehicle")) {
								 File fileInD = new File("D:\\AudiDelhiServiceData\\Vehicle Data.xlsx");
							      
							        File rename = new File("D:\\AudiDelhiServiceData\\"+imformat(yesterday1)+" "+"Vehicle Data.xlsx");
							        boolean flag = file.renameTo(rename);
							        if (flag == true) {
							            System.out.println("File Successfully Rename");
							        }
							        else {
							            System.out.println("Operation Failed");
							        }
								//channelSftp.put(file.getAbsolutePath(),file.getAbsolutePath().replace("D:", "").replace("\\", "/"));
								channelSftp.put(rename.getAbsolutePath(),rename.getAbsolutePath().replace("D:", "").replace("\\", "/"));
							}else {}
						     
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
		String d=date.substring(8, 10)+"-"+date.substring(5, 7)+"-"+date.substring(0, 4);
		return d;
	}
    }

    
