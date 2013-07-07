package com.mehmetakiftutuncu.textinstead.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
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
public class PreferencesActivity extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener
{
	/** Preferences object to read and write preferences */
	private SharedPreferences mPreferences;
	
	/** Preference for custom messages */
	private Preference mMessages;
	
	/** A reference to the overflow menu */
	private Menu mMenu;
	
	/** Tag for logging */
	public static final String DEBUG_TAG = "TextInstead_PreferencesActivity";
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        initialize();
    }
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences preferences, String key)
	{
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
        mPreferences.registerOnSharedPreferenceChangeListener(this);
        
        mMessages = findPreference(Constants.PREFERENCE_MESSAGES);
        mMessages.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				Intent intent = new Intent(PreferencesActivity.this, CustomMessagesActivity.class);
				startActivity(intent);
				
				return true;
			}
		});
        
        // Show home button in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}