package Imacro;
import com.jacob.activeX.ActiveXComponent;

public class TallyInvoiceDownloader {

	public static void main(String args[]) {
	

	TallyInvoiceDownloader tl=new TallyInvoiceDownloader();
	tl.IterationDownloader();
	
}
public void IterationDownloader() {
	ActiveXComponent iim = new ActiveXComponent("imacro"); 				
	System.out.println("Calling iimInit"); 
	iim.invoke("iimInit"); 
	System.out.println("Calling iimPlay"); 



	iim.invoke("iimPlay", "CODE:VERSION BUILD=2021");
	iim.invoke("iimPlay", "CODE:TAB T=1");
	iim.invoke("iimPlay","CODE:URL GOTO=https://tallysolutions.com/website/html/tally_login.html?destination_url=https%3A%2F%2Fcustomer.tallysolutions.com%2Fcustomerapp%2F");
	iim.invoke("iimPlay", "CODE:WAIT SECONDS=10");
	iim.invoke("iimPlay", "CODE:TAG POS=1 TYPE=INPUT:TEXT ATTR=ID:loginname CONTENT=jrgroup.accounts@gmail.com");
iim.invoke("iimPlay","CODE:SET !ENCRYPTION NO");
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=INPUT:PASSWORD ATTR=ID:password CONTENT=POLYMAXX@DEL28");
    iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=BUTTON ATTR=ID:login_btn");
    iim.invoke("iimPlay","CODE:WAIT SECONDS=8" );
   
    iim.invoke("iimPlay","CODE:TAG POS=12 TYPE=DIV ATTR=CLASS:ng-star-inserted");
   iim.invoke("iimPlay","CODE:WAIT SECONDS=3");
   iim.invoke("iimPlay","CODETAG POS=1 TYPE=INPUT:TEXT ATTR=ID:searchBox CONTENT=register");
   iim.invoke("iimPlay","CODE:WAIT SECONDS=4");
    iim.invoke("iimPlay","CODE:TAG POS=129 TYPE=DIV ATTR=CLASS: field desktop_style");
    iim.invoke("iimPlay","CODE:TAG POS=2 TYPE=DIV ATTR=CLASS: line-container");
   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=CLASS: field val_field desktop_style text-left string field_with_no_width");
iim.invoke("iimPlay","CODE:WAIT SECONDS=5");
   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=ID:f1p4l1f5");
   iim.invoke("iimPlay","CODE:WAIT SECONDS=2");
  		iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=ID:f1p2p1");
    iim.invoke("iimPlay","CODE:WAIT SECONDS=3");
 	iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=DIV ATTR=ID:Print");
    iim.invoke("iimPlay","CODE:WAIT SECONDS=4");
  iim.invoke("iimPlay","CODE:ONDOWNLOAD FOLDER=D:\\Tally\\Aprill FILE=April.xlsx WAIT=YES");
   iim.invoke("iimPlay","CODE:TAG POS=1 TYPE=SPAN ATTR=CLASS:downText");

   iim.invoke("iimPlay","CODE:WAIT SECONDS=19");
   
}
public void  imformat(String date, int f) {
		
		String d="";
		if(f==1) {
			d=date.substring(8, 10)+"-"+date.substring(5, 7)+"-"+date.substring(0, 4);
		}
		else if(f==2) {
			d=date.substring(8, 10)+"/"+date.substring(5, 7)+"/"+date.substring(0, 4);
			System.out.println("2nd"+d);
		}
		else if(f==3) {
			d=date.substring(8, 10)+""+date.substring(5, 7)+""+date.substring(0, 4);
		}else if(f==4) {
			d=date.substring(5, 7)+""+date.substring(0, 4);
		}else if(f==5) {
		
		}else if(f==6) {
			String month=(date.substring(5,7));
			System.out.println(month);
			int month1=Integer.parseInt(month);
			if(month1==1) {
				month="";			
			}else if(month1==2) {
				month="Feb";
			}else if(month1==3) {
				month="Mar";
			}else if(month1==4) {
				month="Apr";
			}else if(month1==5) {
				month="May";
			}else if(month1==6) {
				month="Jun";
			}else if(month1==7) {
				month="Jul";
			}else if(month1==8) {
				month="Aug";
			}else if(month1==9) {
				month="Sep";
			}else if(month1==10) {
				month="Oct";
			}else if(month1==11) {
				month="Nov";
			}else if(month1==12) {
				month="Dec";
			}
	d=date.substring(8)+"/"+month+"/"+date.substring(0,4);
	System.out.println(d);
		}
		} 

	}

