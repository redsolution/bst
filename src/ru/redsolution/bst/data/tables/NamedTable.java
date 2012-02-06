package ru.redsolution.bst.data.tables;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Таблица с именен объекта и его идентификатором.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class NamedTable extends BaseTable {
	public static interface Fields extends BaseTable.Fields {
		public static final String NAME = "name";
	}

	@Override
	public Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.NAME);
		return collection;
	}

	/**
	 * @param id
	 * @return Имя объекта по его идентификатору или <code>null</code>.
	 */
	public String getName(String id) {
		Cursor cursor = filter(Fields._ID + " = ?", new String[] { id });
		try {
			if (cursor.moveToFirst())
				return cursor.getString(cursor.getColumnIndex(Fields.NAME));
		} finally {
			cursor.close();
		}
		return null;
	}

	/**
	 * @param id
	 * @param name
	 * @return Набор значений для добавления элемента.
	 */
	protected ContentValues getValues(String id, String name) {
		ContentValues values = new ContentValues();
		values.put(Fields._ID, id);
		values.put(Fields.NAME, name);
		return values;
	}

	/**
	 * Добавить элемент.
	 * 
	 * @param id
	 * @param name
	 */
	public void add(String id, String name) {
		add(getValues(id, name));
	}
}
