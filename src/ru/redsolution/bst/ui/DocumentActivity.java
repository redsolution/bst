package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

/**
 * Просмотр документа.
 * 
 * @author alexander.ivanov
 * 
 */
public class DocumentActivity extends PreferenceActivity implements
		OnPreferenceClickListener {

	public static final String SAVED_SCANNED = "ru.redsolution.bst.ui.DocumentActivity.SAVED_SCANNED";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.document);
		findPreference(getString(R.string.scan_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.list_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.header_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.send_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.cancel_action))
				.setOnPreferenceClickListener(this);
		if (savedInstanceState == null
				|| !savedInstanceState.getBoolean(SAVED_SCANNED, false))
			startActivity(new Intent(this, VerifyActivity.class));
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVED_SCANNED, true);
	}

	@Override
	public boolean onPreferenceClick(Preference paramPreference) {
		if (paramPreference.getKey().equals(getString(R.string.scan_action))) {
			startActivity(new Intent(this, VerifyActivity.class));
		} else if (paramPreference.getKey().equals(
				getString(R.string.list_action))) {
			// TODO
		} else if (paramPreference.getKey().equals(
				getString(R.string.header_action))) {
			// TODO
		} else if (paramPreference.getKey().equals(
				getString(R.string.send_action))) {
			// TODO
		} else if (paramPreference.getKey().equals(
				getString(R.string.cancel_action))) {
			// TODO: showDialog(DIALOG_CANCEL_ID);
		}
		return true;
	}

	private void updateView() {
		setTitle(R.string.inventory_action);
		// TODO:
		findPreference(getString(R.string.list_action)).setSummary(
				String.format(getString(R.string.list_summary), 0, 0));
	}

}