package com.mehmetakiftutuncu.textinstead.utilities;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mehmetakiftutuncu.textinstead.Constants;
import com.mehmetakiftutuncu.textinstead.R;
import com.mehmetakiftutuncu.textinstead.activities.CustomMessagesPreferenceActivity;
import com.mehmetakiftutuncu.textinstead.activities.MainActivity;
import com.mehmetakiftutuncu.textinstead.activities.PreferencesActivity;
import com.mehmetakiftutuncu.textinstead.broadcastreceivers.ReminderBroadcastReceiver;

/**
 * A utility class for dialogs
 * 
 * @author Mehmet Akif Tütüncü
 */
public class Dialogs
{
	/** Context of the activity */
	private Context mContext;
	
	/** Name of the person */
	private String mName;
    /** Phone mNumber of the person */
	private String mNumber;
	
	/** Tag for logging */
	public static final String DEBUG_TAG = "TextInstead_Dialogs";
	
	public Dialogs(Context context, String name, String number)
	{
		mContext = context;
		mName = name;
		mNumber = number;
	}
	
	/**
     * Shows the question dialog after an unsuccessful outgoing call
     */
	public void showQuestionDialog()
	{
		AlertDialog dialog;
    	AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
    	
    	Log.d(DEBUG_TAG, "Creating question dialog...");
    	
    	mName = (mName == null || mName.equals("") ? mContext.getString(R.string.unregistered_contact) : mName);
    	
    	// Start preparing dialog
		dialogBuilder.setTitle(mContext.getString(R.string.questionDialog_title));
		dialogBuilder.setMessage(mContext.getString(R.string.questionDialog_message, mName, mNumber));
		dialogBuilder.setPositiveButton(mContext.getString(R.string.questionDialog_sendSms), new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				showMessagesDialog();
			}
		});
		dialogBuilder.setNeutralButton(mContext.getString(R.string.questionDialog_callAgain), new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Log.d(DEBUG_TAG, "Calling again...");
				
				callNumber();
			}
		});
		dialogBuilder.setNegativeButton(mContext.getString(R.string.questionDialog_remindLater), new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				showReminderDialog();
			}
		});
		dialogBuilder.setIcon(R.drawable.app_icon);
		
		dialog = dialogBuilder.create();
		dialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				Log.d(DEBUG_TAG, "Cancelling upon user request...");
				
				((Activity) mContext).finish();
			}
		});
		dialog.show();
	}
	
	/**
     * Shows the messages dialog after user chooses to send a message
     */
	public void showMessagesDialog()
	{
		Log.d(DEBUG_TAG, "Reading the list of custom messages...");
				
		// Read defined custom messages
		ArrayList<String> messages = new ArrayList<String>();
		
		String unregistered = mContext.getString(R.string.unregistered_contact);
		
		for(String i : Constants.PREFERENCE_MESSAGES_LIST)
		{
			String j = PreferenceManager.getDefaultSharedPreferences(mContext).getString(i, "");
			
			if(!j.equals(""))
			{
				// Replace variables if there's any
				if(!mName.equals(unregistered))
				{
					j = j.replaceAll(Constants.CUSTOM_MESSAGE_VARIABLE_NAME, getFirstName(mName));
					j = j.replaceAll(Constants.CUSTOM_MESSAGE_VARIABLE_FULL_NAME, mName);
				}
				else
				{
					j = j.replaceAll(Constants.CUSTOM_MESSAGE_VARIABLE_NAME, "");
					j = j.replaceAll(Constants.CUSTOM_MESSAGE_VARIABLE_FULL_NAME, "");
				}
				
				messages.add(j);
			}
		}
		
		// There are defined messages
		if(messages.size() > 0)
		{
			Log.d(DEBUG_TAG, "Creating messages dialog...");
			
			final String[] items = new String[messages.size() + 1];
			items[0] = mContext.getString(R.string.messageDialog_newSms);
			for(int i = 1; i < items.length; i++)
			{
				items[i] = messages.get(i - 1);
			}
			
			AlertDialog messagesDialog;
        	AlertDialog.Builder messagesDialogBuilder = new AlertDialog.Builder(mContext);
        	
        	messagesDialogBuilder.setTitle(mContext.getString(R.string.messageDialog_title));
        	messagesDialogBuilder.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, items), new OnClickListener()
        	{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// User chose to write a new sms
					if(which == 0)
					{
						Log.d(DEBUG_TAG, "Going to write a new sms to [Name: " + mName + ", Number: " + mNumber + "]");
						
						sendSms("");
					}
					else
					{
						Log.d(DEBUG_TAG, "Going to send an sms to [Name: " + mName + ", Number: " + mNumber + ", Message: " + items[which] + "]");
	    				
						sendSms(items[which].toString());
					}
				}
			});
        	messagesDialogBuilder.setNegativeButton(mContext.getString(R.string.dialog_cancel), new OnClickListener()
    		{
    			@Override
    			public void onClick(DialogInterface dialog, int which)
    			{
    				dialog.cancel();
    			}
    		});
        	messagesDialogBuilder.setIcon(R.drawable.app_icon);
        	
        	messagesDialog = messagesDialogBuilder.create();
        	messagesDialog.setOnCancelListener(new OnCancelListener()
    		{
    			@Override
    			public void onCancel(DialogInterface dialog)
    			{
    				Log.d(DEBUG_TAG, "Cancelling upon user request...");
    				
    				((Activity) mContext).finish();
    			}
    		});
        	messagesDialog.show();
		}
		else
		{
			// No messages, show toast, start message activity
			
			Toast.makeText(mContext, mContext.getString(R.string.messageDialog_error), Toast.LENGTH_LONG).show();
			
			Log.d(DEBUG_TAG, "No custom messages found. Going to write a new sms to [Name: " + mName + ", Number: " + mNumber + "]");
			
			sendSms("");
		}
	}
	
	/**
     * Shows the reminder dialog after user chooses to create a reminder
     */
	public void showReminderDialog()
	{
		AlertDialog dialog;
    	AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
    	
    	Log.d(DEBUG_TAG, "Creating reminder dialog...");
    	
    	View dialogLayout = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_reminder, null);
    	final TimePicker timePicker = (TimePicker) dialogLayout.findViewById(R.id.timePicker_reminderDialog);
    	timePicker.setIs24HourView(true);
    	timePicker.setCurrentHour(Integer.valueOf(0));
    	timePicker.setCurrentMinute(Integer.valueOf(5));
    	final EditText reminderMessage = (EditText) dialogLayout.findViewById(R.id.editText_reminderDialog_customReminderMessage);
    	
    	// Start preparing dialog
		dialogBuilder.setTitle(mContext.getString(R.string.reminderDialog_title));
		dialogBuilder.setView(dialogLayout);
		dialogBuilder.setPositiveButton(mContext.getString(R.string.reminderDialog_set), new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String message = reminderMessage.getText().toString();
				
				createReminder(timePicker.getCurrentHour(), timePicker.getCurrentMinute(), message.equals("") ? mContext.getString(R.string.reminderNotification_text) : message);
			}
		});
		dialogBuilder.setNegativeButton(mContext.getString(R.string.dialog_cancel), new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});
		dialogBuilder.setIcon(R.drawable.app_icon);
		
		dialog = dialogBuilder.create();
		dialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				Log.d(DEBUG_TAG, "Cancelling upon user request...");
				
				((Activity) mContext).finish();
			}
		});
		dialog.show();
	}
	
	/**
     * Shows the help dialog either because it was the first time of that activity or user selected help in the menu
     * 
     * @param context Context of the activity
     */
	public static void showHelpDialog(Context context)
	{
		AlertDialog dialog;
    	AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
    	
    	Log.d(DEBUG_TAG, "Creating help dialog...");
    	
    	// Start preparing dialog
		dialogBuilder.setTitle(context.getString(R.string.helpDialog_title));
		if(((Activity) context) instanceof MainActivity)
		{
			dialogBuilder.setMessage(context.getString(R.string.helpDialog_message_main));
		}
		else if(((Activity) context) instanceof PreferencesActivity)
		{
			dialogBuilder.setMessage(context.getString(R.string.helpDialog_message_preferences));
		}
		else if(((Activity) context) instanceof CustomMessagesPreferenceActivity)
		{
			dialogBuilder.setMessage(context.getString(R.string.helpDialog_message_messages));
		}
		dialogBuilder.setPositiveButton(context.getString(R.string.dialog_ok), new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});
		dialogBuilder.setIcon(R.drawable.app_icon);
		
		dialog = dialogBuilder.create();
		dialog.show();
	}
	
	/**
     * Shows the about dialog when user selected about in the menu
     * 
     * @param context Context of the activity
     */
	public static void showAboutDialog(Context context)
	{
		AlertDialog dialog;
    	AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
    	
    	Log.d(DEBUG_TAG, "Creating about dialog...");
    	
    	// Start preparing dialog
		dialogBuilder.setTitle(context.getString(R.string.aboutDialog_title));
		dialogBuilder.setMessage(context.getString(R.string.aboutDialog_message));
		dialogBuilder.setPositiveButton(context.getString(R.string.dialog_ok), new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});
		dialogBuilder.setIcon(R.drawable.app_icon);
		
		dialog = dialogBuilder.create();
		dialog.show();
	}
    
    /**
     * Starts an sms activity with provided information
     * 
     * @param message Message to be sent
     */
    public void sendSms(String message)
    {
    	// Create an intent for sending an sms to a specified mNumber
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(Constants.URI_SMS + mNumber));
		
		// Optional, text to send
		intent.putExtra(Constants.URI_SMS_BODY, message);
		
		((Activity) mContext).finish();
		mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.questionDialog_sendSms)));
    }
    
    /**
     * Starts a call activity with provided information
     */
    public void callNumber()
    {
    	// Create an intent for calling with the specified mNumber
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(Constants.URI_CALL + mNumber));
		
		((Activity) mContext).finish();
		mContext.startActivity(intent);
    }
    
    /**
     * Creates a reminder with provided information
     * 
     * @param hours Hours until the notification
     * @param minutes Minutes until the notification
     * @param reminderMessage An optional reminder message for the notification
     */
    public void createReminder(int hours, int minutes, String reminderMessage)
    {
    	Log.d(DEBUG_TAG, "Creating a reminder for [Name: " + mName + ", Number: " + mNumber + "] scheduled in " + String.format("%02d hour(s) %02d minute(s)", hours, minutes));
    	
    	AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    	Intent intent = new Intent(mContext, ReminderBroadcastReceiver.class);
    	
    	/* This is a workaround to get the notification work with the latest data
		 * 
		 * The intent extras weren't working as expected so the latest data is written
		 * to shared preferences here and read in the broadcast receiver before creating the notification.
		 */
    	SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
    	editor.putString(Constants.EXTRA_NAME, mName);
    	editor.putString(Constants.EXTRA_NUMBER, mNumber);
    	editor.putString(Constants.EXTRA_REMINDER_MESSAGE, reminderMessage);
    	editor.commit();
    	
    	PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
    	
    	Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        
        calendar.add(Calendar.HOUR, hours);
        calendar.add(Calendar.MINUTE, minutes);
        
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
		
		((Activity) mContext).finish();
    }
    
    private String getFirstName(String name)
    {
		return name.split(" ")[0];
	}
}