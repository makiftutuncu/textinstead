package com.mehmetakiftutuncu.textinstead.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mehmetakiftutuncu.textinstead.Constants;
import com.mehmetakiftutuncu.textinstead.R;

/**
 * Preferences activity of the application
 * 
 * @author Mehmet Akif Tütüncü
 */
@SuppressWarnings("deprecation")
public class ActivityPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	private SharedPreferences myPreferences;
	
	private EditTextPreference[] messages;
	private Preference contact;
	private Preference website;
	private Preference about;
	
	/**
	 * Key for logging
	 */
	public static final String DEBUG_KEY = "TextInstead_ActivityPreferences";
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        myPreferences.registerOnSharedPreferenceChangeListener(this);
        
        messages = new EditTextPreference[]
		{
        	(EditTextPreference) getPreferenceScreen().findPreference(Constants.PREFERENCE_MESSAGES[0]),
        	(EditTextPreference) getPreferenceScreen().findPreference(Constants.PREFERENCE_MESSAGES[1]),
        	(EditTextPreference) getPreferenceScreen().findPreference(Constants.PREFERENCE_MESSAGES[2]),
        	(EditTextPreference) getPreferenceScreen().findPreference(Constants.PREFERENCE_MESSAGES[3]),
        	(EditTextPreference) getPreferenceScreen().findPreference(Constants.PREFERENCE_MESSAGES[4])
		};
        contact = (Preference) getPreferenceScreen().findPreference(Constants.PREFERENCE_OTHER_CONTACT);
        website = (Preference) getPreferenceScreen().findPreference(Constants.PREFERENCE_OTHER_WEBSITE);
        about = (Preference) getPreferenceScreen().findPreference(Constants.PREFERENCE_OTHER_ABOUT);
        
        contact.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("plain/text");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] {contact.getSummary().toString()});
				startActivity(Intent.createChooser(intent, getString(R.string.preferences_other_contact)));
				return true;
			}
		});
        website.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(website.getSummary().toString()));
				startActivity(Intent.createChooser(intent, getString(R.string.preferences_other_website)));
				return true;
			}
		});
        about.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				AlertDialog dialog;
	        	AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityPreferences.this);
	        	
	        	dialogBuilder.setTitle(getString(R.string.aboutDialog_title, getString(R.string.app_name)));
	        	dialogBuilder.setMessage(getString(R.string.aboutDialog_message));
	        	dialogBuilder.setIcon(R.drawable.app_icon);
	        	dialogBuilder.setPositiveButton(getString(R.string.aboutDialog_ok), new OnClickListener()
	        	{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
					}
				});
	    		
	    		dialog = dialogBuilder.create();
	    		dialog.show();
				return true;
			}
		});
        
        updateMessageSummaries();
    }
	
	private void updateMessageSummaries()
	{
		Log.d(DEBUG_KEY, "Updating message summaries...");
		
		for(int i = 0; i < Constants.PREFERENCE_MESSAGES.length; i++)
		{
			String message = myPreferences.getString(Constants.PREFERENCE_MESSAGES[i], "");
			if(message.equals(""))
			{
				message = getString(R.string.preferences_messages_notSet);
			}
			messages[i].setSummary(message);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences preferences, String key)
	{
		updateMessageSummaries();
	}
}