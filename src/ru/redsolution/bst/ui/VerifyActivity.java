package ru.redsolution.bst.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.BaseGoodTable;
import ru.redsolution.bst.data.table.CustomGoodTable;
import ru.redsolution.bst.data.table.GoodBarcodeTable;
import ru.redsolution.bst.data.table.GoodFolderTable;
import ru.redsolution.bst.data.table.GoodTable;
import ru.redsolution.bst.data.table.MultipleObjectsReturnedException;
import ru.redsolution.bst.data.table.NewGoodBarcodeTable;
import ru.redsolution.bst.data.table.ObjectDoesNotExistException;
import ru.redsolution.bst.data.table.ParentableTable;
import ru.redsolution.bst.data.table.SelectedGoodTable;
import ru.redsolution.bst.data.table.UomTable;
import ru.redsolution.bst.ui.dialog.ValueDialogBuilder;
import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.NotificationDialogBuilder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
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
		OnClickListener, AcceptAndDeclineDialogListener {

	public static final String ACTION_MANUAL_BARCODE = "ru.redsolution.bst.ui.VerifyActivity.ACTION_MANUAL_BARCODE";

	private static final int CHOOSE_REQUEST_CODE = 0x10;

	private static final String SAVED_TYPE = "ru.redsolution.bst.ui.VerifyActivity.SAVED_TYPE";
	private static final String SAVED_BARCODE = "ru.redsolution.bst.ui.VerifyActivity.SAVED_BARCODE";
	private static final String SAVED_PRODUCT_ID = "ru.redsolution.bst.ui.VerifyActivity.SAVED_PRODUCT_ID";
	private static final String SAVED_IS_CUSTOM = "ru.redsolution.bst.ui.VerifyActivity.SAVED_IS_CUSTOM";
	private static final String SAVED_QUANTITY = "ru.redsolution.bst.ui.VerifyActivity.SAVED_QUANTITY";
	private static final String SAVED_INSTALL_NOTIFIED = "ru.redsolution.bst.ui.VerifyActivity.SAVED_INSTALL_NOTIFIED";
	private static final String SAVED_NOT_FOUND_NOTIFIED = "ru.redsolution.bst.ui.VerifyActivity.SAVED_NOT_FOUND_NOTIFIED";
	private static final String SAVED_MULTIPLE_FOUND_NOTIFIED = "ru.redsolution.bst.ui.VerifyActivity.SAVED_MULTIPLE_FOUND_NOTIFIED";
	private static final String SAVED_BARCODE_NOTIFIED = "ru.redsolution.bst.ui.VerifyActivity.SAVED_BARCODE_NOTIFIED";
	private static final String SAVED_SAVE_BARCODE = "ru.redsolution.bst.ui.VerifyActivity.SAVED_SAVE_BARCODE";

	private static final String ZXING_EAN_8 = "EAN_8";
	private static final String ZXING_EAN_13 = "EAN_13";
	private static final String ZXING_CODE_128 = "CODE_128";

	private static final String TYPE_EAN_8 = "EAN8";
	private static final String TYPE_EAN_13 = "EAN13";
	private static final String TYPE_CODE_128 = "Code128";

	private static final Collection<String> SUPPORTED_CODE_TYPES = Collections
			.unmodifiableCollection(Arrays.asList(ZXING_EAN_8, ZXING_EAN_13,
					ZXING_CODE_128));

	/**
	 * Таблица преобразования из ZXing типов кодов в МойСклад.
	 */
	private static final Map<String, String> CODE_TYPES = new HashMap<String, String>();

	static {
		CODE_TYPES.put(ZXING_EAN_8, TYPE_EAN_8);
		CODE_TYPES.put(ZXING_EAN_13, TYPE_EAN_13);
		CODE_TYPES.put(ZXING_CODE_128, TYPE_CODE_128);
	}

	private static final int DIALOG_INSTALL_ID = 1;
	private static final int DIALOG_NO_MARKET_ID = 2;
	private static final int DIALOG_OBJECT_DOES_NOT_EXIST_ID = 3;
	private static final int DIALOG_MULTIPLE_OBJECTS_RETURNED_ID = 4;
	private static final int DIALOG_MANUAL_BARCODE_ID = 0x10;

	private static final String RE_EAN_8 = "^\\d{8}$";
	private static final String RE_EAN_13 = "^\\d{13}$";

	private NumberPicker quantityView;
	private TextView restView;

	private String type;
	private String barcode;
	private String productId;
	private boolean isCustom;
	private boolean saveBarcode;

	/**
	 * Пользователь извещен о необходимости установить сканер.
	 */
	private boolean installNotified;

	/**
	 * Пользователь извещен о том, что товар не найден.
	 */
	private boolean notFoundNotified;

	/**
	 * Пользователь извещен о том, что найдено более одной единицы товара.
	 */
	private boolean multipleFoundNotified;

	/**
	 * Пользователь извещен о необходимости ввести штрих код.
	 */
	private boolean barcodeNotified;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.quantity,
				getListView(), false);
		((Button) view.findViewById(R.id.more)).setOnClickListener(this);
		((Button) view.findViewById(R.id.finish)).setOnClickListener(this);
		restView = (TextView) view.findViewById(R.id.rest);
		quantityView = (NumberPicker) view.findViewById(R.id.quantity);
		getListView().addHeaderView(view, null, false);
		addPreferencesFromResource(R.xml.verify);
		findPreference(getString(R.string.name_title)).setLayoutResource(
				R.layout.multiline_preference);

		int quantity;
		if (savedInstanceState != null) {
			quantity = savedInstanceState.getInt(SAVED_QUANTITY, 1);
			type = savedInstanceState.getString(SAVED_TYPE);
			barcode = savedInstanceState.getString(SAVED_BARCODE);
			productId = savedInstanceState.getString(SAVED_PRODUCT_ID);
			isCustom = savedInstanceState.getBoolean(SAVED_IS_CUSTOM);
			installNotified = savedInstanceState.getBoolean(
					SAVED_INSTALL_NOTIFIED, false);
			notFoundNotified = savedInstanceState.getBoolean(
					SAVED_NOT_FOUND_NOTIFIED, false);
			multipleFoundNotified = savedInstanceState.getBoolean(
					SAVED_MULTIPLE_FOUND_NOTIFIED, false);
			barcodeNotified = savedInstanceState.getBoolean(
					SAVED_BARCODE_NOTIFIED, false);
			saveBarcode = savedInstanceState.getBoolean(SAVED_SAVE_BARCODE,
					false);
		} else {
			quantity = 1;
			type = null;
			barcode = null;
			productId = null;
			isCustom = false;
			installNotified = false;
			notFoundNotified = false;
			multipleFoundNotified = false;
			barcodeNotified = false;
			saveBarcode = false;
		}
		quantityView.setCurrent(quantity);
	}

	/**
	 * Запуск сканера.
	 */
	private void scan() {
		if (ACTION_MANUAL_BARCODE.equals(getIntent().getAction())
				&& !barcodeNotified) {
			barcodeNotified = true;
			showDialog(DIALOG_MANUAL_BARCODE_ID);
			return;
		}
		IntentIntegrator integrator = new IntentIntegrator(this);
		AlertDialog alertDialog = integrator.initiateScan(SUPPORTED_CODE_TYPES);
		if (alertDialog != null) {
			alertDialog.dismiss();
			if (!installNotified) {
				installNotified = true;
				showDialog(DIALOG_INSTALL_ID);
			}
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
		outState.putInt(SAVED_QUANTITY, quantityView.getCurrent());
		outState.putString(SAVED_TYPE, type);
		outState.putString(SAVED_BARCODE, barcode);
		outState.putString(SAVED_PRODUCT_ID, productId);
		outState.putBoolean(SAVED_IS_CUSTOM, isCustom);
		outState.putBoolean(SAVED_INSTALL_NOTIFIED, installNotified);
		outState.putBoolean(SAVED_NOT_FOUND_NOTIFIED, notFoundNotified);
		outState.putBoolean(SAVED_MULTIPLE_FOUND_NOTIFIED,
				multipleFoundNotified);
		outState.putBoolean(SAVED_BARCODE_NOTIFIED, barcodeNotified);
		outState.putBoolean(SAVED_SAVE_BARCODE, saveBarcode);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			barcode = scanResult.getContents();
			type = CODE_TYPES.get(scanResult.getFormatName());
			productId = null;
			isCustom = false;
			saveBarcode = false;
			quantityView.setCurrent(1);
			if (barcode == null)
				finish();
		} else if (requestCode == CHOOSE_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				productId = intent
						.getStringExtra(ChooseActivity.EXTRA_PRODUCT_ID);
				isCustom = intent.getBooleanExtra(
						ChooseActivity.EXTRA_IS_CUSTOM, false);
				saveBarcode = true;
				notFoundNotified = false;
				updateView();
			} else {
				showDialog(DIALOG_OBJECT_DOES_NOT_EXIST_ID);
			}
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
		case DIALOG_OBJECT_DOES_NOT_EXIST_ID:
			return new ConfirmDialogBuilder(this, id, this)
					.setTitle(R.string.verification_error)
					.setMessage(R.string.object_does_not_exist).create();
		case DIALOG_MULTIPLE_OBJECTS_RETURNED_ID:
			return new NotificationDialogBuilder(this, id, this)
					.setTitle(R.string.verification_error)
					.setMessage(R.string.multiple_objects_returned).create();
		case DIALOG_MANUAL_BARCODE_ID:
			return new ValueDialogBuilder(this, id, this).setTitle(
					R.string.barcode_title).create();
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
				break;
			}
			installNotified = false;
			break;
		case DIALOG_NO_MARKET_ID:
			finish();
			break;
		case DIALOG_OBJECT_DOES_NOT_EXIST_ID:
			startActivityForResult(new Intent(this, ChooseActivity.class),
					CHOOSE_REQUEST_CODE);
			break;
		case DIALOG_MULTIPLE_OBJECTS_RETURNED_ID:
			multipleFoundNotified = false;
			scan();
			break;
		case DIALOG_MANUAL_BARCODE_ID:
			String value = ((ValueDialogBuilder) dialogBuilder).getValue();
			if ("".equals(value)) {
				showDialog(DIALOG_MANUAL_BARCODE_ID);
				break;
			}
			barcode = value;
			if (barcode.matches(RE_EAN_13))
				type = TYPE_EAN_13;
			else if (barcode.matches(RE_EAN_8))
				type = TYPE_EAN_8;
			else
				type = TYPE_CODE_128;
			productId = null;
			isCustom = false;
			saveBarcode = false;
			quantityView.setCurrent(1);
			updateView();
			return;
		default:
			break;
		}
	}

	@Override
	public void onDecline(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_OBJECT_DOES_NOT_EXIST_ID:
			notFoundNotified = false;
			scan();
			break;
		case DIALOG_MANUAL_BARCODE_ID:
			finish();
			break;
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
			save();
			scan();
			break;
		case R.id.finish:
			save();
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * Сохранить результат.
	 */
	private void save() {
		if (productId == null)
			throw new IllegalStateException();
		if (saveBarcode)
			NewGoodBarcodeTable.getInstance().add(productId, isCustom, type,
					barcode);
		SelectedGoodTable.getInstance().set(productId, isCustom,
				quantityView.getCurrent() + getRest().intValue());
	}

	/**
	 * @return Количество ранее добавленных товаров.
	 */
	private BigDecimal getRest() {
		if (productId == null)
			return BigDecimal.ZERO;
		else
			return SelectedGoodTable.getInstance().getQuantity(productId,
					isCustom);
	}

	/**
	 * Поиск продукта по штрих коду.
	 * 
	 * @throws ObjectDoesNotExistException
	 * @throws MultipleObjectsReturnedException
	 */
	private void searchForProductId() throws ObjectDoesNotExistException,
			MultipleObjectsReturnedException {
		ContentValues values;
		try {
			values = GoodBarcodeTable.getInstance().getByValue(type, barcode);
		} catch (ObjectDoesNotExistException e) {
			try {
				values = NewGoodBarcodeTable.getInstance().getByValue(type,
						barcode);
			} catch (BaseDatabaseException e1) {
				throw e;
			}
			productId = values.getAsString(NewGoodBarcodeTable.Fields._ID);
			isCustom = values
					.getAsBoolean(NewGoodBarcodeTable.Fields.IS_CUSTOM);
			return;
		} catch (MultipleObjectsReturnedException e) {
			throw e;
		}
		productId = values.getAsString(GoodBarcodeTable.Fields._ID);
		isCustom = false;
	}

	private void updateView() {
		ContentValues values = null;
		if (barcode != null)
			try {
				if (productId == null)
					searchForProductId();
				if (isCustom)
					values = CustomGoodTable.getInstance().getById(productId);
				else
					values = GoodTable.getInstance().getById(productId);
			} catch (ObjectDoesNotExistException e) {
				if (!notFoundNotified) {
					notFoundNotified = true;
					showDialog(DIALOG_OBJECT_DOES_NOT_EXIST_ID);
				}
			} catch (MultipleObjectsReturnedException e) {
				if (!multipleFoundNotified) {
					multipleFoundNotified = true;
					showDialog(DIALOG_MULTIPLE_OBJECTS_RETURNED_ID);
				}
			}
		if (values == null) {
			findPreference(getString(R.string.name_title)).setTitle("");
			findPreference(getString(R.string.name_title)).setSummary("");
			findPreference(getString(R.string.barcode_title)).setTitle("");
			findPreference(getString(R.string.code_title)).setTitle("");
			findPreference(getString(R.string.product_code_title)).setTitle("");
		} else {
			String name = values.getAsString(BaseGoodTable.Fields.NAME);
			String uom = "";
			try {
				uom = UomTable.getInstance().getName(
						values.getAsString(BaseGoodTable.Fields.UOM));
			} catch (BaseDatabaseException e) {
			}
			String price;
			if (BST.getInstance().getDocumentType().useSalePrice())
				price = values.getAsString(BaseGoodTable.Fields.SALE_PRICE);
			else
				price = values.getAsString(BaseGoodTable.Fields.BUY_PRICE);
			BigDecimal bigDecimal = null;
			try {
				bigDecimal = new BigDecimal(price);
			} catch (NumberFormatException e) {
			}
			if (bigDecimal != null) {
				bigDecimal = bigDecimal.movePointLeft(2);
				bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
				if (bigDecimal.longValue() == 0)
					bigDecimal = null;
			}
			if (bigDecimal == null)
				price = "";
			else
				price = bigDecimal.toString();
			if ("".equals(uom)) {
				if ("".equals(price))
					;
				else
					name = String.format(getString(R.string.good_with_extra),
							name, price);
			} else {
				if ("".equals(price))
					name = String.format(getString(R.string.good_with_extra),
							name, uom);
				else
					name = String.format(
							getString(R.string.good_with_price_and_uom), name,
							price, uom);
			}
			LinkedList<String> folders = new LinkedList<String>();
			String folder = values
					.getAsString(BaseGoodTable.Fields.GOOD_FOLDER);
			while (!"".equals(folder)) {
				ContentValues folderValues;
				try {
					folderValues = GoodFolderTable.getInstance()
							.getById(folder);
				} catch (BaseDatabaseException e) {
					break;
				}
				folder = folderValues
						.getAsString(ParentableTable.Fields.PARENT);
				folders.addFirst(folderValues
						.getAsString(ParentableTable.Fields.NAME));
			}
			StringBuilder builder = new StringBuilder();
			for (String value : folders) {
				if (builder.length() != 0)
					builder.append(" / ");
				builder.append(value);
			}
			findPreference(getString(R.string.name_title)).setTitle(name);
			findPreference(getString(R.string.name_title)).setSummary(
					builder.toString());
			findPreference(getString(R.string.barcode_title)).setTitle(barcode);
			findPreference(getString(R.string.code_title)).setTitle(
					values.getAsString(BaseGoodTable.Fields.CODE));
			findPreference(getString(R.string.product_code_title)).setTitle(
					values.getAsString(BaseGoodTable.Fields.PRODUCT_CODE));
		}
		restView.setText(String
				.format(getString(R.string.rest_text), getRest()));
	}
}
