package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.DocumentType;
import ru.redsolution.bst.data.tables.BaseDatabaseException;
import ru.redsolution.bst.data.tables.MyCompanyTable;
import ru.redsolution.bst.data.tables.WarehouseTable;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.CursorChoiceDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.DialogListener;
import ru.redsolution.dialogs.NotificationDialogBuilder;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InventoryActivity extends PreferenceActivity implements
		OnPreferenceClickListener, DialogListener, OnClickListener {

	public static final String ACTION_UPDATE = "ru.redsolution.bst.ui.InventoryActivity.ACTION_UPDATE";

	private static final String SAVED_INITIALIZED = "ru.redsolution.bst.ui.InventoryActivity.SAVED_INITIALIZED";
	private static final String SAVED_WAREHOUSE = "ru.redsolution.bst.ui.InventoryActivity.SAVED_WAREHOUSE";
	private static final String SAVED_MY_COMPANY = "ru.redsolution.bst.ui.InventoryActivity.SAVED_MY_COMPANY";

	private static final int DIALOG_WAREHOUSE_ID = 2;
	private static final int DIALOG_MY_COMPANY_ID = 3;
	private static final int DIALOG_DEFAULTS_ID = 4;
	private static final int DIALOG_NOT_COMPLITED_ID = 5;

	/**
	 * Значения были введены.
	 */
	private boolean initialized;

	private String warehouse;
	private String myCompany;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.create, getListView(),
				false);
		Button createButton = (Button) view.findViewById(R.id.create);
		createButton.setOnClickListener(this);
		getListView().addFooterView(view, null, false);
		addPreferencesFromResource(R.xml.inventory);
		findPreference(getString(R.string.selected_warehouse_key))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.selected_my_company_key))
				.setOnPreferenceClickListener(this);

		if (savedInstanceState != null) {
			initialized = savedInstanceState.getBoolean(SAVED_INITIALIZED,
					false);
			warehouse = savedInstanceState.getString(SAVED_WAREHOUSE);
			myCompany = savedInstanceState.getString(SAVED_MY_COMPANY);
		} else {
			initialized = false;
			warehouse = BST.getInstance().getSelectedWarehouse();
			myCompany = BST.getInstance().getSelectedMyCompany();
		}
		if (ACTION_UPDATE.equals(getIntent().getAction())) {
			createButton.setText(android.R.string.ok);
			initialized = true;
		}
	}

	/**
	 * @return Заполнена ли шапка.
	 */
	private boolean isComplited() {
		try {
			WarehouseTable.getInstance().getName(warehouse);
			MyCompanyTable.getInstance().getName(myCompany);
		} catch (BaseDatabaseException e) {
			return false;
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!initialized) {
			initialized = true;
			warehouse = BST.getInstance().getDefaultWarehouse();
			myCompany = BST.getInstance().getDefaultMyCompany();
		}
		if (!isComplited())
			showDialog(DIALOG_DEFAULTS_ID);
		updateView();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVED_INITIALIZED, initialized);
		outState.putString(SAVED_WAREHOUSE, warehouse);
		outState.putString(SAVED_MY_COMPANY, myCompany);
	}

	@Override
	public boolean onPreferenceClick(Preference paramPreference) {
		if (paramPreference.getKey().equals(
				getString(R.string.selected_warehouse_key))) {
			showDialog(DIALOG_WAREHOUSE_ID);
		} else if (paramPreference.getKey().equals(
				getString(R.string.selected_my_company_key))) {
			showDialog(DIALOG_MY_COMPANY_ID);
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_WAREHOUSE_ID:
			return new CursorChoiceDialogBuilder(this, id, this, WarehouseTable
					.getInstance().list(), warehouse,
					WarehouseTable.Fields.NAME).setTitle(
					R.string.warehouse_title).create();
		case DIALOG_MY_COMPANY_ID:
			return new CursorChoiceDialogBuilder(this, id, this, MyCompanyTable
					.getInstance().list(), myCompany,
					MyCompanyTable.Fields.NAME).setTitle(
					R.string.my_company_title).create();
		case DIALOG_DEFAULTS_ID:
			return new ConfirmDialogBuilder(this, id, this).setMessage(
					R.string.defaults_hint).create();
		case DIALOG_NOT_COMPLITED_ID:
			return new NotificationDialogBuilder(this, id, this).setMessage(
					R.string.complite_warning).create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onAccept(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_WAREHOUSE_ID:
			warehouse = ((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId();
			break;
		case DIALOG_MY_COMPANY_ID:
			myCompany = ((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId();
			break;
		case DIALOG_DEFAULTS_ID:
			initialized = false;
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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.create:
			if (isComplited()) {
				BST.getInstance().setSelectedWarehouse(warehouse);
				BST.getInstance().setSelectedMyCompany(myCompany);
				if (!ACTION_UPDATE.equals(getIntent().getAction())) {
					BST.getInstance().setDocumentType(DocumentType.inventory);
					startActivity(new Intent(this, DocumentActivity.class));
				}
				finish();
			} else
				showDialog(DIALOG_NOT_COMPLITED_ID);
			break;
		default:
			break;
		}
	}

	private void updateView() {
		String warehouse = "";
		try {
			warehouse = WarehouseTable.getInstance().getName(this.warehouse);
		} catch (BaseDatabaseException e) {
		}
		String myCompany = "";
		try {
			myCompany = MyCompanyTable.getInstance().getName(this.myCompany);
		} catch (BaseDatabaseException e) {
		}
		findPreference(getString(R.string.selected_warehouse_key)).setSummary(
				warehouse);
		findPreference(getString(R.string.selected_my_company_key)).setSummary(
				myCompany);
	}
}
