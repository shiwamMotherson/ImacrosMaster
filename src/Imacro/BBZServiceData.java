package Imacro;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.time.Instant;
//import java.time.Instant;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import com.jacob.activeX.ActiveXComponent;

public class BBZServiceData {
	static int m=1;
	public static void main(String[] args) {
		BBZServiceData re= new BBZServiceData();
	   Instant now = Instant.now();
		Instant yesterday = now.minus(1,ChronoUnit.DAYS);
		System.out.println(yesterday);
		String d=yesterday.toString().substring(8, 10)+"/"+yesterday.toString().substring(5, 7)+"/"+yesterday.toString().substring(0, 4);
		System.out.println(d+"------------");
		   System.out.println(re.imformat(YearMonth.now().minusMonths( m).atDay(1).toString(),2));
		   System.out.println(re.imformat(YearMonth.now().minusMonths( m ).atEndOfMonth().toString(),2));
	 String p= ">="+d+" AND <="+d+"";
	 System.out.println(p);
     re.reportDownload(p);
}
	//this code is for diamler(DMS) Insurance ">=25/06/2021 AND <=25/06/2021"
	//working fine and integrated to upload on salesforce imformat(YearMonth.now().minusMonths( i ).atDay(1).toString(),1)
	//works on IE11
	
public void reportDownload(String p) {
	ActiveXComponent iim = new ActiveXComponent("imacros");
	String DateRange=p;
	//String DateRange=">=26/07/2021 AND <=26/07/2021";
	 System.out.println("Calling iimInit"); 
     iim.invoke("iimInit"); 
	 //https://dms.daimler-indiacv.com/edealer_enu/start.swe?SWECmd=Login&SWECM=S&SRN=&SWEHo=dms.daimler-indiacv.com
     //iim.invoke("iimPlay","CODE:URL GOTO=https://dms.daimler-indiacv.com/edealer_enu/start.swe?SWECmd=Login&SWECM=S&SRN=&SWEHo=dms.daimler-indiacv.com" );
	 iim.invoke("iimPlay","CODE:URL GOTO=https://dms.daimler-indiacv.com/edealer_enu/start.swe?SWECmd=Login&SWECM=S&SRN=&SWEHo=dms.daimler-indiacv.com" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"More information\"" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Go on to the webpage (not recommended)\"" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:SWEUserName CONTENT=D8GARGAM" );
     iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=NAME:SWEPassword CONTENT=dicvdms@123" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:Login" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=4" );
     List<InsuranceDTO> InList = new ArrayList<InsuranceDTO>();
     InsuranceDTO Ins1= null;
     //Job cards data
     Ins1=new InsuranceDTO();
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card\"" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_13_0" );
     iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Jobcard_Close_Date___Time" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Jobcard_Close_Date___Time CONTENT=\""+DateRange+"\"" );
  //   iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=TXT:Go");
    		// TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_10_0
     //iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_10_0" );
     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=ID:s_1_rc EXTRACT=TXT" );//no of Record Extract
    String iret16 =iim.invoke("iimGetLastExtract").toString();
    String iret17=iret16.replace("[EXTRACT]", "");//.trim();
    System.out.println(iret17+"extracted");
	
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL"); 
	  iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	 
	// iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
    if(!iret17.contains("No")) {
    	String iret18=iret17.split("of")[1].trim();
    	String iret19="";
    	int pg_no=0;
    	while(iret18.contains("+")) {
    		//loop over all 10 rows
    		// for(int k=1; k<3; k++) {
    		for(int k=1; k<11; k++) {
    		     Ins1=new InsuranceDTO();
    			 //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=NAME:_sweview" );
    		     //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=TXT:\"JBC01033B2100617MEC2817CCHP040268MUNISH KUMARClosedD8GARGAMBreakdown312,28411,2*\"" );
    			//search code
                 System.out.println("------------------------------------------------------------------1");
    		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card\"" );
    			 iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
    		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_13_0" ); 
    		     iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
    		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Jobcard_Close_Date___Time" );
    		     iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
    		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Jobcard_Close_Date___Time CONTENT=\""+DateRange+"\"" );
    		     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=TXT:Go");
    		     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_10_0" );
    		     iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
					/*
					 * if(k==1) { iim.invoke("iimPlay","CODE:WAIT SECONDS=4" ); }
					 */
    	    		//click next nuber of pg_no
    	    		for(int i=0;i<pg_no;i++) {
    	    			iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-end\"" );
    	    			iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
    	    		} 
    	    		//loop over row no i
    	    		 // iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    	    		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+k+"_s_1_l_VIN" );
    	    		 //.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    	   	     //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_VIN" );
    	   	    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=NAME:_sweview" );
    	   	     iim.invoke("iimPlay","CODE:TAB T=1" );//TAG POS=11 TYPE=TBODY ATTR=* EXTRACT=TXT
    	   	    // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_220_0 EXTRACT=TXT");
    	   	  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_222_0 EXTRACT=TXT");
    	   	     String ireta = iim.invoke("iimGetLastExtract").toString();
    	   	    	String strNoOfRecorda=ireta.replace("[EXTRACT]", "");
    	   	    	System.out.println("1-VINno-"+strNoOfRecorda);
    	   	    	Ins1.setChassis_No__c(strNoOfRecorda);
    	   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	     iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_61_0 EXTRACT=TXT");
    	   	     String iretb = iim.invoke("iimGetLastExtract").toString();
    	   	    	String strNoOfRecordb=iretb.replace("[EXTRACT]", "");
    	   	    	System.out.println("Reg No-"+strNoOfRecordb);
    	   	    	Ins1.setReg_No__c(strNoOfRecordb);
    	   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	     iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_46_0 EXTRACT=TXT");
    	   	     String iretc = iim.invoke("iimGetLastExtract").toString();
    	   	    	String strNoOfRecordc=iretc.replace("[EXTRACT]", "");
    	   	    	System.out.println("Model No-"+strNoOfRecordc);
    	   	    	Ins1.setModel1__c(strNoOfRecordc);
    	   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    				
    	   	    	Ins1.setSite__c("Bharat Benz Ghaziabad");
    		    	Ins1.setMake__c("BharatBenz");
    	   	    	
    	   	    	 iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_67_0 EXTRACT=TXT");
    	   	         String iret4 = iim.invoke("iimGetLastExtract").toString();
    	   	        	String strNoOfRecord4=iret4.replace("[EXTRACT]", "");
    	   	        	System.out.println("Sold Date-"+strNoOfRecord4);
    	   	        	Ins1.setSold_Date__c(strNoOfRecord4.replaceAll("/", " "), 3);
    	   	        	Ins1.setInsurance_Renewal_Date__c(strNoOfRecord4.replaceAll("/", " "), 4);
    	   	        	Ins1.setModel_Year__c(strNoOfRecord4.split("/",3)[2]);
    	   	        	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	        	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	        	
    	   	        	 iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_69_0 EXTRACT=TXT");
    	   	             String iret5 = iim.invoke("iimGetLastExtract").toString();
    	   	            	String strNoOfRecord5=iret5.replace("[EXTRACT]", "");
    	   	            	System.out.println("Driver name-"+strNoOfRecord5);
    	   	            	Ins1.setDriver_Name__c(strNoOfRecord5);
    	   	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	            	
    	   	            	// iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_157_0 EXTRACT=TXT");
    	   	            	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_159_0 EXTRACT=TXT");
    	   	                 String iret6 = iim.invoke("iimGetLastExtract").toString();
    	   	                	String strNoOfRecord6=iret6.replace("[EXTRACT]", "");
    	   	                	System.out.println("Driver Phone No-"+strNoOfRecord6);
    	   	                	Ins1.setDriver_Phone_No__c(strNoOfRecord6);
    	   	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	                	
    	   	                	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+k+"_s_1_l_Account EXTRACT=TXT" );
    	   	                	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Account EXTRACT=TXT" ); 
    	   	                	String iret12 = iim.invoke("iimGetLastExtract").toString();
    	   	                	String strNoOfRecord12=iret12.replace("[EXTRACT]", "");
    	   	                	System.out.println("Account Name-"+strNoOfRecord12);
    	   	                	Ins1.setName(strNoOfRecord12);
    	   	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");//CONTENT=$\""+arrOfStra[j]+"\""
    	   	                	if(!strNoOfRecord12.equalsIgnoreCase("#EANF#")) {
    	   	                	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\""+strNoOfRecord12+"\"" );
    	   	    	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"MUNISH KUMAR\"" );
    	   	    	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_63_0 EXTRACT=TXT");
    	   	        String irete = iim.invoke("iimGetLastExtract").toString();
    	   	       	String strNoOfRecorde=irete.replace("[EXTRACT]", "");
    	   	       	System.out.println("Fleet Owner-"+strNoOfRecorde);
    	   	     Ins1.setFleet_Owner__c(strNoOfRecorde);
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_65_0 EXTRACT=TXT");
    	   	        String iretf = iim.invoke("iimGetLastExtract").toString();
    	   	       	String strNoOfRecordf=irete.replace("[EXTRACT]", "");
    	   	       	System.out.println("Fleet Mobile-"+strNoOfRecordf);
    	   	     Ins1.setFleet_Owner_Mobile__c(strNoOfRecordf);
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	            iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_137_0 EXTRACT=TXT");
    		             String iret5x = iim.invoke("iimGetLastExtract").toString();
    		            	String strNoOfRecord5x=iret5x.replace("[EXTRACT]", "");
    		            	System.out.println("Adress"+strNoOfRecord5x);//for address full
    		            	Ins1.setAddress__c(strNoOfRecord5x);
    		            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");

    		              //  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_139_0 EXTRACT=TXT");
    		            	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_140_0 EXTRACT=TXT");
    		    	             String iret5x3 = iim.invoke("iimGetLastExtract").toString();
    		    	            	String strNoOfRecord5x3=iret5x3.replace("[EXTRACT]", "");
    		    	            	System.out.println("city"+strNoOfRecord5x3);//for  city
    		    	            	Ins1.setCity__c(strNoOfRecord5x3);
    		    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    		    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    		    	               // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_141_0 EXTRACT=TXT");
    		    	            	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_142_0 EXTRACT=TXT");
    		    	    	             String iret5x4 = iim.invoke("iimGetLastExtract").toString();
    		    	    	            	String strNoOfRecord5x4=iret5x4.replace("[EXTRACT]", "");
    		    	    	            	System.out.println("State"+strNoOfRecord5x4);//for state
    		    	    	            	Ins1.setState__c(strNoOfRecord5x4);
    		    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    		    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    		    	    	              //  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_173_0 EXTRACT=TXT");
    		    	    	            	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_174_0 EXTRACT=TXT");
    		    	    	    	             String iret5x5 = iim.invoke("iimGetLastExtract").toString();
    		    	    	    	            	String strNoOfRecord5x5=iret5x5.replace("[EXTRACT]", "");
    		    	    	    	            	System.out.println("pincode"+strNoOfRecord5x5);//forpincode
    		    	    	    	            	Ins1.setPinCode__c(strNoOfRecord5x5);
    		    	    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    		    	    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	       	
    	   	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_66_0 EXTRACT=TXT");
    	   	        String iretg = iim.invoke("iimGetLastExtract").toString();
    	   	       	String strNoOfRecordg=iretg.replace("[EXTRACT]", "");//email fleet owner
    	   	       	System.out.println("Fleet Email-"+strNoOfRecordg);
    	   	     Ins1.setFleet_Owner_Email__c(strNoOfRecordg);
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	       	
    	   	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_66_0 EXTRACT=TXT");
    	   	        String iretgx1 = iim.invoke("iimGetLastExtract").toString();
    	   	       	String strNoOfRecordgx1=iretgx1.replace("[EXTRACT]", "");//email from contacts detail
    	   	       	System.out.println("Contact Email-"+strNoOfRecordgx1);
    	   	    Ins1.setEmail__c(strNoOfRecordgx1);
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	       	
    	   	 	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_71_0 EXTRACT=TXT");
    		        String iretj = iim.invoke("iimGetLastExtract").toString();
    		       	String strNoOfRecordj=iretj.replace("[EXTRACT]", "");//email fleet manager
    		       	System.out.println("F Manager Email-"+strNoOfRecordj);
    		     Ins1.setFleet_Manager_Email__c(strNoOfRecordj);
    		       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    		       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	       	if(strNoOfRecordg.length()>1) {
    	   	       		Ins1.setEmail__c(strNoOfRecordg);
    	   	       	}
    	   	       	else if(strNoOfRecordgx1.length()>1) {
    	   	       		Ins1.setEmail__c(strNoOfRecordgx1);
    	   	       	}
    	   	       	else if(strNoOfRecordj.length()>1) {
    	   	       		Ins1.setEmail__c(strNoOfRecordj);
    	   	       	}
    	   	       	else {
    	   	       		Ins1.setEmail__c("");
    	   	       	}
    	   	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_68_0 EXTRACT=TXT");
    	   	        String ireth = iim.invoke("iimGetLastExtract").toString();
    	   	       	String strNoOfRecordh=ireth.replace("[EXTRACT]", "");
    	   	       	System.out.println("Fleet Manager-"+strNoOfRecordh);
    	   	 	Ins1.setFleet_Manager__c(strNoOfRecordh);
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	       	
    	   	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_70_0 EXTRACT=TXT");
    	   	        String ireti = iim.invoke("iimGetLastExtract").toString();
    	   	       	String strNoOfRecordi=ireti.replace("[EXTRACT]", "");
    	   	       	System.out.println("10-F manager Mobile-"+strNoOfRecordi);
    	   	     Ins1.setFleet_Manager_Mobile__c(strNoOfRecordi);
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	   	       	
    	   	      	if(Ins1.getDriver_Phone_No__c().length()>9) {
        	       		Ins1.setPhone__c(Ins1.getDriver_Phone_No__c());
        	       		
        	       	}else if(Ins1.getFleet_Manager_Mobile__c().length()>9) {
        	       		Ins1.setPhone__c(Ins1.getFleet_Manager_Mobile__c());
        	       		
        	       	}else if(Ins1.getFleet_Owner_Mobile__c().length()>9) {
        	       		Ins1.setPhone__c(Ins1.getFleet_Owner_Mobile__c());
        	       		
        	       	}else if(!(Ins1.getFleet_Manager_Email__c().contains("@"))) {
        	       		Ins1.setPhone__c(Ins1.getFleet_Manager_Email__c());
        	       		
     	       	   }
        	       	
        	       	if(Ins1.getFleet_Manager_Email__c().contains("@")) {
        	       		Ins1.setEmail__c(Ins1.getFleet_Manager_Email__c());
        	       		
        	       	   }else if(Ins1.getFleet_Owner_Email__c().contains("@")) {
        	       		   Ins1.setEmail__c(Ins1.getFleet_Owner_Email__c());
        	       	   }
    	   	    	
    	   	                	}
    	   	                	
    	   	                	if(Ins1.getName().length()>1 && !(Ins1.getName().equalsIgnoreCase("#EANF#"))) {
    	   	    	        	InList.add(Ins1);
    	   	    	        }
    	                	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
    	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card No:\"");
    		}
    		
    		//next page btn click check
    		 System.out.println("------------------------------------------------------------------2");
    		 iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card\"" );
			 iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_13_0" ); 
		     iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Jobcard_Close_Date___Time" );
		     iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Jobcard_Close_Date___Time CONTENT=\""+DateRange+"\"" );
		     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=TXT:Go");
		     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_10_0" );
		     iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
		     
	    		//click next nuber of pg_no
	    		for(int i=0;i<pg_no;i++) {
	    			iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-end\"" );
	    			iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	    		} 

    			iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-end\"" );
    			iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
    			 pg_no++;
