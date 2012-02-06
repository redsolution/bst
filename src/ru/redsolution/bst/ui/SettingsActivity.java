package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.tables.MyCompanyTable;
import ru.redsolution.bst.data.tables.WarehouseTable;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.CursorChoiceDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.DialogListener;
import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Окно настроет.
 * 
 * @author alexander.ivanov
 * 
 */
public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener, DialogListener {

	public static final String EXTRA_SET_DEFAULTS = "ru.redsolution.bst.ui.SettingsActivity.EXTRA_SET_DEFAULTS";

	private static final int DIALOG_AUTH_ID = 1;
	private static final int DIALOG_WAREHOUSE_ID = 2;
	private static final int DIALOG_MY_COMPANY_ID = 3;
	private EditText loginView;
	private EditText passwordView;
	private Preference loginPreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().getBooleanExtra(EXTRA_SET_DEFAULTS, false))
			addPreferencesFromResource(R.xml.defaults);
		else
			addPreferencesFromResource(R.xml.settings);
		loginPreference = findPreference(getString(R.string.login_key));
		if (loginPreference != null)
			loginPreference.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.warehouse_key))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.my_company_key))
				.setOnPreferenceClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
	}

	@Override
	public boolean onPreferenceClick(Preference paramPreference) {
		if (paramPreference.getKey().equals(getString(R.string.login_key))) {
			showDialog(DIALOG_AUTH_ID);
		} else if (paramPreference.getKey().equals(
				getString(R.string.warehouse_key))) {
			showDialog(DIALOG_WAREHOUSE_ID);
		} else if (paramPreference.getKey().equals(
				getString(R.string.my_company_key))) {
			showDialog(DIALOG_MY_COMPANY_ID);
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_AUTH_ID:
			View dialogLayout = getLayoutInflater().inflate(R.layout.auth,
					null, false);
			loginView = (EditText) dialogLayout.findViewById(R.id.login);
			passwordView = (EditText) dialogLayout.findViewById(R.id.password);
			loginView.setText(BST.getInstance().getLogin());
			passwordView.setText(BST.getInstance().getPassword());
			return new ConfirmDialogBuilder(this, DIALOG_AUTH_ID, this)
					.setView(dialogLayout).setTitle(R.string.auth_title)
					.create();
		case DIALOG_WAREHOUSE_ID:
			return new CursorChoiceDialogBuilder(this, id, this, WarehouseTable
					.getInstance().list(), BST.getInstance()
					.getDefaultWarehouse(), WarehouseTable.Fields.NAME)
					.create();
		case DIALOG_MY_COMPANY_ID:
			return new CursorChoiceDialogBuilder(this, id, this, MyCompanyTable
					.getInstance().list(), BST.getInstance()
					.getDefaultMyCompany(), MyCompanyTable.Fields.NAME)
					.create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onAccept(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_AUTH_ID:
			BST.getInstance().setLoginAndPassword(
					loginView.getText().toString(),
					passwordView.getText().toString());
			break;
		case DIALOG_WAREHOUSE_ID:
			BST.getInstance().setDefaultWarehouse(
					((CursorChoiceDialogBuilder) dialogBuilder).getCheckedId());
			break;
		case DIALOG_MY_COMPANY_ID:
			BST.getInstance().setDefaultMyCompany(
					((CursorChoiceDialogBuilder) dialogBuilder).getCheckedId());
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
		if (loginPreference != null)
			loginPreference.setSummary(BST.getInstance().getLogin());
		findPreference(getString(R.string.warehouse_key)).setSummary(
				WarehouseTable.getInstance().getName(
						BST.getInstance().getDefaultWarehouse()));
		findPreference(getString(R.string.my_company_key)).setSummary(
				MyCompanyTable.getInstance().getName(
						BST.getInstance().getDefaultMyCompany()));
	}
}