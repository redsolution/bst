package ru.redsolution.dialogs;

/**
 * Listener for dialog to be accepted.
 * 
 * @author alexander.ivanov
 * 
 */
public interface AcceptDialogListener {

	/**
	 * Request was accepted. "Yes" or "OK" button was pushed.
	 */
	void onAccept(DialogBuilder dialogBuilder);
}
