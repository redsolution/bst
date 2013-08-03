package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.DocumentType;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.CompanyTable;
import ru.redsolution.bst.data.table.ContractTable;
import ru.redsolution.bst.data.table.MyCompanyTable;
import ru.redsolution.bst.data.table.PriceTypeTable;
import ru.redsolution.bst.data.table.ProjectTable;
import ru.redsolution.bst.data.table.WarehouseTable;
import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.NotificationDialogBuilder;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Настройки документа.
 * 
 * @author alexander.ivanov
 * 
 */
public class HeaderActivity extends BaseSettingsActivity implements
		OnClickListener, AcceptAndDeclineDialogListener {

	public static final String ACTION_UPDATE = "ru.redsolution.bst.ui.HeaderActivity.ACTION_UPDATE";

	private static final String SAVED_INITIALIZED = "ru.redsolution.bst.ui.HeaderActivity.SAVED_INITIALIZED";
	private static final String SAVED_MY_COMPANY = "ru.redsolution.bst.ui.HeaderActivity.SAVED_MY_COMPANY";
	private static final String SAVED_WAREHOUSE = "ru.redsolution.bst.ui.HeaderActivity.SAVED_WAREHOUSE";
	private static final String SAVED_TARGET_WAREHOUSE = "ru.redsolution.bst.ui.HeaderActivity.SAVED_TARGET_WAREHOUSE";
	private static final String SAVED_COMPANY = "ru.redsolution.bst.ui.HeaderActivity.SAVED_COMPANY";
	private static final String SAVED_CONTRACT = "ru.redsolution.bst.ui.HeaderActivity.SAVED_CONTRACT";
	private static final String SAVED_PROJECT = "ru.redsolution.bst.ui.HeaderActivity.SAVED_PROJECT";
	private static final String SAVED_PRICE_TYPE = "ru.redsolution.bst.ui.HeaderActivity.SAVED_PRICE_TYPE";
	private static final String SAVED_DEFAULTS_NOTIFIED = "ru.redsolution.bst.ui.HeaderActivity.SAVED_DEFAULTS_NOTIFIED";

	private static final int DIALOG_DEFAULTS_ID = 0x10;
	private static final int DIALOG_NOT_COMPLITED_ID = 0x11;

	/**
	 * Значения были введены.
	 */
	private boolean initialized;
	private DocumentType type;

	private String myCompany;
	private String warehouse;
	private String targetWarehouse;
	private String company;
	private String contract;
	private String project;
	private String priceType;

	private boolean defaultsNotified;

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
		else if (type == DocumentType.move)
			addPreferencesFromResource(R.xml.move);
		else
			throw new UnsupportedOperationException();

		if (savedInstanceState != null) {
			initialized = savedInstanceState.getBoolean(SAVED_INITIALIZED,
					false);
			myCompany = savedInstanceState.getString(SAVED_MY_COMPANY);
			warehouse = savedInstanceState.getString(SAVED_WAREHOUSE);
			targetWarehouse = savedInstanceState
					.getString(SAVED_TARGET_WAREHOUSE);
			company = savedInstanceState.getString(SAVED_COMPANY);
			contract = savedInstanceState.getString(SAVED_CONTRACT);
			project = savedInstanceState.getString(SAVED_PROJECT);
			priceType = savedInstanceState.getString(SAVED_PRICE_TYPE);
			defaultsNotified = savedInstanceState.getBoolean(
					SAVED_DEFAULTS_NOTIFIED, false);
		} else {
			initialized = false;
			myCompany = BST.getInstance().getSelectedMyCompany();
			warehouse = BST.getInstance().getSelectedWarehouse();
			targetWarehouse = BST.getInstance().getSelectedTargetWarehouse();
			company = BST.getInstance().getSelectedCompany();
			contract = BST.getInstance().getSelectedContract();
			project = BST.getInstance().getSelectedProject();
			priceType = BST.getInstance().getSelectedPriceType();
			defaultsNotified = false;
		}
		if (ACTION_UPDATE.equals(getIntent().getAction())) {
			createButton.setText(android.R.string.ok);
			initialized = true;
		}
	}

	/**
	 * @return Заполнены ли настройки по умолчанию.
	 */
	private boolean isDefaultsSetted() {
		try {
			WarehouseTable.getInstance().getName(warehouse);
			MyCompanyTable.getInstance().getName(myCompany);
			PriceTypeTable.getInstance().getName(priceType);
			if (type != DocumentType.inventory && type != DocumentType.move)
				CompanyTable.getInstance().getName(company);
		} catch (BaseDatabaseException e) {
			return false;
		}
		try {
			ContractTable.getInstance().getById(contract);
		} catch (BaseDatabaseException e) {
			contract = "";
		}
		try {
			ProjectTable.getInstance().getById(project);
		} catch (BaseDatabaseException e) {
			project = "";
		}
		return true;
	}

	/**
	 * @return Заполнена ли шапка.
	 */
	private boolean isComplited() {
		if (!isDefaultsSetted())
			return false;
		if (type == DocumentType.move)
			try {
				WarehouseTable.getInstance().getName(targetWarehouse);
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
			targetWarehouse = "";
			if (type == DocumentType.supply) {
				company = BST.getInstance().getDefaultSupplyCompany();
				contract = BST.getInstance().getDefaultSupplyContract();
			} else {
				company = BST.getInstance().getDefaultDemandCompany();
				contract = BST.getInstance().getDefaultDemandContract();
			}
			project = BST.getInstance().getDefaultProject();
			priceType = BST.getInstance().getDefaultPriceType();
		}
		super.onResume();
		if (!isDefaultsSetted() && !defaultsNotified) {
			defaultsNotified = true;
			showDialog(DIALOG_DEFAULTS_ID);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVED_INITIALIZED, initialized);
		outState.putString(SAVED_MY_COMPANY, myCompany);
		outState.putString(SAVED_WAREHOUSE, warehouse);
		outState.putString(SAVED_TARGET_WAREHOUSE, targetWarehouse);
		outState.putString(SAVED_COMPANY, company);
		outState.putString(SAVED_CONTRACT, contract);
		outState.putString(SAVED_PROJECT, project);
		outState.putString(SAVED_PRICE_TYPE, priceType);
		outState.putBoolean(SAVED_DEFAULTS_NOTIFIED, defaultsNotified);
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
				BST.getInstance().setSelectedTargetWarehouse(targetWarehouse);
				BST.getInstance().setSelectedCompany(company);
				BST.getInstance().setSelectedContract(contract);
				BST.getInstance().setSelectedProject(project);
				BST.getInstance().setSelectedPriceType(priceType);
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
	protected String getTargetWarehouse() {
		return targetWarehouse;
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
	protected String getPriceType() {
		return priceType;
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
	protected void setTargetWarehouse(String value) {
		targetWarehouse = value;
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

	@Override
	protected void setPriceType(String value) {
		priceType = value;
	}

}
