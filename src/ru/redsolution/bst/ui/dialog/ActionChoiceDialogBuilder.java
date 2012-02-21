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
