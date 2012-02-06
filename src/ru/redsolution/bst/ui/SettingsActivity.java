package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.tables.MyCompanyTable;
import ru.redsolution.bst.data.tables.WarehouseTable;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.ConfirmDialogListener;
import ru.redsolution.dialogs.CursorChoiceDialogBuilder;
import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;

/**
 * Окно настроет.
 * 
 * @author alexander.ivanov
 * 
 */
public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener, ConfirmDialogListener {

	private static final int DIALOG_AUTH_ID = 1;
	private static final int DIALOG_WAREHOUSE_ID = 2;
	private static final int DIALOG_MY_COMPANY_ID = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		findPreference(getString(R.string.auth_title))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.warehouse_title))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.company_title))
				.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference paramPreference) {
		if (paramPreference.getKey().equals(getString(R.string.auth_title))) {
			showDialog(DIALOG_AUTH_ID);
		} else if (paramPreference.getKey().equals(
				getString(R.string.warehouse_title))) {
			showDialog(DIALOG_STORE_ID);
		} else if (paramPreference.getKey().equals(
				getString(R.string.company_title))) {
			showDialog(DIALOG_COMPANY_ID);
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_AUTH_ID:
			View layout = getLayoutInflater().inflate(R.layout.auth, null,
					false);
			return new ConfirmDialogBuilder(this, DIALOG_AUTH_ID, this)
					.setView(layout).setTitle(R.string.auth_title).create();
		case DIALOG_WAREHOUSE_ID:
			return new CursorChoiceDialogBuilder(this, id, this, WarehouseTable
					.getInstance().list(), -1, WarehouseTable.Fields.NAME)
					.create();
		case DIALOG_MY_COMPANY_ID:
			return new CursorChoiceDialogBuilder(this, id, this, MyCompanyTable
					.getInstance().list(), -1, MyCompanyTable.Fields.NAME)
					.create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onAccept(ConfirmDialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_AUTH_ID:
			// TODO: BST.getInstance().setLogin();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDecline(ConfirmDialogBuilder dialogBuilder) {
	}

	@Override
	public void onCancel(ConfirmDialogBuilder dialogBuilder) {
	}
}