package ru.redsolution.bst.ui.dialog;

import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.DialogBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * Обертка для добавления кнопки отмены к {@link DialogBuilder}.
 * 
 * @author alexander.ivanov
 * 
 * @param <T>
 */
public class CancelableDialogWrapper<T extends DialogBuilder> implements
		OnClickListener {

	private final AcceptAndDeclineDialogListener listener;
	private final T dialogBuilder;

	public CancelableDialogWrapper(T dialogBuilder, Context context,
			AcceptAndDeclineDialogListener listener) {
		this.listener = listener;
		this.dialogBuilder = dialogBuilder;
		dialogBuilder.setNegativeButton(
				context.getString(android.R.string.cancel), this);
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		switch (id) {
		case DialogInterface.BUTTON_NEGATIVE:
			dialog.dismiss();
			listener.onDecline(this.dialogBuilder);
			break;
		default:
			if (this.dialogBuilder instanceof OnClickListener)
				((OnClickListener) this.dialogBuilder).onClick(dialog, id);
			break;
		}
	}

	public T getDialodBuilder() {
		return dialogBuilder;
	}

}
