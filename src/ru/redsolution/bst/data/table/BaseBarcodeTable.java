package ru.redsolution.bst.data.table;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;

/**
 * Список штрих кодов.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class BaseBarcodeTable extends BaseTable {
	public static interface Fields extends BaseTable.Fields {
		public static final String TYPE = "code_type";
		public static final String VALUE = "value";
	}

	@Override
	protected Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.TYPE);
		collection.add(Fields.VALUE);
		return collection;
	}

	/**
	 * Добавить штрих код.
	 * 
	 * @param id
	 *            Идентификатор объекта, для которого создаётся штрих код.
	 * @param type
	 *            Тип штрих кода.
	 * @param value
	 *            Значение штрих кода.
	 */
	public void add(String id, String type, String value) {
		ContentValues values = new ContentValues();
		values.put(Fields._ID, id);
		values.put(Fields.TYPE, type);
		values.put(Fields.VALUE, value);
		add(values);
	}

	/**
	 * @param type
	 *            Тип штрих кода.
	 * @param value
	 *            Значение штрих кода.
	 * @return Идентификатор родительского объекта.
	 * @throws ObjectDoesNotExistException
	 * @throws MultipleObjectsReturnedException
	 */
	public String getId(String type, String value)
			throws ObjectDoesNotExistException,
			MultipleObjectsReturnedException {
		return get(Fields.TYPE + " = ? AND " + Fields.VALUE + " = ?",
				new String[] { type, value }).getAsString(Fields._ID);
	}

}
