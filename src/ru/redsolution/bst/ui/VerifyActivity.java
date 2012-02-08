package ru.redsolution.bst.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import ru.redsolution.bst.R;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.DialogListener;
import ru.redsolution.dialogs.NotificationDialogBuilder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.quietlycoding.android.picker.NumberPicker;

public class VerifyActivity extends PreferenceActivity implements
		OnClickListener, DialogListener {

	private static final String SAVED_BARCODE = "ru.redsolution.bst.ui.VerifyActivity.SAVED_BARCODE";
	private static final String SAVED_QUANTITY = "ru.redsolution.bst.ui.VerifyActivity.SAVED_QUANTITY";

	private static final Collection<String> SUPPORTED_CODE_TYPES = Collections
			.unmodifiableCollection(Arrays
					.asList("EAN_8", "EAN_13", "CODE_128"));

	private static final int DIALOG_INSTALL_ID = 1;
	private static final int DIALOG_NO_MARKET_ID = 2;

	private View quantityView;
	private TextView restView;
	private String barcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.quantity,
				getListView(), false);
		((Button) view.findViewById(R.id.more)).setOnClickListener(this);
		restView = (TextView) view.findViewById(R.id.rest);
		quantityView = view.findViewById(R.id.quantity);
		if (quantityView instanceof NumberPicker)
			((NumberPicker) quantityView).setRange(1, 100000);
		getListView().addHeaderView(view, null, false);
		addPreferencesFromResource(R.xml.verify);

		int quantity = 1;
		barcode = null;
		if (savedInstanceState != null) {
			quantity = savedInstanceState.getInt(SAVED_QUANTITY, 1);
			barcode = savedInstanceState.getString(SAVED_BARCODE);
		}
		setQuantity(quantity);
	}

	private void setQuantity(int value) {
		if (quantityView instanceof NumberPicker)
			((NumberPicker) quantityView).setCurrent(value);
	}

	private int getQuantity() {
		if (quantityView instanceof NumberPicker)
			return ((NumberPicker) quantityView).getCurrent();
		return 0;
	}

	/**
	 * Запуск сканера.
	 */
	private void scan() {
		IntentIntegrator integrator = new IntentIntegrator(this);
		AlertDialog alertDialog = integrator.initiateScan(SUPPORTED_CODE_TYPES);
		if (alertDialog != null) {
			alertDialog.dismiss();
			showDialog(DIALOG_INSTALL_ID);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
		if (barcode == null && !isFinishing())
			scan();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_QUANTITY, getQuantity());
		outState.putString(SAVED_BARCODE, barcode);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			barcode = scanResult.getContents();
			if (barcode == null)
				finish();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_INSTALL_ID:
			return new ConfirmDialogBuilder(this, id, this)
					.setTitle(R.string.install_title)
					.setMessage(R.string.install_message).create();
		case DIALOG_NO_MARKET_ID:
			return new NotificationDialogBuilder(this, id, this).setMessage(
					R.string.install_fail).create();
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			scan();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAccept(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_INSTALL_ID:
			Uri uri = Uri.parse("market://details?id="
					+ IntentIntegrator.BS_PACKAGE);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException anfe) {
				showDialog(DIALOG_NO_MARKET_ID);
			}
			break;
		case DIALOG_NO_MARKET_ID:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDecline(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_INSTALL_ID:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCancel(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_INSTALL_ID:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.more:
			scan();
			break;
		default:
			break;
		}
	}

	private void updateView() {
		int rest = 0;
		if (barcode == null) {
			findPreference(getString(R.string.barcode_title)).setTitle("");
		} else {
			findPreference(getString(R.string.barcode_title)).setTitle(barcode);
		}
		restView.setText(String.valueOf(rest));
	}

}
