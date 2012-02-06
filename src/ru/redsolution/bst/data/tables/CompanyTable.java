package ru.redsolution.bst.data.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Список организаций.
 * 
 * @author alexander.ivanov
 * 
 */
public class CompanyTable implements DatabaseTable, ListableTable {
	public static final class Fields implements BaseColumns {
		private Fields() {
		}

		public static final String _ID = "_id";
		public static final String ID = "id";
		public static final String NAME = "name";
	}

	private static final String NAME = "company";
	private static final String[] PROJECTION = new String[] { Fields._ID,
			Fields.ID, Fields.NAME };

	private final static CompanyTable instance;

	static {
		instance = new CompanyTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static CompanyTable getInstance() {
		return instance;
	}

	@Override
	public void create(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + NAME + " (" + Fields._ID
				+ " INTEGER PRIMARY KEY, " + Fields.ID + " INTEGER,"
				+ Fields.NAME + " TEXT);");
	}

	@Override
	public void migrate(SQLiteDatabase db, int toVersion) {
	}

	@Override
	public void clear() {
		DatabaseHelper.getInstance().getWritableDatabase()
				.delete(NAME, null, null);
	}

	@Override
	public Cursor list() {
		return DatabaseHelper.getInstance().getReadableDatabase()
				.query(NAME, PROJECTION, null, null, null, null, null);
	}

	/**
	 * Добавить элемент.
	 * 
	 * @param id
	 * @param name
	 */
	public void add(String id, String name) {
		ContentValues values = new ContentValues();
		values.put(Fields.ID, id);
		values.put(Fields.NAME, name);
		DatabaseHelper.getInstance().getWritableDatabase()
				.insert(NAME, Fields.NAME, values);
	}
}
