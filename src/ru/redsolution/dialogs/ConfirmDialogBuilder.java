package ru.redsolution.dialogs;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * Yes / No dialog builder.
 * 
 * @author alexander.ivanov
 * 
 */
public class ConfirmDialogBuilder extends DialogBuilder implements
		DialogInterface.OnClickListener {
	private final AcceptAndDeclineDialogListener listener;

	/**
	 * Yes / No dialog builder.
	 * 
	 * @param activity
	 *            Parent activity.
	 * @param dialogId
	 *            Dialog ID to be removed.
	 * @param listener
	 *            Action listener.
	 */
	public ConfirmDialogBuilder(Activity activity, int dialogId,
			AcceptAndDeclineDialogListener listener) {
		super(activity, dialogId);
		this.listener = listener;
		setPositiveButton(activity.getString(android.R.string.yes), this);
		setNegativeButton(activity.getString(android.R.string.no), this);
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		switch (id) {
		case DialogInterface.BUTTON_POSITIVE:
			dialog.dismiss();
			listener.onAccept(this);
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			dialog.dismiss();
			listener.onDecline(this);
			break;
		}
	}

}
