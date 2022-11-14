package Imacro;
import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;  
  
public class SendAttachment{  

	  final String user="Mukesh.Prajapati@motherson.com";//change accordingly  
	  final String password="Mind@1234567";//change accordingly  
 public static void main(String [] args){
	 SendAttachment sd=new SendAttachment();
	//sd.sendmailF01("F01");
	 //sd.sendmailE02("E02");
	// sd.sendmailE03("E03");
	sd.sendmailForETLogin("E01");
 } 
 
 public void sendmailForETLPrintButtonIssue(String unit) {
	  Properties properties = System.getProperties();  
	  properties.setProperty("mail.smtp.host", "172.29.55.50");  
	  properties.put("mail.smtp.auth", "true"); 
	  Session session = Session.getDefaultInstance(properties,  
	   new javax.mail.Authenticator() {  
	   protected PasswordAuthentication getPasswordAuthentication() {  
	   return new PasswordAuthentication(user,password);  
	   }  
	  });  
	     
	  //2) compose message     
	  try{  
	    MimeMessage message = new MimeMessage(session);  
	    message.setFrom(new InternetAddress(user));  

	    Address[] toGroup = new Address[] {
	    	
	      
	        //   new InternetAddress("Saurabh.Sharma@motherson.com"),
	        //  new InternetAddress("Deepain.Srivastava@motherson.com"),
	            //new InternetAddress("Accounts4@espirit-toyota.com"),
	          // new InternetAddress("Udham.Sorout@motherson.com"),
	        		//new InternetAddress("accountsnoida8@espirit-toyota.com"),//E01         
	    	//new InternetAddress("sunil.joshi@motherson.com"),
	    	 new InternetAddress("Ankit.Jha@motherson.com"),
	    		new InternetAddress("Mukesh.Prajapati@motherson.com")
	    		
	            };

	    message.addRecipients(Message.RecipientType.TO,toGroup); 
	           // message.addRecipients(Message.RecipientType.CC, cc);
	    message.setSubject("ET Downloading Issue");  
	      
	    //3) create MimeBodyPart object and set your message text     
	    BodyPart messageBodyPart1 = new MimeBodyPart();  
	    messageBodyPart1.setText("Downloading is stopped due to -\n 1-Multiple checkbox Selected. \n 2-Print button disabled.");  
	      
	    //4) create new MimeBodyPart object and set DataHandler object to this object      
	    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
	  
	 /*   String filename = "D:/ReconcilationReport.xlsx";//change accordingly  
	    DataSource source = new FileDataSource(filename);  
	  messageBodyPart2.setDataHandler(new DataHandler(source));  
	   messageBodyPart2.setFileName(filename.replaceAll("D:/", ""));  
	   */
	     
	     
	    //5) create Multipart object and add MimeBodyPart objects to this object      
	    Multipart multipart = new MimeMultipart();  
	    multipart.addBodyPart(messageBodyPart1);  
	  //  multipart.addBodyPart(messageBodyPart2);  
	  
	    //6) set the multiplart object to the message object  
	    message.setContent(multipart );  
	     
	    //7) send message  
	    Transport.send(message);  
	   
	   System.out.println("message sent....");  
	   }catch (MessagingException ex) {ex.printStackTrace();}  
	 }  
 
