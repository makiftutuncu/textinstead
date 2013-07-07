package com.mehmetakiftutuncu.textinstead;

/**
 * Constant definitions
 * 
 * @author Mehmet Akif Tütüncü
 */
public class Constants
{
	/**	Name of the person who is just called */
	public static final String EXTRA_NAME = "name";
	/**	Number of the person who is just called */
	public static final String EXTRA_NUMBER = "number";
	/**	URI for an sms */
	public static final String URI_SMS = "sms:";
	/**	URI for the body of an sms */
	public static final String URI_SMS_BODY = "sms_body";
	
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
	
	/**	URI of Text Instead in Play Store */
	public static final String APPLICATION_URI = "market://details?id=com.mehmetakiftutuncu.textinstead";
	/**	E-mail contact address of the developer */
	public static final String CONTACT = "m.akif.tutuncu@gmail.com";
	/**	URL of developer web site */
	public static final String WEBSITE_URL = "http://mehmetakiftutuncu.blogspot.com";
}