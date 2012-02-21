package ru.redsolution.dialogs;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/**
 * Builder for choose dialog.
 * 
 * @author alexander.ivanov
 * 
 * @param <T>
 */
public class SingleChoiceDialogBuilder<T> extends DialogBuilder implements
		DialogInterface.OnClickListener, OnCancelListener {
	private final AcceptAndDeclineDialogListener listener;
	private final List<T> items;
	private T checkedItem;

	public SingleChoiceDialogBuilder(Activity activity, int dialogId,
			AcceptAndDeclineDialogListener listener, List<T> items,
			List<String> labels, T checkedItem) {
		super(activity, dialogId);
		this.listener = listener;
		this.items = items;
		int index = items.indexOf(checkedItem);
		setCheckedItem(index);
		String[] array = labels.toArray(new String[labels.size()]);
		setSingleChoiceItems(array, index, this);
		setOnCancelListener(this);
	}

	private void setCheckedItem(int index) {
		if (index == -1)
			checkedItem = null;
		else
			checkedItem = items.get(index);
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		setCheckedItem(id);
		dialog.dismiss();
		listener.onAccept(this);
	}

	/**
	 * Returns selected item.
	 * 
	 * @return <code>null</code> if there is no selected item.
	 */
	public T getCheckedItem() {
		return checkedItem;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		dialog.dismiss();
		listener.onDecline(this);
	}

}
