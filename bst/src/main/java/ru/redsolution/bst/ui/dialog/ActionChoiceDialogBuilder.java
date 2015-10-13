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

import java.util.ArrayList;
import java.util.List;

import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.SingleChoiceDialogBuilder;
import android.app.Activity;
import android.content.Context;

/**
 * Диалог выбора операции.
 * 
 * @author alexander.ivanov
 * 
 */
public class ActionChoiceDialogBuilder extends
		SingleChoiceDialogBuilder<Integer> {

	public ActionChoiceDialogBuilder(Activity activity, int dialogId,
			AcceptAndDeclineDialogListener listener, int[] resourceIds,
			Integer checkedResourceId) {
		super(activity, dialogId, listener, getItems(resourceIds), getLabels(
				activity, resourceIds), checkedResourceId);
	}

	private static List<Integer> getItems(int[] resourceIds) {
		List<Integer> list = new ArrayList<Integer>();
		for (int resourceId : resourceIds)
			list.add(resourceId);
		return list;
	}

	private static List<String> getLabels(Context context, int[] resourceIds) {
		List<String> list = new ArrayList<String>();
		for (int resourceId : resourceIds)
			list.add(context.getString(resourceId));
		return list;
	}

}
