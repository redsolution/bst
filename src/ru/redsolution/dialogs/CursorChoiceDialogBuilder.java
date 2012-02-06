package ru.redsolution.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Builder for choose dialog.
 * 
 * @author alexander.ivanov
 * 
 */
public class CursorChoiceDialogBuilder extends DialogBuilder implements
		DialogInterface.OnClickListener {
	private final DialogListener listener;
	private final Cursor cursor;
	private String checkedId;
	private boolean selected;

	/**
	 * @param activity
	 * @param dialogId
	 * @param listener
	 * @param cursor
	 * @param checkedId
	 * @param labelColumn
	 */
	public CursorChoiceDialogBuilder(Activity activity, int dialogId,
			DialogListener listener, Cursor cursor, String checkedId,
			String labelColumn) {
		super(activity, dialogId);
		this.listener = listener;
		this.cursor = cursor;
		this.selected = false;
		this.checkedId = null;
		int checkedItem = -1;
		if (cursor.moveToFirst()) {
			int index = 0;
			do {
				if (cursor.getString(cursor.getColumnIndex(BaseColumns._ID))
						.equals(checkedId)) {
					this.checkedId = checkedId;
					checkedItem = index;
					break;
				}
				index++;
			} while (cursor.moveToNext());
		}
		setSingleChoiceItems(cursor, checkedItem, labelColumn, this);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (selected) {
			listener.onAccept(this);
		} else {
			listener.onCancel(this);
		}
		cursor.close();
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		if (cursor.moveToPosition(id)) {
			checkedId = cursor
					.getString(cursor.getColumnIndex(BaseColumns._ID));
		} else {
			throw new IllegalStateException();
		}
		selected = true;
		dialog.dismiss();
	}

	/**
	 * Возвращает идентификатор в базе для выбранного элемента.
	 * 
	 * @return <code>null</code> если нет выбранного элемента.
	 */
	public String getCheckedId() {
		return checkedId;
	}
}
