package ru.redsolution.bst.data.tables;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;

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

	@Override
	public String getFieldType(String name) {
		if (Fields.QUANTITY.equals(name))
			return "INTEGER";
		else
			return super.getFieldType(name);
	}

	/**
	 * @param id
	 * @return Количество выбранных товаров. 0, если нет выбранных товаров.
	 */
	public int getQuantity(String id) {
		try {
			return getById(id).getAsInteger(Fields.QUANTITY);
		} catch (BaseDatabaseException e) {
			return 0;
		}
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
				.getWritableDatabase()
				.delete(getTableName(), Fields._ID + " = ?",
						new String[] { id });
		if (quantity < 1)
			return;
		ContentValues values = new ContentValues();
		values.put(Fields._ID, id);
		values.put(Fields.QUANTITY, quantity);
		add(values);
	}

	/**
	 * @param function
	 * @return Результат выполнения функции для столбца количества единиц
	 *         товара.
	 */
	private int executeForQuantity(String function) {
		Cursor cursor = DatabaseHelper
				.getInstance()
				.getReadableDatabase()
				.rawQuery(
						"SELECT " + function + "(" + Fields.QUANTITY
								+ ") FROM " + getTableName() + ";", null);
		try {
			if (cursor.moveToFirst()) {
				return cursor.getInt(0);
			}
		} finally {
			cursor.close();
		}
		throw new RuntimeException();
	}

	/**
	 * @return Количетсво наименований товаров.
	 */
	public int getGoodsCount() {
		return executeForQuantity("COUNT");
	}

	/**
	 * @return Общее количетсво единиц товаров.
	 */
	public int getTotalQuantity() {
		return executeForQuantity("SUM");
	}

}
