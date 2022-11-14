package Imacro;

import com.jacob.activeX.ActiveXComponent;

public class IM {
public static void main(String[] args) {
	ActiveXComponent iim = new ActiveXComponent("iMacros"); 
	iim.invoke("iiminit"); 
   System.out.println("Calling iimInit"); 
  iim.invoke("iimPlay", "CODE:URL GOTO=https://www.google.com/");
 // System.out.println("Calling iimInit");
    iim.invoke("iimPlay", "CODE:TAB T=1");
    iim.invoke("iimPlay", "CODE:TAB CLOSEOTHERS");
}
}
