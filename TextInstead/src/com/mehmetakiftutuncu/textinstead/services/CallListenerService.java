package com.mehmetakiftutuncu.textinstead.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mehmetakiftutuncu.textinstead.Constants;
import com.mehmetakiftutuncu.textinstead.R;
import com.mehmetakiftutuncu.textinstead.activities.DialogActivity;
import com.mehmetakiftutuncu.textinstead.models.CallLogEntry;

/**
 * Service for listening call states and recognizing outgoing calls with 0 seconds duration
 * 
 * @author Mehmet Akif T�t�nc�
 */
public class CallListenerService extends Service
{
	/** Manager for listening phone states */
	private TelephonyManager mTelephonyManager;
	
	/** Customized PhoneStateListener object */
	private Listener mListener;
	
	/** Tag for logging */
	public static final String DEBUG_TAG = "TextInstead_CallListenerService";
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	/**
	 * Called when the service starts
	 */
	@Override
	public void onCreate()
	{		
		// Initialize a listener
		mListener = new Listener();
		
		// Set telephony manager
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// Start listening for call states
		mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	/**
	 * Called when the service stops
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		// Stop listening for call states
		mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
	}
	
	/**
	 * Reads the device's call log and gets the last call information
	 * 
	 * @param context A Context needed for reading call log
	 * 
	 * @return CallInformation object initialized with information of last call, null in case of any error
	 */
	private CallLogEntry getLastCallInformation(Context context)
	{
		// A CallInformation object to hold the information of last call
		CallLogEntry lastCall = null;
		
		// Create a cursor for reading device's call log database
		Cursor c = context.getContentResolver().query
		(
			android.provider.CallLog.Calls.CONTENT_URI,		// Call log URI
			new String[]									// Columns to read (may be null to get all the columns but it will be inefficient to get columns that won't be used)
			{
				android.provider.CallLog.Calls._ID,
				android.provider.CallLog.Calls.NUMBER,
				android.provider.CallLog.Calls.DATE,
				android.provider.CallLog.Calls.DURATION,
				android.provider.CallLog.Calls.TYPE,
				android.provider.CallLog.Calls.CACHED_NAME
			},
			android.provider.CallLog.Calls._ID + " > ?",	// Selection (will select rows with _id greater than an argument)
			new String[] {"0"},								// Selection argument (0 will be replaced by the ? in the selection expression which will make the selection "_id > 0")
			null											// Ordering
		);
        
        if(c.moveToLast())
        {
	        // Get the id's of necessary information
			int id = c.getColumnIndex(android.provider.CallLog.Calls._ID);
			int number = c.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
			int date = c.getColumnIndex(android.provider.CallLog.Calls.DATE);
			int duration = c.getColumnIndex(android.provider.CallLog.Calls.DURATION);
			int callType = c.getColumnIndex(android.provider.CallLog.Calls.TYPE);
			int name = c.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME);
			
			// Initialize the CallInformation object
			lastCall = new CallLogEntry(c.getString(id),
										c.getString(number),
										c.getString(date),
										c.getString(duration),
										c.getString(callType),
										c.getString(name));
			
			Log.d(DEBUG_TAG, "Last call: " + lastCall);
        }
        
        // Return the resulting object
        return lastCall;
    }
	
	/**
	 * A customized PhoneStateListener class for listening phone states and recognizing outgoing calls with 0 seconds duration
	 * 
	 * @author Mehmet Akif T�t�nc�
	 */
	private class Listener extends PhoneStateListener
	{
		@Override
		public void onCallStateChanged(int state, String incomingNumber)
		{
			super.onCallStateChanged(state, incomingNumber);
			
			// Check the current state, this service will only be started when an outgoing call is initiated,
			// so if the state is idle when the call state changes, it means the outgoing call has ended
			if(state == TelephonyManager.CALL_STATE_IDLE)
			{
	    		// Wait until the last call is written to call log database
	    		try
	    		{
	    			String delay = PreferenceManager.getDefaultSharedPreferences(CallListenerService.this).getString(Constants.PREFERENCE_DELAY, "2");
	    			
	    			Log.d(DEBUG_TAG, "Sleeping for " + delay + " seconds...");
	    			
	    			Thread.sleep(Long.parseLong(delay) * 1000);
				}
	    		catch(InterruptedException e)
	    		{
					e.printStackTrace();
				}
	    		
	    		// Get the information of that call just ended
	        	final CallLogEntry lastCall = getLastCallInformation(CallListenerService.this);
	        	
	        	// If it is not null, outgoing and the duration of it is 2 seconds
	        	if(	lastCall != null &&
	        		lastCall.getCallType().equals(String.valueOf(android.provider.CallLog.Calls.OUTGOING_TYPE)) &&
	        		Integer.parseInt(lastCall.getDuration()) <= 2)
	        	{
	        		Log.d(DEBUG_TAG, "Taking action!");
	        		
	        		if(lastCall.getName() == null || lastCall.getName().equals(""))
	        		{
	        			lastCall.setName(getString(R.string.unregistered_contact));
	        		}
	        		
	        		// Start the question dialog activity
	        		Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
	        		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        		intent.putExtra(Constants.EXTRA_NAME, lastCall.getName());
	        		intent.putExtra(Constants.EXTRA_NUMBER, lastCall.getNumber());
	        		startActivity(intent);
	        	}
	        	
	        	// Call ended, handled, stop service
	        	stopSelf();
	        	
	        	Log.d(DEBUG_TAG, "Stopping call listener service...");
			}
		}
	}
}