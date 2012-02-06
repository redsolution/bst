package ru.redsolution.dialogs;

/**
 * Listener for choice in {@link RequestDialog}.
 */
public interface DialogListener {
	/**
	 * Request was accepted.
	 */
	void onAccept(DialogBuilder dialogBuilder);

	/**
	 * Request was declined.
	 */
	void onDecline(DialogBuilder dialogBuilder);

	/**
	 * Request was canceled.
	 */
	void onCancel(DialogBuilder dialogBuilder);
}