 public void sendmailForETSessionExpired(String unit) {
	  Properties properties = System.getProperties();  
	  properties.setProperty("mail.smtp.host", "172.29.55.50");  
	  properties.put("mail.smtp.auth", "true"); 
	  Session session = Session.getDefaultInstance(properties,  
	   new javax.mail.Authenticator() {  
	   protected PasswordAuthentication getPasswordAuthentication() {  
	   return new PasswordAuthentication(user,password);  
	   }  
	  });  
	     
	  //2) compose message     
	  try{  
	    MimeMessage message = new MimeMessage(session);  
	    message.setFrom(new InternetAddress(user));  

	    Address[] toGroup = new Address[] {
	    	
	      
	        //   new InternetAddress("Saurabh.Sharma@motherson.com"),
	        //  new InternetAddress("Deepain.Srivastava@motherson.com"),
	            //new InternetAddress("Accounts4@espirit-toyota.com"),
	          // new InternetAddress("Udham.Sorout@motherson.com"),
	        		//new InternetAddress("accountsnoida8@espirit-toyota.com"),//E01         
	    	//new InternetAddress("sunil.joshi@motherson.com"),
	    	 new InternetAddress("Ankit.Jha@motherson.com"),
	    		new InternetAddress("Mukesh.Prajapati@motherson.com")
	    		
	            };

	    message.addRecipients(Message.RecipientType.TO,toGroup); 
	           // message.addRecipients(Message.RecipientType.CC, cc);
	    message.setSubject("LOGIN Session Expired"+" "+unit);  
	      
	    //3) create MimeBodyPart object and set your message text     
	    BodyPart messageBodyPart1 = new MimeBodyPart();  
	    messageBodyPart1.setText("Login Session expired due to Session Error");  
	      
	    //4) create new MimeBodyPart object and set DataHandler object to this object      
	    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
	  
	 /*   String filename = "D:/ReconcilationReport.xlsx";//change accordingly  
	    DataSource source = new FileDataSource(filename);  
	  messageBodyPart2.setDataHandler(new DataHandler(source));  
	   messageBodyPart2.setFileName(filename.replaceAll("D:/", ""));  
	   */
	     
	     
	    //5) create Multipart object and add MimeBodyPart objects to this object      
	    Multipart multipart = new MimeMultipart();  
	    multipart.addBodyPart(messageBodyPart1);  
	  //  multipart.addBodyPart(messageBodyPart2);  
	  
	    //6) set the multiplart object to the message object  
	    message.setContent(multipart );  
	     
	    //7) send message  
	    Transport.send(message);  
	   
	   System.out.println("message sent....");  
	   }catch (MessagingException ex) {ex.printStackTrace();}  
	 }  
 
 public void sendmailForETLogin(String unit) {
	  Properties properties = System.getProperties();  
	  properties.setProperty("mail.smtp.host", "172.29.55.50");  
	  properties.put("mail.smtp.auth", "true"); 
	  Session session = Session.getDefaultInstance(properties,  
	   new javax.mail.Authenticator() {  
	   protected PasswordAuthentication getPasswordAuthentication() {  
	   return new PasswordAuthentication(user,password);  
	   }  
	  });  
	     
	  //2) compose message     
	  try{  
	    MimeMessage message = new MimeMessage(session);  
	    message.setFrom(new InternetAddress(user));  

	    Address[] toGroup = new Address[] {
	    	
	      
	        //   new InternetAddress("Saurabh.Sharma@motherson.com"),
	        //  new InternetAddress("Deepain.Srivastava@motherson.com"),
	            //new InternetAddress("Accounts4@espirit-toyota.com"),
	          // new InternetAddress("Udham.Sorout@motherson.com"),
	        		//new InternetAddress("accountsnoida8@espirit-toyota.com"),//E01         
	    	//new InternetAddress("sunil.joshi@motherson.com"),
	    	 new InternetAddress("Ankit.Jha@motherson.com"),
	    		new InternetAddress("Mukesh.Prajapati@motherson.com")
	    		
	            };

	    message.addRecipients(Message.RecipientType.TO,toGroup); 
	           // message.addRecipients(Message.RecipientType.CC, cc);
	    message.setSubject("LOGIN ISSUE");  
	      
	    //3) create MimeBodyPart object and set your message text     
	    BodyPart messageBodyPart1 = new MimeBodyPart();  
	    messageBodyPart1.setText("LOGIN Failed. Trying to login again ");  
	      
	    //4) create new MimeBodyPart object and set DataHandler object to this object      
	    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
	  
	 /*   String filename = "D:/ReconcilationReport.xlsx";//change accordingly  
	    DataSource source = new FileDataSource(filename);  
	  messageBodyPart2.setDataHandler(new DataHandler(source));  
	   messageBodyPart2.setFileName(filename.replaceAll("D:/", ""));  
	   */
	     
	     
	    //5) create Multipart object and add MimeBodyPart objects to this object      
	    Multipart multipart = new MimeMultipart();  
	    multipart.addBodyPart(messageBodyPart1);  
	  //  multipart.addBodyPart(messageBodyPart2);  
	  
	    //6) set the multiplart object to the message object  
	    message.setContent(multipart );  
	     
	    //7) send message  
	    Transport.send(message);  
	   
	   System.out.println("message sent....");  
	   }catch (MessagingException ex) {ex.printStackTrace();}  
	 }  

