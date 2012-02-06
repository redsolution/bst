package ru.redsolution.dialogs;

/**
 * Listener for choice in {@link RequestDialog}.
 */
public interface ConfirmDialogListener {
	/**
	 * Request was accepted.
	 */
	void onAccept(ConfirmDialogBuilder dialogBuilder);

	/**
	 * Request was declined.
	 */
	void onDecline(ConfirmDialogBuilder dialogBuilder);

	/**
	 * Request was canceled.
	 */
	void onCancel(ConfirmDialogBuilder dialogBuilder);
}
