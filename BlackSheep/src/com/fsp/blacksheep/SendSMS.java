package com.fsp.blacksheep;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 
public class SendSMS extends Activity 
{
    Button btnSendSMS;
    EditText txtPhoneNo;
    EditText txtMessage;
    String phoneNo;
    String message;
    String post;
    TextView tv;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) 
    {
    	
    	super.onCreate(icicle);
    	
    }    
    
  //---sends an SMS message to another device---
    @SuppressWarnings("deprecation")
	private void sendSMS(String phoneNumber, String message){        
    	Log.v("phoneNumber",phoneNumber);
    	Log.v("MEssage",message);
        PendingIntent pi = PendingIntent.getActivity(this, 0,
            new Intent(this, SendSMS.class), 0);                
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);        
    }    
}