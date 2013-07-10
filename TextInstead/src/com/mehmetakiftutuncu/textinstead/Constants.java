package com.mehmetakiftutuncu.textinstead;

import com.mehmetakiftutuncu.textinstead.activities.DialogActivity;

/**
 * Constant definitions
 * 
 * @author Mehmet Akif Tütüncü
 */
public class Constants
{
	/**	Flag to check if it is the first time of running for main activity */
	public static final String IS_FIRST_TIME_MAIN = "isFirstTimeMain";
	/**	Flag to check if it is the first time of running for preferences activity */
	public static final String IS_FIRST_TIME_PREFERENCES = "isFirstTimePreferences";
	/**	Flag to check if it is the first time of running for custom messages activity */
	public static final String IS_FIRST_TIME_CUSTOM_MESSAGES = "isFirstTimeCustomMessages";
	
	/**	Name of the person who is just called */
	public static final String EXTRA_NAME = "name";
	/**	Number of the person who is just called */
	public static final String EXTRA_NUMBER = "number";
	/**	Optional reminder message for the notification */
	public static final String EXTRA_REMINDER_MESSAGE = "reminderMessage";
	
	/**	Intent action for directly sending sms
	 * 
	 * If the {@link DialogActivity} is started having this action,
	 * it will directly show send sms dialog for the number given in {@link #EXTRA_NUMBER}
	 * 
	 * This action will be set through the action button in the reminder notification. */
	public static final String ACTION_SEND_SMS = "com.mehmetakiftutuncu.textinstead.sendsms";
	/**	Intent action for directly calling again
	 * 
	 * If the {@link DialogActivity} is started having this action,
	 * it will directly call the number given in {@link #EXTRA_NUMBER}
	 * 
	 * This action will be set through the action button in the reminder notification. */
	public static final String ACTION_CALL_AGAIN = "com.mehmetakiftutuncu.textinstead.callagain";
	
	/**	Variable for the name of the person */
	public static final String CUSTOM_MESSAGE_VARIABLE_NAME = "%1%";
	/**	Variable for the full name of the person */
	public static final String CUSTOM_MESSAGE_VARIABLE_FULL_NAME = "%2%";
	
	/**	URI for an sms */
	public static final String URI_SMS = "sms:";
	/**	URI for the body of an sms */
	public static final String URI_SMS_BODY = "sms_body";
	/**	URI for a call */
	public static final String URI_CALL = "tel:";
	/**	URI for default notification sound */
	public static final String URI_DEFAULT_NOTIFICATION_SOUND = "content://settings/system/notification_sound";
	
	/**	Preference for the status of the application */
	public static final String PREFERENCE_STATUS = "preference_status";
	/**	Preference for the delay of question dialog */
	public static final String PREFERENCE_DELAY = "preference_delay";
	/**	Preference for the custom messages */
	public static final String PREFERENCE_MESSAGES = "preference_messages";
	/**	Preference for the custom messages list */
	public static final String[] PREFERENCE_MESSAGES_LIST = new String[]
	{
		"preference_messages_1",
		"preference_messages_2",
		"preference_messages_3",
		"preference_messages_4",
		"preference_messages_5"
	};
	/**	Preference for the status of the sound of the reminder notifications */
	public static final String PREFERENCE_NOTIFICATION_SOUND_STATUS = "preference_reminder_sound_status";
	/**	Preference for the sound of the reminder notifications */
	public static final String PREFERENCE_NOTIFICATION_SOUND = "preference_reminder_sound";
	/**	Preference for the vibration of the reminder notifications */
	public static final String PREFERENCE_VIBRATION = "preference_reminder_vibration";
	
	/**	URI of Text Instead in Play Store */
	public static final String APPLICATION_URI = "market://details?id=com.mehmetakiftutuncu.textinstead";
	/**	E-mail contact address of the developer */
	public static final String CONTACT = "m.akif.tutuncu@gmail.com";
	/**	URL of developer web site */
	public static final String WEBSITE_URL = "http://mehmetakiftutuncu.blogspot.com";
}