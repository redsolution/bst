package ru.redsolution.bst.ui;

import org.apache.http.auth.AuthenticationException;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.DocumentType;
import ru.redsolution.bst.data.InternalServerException;
import ru.redsolution.bst.data.OperationListener;
import ru.redsolution.bst.data.table.SelectedTable;
import ru.redsolution.bst.ui.dialog.AuthorizationDialog;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
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
 * Просмотр документа.
 * 
 * @author alexander.ivanov
 * 
 */
public class DocumentActivity extends PreferenceActivity implements
		OnPreferenceClickListener, DialogListener, OperationListener {

	public static final String SAVED_SCANNED = "ru.redsolution.bst.ui.DocumentActivity.SAVED_SCANNED";

	private static final int DIALOG_AUTH_ID = 1;
	private static final int DIALOG_CANCEL_ID = 2;

	private ProgressDialog progressDialog;

	/**
	 * Производилось сканирование.
	 */
	private boolean scanned;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.document);
		findPreference(getString(R.string.scan_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.list_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.header_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.send_action))
				.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.cancel_action))
				.setOnPreferenceClickListener(this);
		scanned = savedInstanceState != null
				&& savedInstanceState.getBoolean(SAVED_SCANNED, false);
		DocumentType type = BST.getInstance().getDocumentType();
		if (type == DocumentType.supply)
			setTitle(R.string.supply_action);
		else if (type == DocumentType.inventory)
			setTitle(R.string.inventory_action);
		else if (type == DocumentType.demand)
			setTitle(R.string.demand_action);

		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(true);
		progressDialog.setTitle(R.string.send_action);
		progressDialog.setMessage(getString(R.string.wait));
		progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				BST.getInstance().cancelSend();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
		if (BST.getInstance().getDocumentType() == null) {
			// Документ не существует (отправка завершена).
			finish();
		} else {
			if (!scanned) {
				startActivity(new Intent(this, VerifyActivity.class));
				scanned = true;
			} else {
				BST.getInstance().setOperationListener(this);
				if (BST.getInstance().isSending())
					onBegin();
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		BST.getInstance().setOperationListener(null);
		dismissProgressDialog();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVED_SCANNED, scanned);
	}

	@Override
	public boolean onPreferenceClick(Preference paramPreference) {
		if (paramPreference.getKey().equals(getString(R.string.scan_action))) {
			startActivity(new Intent(this, VerifyActivity.class));
		} else if (paramPreference.getKey().equals(
				getString(R.string.list_action))) {
			startActivity(new Intent(this, GoodsActivity.class));
		} else if (paramPreference.getKey().equals(
				getString(R.string.header_action))) {
			Intent intent = new Intent(this, HeaderActivity.class);
			intent.setAction(HeaderActivity.ACTION_UPDATE);
			startActivity(intent);
		} else if (paramPreference.getKey().equals(
				getString(R.string.send_action))) {
			BST.getInstance().sendData();
		} else if (paramPreference.getKey().equals(
				getString(R.string.cancel_action))) {
			showDialog(DIALOG_CANCEL_ID);
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_CANCEL_ID:
			return new ConfirmDialogBuilder(this, id, this).setMessage(
					R.string.cancel_confirm).create();
		case DIALOG_AUTH_ID:
			return new AuthorizationDialog(this, id, this).create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onAccept(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_CANCEL_ID:
			BST.getInstance().setDocumentType(null);
			finish();
			break;
		case DIALOG_AUTH_ID:
			BST.getInstance().sendData();
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
		progressDialog.show();
	}

	@Override
	public void onDone() {
		dismissProgressDialog();
		finish();
	}

	@Override
	public void onCancelled() {
		dismissProgressDialog();
	}

	@Override
	public void onError(RuntimeException exception) {
		dismissProgressDialog();
		if (exception.getCause() instanceof AuthenticationException) {
			showDialog(DIALOG_AUTH_ID);
			Toast.makeText(this, R.string.auth_error, Toast.LENGTH_LONG).show();
		} else if (exception.getCause() instanceof InternalServerException)
			Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG)
					.show();
		else
			Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG)
					.show();
	}

	private void dismissProgressDialog() {
		updateView();
		progressDialog.dismiss();
	}

	private void updateView() {
		findPreference(getString(R.string.list_action)).setSummary(
				String.format(getString(R.string.list_summary), SelectedTable
						.getInstance().getGoodsCount(), SelectedTable
						.getInstance().getTotalQuantity()));
	}
}