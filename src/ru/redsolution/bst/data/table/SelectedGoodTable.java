package ru.redsolution.bst.data.table;

import java.math.BigDecimal;
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
	public BigDecimal getQuantity(String id, boolean isCustom) {
		ContentValues values;
		try {
			values = get(Fields._ID + " = ? AND " + Fields.IS_CUSTOM + " = ?",
					new String[] { id, getBoolean(isCustom) });
		} catch (BaseDatabaseException e) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(values.getAsString(Fields.QUANTITY));
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
	public void set(String id, boolean isCustom, BigDecimal quantity) {
		DatabaseHelper
				.getInstance()
				.getWritableDatabase()
				.delete(getTableName(),
						Fields._ID + " = ? AND " + Fields.IS_CUSTOM + " = ?",
						new String[] { id, getBoolean(isCustom) });
		if (BigDecimal.ZERO.equals(quantity))
			return;
		ContentValues values = new ContentValues();
		values.put(Fields._ID, id);
		values.put(Fields.IS_CUSTOM, isCustom);
		values.put(Fields.QUANTITY, quantity.toString());
		add(values);
	}

	/**
	 * @return Количетсво наименований товаров.
	 */
	public int getGoodsCount() {
		Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase()
				.rawQuery("SELECT COUNT(*) FROM " + getTableName(), null);
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
	 * @return Общее количетсво единиц товаров.
	 */
	public BigDecimal getTotalQuantity() {
		BigDecimal value = BigDecimal.ZERO;
		Cursor cursor = list();
		try {
			if (cursor.moveToFirst()) {
				do {
					String quantity = getValues(cursor).getAsString(
							Fields.QUANTITY);
					value = value.add(new BigDecimal(quantity));
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return value;
	}

	/**
	 * @param isCustom
	 * @return Список выбранных товаров.
	 */
	public Cursor listCustom(boolean isCustom) {
		return filter(Fields.IS_CUSTOM + " = ?",
				new String[] { getBoolean(isCustom) }, null);
	}

}
