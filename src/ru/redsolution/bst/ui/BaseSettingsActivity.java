package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.CompanyTable;
import ru.redsolution.bst.data.table.ContractTable;
import ru.redsolution.bst.data.table.MyCompanyTable;
import ru.redsolution.bst.data.table.NamedTable;
import ru.redsolution.bst.data.table.ProjectTable;
import ru.redsolution.bst.data.table.WarehouseTable;
import ru.redsolution.dialogs.CursorChoiceDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.DialogListener;
import android.app.Dialog;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public abstract class BaseSettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener, DialogListener {

	private static final int DIALOG_MY_COMPANY_ID = 2;
	private static final int DIALOG_WAREHOUSE_ID = 3;
	private static final int DIALOG_COMPANY_ID = 4;
	private static final int DIALOG_CONTRACT_ID = 5;
	private static final int DIALOG_PROJECT_ID = 6;

	@Override
	protected void onStart() {
		super.onStart();
		registerOnPreferenceClickListener(R.string.warehouse_title);
		registerOnPreferenceClickListener(R.string.my_company_title);
		registerOnPreferenceClickListener(R.string.company_title);
		registerOnPreferenceClickListener(R.string.contract_title);
		registerOnPreferenceClickListener(R.string.project_title);
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
		} else if (preference.getKey()
				.equals(getString(R.string.company_title))) {
			showDialog(DIALOG_COMPANY_ID);
		} else if (preference.getKey().equals(
				getString(R.string.contract_title))) {
			showDialog(DIALOG_CONTRACT_ID);
		} else if (preference.getKey()
				.equals(getString(R.string.project_title))) {
			showDialog(DIALOG_PROJECT_ID);
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
		case DIALOG_COMPANY_ID:
			return new CursorChoiceDialogBuilder(this, id, this, CompanyTable
					.getInstance().list(), getCompany(),
					CompanyTable.Fields.NAME).create();
		case DIALOG_CONTRACT_ID:
			return new CursorChoiceDialogBuilder(this, id, this, ContractTable
					.getInstance().list(getCompany(), getMyCompany()),
					getContract(), ContractTable.Fields.NAME).create();
		case DIALOG_PROJECT_ID:
			return new CursorChoiceDialogBuilder(this, id, this, ProjectTable
					.getInstance().list(), getProject(),
					ProjectTable.Fields.NAME).create();
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
			checkContract();
			break;
		case DIALOG_WAREHOUSE_ID:
			setWarehouse(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			break;
		case DIALOG_COMPANY_ID:
			setCompany(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			checkContract();
			break;
		case DIALOG_CONTRACT_ID:
			setContract(((CursorChoiceDialogBuilder) dialogBuilder)
					.getCheckedId());
			break;
		case DIALOG_PROJECT_ID:
			setProject(((CursorChoiceDialogBuilder) dialogBuilder)
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

	@Override
	public void onCancel(DialogBuilder dialogBuilder) {
	}

	/**
	 * Обнуляет контакт, если он не соответствует выбранному контрагенту и
	 * организации.
	 */
	private void checkContract() {
		if (!ContractTable.getInstance().acceptable(getContract(),
				getMyCompany(), getMyCompany()))
			setContract("");
	}

	/**
	 * Обновляет значения.
	 */
	protected void updateView() {
		setSummary(R.string.my_company_title, MyCompanyTable.getInstance(),
				getMyCompany());
		setSummary(R.string.warehouse_title, WarehouseTable.getInstance(),
				getWarehouse());
		setSummary(R.string.company_title, CompanyTable.getInstance(),
				getCompany());
		setSummary(R.string.contract_title, ContractTable.getInstance(),
				getContract());
		setSummary(R.string.project_title, ProjectTable.getInstance(),
				getProject());
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

	protected abstract String getCompany();

	protected abstract String getContract();

	protected abstract String getProject();

	protected abstract void setMyCompany(String value);

	protected abstract void setWarehouse(String value);

	protected abstract void setCompany(String value);

	protected abstract void setContract(String value);

	protected abstract void setProject(String value);

}
