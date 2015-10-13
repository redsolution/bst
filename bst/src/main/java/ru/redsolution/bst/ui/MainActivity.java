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


import cz.msebera.android.httpclient.auth.AuthenticationException;
import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.DocumentType;
import ru.redsolution.bst.data.ImportOperationListener;
import ru.redsolution.bst.data.ImportProgress;
import ru.redsolution.bst.ui.dialog.AuthorizationDialogBuilder;
import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
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
		OnPreferenceClickListener, ImportOperationListener,
		AcceptAndDeclineDialogListener {

	private static final String SAVED_INTENT = "ru.redsolution.bst.ui.MainActivity.SAVED_INTENT";

	private static final int DIALOG_AUTH_ID = 1;
	private static final int DIALOG_ANOTHER_CONFIRM_ID = 2;
	private static final int DIALOG_PROGRESS_ID = 3;

	private DocumentType type;

	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main);
		findPreference(getString(R.string.continue_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.supply_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.inventory_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.demand_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.move_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.import_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.settings_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.about_action))
				.setOnPreferenceClickListener(this);
		type = null;
		if (savedInstanceState != null) {
			String value = savedInstanceState.getString(SAVED_INTENT);
			if (value != null)
				try {
					type = DocumentType.valueOf(value);
				} catch (IllegalArgumentException e) {
				}
		}
		if (BST.getInstance().showWellcomeScreen()) {
			Intent intent = new Intent(this, AboutActivity.class);
			intent.setAction(AboutActivity.ACTION_WELLCOME_SCREEN);
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		BST.getInstance().setOperationListener(this);
		updateView();
		if (BST.getInstance().isImporting())
			showDialog(DIALOG_PROGRESS_ID);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (type != null)
			outState.putString(SAVED_INTENT, type.toString());
	}

	@Override
	protected void onPause() {
		super.onPause();
		BST.getInstance().setOperationListener(null);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals(getString(R.string.continue_action))) {
			startActivity(new Intent(this, DocumentActivity.class));
		} else if (preference.getKey()
				.equals(getString(R.string.supply_action))) {
			checkAndCreateDocument(DocumentType.supply);
		} else if (preference.getKey().equals(
				getString(R.string.inventory_action))) {
			checkAndCreateDocument(DocumentType.inventory);
		} else if (preference.getKey()
				.equals(getString(R.string.demand_action))) {
			checkAndCreateDocument(DocumentType.demand);
		} else if (preference.getKey().equals(getString(R.string.move_action))) {
			checkAndCreateDocument(DocumentType.move);
		} else if (preference.getKey()
				.equals(getString(R.string.import_action))) {
			if ("".equals(BST.getInstance().getLogin()))
				showDialog(DIALOG_AUTH_ID);
			else
				BST.getInstance().importData();
		} else if (preference.getKey().equals(
				getString(R.string.settings_action))) {
			startActivity(new Intent(this, SettingsActivity.class));
		} else if (preference.getKey().equals(getString(R.string.about_action))) {
			startActivity(new Intent(this, AboutActivity.class));
		}
		return true;
	}

	/**
	 * Создать документ, при необходимости отобразить диалог подтверждения.
	 * 
	 * @param type
	 */
	private void checkAndCreateDocument(DocumentType type) {
		this.type = type;
		if (BST.getInstance().getDocumentType() == null)
			createDocument();
		else
			showDialog(DIALOG_ANOTHER_CONFIRM_ID);
	}

	/**
	 * Открыть окно создания документа.
	 */
	private void createDocument() {
		Intent intent = new Intent(this, HeaderActivity.class);
		intent.putExtra(HeaderActivity.EXTRA_TYPE, this.type.toString());
		startActivity(intent);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_AUTH_ID:
			return new AuthorizationDialogBuilder(this, id, this).create();
		case DIALOG_ANOTHER_CONFIRM_ID:
			return new ConfirmDialogBuilder(this, id, this)
					.setTitle(R.string.another_title)
					.setMessage(R.string.another_confirm).create();
		case DIALOG_PROGRESS_ID:
			progressDialog = new ProgressDialog(this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle(R.string.import_action);
			progressDialog.setMessage(getString(R.string.wait));
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.setIndeterminate(true);
			progressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					BST.getInstance().cancelImport();
				}
			});
			return progressDialog;
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
		case DIALOG_ANOTHER_CONFIRM_ID:
			createDocument();
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
	public void onBegin() {
		updateView();
		showDialog(DIALOG_PROGRESS_ID);
		progressDialog.setMessage(getString(R.string.wait));
		progressDialog.setMax(100);
		progressDialog.setProgress(0);
		progressDialog.setIndeterminate(true);
	}

	@Override
	public void onProgressUpdate(ImportProgress progress) {
		showDialog(DIALOG_PROGRESS_ID);
		progressDialog.setMessage(getString(R.string.wait_import,
				getString(progress.getSourceName()), progress.getSourceIndex(),
				progress.getSourceCount()));
		progressDialog.setIndeterminate(progress.getMaximum() == -1);
		if (progress.getMaximum() == -1) {
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
		} else {
			progressDialog.setMax(progress.getMaximum());
			progressDialog.setProgress(progress.getProgress());
		}
	}

	@Override
	public void onDone() {
		updateView();
		dismissDialog(DIALOG_PROGRESS_ID);
	}

	@Override
	public void onCancelled() {
		updateView();
		dismissDialog(DIALOG_PROGRESS_ID);
	}

	@Override
	public void onError(RuntimeException exception) {
		updateView();
		dismissDialog(DIALOG_PROGRESS_ID);
		if (exception.getCause() instanceof AuthenticationException) {
			showDialog(DIALOG_AUTH_ID);
			Toast.makeText(this, R.string.auth_error, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG)
					.show();
		}
	}

	private void updateView() {
		boolean isImported = BST.getInstance().isImported();
		DocumentType documentType = BST.getInstance().getDocumentType();
		findPreference(getString(R.string.continue_action)).setEnabled(
				isImported && documentType != null);
		if (documentType == DocumentType.supply) {
			findPreference(getString(R.string.continue_action)).setSummary(
					R.string.continue_supply_summary);
		} else if (documentType == DocumentType.inventory) {
			findPreference(getString(R.string.continue_action)).setSummary(
					R.string.continue_inventory_summary);
		} else if (documentType == DocumentType.demand) {
			findPreference(getString(R.string.continue_action)).setSummary(
					R.string.continue_demand_summary);
		} else if (documentType == DocumentType.move) {
			findPreference(getString(R.string.continue_action)).setSummary(
					R.string.continue_move_summary);
		} else {
			findPreference(getString(R.string.continue_action)).setSummary(
					R.string.continue_summary);
		}
		findPreference(getString(R.string.supply_action))
				.setEnabled(isImported);
		findPreference(getString(R.string.inventory_action)).setEnabled(
				isImported);
		findPreference(getString(R.string.demand_action))
				.setEnabled(isImported);
		findPreference(getString(R.string.move_action)).setEnabled(isImported);
		findPreference(getString(R.string.settings_action)).setEnabled(
				isImported);
	}

}