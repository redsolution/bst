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

import ru.redsolution.bst.R;
import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.CursorChoiceDialogBuilder;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ListView.FixedViewInfo;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Диалог с возможностью выбрать один из пунктов курсора либо убрать значение.
 * 
 * @author alexander.ivanov
 * 
 */
public class CursorEmptyChoiceDialogBuilder extends CursorChoiceDialogBuilder {

	private HeaderViewListAdapter adapter;

	public CursorEmptyChoiceDialogBuilder(Activity activity, int dialogId,
			AcceptAndDeclineDialogListener listener, Cursor cursor,
			String checkedId, String labelColumn) {
		super(activity, dialogId, listener, cursor, checkedId, labelColumn);
	}

	/**
	 * ListView.FixedViewInfo class is used as static, but does not declared it
	 * such way.
	 * 
	 * So FixedViewInfo must qualify the allocation with an enclosing instance
	 * of type ListView.
	 * 
	 * @author alexander.ivanov
	 * 
	 */
	private class ExtendedListView extends ListView {
		public FixedViewInfo info;

		public ExtendedListView(Context context) {
			super(context);
			info = new FixedViewInfo();
		}
	}

	@Override
	public Builder setSingleChoiceItems(Cursor cursor, int checkedItem,
			String labelColumn, OnClickListener listener) {
		ListAdapter sourceAdapter = new SimpleCursorAdapter(activity,
				android.R.layout.select_dialog_singlechoice, cursor,
				new String[] { labelColumn }, new int[] { android.R.id.text1 });
		View view = activity.getLayoutInflater().inflate(
				android.R.layout.select_dialog_singlechoice, null, false);
		((TextView) view.findViewById(android.R.id.text1))
				.setText(R.string.nothing);
		FixedViewInfo info = new ExtendedListView(activity).info;
		info.view = view;
		info.data = null;
		info.isSelectable = true;
		ArrayList<FixedViewInfo> header = new ArrayList<FixedViewInfo>();
		ArrayList<FixedViewInfo> footer = new ArrayList<FixedViewInfo>();
		footer.add(info);
		adapter = new HeaderViewListAdapter(header, footer, sourceAdapter);
		if (checkedItem == -1)
			checkedItem = adapter.getWrappedAdapter().getCount();
		return setSingleChoiceItems(adapter, checkedItem, this);
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		if (id == adapter.getWrappedAdapter().getCount()) {
			checkedId = "";
			dialog.dismiss();
			listener.onAccept(this);
		} else {
			super.onClick(dialog, id);
		}
	}
}
