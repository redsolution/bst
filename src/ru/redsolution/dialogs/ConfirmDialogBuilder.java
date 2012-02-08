package ru.redsolution.dialogs;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * Yes / No dailog builder.
 * 
 * @author alexander.ivanov
 * 
 */
public class ConfirmDialogBuilder extends DialogBuilder implements
		DialogInterface.OnClickListener {
	private final DialogListener listener;
	private State state;

	private static enum State {
		/**
		 * Dialog was canceled.
		 */
		canceled,

		/**
		 * Request was accepted.
		 */
		accepted,

		/**
		 * Request was declined.
		 */
		declined,
	}

	/**
	 * Yes / No dialog builder.
	 * 
	 * @param activity
	 *            parent activity.
	 * @param listener
	 *            action listener.
	 * @param dialogId
	 *            ID of the dialog.
	 */
	public ConfirmDialogBuilder(Activity activity, int dialogId,
			DialogListener listener) {
		super(activity, dialogId);
		this.listener = listener;
		state = State.canceled;
		setPositiveButton(activity.getString(android.R.string.yes), this);
		setNegativeButton(activity.getString(android.R.string.no), this);
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		switch (id) {
		case DialogInterface.BUTTON_POSITIVE:
			state = State.accepted;
			dialog.dismiss();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			state = State.declined;
			dialog.dismiss();
			break;
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (state == State.accepted) {
			listener.onAccept(this);
		} else if (state == State.declined) {
			listener.onDecline(this);
		} else { // State.canceled
			listener.onCancel(this);
		}
	}
}
