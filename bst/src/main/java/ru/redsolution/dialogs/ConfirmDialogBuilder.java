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

/**
 * Yes / No dialog builder.
 * 
 * @author alexander.ivanov
 * 
 */
public class ConfirmDialogBuilder extends DialogBuilder implements
		DialogInterface.OnClickListener, OnCancelListener {
	private final AcceptAndDeclineDialogListener listener;

	/**
	 * Yes / No dialog builder.
	 * 
	 * @param activity
	 *            Parent activity.
	 * @param dialogId
	 *            Dialog ID to be removed.
	 * @param listener
	 *            Action listener.
	 */
	public ConfirmDialogBuilder(Activity activity, int dialogId,
			AcceptAndDeclineDialogListener listener) {
		super(activity, dialogId);
		this.listener = listener;
		setPositiveButton(activity.getString(android.R.string.yes), this);
		setNegativeButton(activity.getString(android.R.string.no), this);
		setOnCancelListener(this);
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		switch (id) {
		case DialogInterface.BUTTON_POSITIVE:
			dialog.dismiss();
			listener.onAccept(this);
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			dialog.dismiss();
			listener.onDecline(this);
			break;
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		dialog.dismiss();
		listener.onDecline(this);
	}

}
