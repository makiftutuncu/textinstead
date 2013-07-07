package com.mehmetakiftutuncu.textinstead.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.mehmetakiftutuncu.textinstead.Constants;
import com.mehmetakiftutuncu.textinstead.R;
import com.mehmetakiftutuncu.textinstead.utilities.MenuHandler;

/**
 * Main activity of the application
 * 
 * @author Mehmet Akif Tütüncü
 */
public class MainActivity extends SherlockActivity
{
	/** Preferences of the application */
	private SharedPreferences mPreferences;
	
	/** Button to change status of the application */
	private ImageButton mStatusButton;
	/** Text that shows the status of the application */
	private TextView mStatusText;
	
	/** Current status of the application */
	private boolean mStatus;
	
	/** A reference to the overflow menu */
	private Menu mMenu;
	
	/** Tag for logging */
	public static final String DEBUG_TAG = "TextInstead_MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Keep the reference to the menu
		mMenu = menu;
		
		// Create preferences menu item
		menu.add("item_preferences")
		.setIcon(R.drawable.ic_preferences)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
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
	
	/**
	 * Initializes the main activity
	 */
	private void initialize()
	{
		mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		if(!mPreferences.contains(Constants.PREFERENCE_STATUS))
		{
			mStatus = true;
		}
		else
		{
			mStatus = mPreferences.getBoolean(Constants.PREFERENCE_STATUS, false);
		}
		
		mStatusButton = (ImageButton) findViewById(R.id.imageButton_main_status);
		mStatusButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mStatus = !mStatus;
				updateStatus(mStatus);
			}
		});
		mStatusText = (TextView) findViewById(R.id.textView_main_status);
		
		updateStatus(mStatus);
	}

	/**
	 * Updates the button and text about application status
	 *
	 * @param isEnabled If true, it will indicate that the application is enabled
	 */
	private void updateStatus(boolean isEnabled)
	{
		if(isEnabled)
		{
			mStatusButton.getDrawable().setAlpha(255);
			mStatusText.setText(getString(R.string.main_status_enabled));
			mStatusText.setTextColor(getResources().getColor(R.color.blue));
		}
		else
		{
			mStatusButton.getDrawable().setAlpha(127);
			mStatusText.setText(getString(R.string.main_status_disabled));
			mStatusText.setTextColor(getResources().getColor(R.color.red));
		}
		
		mPreferences.edit().putBoolean(Constants.PREFERENCE_STATUS, isEnabled).commit();
	}
}