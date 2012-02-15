package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.DocumentType;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.CompanyTable;
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

	private static final String SAVED_INITIALIZED = "ru.redsolution.bst.ui.HeaderActivity.SAVED_INITIALIZED";
	private static final String SAVED_MY_COMPANY = "ru.redsolution.bst.ui.HeaderActivity.SAVED_MY_COMPANY";
	private static final String SAVED_WAREHOUSE = "ru.redsolution.bst.ui.HeaderActivity.SAVED_WAREHOUSE";
	private static final String SAVED_COMPANY = "ru.redsolution.bst.ui.HeaderActivity.SAVED_COMPANY";
	private static final String SAVED_CONTRACT = "ru.redsolution.bst.ui.HeaderActivity.SAVED_CONTRACT";
	private static final String SAVED_PROJECT = "ru.redsolution.bst.ui.HeaderActivity.SAVED_PROJECT";

	private static final int DIALOG_DEFAULTS_ID = 0x10;
	private static final int DIALOG_NOT_COMPLITED_ID = 0x11;

	/**
	 * Значения были введены.
	 */
	private boolean initialized;
	private DocumentType type;

	private String myCompany;
	private String warehouse;
	private String company;
	private String contract;
	private String project;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.create, getListView(),
				false);
		Button createButton = (Button) view.findViewById(R.id.create);
		createButton.setOnClickListener(this);
		getListView().addFooterView(view, null, false);

		if (ACTION_UPDATE.equals(getIntent().getAction()))
			type = BST.getInstance().getDocumentType();
		else
			type = DocumentType.valueOf(getIntent().getStringExtra(
					BaseSettingsActivity.EXTRA_TYPE));

		if (type == DocumentType.supply)
			addPreferencesFromResource(R.xml.supply);
		else if (type == DocumentType.inventory)
			addPreferencesFromResource(R.xml.inventory);
		else if (type == DocumentType.demand)
			addPreferencesFromResource(R.xml.demand);
		else
			throw new UnsupportedOperationException();

		if (savedInstanceState != null) {
			initialized = savedInstanceState.getBoolean(SAVED_INITIALIZED,
					false);
			myCompany = savedInstanceState.getString(SAVED_MY_COMPANY);
			warehouse = savedInstanceState.getString(SAVED_WAREHOUSE);
			company = savedInstanceState.getString(SAVED_COMPANY);
			contract = savedInstanceState.getString(SAVED_CONTRACT);
			project = savedInstanceState.getString(SAVED_PROJECT);
		} else {
			initialized = false;
			myCompany = BST.getInstance().getSelectedMyCompany();
			warehouse = BST.getInstance().getSelectedWarehouse();
			company = BST.getInstance().getSelectedCompany();
			contract = BST.getInstance().getSelectedContract();
			project = BST.getInstance().getSelectedProject();
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
			if (type != DocumentType.inventory)
				CompanyTable.getInstance().getName(company);
		} catch (BaseDatabaseException e) {
			return false;
		}
		return true;
	}

	@Override
	protected void onResume() {
		if (!initialized) {
			initialized = true;
			myCompany = BST.getInstance().getDefaultMyCompany();
			warehouse = BST.getInstance().getDefaultWarehouse();
			if (type == DocumentType.supply) {
				company = BST.getInstance().getDefaultSupplyCompany();
				contract = BST.getInstance().getDefaultSupplyContract();
			} else {
				company = BST.getInstance().getDefaultDemandCompany();
				contract = BST.getInstance().getDefaultDemandContract();
			}
			project = BST.getInstance().getDefaultProject();
		}
		super.onResume();
		if (!isComplited())
			showDialog(DIALOG_DEFAULTS_ID);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVED_INITIALIZED, initialized);
		outState.putString(SAVED_MY_COMPANY, myCompany);
		outState.putString(SAVED_WAREHOUSE, warehouse);
		outState.putString(SAVED_COMPANY, company);
		outState.putString(SAVED_CONTRACT, contract);
		outState.putString(SAVED_PROJECT, project);
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
			intent.putExtra(BaseSettingsActivity.EXTRA_TYPE, getIntent()
					.getStringExtra(BaseSettingsActivity.EXTRA_TYPE));
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
				BST.getInstance().setSelectedMyCompany(myCompany);
				BST.getInstance().setSelectedWarehouse(warehouse);
				BST.getInstance().setSelectedCompany(company);
				BST.getInstance().setSelectedContract(contract);
				BST.getInstance().setSelectedProject(project);
				if (!ACTION_UPDATE.equals(getIntent().getAction())) {
					BST.getInstance().setDocumentType(type);
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
	protected String getSupplyCompany() {
		return company;
	}

	@Override
	protected String getSupplyContract() {
		return contract;
	}

	@Override
	protected String getDemandCompany() {
		return company;
	}

	@Override
	protected String getDemandContract() {
		return contract;
	}

	@Override
	protected String getProject() {
		return project;
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
	protected void setSupplyCompany(String value) {
		company = value;
	}

	@Override
	protected void setSupplyContract(String value) {
		contract = value;
	}

	@Override
	protected void setDemandCompany(String value) {
		company = value;
	}

	@Override
	protected void setDemandContract(String value) {
		contract = value;
	}

	@Override
	protected void setProject(String value) {
		project = value;
	}

}