 public void sendmailE03(String unit) {
	  //1) get the session object     
	  Properties properties = System.getProperties();  
	  properties.setProperty("mail.smtp.host", "172.29.55.50");  
	  properties.put("mail.smtp.auth", "true"); 
	  Session session = Session.getDefaultInstance(properties,  
	   new javax.mail.Authenticator() {  
	   protected PasswordAuthentication getPasswordAuthentication() {  
	   return new PasswordAuthentication(user,password);  
	   }  
	  });  
	     
	  //2) compose message     
	  try{  
	    MimeMessage message = new MimeMessage(session);  
	    message.setFrom(new InternetAddress(user));  

	    Address[] toGroup = new Address[] {
	    	
	       // new InternetAddress("Priyanka.Sharma01@motherson.com"),
	        //   new InternetAddress("Saurabh.Sharma@motherson.com"),
	        //  new InternetAddress("Deepain.Srivastava@motherson.com"),
	            //new InternetAddress("Accounts4@espirit-toyota.com"),
	           new InternetAddress("Ankit.Jha@motherson.com"),
	    		//new InternetAddress("Accounts4@espirit-toyota.com"),//E03
	        //new InternetAddress("sunil.joshi@motherson.com"),
	    		new InternetAddress("Mukesh.Prajapati@motherson.com")
	    		
	            };

	    message.addRecipients(Message.RecipientType.TO,toGroup); 
	           // message.addRecipients(Message.RecipientType.CC, cc);
	    message.setSubject("Reconcilation Report "+unit);  
	      
	    //3) create MimeBodyPart object and set your message text     
	    BodyPart messageBodyPart1 = new MimeBodyPart();  
	    messageBodyPart1.setText("Dear Sir,\n\nPlease find the Reconcilation Report for "+unit+".");  
	      
	    //4) create new MimeBodyPart object and set DataHandler object to this object      
	    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
	  
	    String filename = "D:/ReconcilationReport.xlsx";//change accordingly  
	    DataSource source = new FileDataSource(filename);  
	    messageBodyPart2.setDataHandler(new DataHandler(source));  
	    messageBodyPart2.setFileName(filename.replaceAll("D:/", ""));  
	     
	     
	    //5) create Multipart object and add MimeBodyPart objects to this object      
	    Multipart multipart = new MimeMultipart();  
	    multipart.addBodyPart(messageBodyPart1);  
	    multipart.addBodyPart(messageBodyPart2);  
	  
	    //6) set the multiplart object to the message object  
	    message.setContent(multipart );  
	     
	    //7) send message  
	    Transport.send(message);  
	   
	   System.out.println("message sent....");  
	   }catch (MessagingException ex) {ex.printStackTrace();}  
	 } 
 
