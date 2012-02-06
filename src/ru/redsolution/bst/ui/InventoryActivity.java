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
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class InventoryActivity extends PreferenceActivity implements
		OnPreferenceClickListener, DialogListener {

	private static final int DIALOG_WAREHOUSE_ID = 2;
	private static final int DIALOG_MY_COMPANY_ID = 3;
	private static final int DIALOG_DEFAULTS_ID = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListView().addFooterView(
				getLayoutInflater().inflate(R.layout.ok, getListView(), false),
				null, false);
		addPreferencesFromResource(R.xml.inventory);
		findPreference(getString(R.string.warehouse_key))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.my_company_key))
				.setOnPreferenceClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		BST.getInstance().setWarehouse(BST.getInstance().getDefaultWarehouse());
		BST.getInstance().setMyCompany(BST.getInstance().getDefaultMyCompany());
		if (WarehouseTable.getInstance().getName(
				BST.getInstance().getWarehouse()) == null
				|| MyCompanyTable.getInstance().getName(
						BST.getInstance().getMyCompany()) == null) {
			showDialog(DIALOG_DEFAULTS_ID);
		}
		updateView();
	}

	@Override
	public boolean onPreferenceClick(Preference paramPreference) {
		if (paramPreference.getKey().equals(getString(R.string.warehouse_key))) {
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
		case DIALOG_WAREHOUSE_ID:
			return new CursorChoiceDialogBuilder(this, id, this, WarehouseTable
					.getInstance().list(), BST.getInstance().getWarehouse(),
					WarehouseTable.Fields.NAME).setTitle(
					R.string.warehouse_title).create();
		case DIALOG_MY_COMPANY_ID:
			return new CursorChoiceDialogBuilder(this, id, this, MyCompanyTable
					.getInstance().list(), BST.getInstance().getMyCompany(),
					MyCompanyTable.Fields.NAME).setTitle(
					R.string.my_company_title).create();
		case DIALOG_DEFAULTS_ID:
			return new ConfirmDialogBuilder(this, id, this).setMessage(
					R.string.defaults_hint).create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onAccept(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_WAREHOUSE_ID:
			BST.getInstance().setWarehouse(
					((CursorChoiceDialogBuilder) dialogBuilder).getCheckedId());
			break;
		case DIALOG_MY_COMPANY_ID:
			BST.getInstance().setMyCompany(
					((CursorChoiceDialogBuilder) dialogBuilder).getCheckedId());
			break;
		case DIALOG_DEFAULTS_ID:
			Intent intent = new Intent(this, SettingsActivity.class);
			intent.putExtra(SettingsActivity.EXTRA_SET_DEFAULTS, true);
			startActivity(intent);
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
		findPreference(getString(R.string.warehouse_key)).setSummary(
				WarehouseTable.getInstance().getName(
						BST.getInstance().getWarehouse()));
		findPreference(getString(R.string.my_company_key)).setSummary(
				MyCompanyTable.getInstance().getName(
						BST.getInstance().getMyCompany()));
	}
}
