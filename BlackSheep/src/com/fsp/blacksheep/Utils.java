package com.fsp.blacksheep;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class Utils {
	public static boolean ARTICLES_SHARE_VISIBILITY = false;
	public static boolean DRINKING_GAMES_SHARE_VISIBILITY = false;
	public static boolean BARS_SHARE_VISIBILITY = false;
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public static String dateConvertion(String dateValue) {
    	String[] dateSplit = dateValue.split("-");
    	String date = dateSplit[2];
    	String month = dateSplit[1];
    	String year = dateSplit[0];
    	
    	if(month.equals("1")) {
    		month = "Jan";
    	}
    	
    	if(month.equals("2")) {
    		month = "Feb";
    	}
    	
    	if(month.equals("3")) {
    		month = "Mar";
    	}
    	
    	if(month.equals("4")) {
    		month = "Apr";
    	}
    	
    	if(month.equals("5")) {
    		month = "May";
    	}
    	
    	if(month.equals("6")) {
    		month = "Jun";
    	}
    	
    	if(month.equals("7")) {
    		month = "Jul";
    	}
    	
    	if(month.equals("8")) {
    		month = "Aug";
    	}
    	
    	if(month.equals("9")) {
    		month = "Sep";
    	}
    	
    	if(month.equals("10")) {
    		month = "Oct";
    	}
    	
    	if(month.equals("11")) {
    		month = "Nov";
    	}
    	
    	if(month.equals("12")) {
    		month = "Dec";
    	}
    	
    	return month + "" + date + ", " + year;
    }
    
    public static String dayConversion() {
    	String day = null;
    	Calendar cal = Calendar.getInstance();
    	int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    	
    	if(Calendar.MONDAY == dayOfWeek) {
    		day = "MONDAY";
    	}
    	
    	if(Calendar.TUESDAY == dayOfWeek) {
    		day = "TUESDAY";
    	}
    	
    	if(Calendar.WEDNESDAY == dayOfWeek) {
    		day = "WEDNESDAY";
    	}
    	
    	if(Calendar.THURSDAY == dayOfWeek) {
    		day = "THURSDAY";
    	}
    	
    	if(Calendar.FRIDAY == dayOfWeek) {
    		day = "FRIDAY";
    	}
    	
    	if(Calendar.SATURDAY == dayOfWeek) {
    		day = "SATURDAY";
    	}
    	
    	if(Calendar.SUNDAY == dayOfWeek) {
    		day = "SUNDAY";
    	}
    	
    	return day;
    }
}