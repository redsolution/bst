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
	private final AcceptDialogListener listener;

	/**
	 * Notification dialog builder.
	 * 
	 * @param activity
	 *            Parent activity.
	 * @param dialogId
	 *            Dialog ID to be removed.
	 * @param listener
	 *            Action listener.
	 */
	public NotificationDialogBuilder(Activity activity, int dialogId,
			AcceptDialogListener listener) {
		super(activity, dialogId);
		this.listener = listener;
		setPositiveButton(activity.getString(android.R.string.ok), this);
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		switch (id) {
		case DialogInterface.BUTTON_POSITIVE:
			listener.onAccept(this);
			dialog.dismiss();
			break;
		}
	}

}
