package com.mehmetakiftutuncu.textinstead.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mehmetakiftutuncu.textinstead.Constants;
import com.mehmetakiftutuncu.textinstead.services.CallListenerService;

/**
 * Broadcast receiver for listening call states and recognizing outgoing calls
 * 
 * @author Mehmet Akif Tütüncü
 */
public class CallBroadcastReceiver extends BroadcastReceiver
{
	/** Tag for logging */
	public static final String DEBUG_TAG = "TextInstead_CallBroadcastReceiver";
	
	// Called when the broadcast is received
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// Get broadcast intent extras
		Bundle extras = intent.getExtras();
		
		// If there are some extras
		if(extras != null)
		{
			// Get phone state extra as a string
			String stateExtra = extras.getString(TelephonyManager.EXTRA_STATE);
			
			// If phone call state was offhook, it means an outgoing call has started
			if(stateExtra.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
			{
				// If the application is enabled from preferences
				if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.PREFERENCE_STATUS, true))
				{
					Log.d(DEBUG_TAG, "Starting call listener service...");
						
					// Start call listener service to detect when and/or if the call ends with 0 seconds duration
					context.startService(new Intent(context, CallListenerService.class));
				}
				else
				{
					Log.d(DEBUG_TAG, "Not starting call listener service... Application is disabled from preferences.");
				}
			}
		}
	}
}