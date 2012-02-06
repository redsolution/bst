package ru.redsolution.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;

/**
 * Builder for choose dialog.
 * 
 * @author alexander.ivanov
 * 
 */
public class CursorChoiceDialogBuilder extends DialogBuilder implements
		DialogInterface.OnClickListener {
	private final Cursor cursor;
	private int checkedItem;

	public CursorChoiceDialogBuilder(Activity activity, int dialogId,
			ConfirmDialogListener listener, Cursor cursor, int checkedItem,
			String labelColumn) {
		super(activity, dialogId);
		this.cursor = cursor;
		this.checkedItem = checkedItem;
		setSingleChoiceItems(cursor, checkedItem, labelColumn, this);
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		checkedItem = id;
		dialog.dismiss();
	}

	/**
	 * Возвращает cursor для выбранного элемента.
	 * 
	 * @return <code>null</code> если указанной позиции не существует.
	 */
	public Cursor getSelectedItem() {
		if (cursor.moveToPosition(checkedItem))
			return cursor;
		return null;
	}
}