 public void sendmailE02(String unit) {

	  
	  //1) get the session object     
	  Properties properties = System.getProperties();  
	  properties.setProperty("mail.smtp.host", "172.29.55.50");  
	  properties.put("mail.smtp.auth", "true");  
	  Session session = Session.getDefaultInstance(properties,  
	   new javax.mail.Authenticator() {  
	   protected PasswordAuthentication getPasswordAuthentication() {  
	   return new PasswordAuthentication(user,password);  
	   }  
	  });  
	     
	  //2) compose message     
	  try{  
	    MimeMessage message = new MimeMessage(session);  
	    message.setFrom(new InternetAddress(user));  

	    Address[] toGroup = new Address[] {
	    	
	       // new InternetAddress("Priyanka.Sharma01@motherson.com"),
	        //   new InternetAddress("Saurabh.Sharma@motherson.com"),
	        //  new InternetAddress("Deepain.Srivastava@motherson.com"),
	            //new InternetAddress("Accounts4@espirit-toyota.com"),
	          // new InternetAddress("Udham.Sorout@motherson.com"),
	    //	new InternetAddress("accountsgn2@espirit-toyota.com"),//E02
	    			           new InternetAddress("sunil.joshi@motherson.com"),
	    			           new InternetAddress("Ankit.Jha@motherson.com"),
	    		new InternetAddress("Mukesh.Prajapati@motherson.com")
	    		
	            };

	    message.addRecipients(Message.RecipientType.TO,toGroup); 
	           // message.addRecipients(Message.RecipientType.CC, cc);
	    message.setSubject("Reconcilation Report");  
	      
	    //3) create MimeBodyPart object and set your message text     
	    BodyPart messageBodyPart1 = new MimeBodyPart();  
	    messageBodyPart1.setText("Dear Sir,\n\nPlease find the Reconcilation Report for "+unit+".");  
	      
	    //4) create new MimeBodyPart object and set DataHandler object to this object      
	    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
	  
	    String filename = "D:/ReconcilationReport.xlsx";//change accordingly  
	    DataSource source = new FileDataSource(filename);  
	    messageBodyPart2.setDataHandler(new DataHandler(source));  
	    messageBodyPart2.setFileName(filename.replaceAll("D:/", ""));  
	     
	     
	    //5) create Multipart object and add MimeBodyPart objects to this object      
	    Multipart multipart = new MimeMultipart();  
	    multipart.addBodyPart(messageBodyPart1);  
	    multipart.addBodyPart(messageBodyPart2);  
	  
	    //6) set the multiplart object to the message object  
	    message.setContent(multipart );  
	     
	    //7) send message  
	    Transport.send(message);  
	   
	   System.out.println("message sent....");  
	   }catch (MessagingException ex) {ex.printStackTrace();}  
	 } 
 