//    		    int dLoop=0;
//    		    while(!) {
//    		    	dLoop++;
//    			    iim.invoke("iimPlay", "CODE:WAIT SECONDS=5");
//    		    }
    			
    			
    		//reassign iret18
    			
    		 iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=ID:s_1_rc EXTRACT=TXT" );
    		 iret17 = iim.invoke("iimGetLastExtract").toString();
    		 iret17=iret17.replace("[EXTRACT]", "");
    		 System.out.println(iret17);
    		 iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
 	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    		 iret18=iret17.split("of",2)[1].trim();
    		 iret19=iret17.split("of",2)[0].trim();
    		 iret19=iret19.split("-",2)[1].trim();
    		 if(Integer.parseInt(iret19)<Integer.parseInt(iret18.replace("+",""))) {
    			 iret18=iret18+"+";
    		 }
 			iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
    		 System.out.println("\n\n\n\niret"+iret18+" "+iret19);
    	}
    	
    		//total no of records = iret18
    		//extract last rows
    	int total_no_rec=Integer.parseInt(iret18);
    	if(total_no_rec>10) {
    	int loop_row=total_no_rec%10;
    	for(int k=11-loop_row;k<11;k++) {
    		System.out.println("Extracting last row......................."+total_no_rec);
    		Ins1=new InsuranceDTO();
    		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card\"" );
			 iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_13_0" ); 
		     iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Jobcard_Close_Date___Time" );
		     iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Jobcard_Close_Date___Time CONTENT=\""+DateRange+"\"" );
		     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=TXT:Go");
		     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_10_0" );
		     iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
		     
	    		//click next nuber of pg_no
	    		for(int i=0;i<pg_no;i++) {
	    			iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=SPAN ATTR=CLASS:\"ui-icon ui-icon-seek-end\"" );
	    			iim.invoke("iimPlay","CODE:WAIT SECONDS=5" );
	    		} 
    		//loop over row no i
    		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+k+"_s_1_l_VIN" );
    		
   	     //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_VIN" );
   	    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=NAME:_sweview" );
   	     iim.invoke("iimPlay","CODE:TAB T=1" );//TAG POS=11 TYPE=TBODY ATTR=* EXTRACT=TXT
   	     //Ins1.set
   	     
   	    		
   	     iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_222_0 EXTRACT=TXT");
   	 // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_220_0 EXTRACT=TXT");
   	     String ireta = iim.invoke("iimGetLastExtract").toString();
   	    	String strNoOfRecorda=ireta.replace("[EXTRACT]", "");
   	    	System.out.println("1-Vin no-"+strNoOfRecorda);
   	    	Ins1.setChassis_No__c(strNoOfRecorda);
   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	     iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_61_0 EXTRACT=TXT");
   	     String iretb = iim.invoke("iimGetLastExtract").toString();
   	    	String strNoOfRecordb=iretb.replace("[EXTRACT]", "");
   	    	System.out.println("Reg no-"+strNoOfRecordb);
   	    	Ins1.setReg_No__c(strNoOfRecordb);
   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	     iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_46_0 EXTRACT=TXT");
   	     String iretc = iim.invoke("iimGetLastExtract").toString();
   	    	String strNoOfRecordc=iretc.replace("[EXTRACT]", "");
   	    	System.out.println("Model-"+strNoOfRecordc);
   	    	Ins1.setModel1__c(strNoOfRecordc);
   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
			
   	    	Ins1.setSite__c("Bharat Benz Ghaziabad");
	    	Ins1.setMake__c("BharatBenz");
   	    	
   	    	 iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_67_0 EXTRACT=TXT");
   	         String iret4 = iim.invoke("iimGetLastExtract").toString();
   	        	String strNoOfRecord4=iret4.replace("[EXTRACT]", "");
   	        	System.out.println("Sold Date-"+strNoOfRecord4);
   	        	Ins1.setSold_Date__c(strNoOfRecord4.replaceAll("/", " "), 3);
   	        	Ins1.setInsurance_Renewal_Date__c(strNoOfRecord4.replaceAll("/", " "), 4);
   	        	Ins1.setModel_Year__c(strNoOfRecord4.split("/",3)[2]);
   	        	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	        	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	        	
   	        	 iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_69_0 EXTRACT=TXT");
   	             String iret5 = iim.invoke("iimGetLastExtract").toString();
   	            	String strNoOfRecord5=iret5.replace("[EXTRACT]", "");
   	            	System.out.println("Driver Name-"+strNoOfRecord5);
   	            	Ins1.setDriver_Name__c(strNoOfRecord5);
   	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	            	
   	            	// iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_157_0 EXTRACT=TXT");
   	            	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_159_0 EXTRACT=TXT");
   	                 String iret6 = iim.invoke("iimGetLastExtract").toString();
   	                	String strNoOfRecord6=iret6.replace("[EXTRACT]", "");
   	                	System.out.println("Driver Ph No-"+strNoOfRecord6);
   	                	Ins1.setDriver_Phone_No__c(strNoOfRecord6);
   	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	                	
   	                	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+k+"_s_1_l_Account EXTRACT=TXT" );
   	                	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Account EXTRACT=TXT" ); 
   	                	String iret12 = iim.invoke("iimGetLastExtract").toString();
   	                	String strNoOfRecord12=iret12.replace("[EXTRACT]", "");
   	                	System.out.println("Account Name-"+strNoOfRecord12);
   	                	Ins1.setName(strNoOfRecord12);
   	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");//CONTENT=$\""+arrOfStra[j]+"\""
   	                	if(!strNoOfRecord12.equalsIgnoreCase("#EANF#")) {
   	                	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\""+strNoOfRecord12+"\"" );
   	    	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"MUNISH KUMAR\"" );
   	    	
   	    	
   	    	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_63_0 EXTRACT=TXT");
   	        String irete = iim.invoke("iimGetLastExtract").toString();
   	       	String strNoOfRecorde=irete.replace("[EXTRACT]", "");
   	       	System.out.println("Fleet Owner-"+strNoOfRecorde);
   	     Ins1.setFleet_Owner__c(strNoOfRecorde);
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	       	
   	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_65_0 EXTRACT=TXT");
   	        String iretf = iim.invoke("iimGetLastExtract").toString();
   	       	String strNoOfRecordf=irete.replace("[EXTRACT]", "");
   	       	System.out.println("Fleet Mobile-"+strNoOfRecordf);
   	     Ins1.setFleet_Owner_Mobile__c(strNoOfRecordf);
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	       	
            iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_137_0 EXTRACT=TXT");
	             String iret5x = iim.invoke("iimGetLastExtract").toString();
	            	String strNoOfRecord5x=iret5x.replace("[EXTRACT]", "");
	            	System.out.println("Adress"+strNoOfRecord5x);//for address full
	            	Ins1.setAddress__c(strNoOfRecord5x);
	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	            	

	               // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_139_0 EXTRACT=TXT");
	            	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_140_0 EXTRACT=TXT");
	    	             String iret5x3 = iim.invoke("iimGetLastExtract").toString();
	    	            	String strNoOfRecord5x3=iret5x3.replace("[EXTRACT]", "");
	    	            	System.out.println("city"+strNoOfRecord5x3);//for  city
	    	            	Ins1.setCity__c(strNoOfRecord5x3);
	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	    	            	

	    	              //  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_141_0 EXTRACT=TXT");
	    	            	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_142_0 EXTRACT=TXT");
	    	    	             String iret5x4 = iim.invoke("iimGetLastExtract").toString();
	    	    	            	String strNoOfRecord5x4=iret5x4.replace("[EXTRACT]", "");
	    	    	            	System.out.println("State"+strNoOfRecord5x4);//for state
	    	    	            	Ins1.setState__c(strNoOfRecord5x4);
	    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
	       	       	

	    	    	              //  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_173_0 EXTRACT=TXT");
	    	    	            	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_174_0 EXTRACT=TXT");
	    	    	    	             String iret5x5 = iim.invoke("iimGetLastExtract").toString();
	    	    	    	            	String strNoOfRecord5x5=iret5x5.replace("[EXTRACT]", "");
	    	    	    	            	System.out.println("pincode"+strNoOfRecord5x5);//forpincode
	    	    	    	            	Ins1.setPinCode__c(strNoOfRecord5x5);
	    	    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	    	    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	       	
   	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_66_0 EXTRACT=TXT");
   	        String iretg = iim.invoke("iimGetLastExtract").toString();
   	       	String strNoOfRecordg=iretg.replace("[EXTRACT]", "");//email fleet owner
   	       	System.out.println("Fleet Email-"+strNoOfRecordg);
   	     Ins1.setFleet_Owner_Email__c(strNoOfRecordg);
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	       	
   	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_66_0 EXTRACT=TXT");
   	        String iretgx1 = iim.invoke("iimGetLastExtract").toString();
   	       	String strNoOfRecordgx1=iretgx1.replace("[EXTRACT]", "");//email from contacts detail
   	       	System.out.println("Contact Mail-"+strNoOfRecordgx1);
   	    Ins1.setEmail__c(strNoOfRecordgx1);
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	       	
   	 	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_71_0 EXTRACT=TXT");
	        String iretj = iim.invoke("iimGetLastExtract").toString();
	       	String strNoOfRecordj=iretj.replace("[EXTRACT]", "");//email fleet manager
	       	System.out.println("F Manager Mail-"+strNoOfRecordj);
	     Ins1.setFleet_Manager_Email__c(strNoOfRecordj);
	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	       	if(strNoOfRecordg.length()>1) {
   	       		Ins1.setEmail__c(strNoOfRecordg);
   	       	}
   	       	else if(strNoOfRecordgx1.length()>1) {
   	       		Ins1.setEmail__c(strNoOfRecordgx1);
   	       	}
   	       	else if(strNoOfRecordj.length()>1) {
   	       		Ins1.setEmail__c(strNoOfRecordj);
   	       	}
   	       	else {
   	       		Ins1.setEmail__c("");
   	       	}
   	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_68_0 EXTRACT=TXT");
   	        String ireth = iim.invoke("iimGetLastExtract").toString();
   	       	String strNoOfRecordh=ireth.replace("[EXTRACT]", "");
   	       	System.out.println("Fleet Manager-"+strNoOfRecordh);
   	 	Ins1.setFleet_Manager__c(strNoOfRecordh);
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	       	
   	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_70_0 EXTRACT=TXT");
   	        String ireti = iim.invoke("iimGetLastExtract").toString();
   	       	String strNoOfRecordi=ireti.replace("[EXTRACT]", "");
   	       	System.out.println("10-Fleet Manger Mobile-"+strNoOfRecordi);
   	     Ins1.setFleet_Manager_Mobile__c(strNoOfRecordi);
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
   	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
   	       	
   	      	if(Ins1.getDriver_Phone_No__c().length()>9) {
	       		Ins1.setPhone__c(Ins1.getDriver_Phone_No__c());
	       		
	       	}else if(Ins1.getFleet_Manager_Mobile__c().length()>9) {
	       		Ins1.setPhone__c(Ins1.getFleet_Manager_Mobile__c());
	       		
	       	}else if(Ins1.getFleet_Owner_Mobile__c().length()>9) {
	       		Ins1.setPhone__c(Ins1.getFleet_Owner_Mobile__c());
	       		
	       	}else if(!(Ins1.getFleet_Manager_Email__c().contains("@"))) {
	       		Ins1.setPhone__c(Ins1.getFleet_Manager_Email__c());
	       		
	       	   }
	       	
	       	if(Ins1.getFleet_Manager_Email__c().contains("@")) {
	       		Ins1.setEmail__c(Ins1.getFleet_Manager_Email__c());
	       		
	       	   }else if(Ins1.getFleet_Owner_Email__c().contains("@")) {
	       		   Ins1.setEmail__c(Ins1.getFleet_Owner_Email__c());
	       	   }
   	                	}
   	                	if(Ins1.getName().length()>1 && !(Ins1.getName().equalsIgnoreCase("#EANF#"))) {
   	    	        	InList.add(Ins1);
   	    	        }
   	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card No:\"");
   	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
    	}
    	}
    	else {
    		for(int k=1;k<1+total_no_rec;k++) {
    			Ins1=new InsuranceDTO();
        		//loop over row no i
    			 System.out.println("looping over rows of same page------------------------------------------------------------------3");
    			iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card\"" );
   			 iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
   		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_13_0" ); 
   		     iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS:ui-jqgrid-bdiv" );
   		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Jobcard_Close_Date___Time" );
   		     iim.invoke("iimPlay","CODE:WAIT SECONDS=3" );
   		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:Jobcard_Close_Date___Time CONTENT=\""+DateRange+"\"" );
   		     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
   		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=TXT:Go");
   		     iim.invoke("iimPlay","CODE:WAIT SECONDS=1" );
   		     iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=NAME:s_1_1_10_0" );
   		     iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
    		     
   		//loop over row no i
     		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+k+"_s_1_l_VIN" );
    	     //iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_VIN" );
    	    // iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=NAME:_sweview" );
    	     iim.invoke("iimPlay","CODE:TAB T=1" );//TAG POS=11 TYPE=TBODY ATTR=* EXTRACT=TXT
    	    // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_220_0 EXTRACT=TXT");
    	  iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_222_0 EXTRACT=TXT");
    	     String ireta = iim.invoke("iimGetLastExtract").toString();
    	    	String strNoOfRecorda=ireta.replace("[EXTRACT]", "");
    	    	System.out.println("1-VIN No-"+strNoOfRecorda);
    	    	Ins1.setChassis_No__c(strNoOfRecorda);
    	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	     iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_61_0 EXTRACT=TXT");
    	     String iretb = iim.invoke("iimGetLastExtract").toString();
    	    	String strNoOfRecordb=iretb.replace("[EXTRACT]", "");
    	    	System.out.println("Reg No-"+strNoOfRecordb);
    	    	Ins1.setReg_No__c(strNoOfRecordb);
    	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	     iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_46_0 EXTRACT=TXT");
    	     String iretc = iim.invoke("iimGetLastExtract").toString();
    	    	String strNoOfRecordc=iretc.replace("[EXTRACT]", "");
    	    	System.out.println("Model-"+strNoOfRecordc);
    	    	Ins1.setModel1__c(strNoOfRecordc);
    	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	    	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
 		
    	    	Ins1.setSite__c("Bharat Benz Ghaziabad");
 	    	Ins1.setMake__c("BharatBenz");
    	    	
    	    	 iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_67_0 EXTRACT=TXT");
    	         String iret4 = iim.invoke("iimGetLastExtract").toString();
    	        	String strNoOfRecord4=iret4.replace("[EXTRACT]", "");
    	        	System.out.println("Sold Date-"+strNoOfRecord4);
    	        	Ins1.setSold_Date__c(strNoOfRecord4.replaceAll("/", " "), 3);
    	        	Ins1.setInsurance_Renewal_Date__c(strNoOfRecord4.replaceAll("/", " "), 4);
    	        	Ins1.setModel_Year__c(strNoOfRecord4.split("/",3)[2]);
    	        	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	        	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	        	
    	        	 iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_69_0 EXTRACT=TXT");
    	             String iret5 = iim.invoke("iimGetLastExtract").toString();
    	            	String strNoOfRecord5=iret5.replace("[EXTRACT]", "");
    	            	System.out.println("Driver Name-"+strNoOfRecord5);
    	            	Ins1.setDriver_Name__c(strNoOfRecord5);
    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	            	
    	            	// iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_157_0 EXTRACT=TXT");
    	            	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_159_0 EXTRACT=TXT");
    	                 String iret6 = iim.invoke("iimGetLastExtract").toString();
    	                	String strNoOfRecord6=iret6.replace("[EXTRACT]", "");
    	                	System.out.println("Driver Phone No-"+strNoOfRecord6);
    	                	Ins1.setDriver_Phone_No__c(strNoOfRecord6);
    	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	                	
    	                	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:"+k+"_s_1_l_Account EXTRACT=TXT" );
    	                	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=TD ATTR=ID:1_s_1_l_Account EXTRACT=TXT" ); 
    	                	String iret12 = iim.invoke("iimGetLastExtract").toString();
    	                	String strNoOfRecord12=iret12.replace("[EXTRACT]", "");
    	                	System.out.println("Account Name-"+strNoOfRecord12);
    	                	Ins1.setName(strNoOfRecord12);
    	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	                	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");//CONTENT=$\""+arrOfStra[j]+"\""
    	                	if(!strNoOfRecord12.equalsIgnoreCase("#EANF#")) {
    	                	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\""+strNoOfRecord12+"\"" );
    	    	//iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=A ATTR=TXT:\"MUNISH KUMAR\"" );
    	    	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_63_0 EXTRACT=TXT");
    	        String irete = iim.invoke("iimGetLastExtract").toString();
    	       	String strNoOfRecorde=irete.replace("[EXTRACT]", "");
    	       	System.out.println("Fleet Owner-"+strNoOfRecorde);
    	     Ins1.setFleet_Owner__c(strNoOfRecorde);
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	       	
    	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_65_0 EXTRACT=TXT");
    	        String iretf = iim.invoke("iimGetLastExtract").toString();
    	       	String strNoOfRecordf=irete.replace("[EXTRACT]", "");
    	       	System.out.println("Fleet Owner Mobile-"+strNoOfRecordf);
    	     Ins1.setFleet_Owner_Mobile__c(strNoOfRecordf);
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	       	
             iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_137_0 EXTRACT=TXT");
 	             String iret5x = iim.invoke("iimGetLastExtract").toString();
 	            	String strNoOfRecord5x=iret5x.replace("[EXTRACT]", "");
 	            	System.out.println("Adress"+strNoOfRecord5x);//for address full
 	            	Ins1.setAddress__c(strNoOfRecord5x);
 	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
 	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");

 	             //   iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_139_0 EXTRACT=TXT");
 	            	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_140_0 EXTRACT=TXT");
 	    	             String iret5x3 = iim.invoke("iimGetLastExtract").toString();
 	    	            	String strNoOfRecord5x3=iret5x3.replace("[EXTRACT]", "");
 	    	            	System.out.println("city"+strNoOfRecord5x3);//for  city
 	    	            	Ins1.setCity__c(strNoOfRecord5x3);
 	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
 	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");

 	    	               // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_141_0 EXTRACT=TXT");
 	    	               iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_142_0 EXTRACT=TXT");
 	    	    	             String iret5x4 = iim.invoke("iimGetLastExtract").toString();
 	    	    	            	String strNoOfRecord5x4=iret5x4.replace("[EXTRACT]", "");
 	    	    	            	System.out.println("State"+strNoOfRecord5x4);//for state
 	    	    	            	Ins1.setState__c(strNoOfRecord5x4);
 	    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
 	    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");

 	    	    	               // iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_173_0 EXTRACT=TXT");
 	    	    	            	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_174_0 EXTRACT=TXT");
 	    	    	    	             String iret5x5 = iim.invoke("iimGetLastExtract").toString();
 	    	    	    	            	String strNoOfRecord5x5=iret5x5.replace("[EXTRACT]", "");
 	    	    	    	            	System.out.println("pincode"+strNoOfRecord5x5);//forpincode
 	    	    	    	            	Ins1.setPinCode__c(strNoOfRecord5x5);
 	    	    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
 	    	    	    	            	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	       	
    	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_66_0 EXTRACT=TXT");
    	        String iretg = iim.invoke("iimGetLastExtract").toString();
    	       	String strNoOfRecordg=iretg.replace("[EXTRACT]", "");//email fleet owner
    	       	System.out.println("Fleet Owner Mail-"+strNoOfRecordg);
    	     Ins1.setFleet_Owner_Email__c(strNoOfRecordg);
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	       	
    	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_66_0 EXTRACT=TXT");
    	        String iretgx1 = iim.invoke("iimGetLastExtract").toString();
    	       	String strNoOfRecordgx1=iretgx1.replace("[EXTRACT]", "");//email from contacts detail
    	       	System.out.println("Contact Mail-"+strNoOfRecordgx1);
    	    Ins1.setEmail__c(strNoOfRecordgx1);
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	       	
    	 	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_71_0 EXTRACT=TXT");
 	        String iretj = iim.invoke("iimGetLastExtract").toString();
 	       	String strNoOfRecordj=iretj.replace("[EXTRACT]", "");//email fleet manager
 	       	System.out.println("Fleet Manager Mail-"+strNoOfRecordj);
 	     Ins1.setFleet_Manager_Email__c(strNoOfRecordj);
 	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
 	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	       	if(strNoOfRecordg.length()>1) {
    	       		Ins1.setEmail__c(strNoOfRecordg);
    	       	}
    	       	else if(strNoOfRecordgx1.length()>1) {
    	       		Ins1.setEmail__c(strNoOfRecordgx1);
    	       	}
    	       	else if(strNoOfRecordj.length()>1) {
    	       		Ins1.setEmail__c(strNoOfRecordj);
    	       	}
    	       	else {
    	       		Ins1.setEmail__c("");
    	       	}
    	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_68_0 EXTRACT=TXT");
    	        String ireth = iim.invoke("iimGetLastExtract").toString();
    	       	String strNoOfRecordh=ireth.replace("[EXTRACT]", "");
    	       	System.out.println("Fleet Manager-"+strNoOfRecordh);
    	 	Ins1.setFleet_Manager__c(strNoOfRecordh);
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	       	
    	       	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=NAME:s_2_1_70_0 EXTRACT=TXT");
    	        String ireti = iim.invoke("iimGetLastExtract").toString();
    	       	String strNoOfRecordi=ireti.replace("[EXTRACT]", "");
    	       	System.out.println("10-Fleet Manager Mobile-"+strNoOfRecordi);
    	     Ins1.setFleet_Manager_Mobile__c(strNoOfRecordi);
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT NULL");
    	       	iim.invoke("iimPlay", "CODE:SET !EXTRACT_TEST_POPUP NO");
    	       	
    	       	if(Ins1.getDriver_Phone_No__c().length()>9) {
    	       		Ins1.setPhone__c(Ins1.getDriver_Phone_No__c());
    	       		
    	       	}else if(Ins1.getFleet_Manager_Mobile__c().length()>9) {
    	       		Ins1.setPhone__c(Ins1.getFleet_Manager_Mobile__c());
    	       		
    	       	}else if(Ins1.getFleet_Owner_Mobile__c().length()>9) {
    	       		Ins1.setPhone__c(Ins1.getFleet_Owner_Mobile__c());
    	       		
    	       	}else if(!(Ins1.getFleet_Manager_Email__c().contains("@"))) {
    	       		Ins1.setPhone__c(Ins1.getFleet_Manager_Email__c());
    	       		
 	       	   }
    	       	
    	       	if(Ins1.getFleet_Manager_Email__c().contains("@")) {
    	       		Ins1.setEmail__c(Ins1.getFleet_Manager_Email__c());
    	       		
    	       	   }else if(Ins1.getFleet_Owner_Email__c().contains("@")) {
    	       		   Ins1.setEmail__c(Ins1.getFleet_Owner_Email__c());
    	       	   }
    	       	
}
    	                	if(Ins1.getName().length()>1 && !(Ins1.getName().equalsIgnoreCase("#EANF#"))) {
    	    	        	InList.add(Ins1);
    	    	        }
    	      	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=A ATTR=TXT:\"Job Card No:\"");
    	      	iim.invoke("iimPlay","CODE:WAIT SECONDS=2" );
    	       	}
    	    		
        	}
    	}
    

    System.out.println("------------------------------------------------------------------5");
       for(InsuranceDTO Ins2:InList) {
    	System.out.println(Ins2.getPhone__c()+"-----------"+Ins2.getEmail__c()+"---------"+Ins2.getName());
          }
       if(InList!=null && InList.size()>0) {
    	    xlRW w = new xlRW();
    	   // System.out.println(InList.size());
    	w.DaimlerCall(InList);
       }
    
    }
   
public String imformat(String date, int f) {
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

