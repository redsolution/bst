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