 public void sendmailF04(String unit) {
	  //1) get the session object     
	  Properties properties = System.getProperties();  
	  properties.setProperty("mail.smtp.host", "172.29.55.50");  
	  properties.put("mail.smtp.auth", "true");  
	  Session session = Session.getDefaultInstance(properties,  
	   new javax.mail.Authenticator() {  
	   protected PasswordAuthentication getPasswordAuthentication() {  
	   return new PasswordAuthentication(user,password);  
	   }  
	  });  
	     
	  //2) compose message     
	  try{  
	    MimeMessage message = new MimeMessage(session);  
	    message.setFrom(new InternetAddress(user));  

	    Address[] toGroup = new Address[] {
	    		 new InternetAddress("Ankit.Jha@motherson.com"),
	       // new InternetAddress("Priyanka.Sharma01@motherson.com"),
	        //   new InternetAddress("Saurabh.Sharma@motherson.com"),
	        //  new InternetAddress("Deepain.Srivastava@motherson.com"),
	            //new InternetAddress("Accounts4@espirit-toyota.com"),
	          // new InternetAddress("Udham.Sorout@motherson.com"),
	        		//new InternetAddress("accountsnoida8@espirit-toyota.com"),//E01         
	    	//new InternetAddress("sunil.joshi@motherson.com"),
	    		new InternetAddress("Mukesh.Prajapati@motherson.com")
	    		
	            };

	    message.addRecipients(Message.RecipientType.TO,toGroup); 
	           // message.addRecipients(Message.RecipientType.CC, cc);
	    message.setSubject("Reconcilation Report");  
	      
	    //3) create MimeBodyPart object and set your message text     
	    BodyPart messageBodyPart1 = new MimeBodyPart();  
	    messageBodyPart1.setText("Dear Sir,\n\nPlease find the Reconcilation Report for "+unit+".");  
	      
	    //4) create new MimeBodyPart object and set DataHandler object to this object      
	    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
	  
	    String filename = "D:/ReconcilationReport.xlsx";//change accordingly  
	    DataSource source = new FileDataSource(filename);  
	    messageBodyPart2.setDataHandler(new DataHandler(source));  
	    messageBodyPart2.setFileName(filename.replaceAll("D:/", ""));  
	     
	     
	    //5) create Multipart object and add MimeBodyPart objects to this object      
	    Multipart multipart = new MimeMultipart();  
	    multipart.addBodyPart(messageBodyPart1);  
	    multipart.addBodyPart(messageBodyPart2);  
	  
	    //6) set the multiplart object to the message object  
	    message.setContent(multipart );  
	     
	    //7) send message  
	    Transport.send(message);  
	   
	   System.out.println("message sent....");  
	   }catch (MessagingException ex) {ex.printStackTrace();}  
	 }  

 
 public void sendmailF03(String unit) {
	  //1) get the session object     
	  Properties properties = System.getProperties();  
	  properties.setProperty("mail.smtp.host", "172.29.55.50");  
	  properties.put("mail.smtp.auth", "true");  
	  Session session = Session.getDefaultInstance(properties,  
	   new javax.mail.Authenticator() {  
	   protected PasswordAuthentication getPasswordAuthentication() {  
	   return new PasswordAuthentication(user,password);  
	   }  
	  });  
	     
	  //2) compose message     
	  try{  
	    MimeMessage message = new MimeMessage(session);  
	    message.setFrom(new InternetAddress(user));  

	    Address[] toGroup = new Address[] {
	    		  new InternetAddress("Ankit.Jha@motherson.com"),
	       // new InternetAddress("Priyanka.Sharma01@motherson.com"),
	        //   new InternetAddress("Saurabh.Sharma@motherson.com"),
	        //  new InternetAddress("Deepain.Srivastava@motherson.com"),
	            //new InternetAddress("Accounts4@espirit-toyota.com"),
	          // new InternetAddress("Udham.Sorout@motherson.com"),
	        		//new InternetAddress("accountsnoida8@espirit-toyota.com"),//E01         
	    	//new InternetAddress("sunil.joshi@motherson.com"),
	    		new InternetAddress("Mukesh.Prajapati@motherson.com")
	    		
	            };

	    message.addRecipients(Message.RecipientType.TO,toGroup); 
	           // message.addRecipients(Message.RecipientType.CC, cc);
	    message.setSubject("Reconcilation Report");  
	      
	    //3) create MimeBodyPart object and set your message text     
	    BodyPart messageBodyPart1 = new MimeBodyPart();  
	    messageBodyPart1.setText("Dear Sir,\n\nPlease find the Reconcilation Report for "+unit+".");  
	      
	    //4) create new MimeBodyPart object and set DataHandler object to this object      
	    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
	  
	    String filename = "D:/ReconcilationReport.xlsx";//change accordingly  
	    DataSource source = new FileDataSource(filename);  
	    messageBodyPart2.setDataHandler(new DataHandler(source));  
	    messageBodyPart2.setFileName(filename.replaceAll("D:/", ""));  
	     
	     
	    //5) create Multipart object and add MimeBodyPart objects to this object      
	    Multipart multipart = new MimeMultipart();  
	    multipart.addBodyPart(messageBodyPart1);  
	    multipart.addBodyPart(messageBodyPart2);  
	  
	    //6) set the multiplart object to the message object  
	    message.setContent(multipart );  
	     
	    //7) send message  
	    Transport.send(message);  
	   
	   System.out.println("message sent....");  
	   }catch (MessagingException ex) {ex.printStackTrace();}  
	 }  
 
 
 
