package com.mehmetakiftutuncu.textinstead.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.util.Log;
import android.view.KeyEvent;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.mehmetakiftutuncu.textinstead.Constants;
import com.mehmetakiftutuncu.textinstead.R;
import com.mehmetakiftutuncu.textinstead.utilities.MenuHandler;

/**
 * Preferences activity of the application
 * 
 * @author Mehmet Akif Tütüncü
 */
@SuppressWarnings("deprecation")
public class PreferencesActivity extends SherlockPreferenceActivity
{
	/** Preferences object to read and write preferences */
	private SharedPreferences mPreferences;
	
	/** Preference for delay */
	private ListPreference mDelayPreference;
	
	/** Preference for custom messages */
	private Preference mMessages;
	
	/** Preference for notification sound */
	private RingtonePreference mSoundPreference;
	
	/** A reference to the overflow menu */
	private Menu mMenu;
	
	/** Tag for logging */
	public static final String DEBUG_TAG = "TextInstead_PreferencesActivity";
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_preferences);
        addPreferencesFromResource(R.xml.reminder_preferences);
        
        initialize();
    }
	
	/* Checks the menu key press event to toggle the overflow menu manually
	 * 
	 * Using onKeyUp instead of onKeyDown is working, otherwise the menu would just close itself after opening
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		// If pressed menu key
		if(keyCode == KeyEvent.KEYCODE_MENU)
		{
			if (event.getAction() == KeyEvent.ACTION_UP && mMenu != null)
			{
				// Toggle the overflow menu
				mMenu.performIdentifierAction(R.id.item_more, 0);
				
				return true;
			}
		}
		
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Keep the reference to the menu
		mMenu = menu;
		
		// Inflate the menu and add the overflow menu
		MenuInflater inflater = getSherlock().getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle the selection of any menu item
		MenuHandler.handle(this, item);
		
		return true;
	}
	
	/**
	 * Initializes preferences activity
	 */
	private void initialize()
	{
		// Initialize preferences
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(!mPreferences.contains(Constants.PREFERENCE_NOTIFICATION_SOUND))
		{
			mPreferences.edit().putString(Constants.PREFERENCE_NOTIFICATION_SOUND, Constants.URI_DEFAULT_NOTIFICATION_SOUND).commit();
		}
		
		mDelayPreference = (ListPreference) findPreference(Constants.PREFERENCE_DELAY);
		mDelayPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				updateDelaySummary((String) newValue);
				
				return true;
			}
		});
        
        updateDelaySummary(mPreferences.getString(Constants.PREFERENCE_DELAY, "" + 2));
        
        mMessages = findPreference(Constants.PREFERENCE_MESSAGES);
        mMessages.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				Intent intent = new Intent(PreferencesActivity.this, CustomMessagesPreferenceActivity.class);
				startActivity(intent);
				
				return true;
			}
		});
        
        mSoundPreference = (RingtonePreference) findPreference(Constants.PREFERENCE_NOTIFICATION_SOUND);
        mSoundPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				updateSoundSummary((String) newValue);
				
				return true;
			}
		});
        
        updateSoundSummary(mPreferences.getString(Constants.PREFERENCE_NOTIFICATION_SOUND, Constants.URI_DEFAULT_NOTIFICATION_SOUND));
        
        // Show home button in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	private void updateDelaySummary(String newValue)
	{
		mDelayPreference.setSummary(getString(R.string.preferences_delay_summary, newValue));
		
		Log.d(DEBUG_TAG, "Updating delay to " + newValue);
	}
	
	private void updateSoundSummary(String newValue)
	{
		Ringtone ringtone = RingtoneManager.getRingtone(PreferencesActivity.this, Uri.parse(newValue));
		String name = ringtone.getTitle(PreferencesActivity.this);
		
		mSoundPreference.setSummary(name);
		
		Log.d(DEBUG_TAG, "Updating sound to " + name);
	}
}