package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.DialogListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;

/**
 * Просмотр документа.
 * 
 * @author alexander.ivanov
 * 
 */
public class DocumentActivity extends PreferenceActivity implements
		OnPreferenceClickListener, DialogListener {

	public static final String SAVED_SCANNED = "ru.redsolution.bst.ui.DocumentActivity.SAVED_SCANNED";

	private static final int DIALOG_CANCEL_ID = 1;

	/**
	 * Производилось сканирование.
	 */
	private boolean scanned;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.document);
		findPreference(getString(R.string.scan_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.list_action)).setEnabled(false);
		// TODO .setOnPreferenceClickListener(this);
		findPreference(getString(R.string.header_action)).setEnabled(false);
		// TODO .setOnPreferenceClickListener(this);
		findPreference(getString(R.string.send_action)).setEnabled(false);
		// TODO .setOnPreferenceClickListener(this);
		findPreference(getString(R.string.cancel_action))
				.setOnPreferenceClickListener(this);
		scanned = savedInstanceState != null
				&& savedInstanceState.getBoolean(SAVED_SCANNED, false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
		if (!scanned) {
			startActivity(new Intent(this, VerifyActivity.class));
			scanned = true;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVED_SCANNED, scanned);
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
			showDialog(DIALOG_CANCEL_ID);
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			showDialog(DIALOG_CANCEL_ID);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_CANCEL_ID:
			return new ConfirmDialogBuilder(this, id, this).setMessage(
					R.string.cancel_confirm).create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onAccept(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_CANCEL_ID:
			finish();
			break;
		default:
			break;
		}
		updateView();
	}

	@Override
	public void onDecline(DialogBuilder dialogBuilder) {
	}

	@Override
	public void onCancel(DialogBuilder dialogBuilder) {
	}

	private void updateView() {
		setTitle(R.string.inventory_action);
		// TODO:
		findPreference(getString(R.string.list_action)).setSummary(
				String.format(getString(R.string.list_summary), 0, 0));
	}

}