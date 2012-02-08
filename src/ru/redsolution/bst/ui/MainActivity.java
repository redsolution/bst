package ru.redsolution.bst.ui;

import org.apache.http.auth.AuthenticationException;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.BST.State;
import ru.redsolution.bst.data.OperationListener;
import ru.redsolution.bst.ui.dialogs.AuthorizationDialog;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.DialogListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

/**
 * Главное окно приложения.
 * 
 * @author alexander.ivanov
 * 
 */
public class MainActivity extends PreferenceActivity implements
		OnPreferenceClickListener, OperationListener, DialogListener {

	private static final int DIALOG_AUTH_ID = 1;

	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main);
		findPreference(getString(R.string.import_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.inventory_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.settings_action))
				.setOnPreferenceClickListener(this);

		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage(getString(R.string.wait));
		progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				BST.getInstance().cancel();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
		BST.getInstance().setOperationListener(this);
		if (BST.getInstance().getState() != BST.State.idle)
			onBegin();
	}

	@Override
	protected void onPause() {
		super.onPause();
		BST.getInstance().setOperationListener(null);
		onDone();
	}

	@Override
	public boolean onPreferenceClick(Preference paramPreference) {
		if (paramPreference.getKey().equals(getString(R.string.import_action))) {
			if ("".equals(BST.getInstance().getLogin()))
				showDialog(DIALOG_AUTH_ID);
			else
				BST.getInstance().importData();
		} else if (paramPreference.getKey().equals(
				getString(R.string.inventory_action))) {
			startActivity(new Intent(this, InventoryActivity.class));
		} else if (paramPreference.getKey().equals(
				getString(R.string.settings_action))) {
			startActivity(new Intent(this, SettingsActivity.class));
		}
		return true;
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
	public void onAccept(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_AUTH_ID:
			BST.getInstance().importData();
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
	public void onBegin() {
		updateView();
		if (BST.getInstance().getState() == State.importing)
			progressDialog.setTitle(R.string.import_action);
		progressDialog.show();
	}

	@Override
	public void onDone() {
		updateView();
		progressDialog.dismiss();
	}

	@Override
	public void onError(RuntimeException exception) {
		onDone();
		if (exception.getCause() instanceof AuthenticationException) {
			showDialog(DIALOG_AUTH_ID);
			Toast.makeText(this, R.string.auth_error, Toast.LENGTH_LONG).show();
		} else
			Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG)
					.show();
	}

	private void updateView() {
		findPreference(getString(R.string.inventory_action)).setEnabled(
				BST.getInstance().isImported());
		findPreference(getString(R.string.settings_action)).setEnabled(
				BST.getInstance().isImported());
	}

}