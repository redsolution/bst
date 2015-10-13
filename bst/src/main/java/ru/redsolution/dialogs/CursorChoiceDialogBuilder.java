/**
 * Copyright (c) 2013, Redsolution LTD. All rights reserved.
 *
 * This file is part of Barcode Scanner Terminal project;
 * you can redistribute it and/or modify it under the terms of
 *
 * Barcode Scanner Terminal is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package ru.redsolution.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Builder for choose dialog.
 * 
 * @author alexander.ivanov
 * 
 */
public class CursorChoiceDialogBuilder extends DialogBuilder implements
		DialogInterface.OnClickListener, OnCancelListener {
	protected final AcceptAndDeclineDialogListener listener;
	protected final Cursor cursor;
	protected String checkedId;

	/**
	 * @param activity
	 * @param dialogId
	 * @param listener
	 * @param cursor
	 * @param checkedId
	 * @param labelColumn
	 */
	public CursorChoiceDialogBuilder(Activity activity, int dialogId,
			AcceptAndDeclineDialogListener listener, Cursor cursor,
			String checkedId, String labelColumn) {
		super(activity, dialogId);
		this.listener = listener;
		this.cursor = cursor;
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
		setOnCancelListener(this);
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		if (cursor.moveToPosition(id)) {
			checkedId = cursor
					.getString(cursor.getColumnIndex(BaseColumns._ID));
		} else {
			throw new IllegalStateException();
		}
		dialog.dismiss();
		listener.onAccept(this);
	}

	/**
	 * Возвращает идентификатор в базе для выбранного элемента.
	 * 
	 * @return <code>null</code> если нет выбранного элемента.
	 */
	public String getCheckedId() {
		return checkedId;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		dialog.dismiss();
		listener.onDecline(this);
	}

}
