package com.fsp.blacksheep;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validation {

	public boolean validateName(String name){
		//String name="kannan";
		Pattern p = Pattern.compile("^[a-zA-Z]{1,}$");   
	    Matcher m = p.matcher(name);
	    boolean matchFound = m.matches();
	    if (matchFound){
	    	System.out.println("Valid Name ");
	    	return true;
	    }
	    else{
	    	System.out.println("Invalid Name ");
	    	return false;
	    }
	}
	
	public boolean validateEmail(String email){
		
		//String email="kannan123abc@yahoo.co.in";
		Pattern p = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");   
	    Matcher m = p.matcher(email);
	    boolean matchFound = m.matches();
	    if (matchFound){
	    	//System.out.println("Valid Email Id ");
	    	return true;
	    }	    	
	    else{
	    	System.out.println("Invalid Email Id ");
	    	return false;
	    }
	      
	}
	
	public boolean validateMobile(String mobile){
		//String mobile="+91-9994445556"; //+91-9998887776, 09998887776, +919998887776	9998887776
		
		if(mobile.startsWith("0")){
			int count=mobile.length();
			System.out.println("Count Value 0======>>>"+count);
			if(count<11 || count>11){
				System.out.println("Invali Mobile Length========0");
				return false;
			}
			else{
				System.out.println("Valid Mobile Number========0");
				return true;
			}
		}
		else if(mobile.startsWith("+91-")){
			int count=mobile.length();
			System.out.println("Count Value +91-======>>>"+count);
			if(count<14 || count>14){
				System.out.println("Invali Mobile Length========+91-");
				return false;
			}
			else{
				System.out.println("Valid Mobile Number========+91-");
				return true;
			}
		}
		else if(mobile.startsWith("+91")){
			int count=mobile.length();
			System.out.println("Count Value +91======>>>"+count);
			if(count<13 || count>13){
				System.out.println("Invali Mobile Length========+91");
				return false;
			}
			else{
				System.out.println("Valid Mobile Number========+91");
				return true;
			}
		}
		else if(mobile.length()==10){
			int count=mobile.length();
			System.out.println("Count Value ======>>>"+count);
			System.out.println("Valid Mobile Number in 10 Digits");
			return true;
		}
		else{
			System.out.println("Invalid Indian Mobile Number Format");
			return false;
		}
	}
	
	public boolean validateCreditCard(String cardType,String number){
		if(cardType.equals("Visa")){
			//String number="4111111111111111";
			String exp="^4[0-9]{12}(?:[0-9]{3})?$";
			boolean res=validate(exp,number);
			System.out.println("Visa Result=======>>>"+res);
			return res;
		}
		if(cardType.equals("Master Card")){
			//String number="5100000000000004";
			String exp="^5[1-5][0-9]{14}$";
			boolean res=validate(exp,number);
			System.out.println("MasterCard Result=======>>>"+res);
			return res;
		}
		if(cardType.equals("American Express")){
			//String number="378282246310005";
			String exp="^3[47][0-9]{13}$";
			boolean res=validate(exp,number);
			System.out.println("American Express Result=======>>>"+res);
			return res;
		}
		if(cardType.equals("Diners Club")){
			//String number="38520000023287";
			String exp="^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
			boolean res=validate(exp,number);
			System.out.println("Dinner Club Result=======>>>"+res);
			return res;
			
		}
		if(cardType.equals("Discover")){
			//String number="6011000000000004";
			String exp="^6(?:011|5[0-9]{2})[0-9]{12}$";
			boolean res=validate(exp,number);
			System.out.println("Discover Result=======>>>"+res);
			return res;
		}
		
		return false;
		
	}
	
	public boolean validate(String pattern,String value){
		Pattern p = Pattern.compile(pattern);   
	    Matcher m = p.matcher(value);
	    boolean matchFound = m.matches();
	    if (matchFound)
	      return true;
	    else
	      return false;
	}	
	
}
