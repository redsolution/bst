package ru.redsolution.bst.data.tables;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;

/**
 * Список выбранных товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class SelectedTable extends BaseTable {
	public static interface Fields extends BaseTable.Fields {
		public static final String QUANTITY = "quantity";
	}

	private static final String NAME = "selected";

	private final static SelectedTable instance;

	static {
		instance = new SelectedTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static SelectedTable getInstance() {
		return instance;
	}

	private SelectedTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

	@Override
	public Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.QUANTITY);
		return collection;
	}

	/**
	 * Установить новое количество выбранных товаров.
	 * 
	 * @param id
	 *            Идентификатор объекта.
	 * @param quantity
	 *            Количество. 0 удаляет товар.
	 */
	public void set(String id, int quantity) {
		DatabaseHelper
				.getInstance()
				.getReadableDatabase()
				.delete(getTableName(), Fields._ID + " = ?",
						new String[] { id });
		if (quantity < 1)
			return;
		ContentValues values = new ContentValues();
		values.put(Fields._ID, id);
		values.put(Fields.QUANTITY, quantity);
		add(values);
	}
}
