package com.mehmetakiftutuncu.textinstead.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
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
 * Custom messages activity of the application
 * 
 * @author Mehmet Akif Tütüncü
 */
@SuppressWarnings("deprecation")
public class CustomMessagesPreferenceActivity extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener
{
	/** Preferences object to read and write preferences */
	private SharedPreferences mPreferences;
	
	/** Preference items for custom messages */
	private EditTextPreference[] mMessages;
	
	/** A reference to the overflow menu */
	private Menu mMenu;
	
	/** Tag for logging */
	public static final String DEBUG_TAG = "TextInstead_CustomMessagesActivity";
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.custommessages_preferences);
        
        initialize();
    }
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences preferences, String key)
	{
		updateMessageSummaries();
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
	
	public void initialize()
	{
		// Initialize preferences
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences.registerOnSharedPreferenceChangeListener(this);
        mMessages = new EditTextPreference[]
		{
        	(EditTextPreference) getPreferenceScreen().findPreference(Constants.PREFERENCE_MESSAGES_LIST[0]),
        	(EditTextPreference) getPreferenceScreen().findPreference(Constants.PREFERENCE_MESSAGES_LIST[1]),
        	(EditTextPreference) getPreferenceScreen().findPreference(Constants.PREFERENCE_MESSAGES_LIST[2]),
        	(EditTextPreference) getPreferenceScreen().findPreference(Constants.PREFERENCE_MESSAGES_LIST[3]),
        	(EditTextPreference) getPreferenceScreen().findPreference(Constants.PREFERENCE_MESSAGES_LIST[4])
		};
        
        // Update the message summaries to show saved messages at the beginning
        updateMessageSummaries();
        
        // Show home button in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Updates the message summaries which will show the content of the messages in the summary sections
	 */
	private void updateMessageSummaries()
	{
		Log.d(DEBUG_TAG, "Updating message summaries...");
		
		for(int i = 0; i < Constants.PREFERENCE_MESSAGES_LIST.length; i++)
		{
			String message = mPreferences.getString(Constants.PREFERENCE_MESSAGES_LIST[i], "");
			if(message.equals(""))
			{
				message = getString(R.string.preferences_messages_notSet);
			}
			mMessages[i].setSummary(message);
		}
	}
}