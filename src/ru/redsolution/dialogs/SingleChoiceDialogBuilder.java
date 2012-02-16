package ru.redsolution.dialogs;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * Builder for choose dialog.
 * 
 * @author alexander.ivanov
 * 
 * @param <T>
 */
public class SingleChoiceDialogBuilder<T> extends DialogBuilder implements
		DialogInterface.OnClickListener {
	private final AcceptDialogListener listener;
	private final List<T> items;
	private Integer selected;

	public SingleChoiceDialogBuilder(Activity activity, int dialogId,
			AcceptDialogListener listener, List<T> items, List<String> labels,
			Integer selected) {
		super(activity, dialogId);
		this.listener = listener;
		this.items = items;
		this.selected = selected;
		String[] array = labels.toArray(new String[labels.size()]);
		setSingleChoiceItems(array, selected == null ? -1 : selected, this);
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		selected = id;
		dialog.dismiss();
		listener.onAccept(this);
	}

	/**
	 * Returns selected item.
	 * 
	 * @return <code>null</code> if there is no selected item.
	 */
	public T getSelectedItem() {
		if (selected == null)
			return null;
		else
			return items.get(selected);
	}
}
