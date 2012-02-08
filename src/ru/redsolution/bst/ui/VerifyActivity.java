package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;

import com.quietlycoding.android.picker.NumberPicker;

public class VerifyActivity extends PreferenceActivity {

	private static final String SAVED_QUANTITY = "ru.redsolution.bst.ui.VerifyActivity.SAVED_QUANTITY";

	private View quantityView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.quantity,
				getListView(), false);
		quantityView = view.findViewById(R.id.quantity);
		if (quantityView instanceof NumberPicker)
			((NumberPicker) quantityView).setRange(1, 100000);
		getListView().addFooterView(view, null, false);
		addPreferencesFromResource(R.xml.verify);
		if (savedInstanceState != null) {
			setQuantity(savedInstanceState.getInt(SAVED_QUANTITY));
		} else {
			setQuantity(1);
		}
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

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_QUANTITY, getQuantity());
	}

	private void updateView() {
	}

}
