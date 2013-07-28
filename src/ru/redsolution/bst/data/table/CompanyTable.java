package ru.redsolution.bst.data.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Список организаций.
 * 
 * @author alexander.ivanov
 * 
 */
public class CompanyTable extends NamedTable {

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
	public void migrate(SQLiteDatabase db, int toVersion) {
		super.migrate(db, toVersion);
		String sql;
		switch (toVersion) {
		case 3:
			sql = "ALTER TABLE company RENAME TO company_;";
			DatabaseHelper.execSQL(db, sql);
			sql = "CREATE TABLE company (" + "_id TEXT,"
					+ "name TEXT COLLATE UNICODE);";
			DatabaseHelper.execSQL(db, sql);
			sql = "INSERT INTO company (_id, name) "
					+ "SELECT _id, name FROM company_;";
			DatabaseHelper.execSQL(db, sql);
			sql = "DROP TABLE company_;";
			DatabaseHelper.execSQL(db, sql);
			break;
		default:
			break;
		}
	}

}
