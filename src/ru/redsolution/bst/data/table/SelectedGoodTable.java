package ru.redsolution.bst.data.table;

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
public class SelectedGoodTable extends BaseTable {
	public static interface Fields extends BaseTable.Fields {
		public static final String IS_CUSTOM = "is_custom";
		public static final String QUANTITY = "quantity";
	}

	private static final String NAME = "selected_good";

	private final static SelectedGoodTable instance;

	static {
		instance = new SelectedGoodTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static SelectedGoodTable getInstance() {
		return instance;
	}

	private SelectedGoodTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

	@Override
	protected Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.IS_CUSTOM);
		collection.add(Fields.QUANTITY);
		return collection;
	}

	@Override
	protected String getFieldType(String name) {
		if (Fields.QUANTITY.equals(name))
			return "INTEGER";
		else
			return super.getFieldType(name);
	}

	@Override
	protected void putValue(ContentValues values, String name, Cursor cursor) {
		super.putValue(values, name, cursor);
		if (Fields.IS_CUSTOM.equals(name))
			values.put(name, getBoolean(values.getAsString(name)));
	}

	@Override
	public ContentValues getById(String id) throws ObjectDoesNotExistException,
			MultipleObjectsReturnedException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param id
	 * @param isCustom
	 * @return Количество выбранных товаров. 0, если нет выбранных товаров.
	 */
	public int getQuantity(String id, boolean isCustom) {
		ContentValues values;
		try {
			values = get(Fields._ID + " = ? AND " + Fields.IS_CUSTOM + " = ?",
					new String[] { id, getBoolean(isCustom) });
		} catch (BaseDatabaseException e) {
			return 0;
		}
		return values.getAsInteger(Fields.QUANTITY);
	}

	/**
	 * Установить новое количество выбранных товаров.
	 * 
	 * @param id
	 *            Идентификатор объекта.
	 * @param isCustom
	 *            Является пользовательским товаром.
	 * @param quantity
	 *            Количество. 0 удаляет товар.
	 */
	public void set(String id, boolean isCustom, int quantity) {
		DatabaseHelper
				.getInstance()
				.getWritableDatabase()
				.delete(getTableName(),
						Fields._ID + " = ? AND " + Fields.IS_CUSTOM + " = ?",
						new String[] { id, getBoolean(isCustom) });
		if (quantity < 1)
			return;
		ContentValues values = new ContentValues();
		values.put(Fields._ID, id);
		values.put(Fields.IS_CUSTOM, isCustom);
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
								+ ") FROM " + getTableName(), null);
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
