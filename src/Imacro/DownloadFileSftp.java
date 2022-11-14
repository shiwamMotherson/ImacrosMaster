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


public class DownloadFileSftp {
    private static final String REMOTE_HOST = "172.29.54.40";
    private static final String USERNAME = "mind/mukesh.prajapati";
    private static final String PASSWORD = "Mind@123456";

    private static final int REMOTE_PORT = 22;
    public static void main(String args[]) throws IOException {
    	Instant now = Instant.now();
		Instant yesterday = now.minus(0, ChronoUnit.DAYS);
		System.out.println();
		String yesterday1=yesterday.toString();
		System.out.println(yesterday1);
 	

 	   DownloadFileSftp sftpFileTransfer=new DownloadFileSftp();
 	   sftpFileTransfer.createRootDirectory();
    }
    public void createRootDirectory() {
        
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
            createChildDirectory(channelSftp);
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
    public void createChildDirectory(ChannelSftp channelSftp) {
    	//ChannelSftp channelSftp1= setupJsch();
    	String remoteDir = "AudiDelhiServiceData/Vehicle Data.xlsx";
    	String localDir = "D:\\AUDI11\\";

    	try {
			channelSftp.get(remoteDir, localDir + "Abc.xlsx");
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	channelSftp.exit();
		  }
		  
 
    public static String imformat(String date) {
		String d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
		return d;
	}
    }

    
