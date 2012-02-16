package ru.redsolution.bst.ui.dialog;

import ru.redsolution.bst.R;
import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import android.app.Activity;
import android.content.DialogInterface;
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

	@Override
	public void onClick(DialogInterface dialog, int id) {
		if (id == DialogInterface.BUTTON_POSITIVE && "".equals(getValue()))
			return;
		super.onClick(dialog, id);
	}

}
