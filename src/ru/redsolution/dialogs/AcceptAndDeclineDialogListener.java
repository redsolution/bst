package ru.redsolution.dialogs;

/**
 * Listener for dialog to be accepted or declined.
 * 
 * @author alexander.ivanov
 * 
 */
public interface AcceptAndDeclineDialogListener extends AcceptDialogListener {

	/**
	 * Request was declined. "No" button was pushed.
	 */
	void onDecline(DialogBuilder dialogBuilder);
}
