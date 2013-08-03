package ru.redsolution.bst.data.table;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;

/**
 * Таблица со столбцом ссылки на строку в собственной таблице.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class ParentableTable extends NamedTable {
	public static interface Fields extends NamedTable.Fields {
		public static final String PARENT = "parent";
	}

	@Override
	public abstract String getTableName();

	@Override
	protected Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.PARENT);
		return collection;
	}

	@Override
	protected ContentValues getValues(String id, String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(String id, String name) {
		throw new UnsupportedOperationException();
	}

	protected ContentValues getValues(String id, String name, String parent) {
		ContentValues values = super.getValues(id, name);
		values.put(Fields.PARENT, parent);
		return values;
	}

	/**
	 * Добавить элемент.
	 * 
	 * @param id
	 * @param name
	 * @param parent
	 */
	public void add(String id, String name, String parent) {
		add(getValues(id, name, parent));
	}

}
