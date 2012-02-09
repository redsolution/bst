package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.tables.BaseDatabaseException;
import ru.redsolution.bst.data.tables.GoodTable;
import ru.redsolution.bst.data.tables.SelectedTable;
import ru.redsolution.bst.data.tables.UomTable;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.DialogListener;
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

public class GoodsActivity extends ListActivity implements DialogListener {

	private static final int CONTEXT_MENU_CHANGE_QANTITY_ID = 1;
	private static final int CONTEXT_MENU_REMOVE_ID = 2;

	private static final int DIALOG_CHANGE_QANTITY_ID = 1;
	private static final int DIALOG_REMOVE_ID = 2;

	private String id;
	private View quantityView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods);
		setListAdapter(new ResourceCursorAdapter(this,
				android.R.layout.simple_list_item_2, SelectedTable
						.getInstance().list()) {
			@Override
			public void bindView(View view, Context context, Cursor cursor) {
				String id = cursor.getString(cursor
						.getColumnIndex(SelectedTable.Fields._ID));
				int quantity = cursor.getInt(cursor
						.getColumnIndex(SelectedTable.Fields.QUANTITY));
				ContentValues values;
				try {
					values = GoodTable.getInstance().getById(id);
				} catch (BaseDatabaseException e) {
					values = null;
				}
				String name = "";
				String uom = "";
				if (values != null) {
					name = values.getAsString(GoodTable.Fields.NAME);
					String uomId = values.getAsString(GoodTable.Fields.UOM);
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
		id = cursor.getString(cursor.getColumnIndex(SelectedTable.Fields._ID));
		try {
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
			quantityView = getLayoutInflater().inflate(
					R.layout.quantity_picker, null, false);
			if (quantityView instanceof NumberPicker) {
				((NumberPicker) quantityView).setRange(1, 99999999);
				try {
					((NumberPicker) quantityView).setCurrent(SelectedTable
							.getInstance().getById(this.id)
							.getAsInteger(SelectedTable.Fields.QUANTITY));
				} catch (BaseDatabaseException e) {
				}
			}
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
				SelectedTable.getInstance().set(id,
						((NumberPicker) quantityView).getCurrent());
			}
			break;
		case DIALOG_REMOVE_ID:
			SelectedTable.getInstance().set(id, 0);
			break;
		default:
			break;
		}
		updateView();
	}

	@Override
	public void onDecline(DialogBuilder dialogBuilder) {
	}

	@Override
	public void onCancel(DialogBuilder dialogBuilder) {
	}

	private void updateView() {
		((ResourceCursorAdapter) getListAdapter()).changeCursor(SelectedTable
				.getInstance().list());
	}

}
