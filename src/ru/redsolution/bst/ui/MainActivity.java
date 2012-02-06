package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

/**
 * Главное окно приложения.
 * 
 * @author alexander.ivanov
 * 
 */
public class MainActivity extends PreferenceActivity implements
		OnPreferenceClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main);
		findPreference(getString(R.string.import_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.inventory_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.settings_action))
				.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference paramPreference) {
		if (paramPreference.getKey().equals(getString(R.string.import_action))) {
			BST.getInstance().importData();
			// TODO
		} else if (paramPreference.getKey().equals(
				getString(R.string.inventory_action))) {
			startActivity(new Intent(this, InventoryActivity.class));
		} else if (paramPreference.getKey().equals(
				getString(R.string.settings_action))) {
			startActivity(new Intent(this, SettingsActivity.class));
		}
		return true;
	}
}