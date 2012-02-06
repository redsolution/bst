package ru.redsolution.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Builder for auto removed dialog on dismiss.
 * 
 * @author alexander.ivanov
 * 
 */
public class DialogBuilder extends AlertDialog.Builder implements
		DialogInterface.OnDismissListener {
	protected final Activity activity;
	protected final int dialogId;

	/**
	 * Create builder.
	 * 
	 * @param activity
	 *            Parent activity.
	 * @param dialogId
	 *            Dialog ID to be removed.
	 */
	public DialogBuilder(Activity activity, int dialogId) {
		super(activity);
		this.activity = activity;
		this.dialogId = dialogId;
	}

	@Override
	public AlertDialog create() {
		AlertDialog alertDialog = super.create();
		alertDialog.setOnDismissListener(this);
		return alertDialog;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		activity.removeDialog(dialogId);
	}

	/**
	 * Returns dialog ID.
	 * 
	 * @return
	 */
	public int getDialogId() {
		return dialogId;
	}
}
