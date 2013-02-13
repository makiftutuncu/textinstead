package com.mehmetakiftutuncu.textinstead.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.mehmetakiftutuncu.textinstead.Constants;
import com.mehmetakiftutuncu.textinstead.R;

/**
 * Activity for showing the question dialog
 * 
 * @author Mehmet Akif Tütüncü
 */
public class ActivityQuestion extends Activity
{
	/**
	 * Key for logging
	 */
	public static final String DEBUG_KEY = "TextInstead_ActivityQuestion";
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        /* Get intent extras*/
        Bundle extras = getIntent().getExtras();
        
        /* If there are some extras */
        if(extras != null)
        {
        	/* Get name and the number of the last call */
        	final String name = extras.getString(Constants.EXTRA_NAME);
        	final String number = extras.getString(Constants.EXTRA_NUMBER);
        	
        	Log.d(DEBUG_KEY, "Sent information [Name: " + name + ", Number: " + number + "]");
        	
        	AlertDialog dialog;
        	AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        	
        	Log.d(DEBUG_KEY, "Creating question dialog...");
        	
        	/* Start preparing dialog */
    		dialogBuilder.setTitle(getString(R.string.dialog_title));
    		dialogBuilder.setMessage(getQuestion(name, number));
    		dialogBuilder.setPositiveButton(getString(R.string.dialog_yes), new OnClickListener()
    		{
    			@Override
    			public void onClick(DialogInterface dialog, int which)
    			{
    				Log.d(DEBUG_KEY, "Going to send an sms to [Name: " + name + ", Number: " + number + "]");
    				
    				/* Create an intent for sending an sms to a specified number */
    				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(Constants.URI_SMS + number));
            		
    				/* Optional, text to send */
    				intent.putExtra(Constants.URI_SMS_BODY, "Hello world!");
            		
            		finish();
            		startActivity(intent);
    			}
    		});
    		dialogBuilder.setNegativeButton(getString(R.string.dialog_no), new OnClickListener()
    		{
    			@Override
    			public void onClick(DialogInterface dialog, int which)
    			{
    				Log.d(DEBUG_KEY, "Cancelling upon user request...");
    				
    				finish();
    			}
    		});
    		dialogBuilder.setIcon(R.drawable.ic_launcher);
    		
    		dialog = dialogBuilder.create();
    		dialog.show();
        }
    }
    
    /**
     * Makes a human readable question using name and the number given<br><br>
     * 
     * Format: Name (Number)
     * 
     * @param name		Name of the person
     * @param number	Phone number of the person
     * 
     * @return A formatted human readable question
     */
    private String getQuestion(String name, String number)
    {
    	return String.format(getString(R.string.dialog_message, ((name != null && !name.equals("")) ? name : getString(R.string.unknown_name)), number));
    }
}