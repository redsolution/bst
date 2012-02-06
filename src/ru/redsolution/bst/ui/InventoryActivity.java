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

public class InventoryActivity extends PreferenceActivity implements
		OnPreferenceClickListener, ConfirmDialogListener {

	private static final int DIALOG_WAREHOUSE_ID = 2;
	private static final int DIALOG_MY_COMPANY_ID = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListView().addFooterView(
				getLayoutInflater().inflate(R.layout.ok, getListView(), false),
				null, false);
		addPreferencesFromResource(R.xml.inventory);
		findPreference(getString(R.string.warehouse_title))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.company_title))
				.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference paramPreference) {
		if (paramPreference.getKey()
				.equals(getString(R.string.warehouse_title))) {
			showDialog(DIALOG_WAREHOUSE_ID);
		} else if (paramPreference.getKey().equals(
				getString(R.string.company_title))) {
			showDialog(DIALOG_MY_COMPANY_ID);
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_WAREHOUSE_ID:
			return new CursorChoiceDialogBuilder(this, id, this, WarehouseTable
					.getInstance().list(), -1, WarehouseTable.Fields.NAME)
					.setTitle(R.string.warehouse_title).create();
		case DIALOG_MY_COMPANY_ID:
			return new CursorChoiceDialogBuilder(this, id, this, MyCompanyTable
					.getInstance().list(), -1, MyCompanyTable.Fields.NAME)
					.setTitle(R.string.my_company_title).create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onAccept(ConfirmDialogBuilder dialogBuilder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDecline(ConfirmDialogBuilder dialogBuilder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancel(ConfirmDialogBuilder dialogBuilder) {
		// TODO Auto-generated method stub

	}
}
