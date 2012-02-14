package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.CompanyTable;
import ru.redsolution.bst.data.table.ContractTable;
import ru.redsolution.bst.data.table.MyCompanyTable;
import ru.redsolution.bst.data.table.NamedTable;
import ru.redsolution.bst.data.table.ProjectTable;
import ru.redsolution.bst.data.table.WarehouseTable;
import ru.redsolution.bst.ui.dialog.AuthorizationDialog;
import ru.redsolution.dialogs.CursorChoiceDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.DialogListener;
import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

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
	private static final int DIALOG_MY_COMPANY_ID = 2;
	private static final int DIALOG_WAREHOUSE_ID = 3;
	private static final int DIALOG_COMPANY_ID = 4;
	private static final int DIALOG_CONTRACT_ID = 5;
	private static final int DIALOG_PROJECT_ID = 6;
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
		registerOnPreferenceClickListener(R.string.default_warehouse_key);
		registerOnPreferenceClickListener(R.string.default_my_company_key);
		registerOnPreferenceClickListener(R.string.default_company_key);
		registerOnPreferenceClickListener(R.string.default_contract_key);
		registerOnPreferenceClickListener(R.string.default_project_key);
	}

	/**
	 * Регистрирует слушатель нажатий.
	 * 
	 * @param resource
	 */
	private void registerOnPreferenceClickListener(int resource) {
		Preference preference = findPreference(getString(resource));
		if (preference != null)
			preference.setOnPreferenceClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals(getString(R.string.login_key))) {
			showDialog(DIALOG_AUTH_ID);
		} else if (preference.getKey().equals(
				getString(R.string.default_my_company_key))) {
			showDialog(DIALOG_MY_COMPANY_ID);
		} else if (preference.getKey().equals(
				getString(R.string.default_warehouse_key))) {
			showDialog(DIALOG_WAREHOUSE_ID);
		} else if (preference.getKey().equals(
				getString(R.string.default_company_key))) {
			showDialog(DIALOG_COMPANY_ID);
		} else if (preference.getKey().equals(
				getString(R.string.default_contract_key))) {
			showDialog(DIALOG_CONTRACT_ID);
		} else if (preference.getKey().equals(
				getString(R.string.default_project_key))) {
			showDialog(DIALOG_PROJECT_ID);
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_AUTH_ID:
			return new AuthorizationDialog(this, id, this).create();
		case DIALOG_MY_COMPANY_ID:
			return new CursorChoiceDialogBuilder(this, id, this, MyCompanyTable
					.getInstance().list(), BST.getInstance()
					.getDefaultMyCompany(), MyCompanyTable.Fields.NAME)
					.create();
		case DIALOG_WAREHOUSE_ID:
			return new CursorChoiceDialogBuilder(this, id, this, WarehouseTable
					.getInstance().list(), BST.getInstance()
					.getDefaultWarehouse(), WarehouseTable.Fields.NAME)
					.create();
		case DIALOG_COMPANY_ID:
			return new CursorChoiceDialogBuilder(this, id, this, CompanyTable
					.getInstance().list(), BST.getInstance()
					.getDefaultCompany(), CompanyTable.Fields.NAME).create();
		case DIALOG_CONTRACT_ID:
			return new CursorChoiceDialogBuilder(this, id, this, ContractTable
					.getInstance().list(BST.getInstance().getDefaultCompany(),
							BST.getInstance().getDefaultMyCompany()), BST
					.getInstance().getDefaultContract(),
					ContractTable.Fields.NAME).create();
		case DIALOG_PROJECT_ID:
			return new CursorChoiceDialogBuilder(this, id, this, ProjectTable
					.getInstance().list(), BST.getInstance()
					.getDefaultProject(), ProjectTable.Fields.NAME).create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onAccept(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_AUTH_ID:
			break;
		case DIALOG_MY_COMPANY_ID:
			BST.getInstance().setDefaultMyCompany(
					((CursorChoiceDialogBuilder) dialogBuilder).getCheckedId());
			checkContract();
			break;
		case DIALOG_WAREHOUSE_ID:
			BST.getInstance().setDefaultWarehouse(
					((CursorChoiceDialogBuilder) dialogBuilder).getCheckedId());
			break;
		case DIALOG_COMPANY_ID:
			BST.getInstance().setDefaultCompany(
					((CursorChoiceDialogBuilder) dialogBuilder).getCheckedId());
			checkContract();
			break;
		case DIALOG_CONTRACT_ID:
			BST.getInstance().setDefaultContract(
					((CursorChoiceDialogBuilder) dialogBuilder).getCheckedId());
			break;
		case DIALOG_PROJECT_ID:
			BST.getInstance().setDefaultProject(
					((CursorChoiceDialogBuilder) dialogBuilder).getCheckedId());
			break;
		default:
			break;
		}
		updateView();
	}

	/**
	 * Обнуляет контакт, если он не соответствует выбранному контрагенту и
	 * организации.
	 */
	private void checkContract() {
		if (!ContractTable.getInstance().acceptable(
				BST.getInstance().getDefaultContract(),
				BST.getInstance().getDefaultCompany(),
				BST.getInstance().getDefaultMyCompany()))
			BST.getInstance().setDefaultContract("");
	}

	@Override
	public void onDecline(DialogBuilder dialogBuilder) {
	}

	@Override
	public void onCancel(DialogBuilder dialogBuilder) {
	}

	/**
	 * Устанавлиает информацию для отображения.
	 * 
	 * @param id
	 * @param table
	 * @param value
	 */
	private void setSummary(int id, NamedTable table, String value) {
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

	private void updateView() {
		if (loginPreference != null)
			loginPreference.setSummary(BST.getInstance().getLogin());
		setSummary(R.string.default_my_company_key,
				MyCompanyTable.getInstance(), BST.getInstance()
						.getDefaultMyCompany());
		setSummary(R.string.default_warehouse_key,
				WarehouseTable.getInstance(), BST.getInstance()
						.getDefaultWarehouse());
		setSummary(R.string.default_company_key, CompanyTable.getInstance(),
				BST.getInstance().getDefaultCompany());
		setSummary(R.string.default_contract_key, ContractTable.getInstance(),
				BST.getInstance().getDefaultContract());
		setSummary(R.string.default_project_key, ProjectTable.getInstance(),
				BST.getInstance().getDefaultProject());
	}
}