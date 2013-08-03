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

import ru.redsolution.bst.R;
import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;

/**
 * Диалог ввода значения.
 * 
 * @author alexander.ivanov
 * 
 */
public class ValueDialogBuilder extends ConfirmDialogBuilder {

	private final EditText valueView;

	public ValueDialogBuilder(Activity activity, int dialogId,
			final AcceptAndDeclineDialogListener listener) {
		super(activity, dialogId, listener);
		View dialogLayout = activity.getLayoutInflater().inflate(
				R.layout.value_dialog, null, false);
		valueView = (EditText) dialogLayout.findViewById(R.id.value);
		setView(dialogLayout);
	}

	public String getValue() {
		return valueView.getText().toString();
	}

}
