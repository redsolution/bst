package ru.redsolution.bst.data.tables;

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
		public static final String VALUE = "value";
	}

	@Override
	public Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.VALUE);
		return collection;
	}

	/**
	 * Добавить штрих код.
	 * 
	 * @param id
	 *            Идентификатор объекта, для которого создаётся штрих код.
	 * @param value
	 *            Значение штрих кода.
	 */
	public void add(String id, String value) {
		ContentValues values = new ContentValues();
		values.put(Fields._ID, id);
		values.put(Fields.VALUE, value);
		add(values);
	}
}
