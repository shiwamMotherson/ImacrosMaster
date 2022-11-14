 

package Imacro;

 

import java.util.*;

 

import com.jacob.activeX.ActiveXComponent;

public class ChromeTest {

 

       public static void main(String[] args) {

    	 ActiveXComponent iim = new ActiveXComponent("imacros"); 				
  		 System.out.println("Calling iimInit"); 
  	    // iim.invoke("iimInit"); 
  	     System.out.println("Calling iimOpen");
  	     iim.invoke("-cr","True");
  	     
  	 //  System.out.println(iimOpen('-ie', 'True', '300');
  	     System.out.println("Calling iimPlay");
  	     iim.invoke("iimPlay","CODE:TAB T=1" );
  	     iim.invoke("iimPlay","CODE:TAB CLOSEALLOTHERS" );
  	     iim.invoke("iimPlay","CODE:URL GOTO=www.google.com");
          

       }

}