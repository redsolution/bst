/**
 * Copyright (c) 2013, Redsolution LTD. All rights reserved.
 *
 * This file is part of Barcode Scanner Terminal project;
 * you can redistribute it and/or modify it under the terms of
 *
 * Barcode Scanner Terminal is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.CompanyTable;
import ru.redsolution.bst.data.table.ContractTable;
import ru.redsolution.bst.data.table.MyCompanyTable;
import ru.redsolution.bst.data.table.NamedTable;
import ru.redsolution.bst.data.table.PriceTypeTable;
import ru.redsolution.bst.data.table.ProjectTable;
import ru.redsolution.bst.data.table.WarehouseTable;
import ru.redsolution.bst.ui.dialog.CursorEmptyChoiceDialogBuilder;
import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.CursorChoiceDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import android.app.Dialog;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

/**
 * Базовое окно настроек документа.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class BaseSettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener, AcceptAndDeclineDialogListener {

	public static final String EXTRA_TYPE = "ru.redsolution.bst.ui.BaseSettingsActivity.EXTRA_TYPE";

	private static final int DIALOG_MY_COMPANY_ID = 0x01;
	private static final int DIALOG_WAREHOUSE_ID = 0x02;
	private static final int DIALOG_TARGET_WAREHOUSE_ID = 0x08;
	private static final int DIALOG_SUPPLY_COMPANY_ID = 0x03;
	private static final int DIALOG_SUPPLY_CONTRACT_ID = 0x04;
	private static final int DIALOG_DEMAND_COMPANY_ID = 0x05;
	private static final int DIALOG_DEMAND_CONTRACT_ID = 0x06;
	private static final int DIALOG_PROJECT_ID = 0x07;
	private static final int DIALOG_PRICE_TYPE_ID = 0x09;

	@Override
	protected void onStart() {
		super.onStart();
		registerOnPreferenceClickListener(R.string.my_company_title);
		registerOnPreferenceClickListener(R.string.warehouse_title);
		registerOnPreferenceClickListener(R.string.target_warehouse_title);
		registerOnPreferenceClickListener(R.string.supply_company_title);
		registerOnPreferenceClickListener(R.string.supply_contract_title);
		registerOnPreferenceClickListener(R.string.demand_company_title);
		registerOnPreferenceClickListener(R.string.demand_contract_title);
		registerOnPreferenceClickListener(R.string.project_title);
		registerOnPreferenceClickListener(R.string.price_type_title);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals(getString(R.string.my_company_title))) {
			showDialog(DIALOG_MY_COMPANY_ID);
		} else if (preference.getKey().equals(
				getString(R.string.warehouse_title))) {
			showDialog(DIALOG_WAREHOUSE_ID);
		} else if (preference.getKey().equals(
				getString(R.string.target_warehouse_title))) {
			showDialog(DIALOG_TARGET_WAREHOUSE_ID);
		} else if (preference.getKey().equals(
				getString(R.string.supply_company_title))) {
			showDialog(DIALOG_SUPPLY_COMPANY_ID);
		} else if (preference.getKey().equals(
				getString(R.string.supply_contract_title))) {
			showDialog(DIALOG_SUPPLY_CONTRACT_ID);
		} else if (preference.getKey().equals(
				getString(R.string.demand_company_title))) {
			showDialog(DIALOG_DEMAND_COMPANY_ID);
		} else if (preference.getKey().equals(
				getString(R.string.demand_contract_title))) {
			showDialog(DIALOG_DEMAND_CONTRACT_ID);
		} else if (preference.getKey()
				.equals(getString(R.string.project_title))) {
			showDialog(DIALOG_PROJECT_ID);
		} else if (preference.getKey().equals(
				getString(R.string.price_type_title))) {
			showDialog(DIALOG_PRICE_TYPE_ID);
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_MY_COMPANY_ID:
			return new CursorChoiceDialogBuilder(this, id, this, MyCompanyTable
					.getInstance().list(), getMyCompany(),
					MyCompanyTable.Fields.NAME).create();
		case DIALOG_WAREHOUSE_ID:
			return new CursorChoiceDialogBuilder(this, id, this, WarehouseTable
					.getInstance().list(), getWarehouse(),
					WarehouseTable.Fields.NAME).create();
		case DIALOG_TARGET_WAREHOUSE_ID:
			return new CursorChoiceDialogBuilder(this, id, this, WarehouseTable
					.getInstance().list(), getTargetWarehouse(),
					WarehouseTable.Fields.NAME).create();
		case DIALOG_SUPPLY_COMPANY_ID:
			return new CursorChoiceDialogBuilder(this, id, this, CompanyTable
					.getInstance().list(), getSupplyCompany(),
					CompanyTable.Fields.NAME).create();
		case DIALOG_SUPPLY_CONTRACT_ID:
			return new CursorEmptyChoiceDialogBuilder(this, id, this,
					ContractTable.getInstance().list(getSupplyCompany(),
							getMyCompany()), getSupplyContract(),
					ContractTable.Fields.NAME).create();
		case DIALOG_DEMAND_COMPANY_ID:
			return new CursorChoiceDialogBuilder(this, id, this, CompanyTable
					.getInstance().list(), getDemandCompany(),
					CompanyTable.Fields.NAME).create();
		case DIALOG_DEMAND_CONTRACT_ID:
			return new CursorEmptyChoiceDialogBuilder(this, id, this,
					ContractTable.getInstance().list(getDemandCompany(),
							getMyCompany()), getDemandContract(),
					ContractTable.Fields.NAME).create();
		case DIALOG_PROJECT_ID:
			return new CursorEmptyChoiceDialogBuilder(this, id, this,
					ProjectTable.getInstance().list(), getProject(),
					ProjectTable.Fields.NAME).create();
		case DIALOG_PRICE_TYPE_ID:
			return new CursorChoiceDialogBuilder(this, id, this, PriceTypeTable
					.getInstance().list(), getPriceType(),
					PriceTypeTable.Fields.NAME).create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onAccept(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_MY_COMPANY_ID:
			setMyCompany(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			checkSupplyContract();
			checkDemandContract();
			break;
		case DIALOG_WAREHOUSE_ID:
			setWarehouse(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			break;
		case DIALOG_TARGET_WAREHOUSE_ID:
			setTargetWarehouse(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			break;
		case DIALOG_SUPPLY_COMPANY_ID:
			setSupplyCompany(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			checkSupplyContract();
			break;
		case DIALOG_SUPPLY_CONTRACT_ID:
			setSupplyContract(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			break;
		case DIALOG_DEMAND_COMPANY_ID:
			setDemandCompany(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			checkDemandContract();
			break;
		case DIALOG_DEMAND_CONTRACT_ID:
			setDemandContract(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			break;
		case DIALOG_PROJECT_ID:
			setProject(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			break;
		case DIALOG_PRICE_TYPE_ID:
			setPriceType(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			break;
		default:
			break;
		}
		updateView();
	}

	@Override
	public void onDecline(DialogBuilder dialogBuilder) {
	}

	/**
	 * Обнуляет контакт, если он не соответствует выбранному контрагенту и
	 * организации.
	 */
	private void checkSupplyContract() {
		if (!ContractTable.getInstance().acceptable(getSupplyContract(),
				getSupplyCompany(), getMyCompany()))
			setSupplyContract("");
	}

	/**
	 * Обнуляет контакт, если он не соответствует выбранному контрагенту и
	 * организации.
	 */
	private void checkDemandContract() {
		if (!ContractTable.getInstance().acceptable(getDemandContract(),
				getDemandCompany(), getMyCompany()))
			setDemandContract("");
	}

	/**
	 * Обновляет значения.
	 */
	protected void updateView() {
		setSummary(R.string.my_company_title, MyCompanyTable.getInstance(),
				getMyCompany());
		setSummary(R.string.warehouse_title, WarehouseTable.getInstance(),
				getWarehouse());
		setSummary(R.string.target_warehouse_title,
				WarehouseTable.getInstance(), getTargetWarehouse());
		setSummary(R.string.supply_company_title, CompanyTable.getInstance(),
				getSupplyCompany());
		setSummary(R.string.supply_contract_title, ContractTable.getInstance(),
				getSupplyContract());
		setSummary(R.string.demand_company_title, CompanyTable.getInstance(),
				getDemandCompany());
		setSummary(R.string.demand_contract_title, ContractTable.getInstance(),
				getDemandContract());
		setSummary(R.string.project_title, ProjectTable.getInstance(),
				getProject());
		setSummary(R.string.price_type_title, PriceTypeTable.getInstance(),
				getPriceType());
	}

	/**
	 * Регистрирует слушатель нажатий.
	 * 
	 * @param resource
	 */
	public void registerOnPreferenceClickListener(int resource) {
		Preference preference = findPreference(getString(resource));
		if (preference != null)
			preference.setOnPreferenceClickListener(this);
	}

	/**
	 * Устанавлиает информацию для отображения.
	 * 
	 * @param id
	 * @param table
	 * @param value
	 */
	public void setSummary(int id, NamedTable table, String value) {
		Preference preference = findPreference(getString(id));
		if (preference == null)
			return;
		String summary = "";
		try {
			summary = table.getName(value);
		} catch (BaseDatabaseException e) {
		}
		preference.setSummary(summary);
	}

	protected abstract String getMyCompany();

	protected abstract String getWarehouse();

	protected abstract String getTargetWarehouse();

	protected abstract String getSupplyCompany();

	protected abstract String getSupplyContract();

	protected abstract String getDemandCompany();

	protected abstract String getDemandContract();

	protected abstract String getProject();

	protected abstract String getPriceType();

	protected abstract void setMyCompany(String value);

	protected abstract void setWarehouse(String value);

	protected abstract void setTargetWarehouse(String value);

	protected abstract void setSupplyCompany(String value);

	protected abstract void setSupplyContract(String value);

	protected abstract void setDemandCompany(String value);

	protected abstract void setDemandContract(String value);

	protected abstract void setProject(String value);

	protected abstract void setPriceType(String value);

}
