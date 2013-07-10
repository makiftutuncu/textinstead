package com.mehmetakiftutuncu.textinstead.broadcastreceivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mehmetakiftutuncu.textinstead.Constants;
import com.mehmetakiftutuncu.textinstead.R;
import com.mehmetakiftutuncu.textinstead.activities.DialogActivity;

/**
 * Broadcast receiver for showing reminder notification when it is time
 * 
 * @author Mehmet Akif Tütüncü
 */
public class ReminderBroadcastReceiver extends BroadcastReceiver
{
	/** Tag for logging */
	public static final String DEBUG_TAG = "TextInstead_ReminderBroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		/* This is a workaround to get the notification work with the latest data
		 * 
		 * The intent extras weren't working as expected so the latest data is written
		 * to shared preferences before and read here.
		 */
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String name = preferences.getString(Constants.EXTRA_NAME, context.getString(R.string.unregistered_contact));
		String number = preferences.getString(Constants.EXTRA_NUMBER, "");
		String reminderMessage = preferences.getString(Constants.EXTRA_REMINDER_MESSAGE, context.getString(R.string.reminderNotification_text));
		boolean soundEnabled = preferences.getBoolean(Constants.PREFERENCE_NOTIFICATION_SOUND_STATUS, true);
		String sound = preferences.getString(Constants.PREFERENCE_NOTIFICATION_SOUND, null);
		boolean vibrate = preferences.getBoolean(Constants.PREFERENCE_VIBRATION, true);
		
		createNotification(context, name, number, soundEnabled, sound, reminderMessage, vibrate);
	}
	
	/**
	 * Creates and schedules a notification as a reminder
	 * 
	 * @param context Context of the activity
	 * @param name Name of the person
     * @param number Phone number of the person
	 * @param soundEnabled If true sound will play during notification
	 * @param sound Path of the notification sound
	 * @param reminderMessage An optional message for the notification
	 * @param vibrate If true the device will vibrate during notification
	 */
	private void createNotification(Context context, String name, String number, boolean soundEnabled, String sound, String reminderMessage, boolean vibrate)
	{
		Log.d(DEBUG_TAG, "Creating notification for [Name: " + name + ", Number: " + number + "] with reminder message as " + reminderMessage);
		
		Intent intent = new Intent(context, DialogActivity.class);
    	intent.putExtra(Constants.EXTRA_NAME, name);
    	intent.putExtra(Constants.EXTRA_NUMBER, number);
    	intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_RECEIVER_REPLACE_PENDING);
    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    	
    	Intent sendSmsIntent = new Intent(intent);
    	sendSmsIntent.setAction(Constants.ACTION_SEND_SMS);
    	PendingIntent pendingSendSmsIntent = PendingIntent.getActivity(context, 0, sendSmsIntent, 0);
    	
    	Intent callAgainIntent = new Intent(intent);
    	callAgainIntent.setAction(Constants.ACTION_CALL_AGAIN);
    	PendingIntent pendingCallAgainIntent = PendingIntent.getActivity(context, 0, callAgainIntent, 0);
    	
    	NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
		.setContentTitle(context.getString(R.string.reminderNotification_title, name, number))
		.setContentText(reminderMessage)
		.setTicker(context.getString(R.string.reminderNotification_ticker, name, number))
		.setAutoCancel(true)
		.setSmallIcon(R.drawable.ic_notification)
		.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
		.setContentIntent(pendingIntent)
		.addAction(R.drawable.ic_action_sms, context.getString(R.string.reminderNotification_sendSms), pendingSendSmsIntent)
		.addAction(R.drawable.ic_action_call, context.getString(R.string.reminderNotification_callAgain), pendingCallAgainIntent)
		.setDefaults(Notification.DEFAULT_LIGHTS);
    	
    	Notification notification = notificationBuilder.build();
    	
    	if(soundEnabled)
    	{
    		if(sound != null)
    		{
    			notification.sound = !sound.equals("") ? Uri.parse(sound) : Uri.parse(Constants.URI_DEFAULT_NOTIFICATION_SOUND);
    			//notificationBuilder.setSound(!sound.equals("") ? Uri.parse(sound) : Uri.parse(Constants.URI_DEFAULT_NOTIFICATION_SOUND));
    		}
    	}
    	
		if(vibrate)
		{
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			//notificationBuilder.setVibrate(null);
		}
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
	}
}