package ru.redsolution.dialogs;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * Notification dailog builder.
 * 
 * @author alexander.ivanov
 * 
 */
public class NotificationDialogBuilder extends DialogBuilder implements
		DialogInterface.OnClickListener {
	private final DialogListener listener;

	/**
	 * Notification dialog builder.
	 * 
	 * @param activity
	 *            parent activity.
	 * @param listener
	 *            action listener.
	 * @param dialogId
	 *            ID of the dialog.
	 */
	public NotificationDialogBuilder(Activity activity, int dialogId,
			DialogListener listener) {
		super(activity, dialogId);
		this.listener = listener;
		setPositiveButton(activity.getString(android.R.string.ok), this);
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		switch (id) {
		case DialogInterface.BUTTON_POSITIVE:
			dialog.dismiss();
			break;
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		listener.onAccept(this);
	}
}
