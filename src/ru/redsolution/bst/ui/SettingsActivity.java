package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.ui.dialog.AuthorizationDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;

/**
 * Окно настроет.
 * 
 * @author alexander.ivanov
 * 
 */
public class SettingsActivity extends BaseSettingsActivity {

	public static final String EXTRA_SET_DEFAULTS = "ru.redsolution.bst.ui.SettingsActivity.EXTRA_SET_DEFAULTS";

	private static final int DIALOG_AUTH_ID = 1;
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
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals(getString(R.string.login_key))) {
			showDialog(DIALOG_AUTH_ID);
			return true;
		} else
			return super.onPreferenceClick(preference);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_AUTH_ID:
			return new AuthorizationDialog(this, id, this).create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	protected void updateView() {
		super.updateView();
		if (loginPreference != null)
			loginPreference.setSummary(BST.getInstance().getLogin());
	}

	@Override
	protected String getMyCompany() {
		return BST.getInstance().getDefaultMyCompany();
	}

	@Override
	protected String getWarehouse() {
		return BST.getInstance().getDefaultWarehouse();
	}

	@Override
	protected String getCompany() {
		return BST.getInstance().getDefaultCompany();
	}

	@Override
	protected String getContract() {
		return BST.getInstance().getDefaultContract();
	}

	@Override
	protected String getProject() {
		return BST.getInstance().getDefaultProject();
	}

	@Override
	protected void setMyCompany(String value) {
		BST.getInstance().setDefaultMyCompany(value);
	}

	@Override
	protected void setWarehouse(String value) {
		BST.getInstance().setDefaultWarehouse(value);
	}

	@Override
	protected void setCompany(String value) {
		BST.getInstance().setDefaultCompany(value);
	}

	@Override
	protected void setContract(String value) {
		BST.getInstance().setDefaultContract(value);
	}

	@Override
	protected void setProject(String value) {
		BST.getInstance().setDefaultProject(value);
	}

}