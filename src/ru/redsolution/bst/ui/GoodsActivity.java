package ru.redsolution.bst.ui;

import java.math.BigDecimal;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.BaseGoodTable;
import ru.redsolution.bst.data.table.CustomGoodTable;
import ru.redsolution.bst.data.table.GoodTable;
import ru.redsolution.bst.data.table.SelectedGoodTable;
import ru.redsolution.bst.data.table.UomTable;
import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.quietlycoding.android.picker.NumberPicker;

public class GoodsActivity extends ListActivity implements
		AcceptAndDeclineDialogListener {

	private static final int CONTEXT_MENU_CHANGE_QANTITY_ID = 1;
	private static final int CONTEXT_MENU_REMOVE_ID = 2;

	private static final int DIALOG_CHANGE_QANTITY_ID = 1;
	private static final int DIALOG_REMOVE_ID = 2;

	private String id;
	private boolean isCustom;
	private NumberPicker quantityView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods);
		setListAdapter(new ResourceCursorAdapter(this,
				android.R.layout.simple_list_item_2, SelectedGoodTable
						.getInstance().list()) {
			@Override
			public void bindView(View view, Context context, Cursor cursor) {
				ContentValues values;
				values = SelectedGoodTable.getInstance().getValues(cursor);
				String id = values.getAsString(SelectedGoodTable.Fields._ID);
				String quantity = values
						.getAsString(SelectedGoodTable.Fields.QUANTITY);
				try {
					if (values.getAsBoolean(SelectedGoodTable.Fields.IS_CUSTOM))
						values = CustomGoodTable.getInstance().getById(id);
					else
						values = GoodTable.getInstance().getById(id);
				} catch (BaseDatabaseException e) {
					values = null;
				}
				String name = "";
				String uom = "";
				if (values != null) {
					name = values.getAsString(BaseGoodTable.Fields.NAME);
					String uomId = values.getAsString(BaseGoodTable.Fields.UOM);
					try {
						uom = UomTable.getInstance().getName(uomId);
					} catch (BaseDatabaseException e) {
					}
				}
				((TextView) view.findViewById(android.R.id.text1))
						.setText(name);
				((TextView) view.findViewById(android.R.id.text2))
						.setText(String
								.format(getString(R.string.summary_text),
										quantity, uom));
			}
		});
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		System.out.println(getListView().getItemAtPosition(info.position));
		Cursor cursor = (Cursor) getListView().getItemAtPosition(info.position);
		ContentValues values = SelectedGoodTable.getInstance()
				.getValues(cursor);
		id = values.getAsString(SelectedGoodTable.Fields._ID);
		isCustom = values.getAsBoolean(SelectedGoodTable.Fields.IS_CUSTOM);
		try {
			if (isCustom)
				menu.setHeaderTitle(CustomGoodTable.getInstance().getName(id));
			else
				menu.setHeaderTitle(GoodTable.getInstance().getName(id));
		} catch (BaseDatabaseException e) {
		}
		menu.add(0, CONTEXT_MENU_CHANGE_QANTITY_ID, 0,
				getResources().getText(R.string.quantity_action));
		menu.add(0, CONTEXT_MENU_REMOVE_ID, 0,
				getResources().getText(R.string.remove_action));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		switch (item.getItemId()) {
		case CONTEXT_MENU_CHANGE_QANTITY_ID:
			showDialog(DIALOG_CHANGE_QANTITY_ID);
			return true;
		case CONTEXT_MENU_REMOVE_ID:
			showDialog(DIALOG_REMOVE_ID);
			return true;
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_CHANGE_QANTITY_ID:
			quantityView = (NumberPicker) getLayoutInflater().inflate(
					R.layout.quantity_picker, null, false);
			quantityView.setCurrent(SelectedGoodTable.getInstance()
					.getQuantity(this.id, isCustom));
			return new ConfirmDialogBuilder(this, id, this).setView(
					quantityView).create();
		case DIALOG_REMOVE_ID:
			return new ConfirmDialogBuilder(this, id, this).setMessage(
					R.string.remove_confirm).create();

		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onAccept(DialogBuilder dialogBuilder) {
		switch (dialogBuilder.getDialogId()) {
		case DIALOG_CHANGE_QANTITY_ID:
			if (quantityView instanceof NumberPicker) {
				SelectedGoodTable.getInstance().set(id, isCustom,
						quantityView.getCurrent());
			}
			break;
		case DIALOG_REMOVE_ID:
			SelectedGoodTable.getInstance().set(id, isCustom, BigDecimal.ZERO);
			break;
		default:
			break;
		}
		updateView();
	}

	@Override
	public void onDecline(DialogBuilder dialogBuilder) {
	}

	private void updateView() {
		((ResourceCursorAdapter) getListAdapter())
				.changeCursor(SelectedGoodTable.getInstance().list());
	}

}
