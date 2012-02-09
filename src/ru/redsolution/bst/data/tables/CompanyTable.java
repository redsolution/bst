package ru.redsolution.bst.data.tables;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;

/**
 * Список организаций.
 * 
 * @author alexander.ivanov
 * 
 */
public class CompanyTable extends NamedTable {
	public static final class Fields implements NamedTable.Fields {
		private Fields() {
		}

		public static final String FOLDER = "folder";
	}

	private static final String NAME = "company";

	private final static CompanyTable instance;

	static {
		instance = new CompanyTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static CompanyTable getInstance() {
		return instance;
	}

	private CompanyTable() {
	}

	@Override
	protected String getTableName() {
		return NAME;
	}

	@Override
	public Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.FOLDER);
		return collection;
	}

	@Override
	public void add(String id, String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Добавить элемент.
	 * 
	 * @param id
	 * @param name
	 * @param folder
	 */
	public void add(String id, String name, String folder) {
		ContentValues values = getValues(id, name);
		values.put(Fields.FOLDER, folder);
		add(values);
	}
}
