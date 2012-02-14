package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.DocumentType;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.MyCompanyTable;
import ru.redsolution.bst.data.table.WarehouseTable;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.NotificationDialogBuilder;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HeaderActivity extends BaseSettingsActivity implements
		OnClickListener {

	public static final String ACTION_UPDATE = "ru.redsolution.bst.ui.HeaderActivity.ACTION_UPDATE";
	public static final String EXTRA_TYPE = "ru.redsolution.bst.ui.HeaderActivity.EXTRA_TYPE";

	private static final String SAVED_INITIALIZED = "ru.redsolution.bst.ui.InventoryActivity.SAVED_INITIALIZED";
	private static final String SAVED_WAREHOUSE = "ru.redsolution.bst.ui.InventoryActivity.SAVED_WAREHOUSE";
	private static final String SAVED_MY_COMPANY = "ru.redsolution.bst.ui.InventoryActivity.SAVED_MY_COMPANY";

	private static final int DIALOG_DEFAULTS_ID = 0x10;
	private static final int DIALOG_NOT_COMPLITED_ID = 0x11;

	/**
	 * Значения были введены.
	 */
	private boolean initialized;

	private String myCompany;
	private String warehouse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.create, getListView(),
				false);
		Button createButton = (Button) view.findViewById(R.id.create);
		createButton.setOnClickListener(this);
		getListView().addFooterView(view, null, false);
		addPreferencesFromResource(R.xml.inventory);

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
		if (!initialized) {
			initialized = true;
			warehouse = BST.getInstance().getDefaultWarehouse();
			myCompany = BST.getInstance().getDefaultMyCompany();
		}
		super.onResume();
		if (!isComplited())
			showDialog(DIALOG_DEFAULTS_ID);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVED_INITIALIZED, initialized);
		outState.putString(SAVED_WAREHOUSE, warehouse);
		outState.putString(SAVED_MY_COMPANY, myCompany);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
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
		case DIALOG_DEFAULTS_ID:
			initialized = false;
			Intent intent = new Intent(this, SettingsActivity.class);
			intent.putExtra(SettingsActivity.EXTRA_SET_DEFAULTS, true);
			startActivity(intent);
			break;
		default:
			super.onAccept(dialogBuilder);
			break;
		}
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

	@Override
	protected String getMyCompany() {
		return myCompany;
	}

	@Override
	protected String getWarehouse() {
		return warehouse;
	}

	@Override
	protected String getCompany() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	protected String getContract() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	protected String getProject() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	protected void setMyCompany(String value) {
		myCompany = value;
	}

	@Override
	protected void setWarehouse(String value) {
		warehouse = value;
	}

	@Override
	protected void setCompany(String value) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void setContract(String value) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void setProject(String value) {
		// TODO Auto-generated method stub
	}
}
