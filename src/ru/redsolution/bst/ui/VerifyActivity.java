package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class VerifyActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.verify);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
	}

	private void updateView() {
	}

}