 public void sendmailF01(String unit) {
	  Properties properties = System.getProperties();  
	  properties.setProperty("mail.smtp.host", "172.29.55.50");  
	  properties.put("mail.smtp.auth", "true"); 
	  Session session = Session.getDefaultInstance(properties,  
	   new javax.mail.Authenticator() {  
	   protected PasswordAuthentication getPasswordAuthentication() {  
	   return new PasswordAuthentication(user,password);  
	   }  
	  });  
	     
	  //2) compose message     
	  try{  
	    MimeMessage message = new MimeMessage(session);  
	    message.setFrom(new InternetAddress(user));  

	    Address[] toGroup = new Address[] {
	    	
	       new InternetAddress("Ankit.Jha@motherson.com"),
	        //   new InternetAddress("Saurabh.Sharma@motherson.com"),
	        //  new InternetAddress("Deepain.Srivastava@motherson.com"),
	            //new InternetAddress("Accounts4@espirit-toyota.com"),
	          // new InternetAddress("Udham.Sorout@motherson.com"),
	        		//new InternetAddress("accountsnoida8@espirit-toyota.com"),//E01         
	    	//new InternetAddress("sunil.joshi@motherson.com"),
	    		new InternetAddress("Mukesh.Prajapati@motherson.com")
	    		
	            };

	    message.addRecipients(Message.RecipientType.TO,toGroup); 
	           // message.addRecipients(Message.RecipientType.CC, cc);
	    message.setSubject("Reconcilation Report");  
	      
	    //3) create MimeBodyPart object and set your message text     
	    BodyPart messageBodyPart1 = new MimeBodyPart();  
	    messageBodyPart1.setText("Dear Sir,\n\nPlease find the Reconcilation Report for "+unit+".");  
	      
	    //4) create new MimeBodyPart object and set DataHandler object to this object      
	    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
	  
	    String filename = "D:/ReconcilationReport.xlsx";//change accordingly  
	    DataSource source = new FileDataSource(filename);  
	    messageBodyPart2.setDataHandler(new DataHandler(source));  
	    messageBodyPart2.setFileName(filename.replaceAll("D:/", ""));  
	     
	     
	    //5) create Multipart object and add MimeBodyPart objects to this object      
	    Multipart multipart = new MimeMultipart();  
	    multipart.addBodyPart(messageBodyPart1);  
	    multipart.addBodyPart(messageBodyPart2);  
	  
	    //6) set the multiplart object to the message object  
	    message.setContent(multipart );  
	     
	    //7) send message  
	    Transport.send(message);  
	   
	   System.out.println("message sent....");  
	   }catch (MessagingException ex) {ex.printStackTrace();}  
	 }  
 
  public void sendmailE01(String unit) {
  //1) get the session object     
  Properties properties = System.getProperties();  
  properties.setProperty("mail.smtp.host", "172.29.55.50");  
  properties.put("mail.smtp.auth", "true");  
  Session session = Session.getDefaultInstance(properties,  
   new javax.mail.Authenticator() {  
   protected PasswordAuthentication getPasswordAuthentication() {  
   return new PasswordAuthentication(user,password);  
   }  
  });  
     
  //2) compose message     
  try{  
    MimeMessage message = new MimeMessage(session);  
    message.setFrom(new InternetAddress(user));  

    Address[] toGroup = new Address[] {
    	
       // new InternetAddress("Priyanka.Sharma01@motherson.com"),
        //   new InternetAddress("Saurabh.Sharma@motherson.com"),
        //  new InternetAddress("Deepain.Srivastava@motherson.com"),
            //new InternetAddress("Accounts4@espirit-toyota.com"),
          // new InternetAddress("Udham.Sorout@motherson.com"),
        		//new InternetAddress("accountsnoida8@espirit-toyota.com"),//E01  
    		   new InternetAddress("Ankit.Jha@motherson.com"),
    	new InternetAddress("sunil.joshi@motherson.com"),
    		new InternetAddress("Mukesh.Prajapati@motherson.com")
    		
            };

    message.addRecipients(Message.RecipientType.TO,toGroup); 
           // message.addRecipients(Message.RecipientType.CC, cc);
    message.setSubject("Reconcilation Report");  
      
    //3) create MimeBodyPart object and set your message text     
    BodyPart messageBodyPart1 = new MimeBodyPart();  
    messageBodyPart1.setText("Dear Sir,\n\nPlease find the Reconcilation Report for "+unit+".");  
      
    //4) create new MimeBodyPart object and set DataHandler object to this object      
    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
  
    String filename = "D:/ReconcilationReport.xlsx";//change accordingly  
    DataSource source = new FileDataSource(filename);  
    messageBodyPart2.setDataHandler(new DataHandler(source));  
    messageBodyPart2.setFileName(filename.replaceAll("D:/", ""));  
     
     
    //5) create Multipart object and add MimeBodyPart objects to this object      
    Multipart multipart = new MimeMultipart();  
    multipart.addBodyPart(messageBodyPart1);  
    multipart.addBodyPart(messageBodyPart2);  
  
    //6) set the multiplart object to the message object  
    message.setContent(multipart );  
     
    //7) send message  
    Transport.send(message);  
   
   System.out.println("message sent....");  
   }catch (MessagingException ex) {ex.printStackTrace();}  
 }  
}  