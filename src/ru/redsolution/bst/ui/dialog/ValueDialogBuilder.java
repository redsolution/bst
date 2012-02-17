package ru.redsolution.bst.ui.dialog;

import ru.redsolution.bst.R;
import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;

/**
 * Диалог ввода значения.
 * 
 * @author alexander.ivanov
 * 
 */
public class ValueDialogBuilder extends ConfirmDialogBuilder {

	private final EditText valueView;

	public ValueDialogBuilder(Activity activity, int dialogId,
			final AcceptAndDeclineDialogListener listener) {
		super(activity, dialogId, listener);
		View dialogLayout = activity.getLayoutInflater().inflate(
				R.layout.value_dialog, null, false);
		valueView = (EditText) dialogLayout.findViewById(R.id.value);
		setView(dialogLayout);
	}

	public String getValue() {
		return valueView.getText().toString();
	}

}
